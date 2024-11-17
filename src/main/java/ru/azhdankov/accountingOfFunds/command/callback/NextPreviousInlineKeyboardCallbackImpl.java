package ru.azhdankov.accountingOfFunds.command.callback;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.command.KeyboardHelper;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NextPreviousInlineKeyboardCallbackImpl extends CommandHelper implements Command<EditMessageReplyMarkup> {
    @Override
    public EditMessageReplyMarkup run(Update update) {

        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        String callbackData = update.getCallbackQuery().getData();
        String chatID = update.getCallbackQuery().getMessage().getChatId().toString();
        editMessageReplyMarkup.setChatId(chatID);

        List<String> readableCategoryList = new ArrayList<>();
        HashMap<String, String> categoryIDMap = new HashMap<>();
        botService
                .findAllCategoryInfoByChatID(chatID)
                .forEach(
                        e -> {
                            readableCategoryList.add(e.getReadableCategoryName());
                            categoryIDMap.put(e.getReadableCategoryName(), e.getId());
                        });

        Collections.sort(readableCategoryList);
        String[] categoriesToInlineKeyboard = readableCategoryList.toArray(new String[0]);

        int startIndex = Integer.parseInt(callbackData.substring(callbackData.indexOf(":") + 1));
        int endIndex = readableCategoryList.size() - startIndex > KeyboardHelper.MAX_BUTTONS?
                startIndex + KeyboardHelper.MAX_BUTTONS : readableCategoryList.size();

        String callBackDataPrefix = callbackData.substring(callbackData.indexOf("_") + 1,
                callbackData.indexOf(":"));
        KeyboardHelper keyboardHelper = KeyboardHelper.builder()
                .startIndex(startIndex)
                .endIndex(endIndex)
                .callBackDataPrefix(callBackDataPrefix)
                .callBackDataMap(categoryIDMap)
                .build();

        editMessageReplyMarkup.setReplyMarkup(keyboardHelper.getInlineKeyboardMarkup(categoriesToInlineKeyboard));
        return editMessageReplyMarkup;
    }
}
