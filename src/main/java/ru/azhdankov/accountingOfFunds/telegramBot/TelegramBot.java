package ru.azhdankov.accountingOfFunds.telegramBot;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
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

    public TelegramBot(BotConfig botConfig) {
        super(botConfig.getBotToken());
        log.info("Bot started");
    }

    @Override
    public void onUpdateReceived(Update update) {
        new Thread(
                        () -> {
                            List<Command<?>> commandList = commandWrapper.getSendObject(update);
                            Integer messageID = 0;
                            for (Command<?> e : commandList) {
                                try {
                                    Object o = e.run(update);
                                    if (o instanceof SendMessage sendMessage) {
                                        messageID = execute(sendMessage).getMessageId();
                                        /* надо что-то придумать с тем, чтобы уходить в sleep только с нужными командами * */
                                        if (commandList.size() > 1) {
                                            Thread.sleep(1800);
                                        }
                                    }
                                    if (o instanceof EditMessageText editMessageText) {
                                        editMessageText.setMessageId(messageID);
                                        execute(editMessageText);
                                    }
                                    if (o
                                            instanceof
                                            EditMessageReplyMarkup editMessageReplyMarkup) {
                                        messageID =
                                                update.getCallbackQuery()
                                                        .getMessage()
                                                        .getMessageId();
                                        editMessageReplyMarkup.setMessageId(messageID);
                                        execute(editMessageReplyMarkup);
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
