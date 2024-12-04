package ru.azhdankov.accountingOfFunds.command.impl;

import static ru.azhdankov.accountingOfFunds.messageDictionary.MessageName.*;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.command.KeyboardHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.CommandName;
import ru.azhdankov.accountingOfFunds.model.user.User;

public class StartFromLinkCommandImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);

        KeyboardHelper keyboardHelper = KeyboardHelper.builder().build();
        sendMessage.setReplyMarkup(
                keyboardHelper.getReplyKeyboardMarkup(
                        CommandName.ADD_NEW_CATEGORY_COMMAND.getCommandName(),
                        CommandName.PRINT_ALL_CATEGORY_TO_SCREEN_COMMAND.getCommandName(),
                        CommandName.ADD_SUM_TO_CATEGORY_COMMAND.getCommandName(),
                        CommandName.RENAME_CATEGORY_COMMAND.getCommandName(),
                        CommandName.MONTH_SUM_COMMAND.getCommandName(),
                        CommandName.REMOVE_ALL_DATA_COMMAND.getCommandName()));

        String pairID =
                update.getMessage()
                        .getText()
                        .substring(update.getMessage().getText().indexOf(" ") + 1);
        if (botService.findUsersByPairID(pairID).isEmpty()) {
            sendMessage.setText(THERE_IS_NO_PAIR_FOR_START_MESSAGE.getMessageName());
            return sendMessage;
        }

        if (botService.findUserByChatID(chatID) != null) {
            if (botService.findUserByChatID(chatID).getPairID() != null
                    && botService.findUserByChatID(chatID).getPairID().equals(pairID)) {
                sendMessage.setText(CURRENT_MODE_WAS_CHANGED_TO_GROUP_MESSAGE.getMessageName());
            } else if (botService.findUserByChatID(chatID).getPairID() != null) {
                sendMessage.setText(PREVIOUS_GROUP_WAS_REMOVED_MESSAGE.getMessageName());
            } else {
                sendMessage.setText(GROUP_MODE_WAS_APPLIED_MESSAGE.getMessageName());
            }
        } else {
            sendMessage.setText(WELCOME_FROM_LINK.getMessageName());
        }

        this.botService.saveUser(
                User.builder().id(chatID).isSingleMode(false).pairID(pairID).build());
        return sendMessage;
    }
}
