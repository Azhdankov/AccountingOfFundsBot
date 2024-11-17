package ru.azhdankov.accountingOfFunds.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.command.KeyboardHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.CommandName;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;

public class AllCategoriesEnteredCommandImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);

        sendMessage.setText(MessageName.AFTER_CATEGORIES_ADDED_MESSAGE.getMessageName());
        KeyboardHelper keyboardHelper = KeyboardHelper.builder().build();
        sendMessage.setReplyMarkup(
                keyboardHelper.getReplyKeyboardMarkup(
                        CommandName.ADD_NEW_CATEGORY_COMMAND.getCommandName(),
                        CommandName.PRINT_ALL_CATEGORY_TO_SCREEN_COMMAND.getCommandName(),
                        CommandName.ADD_SUM_TO_CATEGORY_COMMAND.getCommandName(),
                        CommandName.RENAME_CATEGORY_COMMAND.getCommandName(),
                        CommandName.MONTH_SUM_COMMAND.getCommandName(),
                        CommandName.REMOVE_ALL_DATA_COMMAND.getCommandName()));

        botService.deleteCallbackDataByChatID(chatID);
        return sendMessage;
    }
}
