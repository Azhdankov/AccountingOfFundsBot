package ru.azhdankov.accountingOfFunds.telegramBot;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
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
    private final HashMap<String, String> categoryKeyForUpdate = new HashMap<>();
    private final HashMap<String, String> userActionByChatID = new HashMap<>();

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
                                        Thread.sleep(1800);
                                    }
                                    if (o instanceof EditMessageText editMessageText) {
                                        editMessageText.setMessageId(messageId.get());
                                        execute(editMessageText);
                                    }
                                } catch (TelegramApiException | InterruptedException ex) {
                                    log.error(ex.getMessage());
                                }
                            }
                        })
                .start();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}
