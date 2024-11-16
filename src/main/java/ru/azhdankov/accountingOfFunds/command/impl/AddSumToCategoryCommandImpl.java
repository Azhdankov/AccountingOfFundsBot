package ru.azhdankov.accountingOfFunds.command.impl;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
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
        List<String> categoryIDList = new ArrayList<>();
        botService
                .findAllCategoryInfoByChatID(chatID)
                .forEach(
                        e -> {
                            readableCategoryList.add(e.getReadableCategoryName());
                            categoryIDList.add(e.getId());
                        });

        if (categoryIDList.isEmpty()) {
            sendMessage.setText(MessageName.NO_CATEGORY_FOR_ADD_SUM_MESSAGE.getMessageName());
            return sendMessage;
        }

        String[] categoriesToInlineKeyboard = readableCategoryList.toArray(new String[0]);

        sendMessage.setReplyMarkup(
                getInlineKeyboardMarkup(
                        "AddSum", categoryIDList, null, categoriesToInlineKeyboard));
        return sendMessage;
    }
}
