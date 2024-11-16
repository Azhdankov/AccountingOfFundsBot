package ru.azhdankov.accountingOfFunds.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.CommandName;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatID;
import ru.azhdankov.accountingOfFunds.model.user.User;

public class StartCommandImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);

        sendMessage.setText(MessageName.FIRST_MESSAGE_BOT_MESSAGE.getMessageName());
        sendMessage.setReplyMarkup(
                getReplyKeyboardMarkup(
                        CommandName.ALL_CATEGORIES_ENTERED_COMMAND.getCommandName()));

        CallbackDataByChatID callbackDataByChatID =
                CallbackDataByChatID.builder()
                        .chatID(chatID)
                        .callbackData("FromStartCommand")
                        .build();
        this.botService.saveCallbackData(callbackDataByChatID);
        this.botService.saveUser(User.builder().id(chatID).build());
        return sendMessage;
    }
}
