package ru.azhdankov.accountingOfFunds.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatID;

public class AddNewCategoryCommandImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);
        sendMessage.setText(MessageName.WAIT_NEW_CATEGORY_MESSAGE.getMessageName());
        CallbackDataByChatID callbackDataByChatID =
                CallbackDataByChatID.builder()
                        .chatID(chatID)
                        .callbackData("FromAddNewCategory")
                        .build();
        this.botService.saveCallbackData(callbackDataByChatID);
        return sendMessage;
    }
}
