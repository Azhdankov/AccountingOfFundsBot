package ru.azhdankov.accountingOfFunds.command.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.command.KeyboardHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;

public class AddSumToCategoryCommandImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);
        botService.deleteCallbackDataByChatID(chatID);

        sendMessage.setText(MessageName.CHOOSE_CATEGORY_NAME_MESSAGE.getMessageName());

        List<String> readableCategoryList = new ArrayList<>();
        HashMap<String, String> categoryIDMap = new HashMap<>();
        botService
                .findAllCategoryInfoByChatID(chatID)
                .forEach(
                        e -> {
                            readableCategoryList.add(e.getReadableCategoryName());
                            categoryIDMap.put(e.getReadableCategoryName(), e.getId());
                        });

        if (categoryIDMap.isEmpty()) {
            sendMessage.setText(MessageName.NO_CATEGORY_FOR_ADD_SUM_MESSAGE.getMessageName());
            return sendMessage;
        }

        Collections.sort(readableCategoryList);
        String[] categoriesToInlineKeyboard = readableCategoryList.toArray(new String[0]);

        KeyboardHelper keyboardHelper =
                KeyboardHelper.builder()
                        .callBackDataPrefix("AddSum")
                        .callBackDataMap(categoryIDMap)
                        .build();

        sendMessage.setReplyMarkup(
                keyboardHelper.getInlineKeyboardMarkup(categoriesToInlineKeyboard));
        return sendMessage;
    }
}
