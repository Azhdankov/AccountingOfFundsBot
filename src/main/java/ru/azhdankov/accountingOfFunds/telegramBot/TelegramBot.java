package ru.azhdankov.accountingOfFunds.telegramBot;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandWrapper;
import ru.azhdankov.accountingOfFunds.configuration.BotConfig;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatIDDAO;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfoDAO;
import ru.azhdankov.accountingOfFunds.model.user.UserDAO;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired private BotConfig botConfig;

    @Autowired private CategoryInfoDAO categoryInfoDAO;

    @Autowired private CallbackDataByChatIDDAO callbackDataByChatIDDAO;

    @Autowired private UserDAO userDAO;

    @Autowired private CommandWrapper commandWrapper;
    private final ConcurrentHashMap<String, Integer> editMessageIDByChatID = new ConcurrentHashMap<>();

    public TelegramBot(BotConfig botConfig) {
        super(botConfig.getBotToken());
        log.info("Bot started");
    }

    @Override
    public void onUpdateReceived(Update update) {
        new Thread(
                        () -> {
                            List<Command<?>> commandList = commandWrapper.getSendObject(update);
                            AtomicReference<Integer> messageId = new AtomicReference<>(0);

                            for (Command<?> e : commandList) {
                                try {
                                    Object o = e.run(update);
                                    if (o instanceof SendMessage sendMessage) {
                                        messageId.set(execute(sendMessage).getMessageId());
                                        if (update.hasMessage()) {
                                            editMessageIDByChatID.put(update.getMessage().getChatId().toString(), messageId.get());
                                        }
                                        if (commandList.size() > 1) {
                                            Thread.sleep(1800);
                                        }
                                    }
                                    if (o instanceof EditMessageText editMessageText) {
                                        editMessageText.setMessageId(messageId.get());
                                        execute(editMessageText);
                                    }
                                    if (o instanceof EditMessageReplyMarkup editMessageReplyMarkup) {
                                        editMessageReplyMarkup.setMessageId(editMessageIDByChatID.get(
                                                update.getCallbackQuery().getMessage().getChatId().toString()
                                        ));
                                        execute(editMessageReplyMarkup);
                                    }
                                } catch (TelegramApiException | InterruptedException ex) {
                                    log.error(ex.getMessage());
                                }
                            }
                        })
                .start();
    }

    private InlineKeyboardMarkup newReplyMarkup() {
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        List<List<InlineKeyboardButton>> inlineKeyboardRows = new ArrayList<>();
        inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardRows.add(inlineKeyboardButtons);

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(EmojiParser.parseToUnicode("arrow_right"));
        inlineKeyboardButton.setCallbackData("arrow_right");

        inlineKeyboardButtons.add(inlineKeyboardButton);

        return new InlineKeyboardMarkup(inlineKeyboardRows);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}
