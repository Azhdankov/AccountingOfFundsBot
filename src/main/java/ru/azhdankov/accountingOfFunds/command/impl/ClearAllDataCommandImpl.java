package ru.azhdankov.accountingOfFunds.command.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.command.KeyboardHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;

public class ClearAllDataCommandImpl extends CommandHelper implements Command<SendMessage> {

    @Override
    public SendMessage run(Update update) {

        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);
        botService.deleteCallbackDataByChatID(chatID);

        sendMessage.setText(MessageName.ARE_YOU_SURE_DELETE_ALL_AMOUNTS_MESSAGE.getMessageName());

        // нужен рефакторинг
        List<String> yesNoAnswers = new ArrayList<>();
        yesNoAnswers.add("Да");
        yesNoAnswers.add("Нет");
        String[] yesNoAnswersToInlineKeyboard = yesNoAnswers.toArray(new String[0]);

        HashMap<String,String> yesNoKeys = new HashMap<>();
        yesNoKeys.put("Да", "yes");
        yesNoKeys.put("Нет", "no");
        // нужен рефакторинг

        KeyboardHelper keyboardHelper = KeyboardHelper.builder()
                .callBackDataPrefix("ApproveForClearData")
                .callBackDataMap(yesNoKeys)
                .build();

        sendMessage.setReplyMarkup(
                keyboardHelper.getInlineKeyboardMarkup(yesNoAnswersToInlineKeyboard));

        return sendMessage;
    }
}
