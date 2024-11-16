package ru.azhdankov.accountingOfFunds.command.impl;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;

public class ClearAllDataCommandImpl extends CommandHelper implements Command<SendMessage> {

    @Override
    public SendMessage run(Update update) {

        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);
        botService.deleteCallbackDataByChatID(chatID);

        sendMessage.setText(MessageName.ARE_YOU_SURE_DELETE_ALL_AMOUNTS_MESSAGE.getMessageName());

        List<String> yesNoAnswers = new ArrayList<>();
        yesNoAnswers.add("Да");
        yesNoAnswers.add("Нет");
        String[] yesNoAnswersToInlineKeyboard = yesNoAnswers.toArray(new String[0]);

        List<String> yesNoKeys = new ArrayList<>();
        yesNoKeys.add("yes");
        yesNoKeys.add("no");

        sendMessage.setReplyMarkup(
                getInlineKeyboardMarkup(
                        "ApproveForClearData", yesNoKeys, null, yesNoAnswersToInlineKeyboard));

        return sendMessage;
    }
}
