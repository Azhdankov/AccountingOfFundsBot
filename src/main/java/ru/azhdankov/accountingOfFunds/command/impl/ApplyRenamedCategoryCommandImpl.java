package ru.azhdankov.accountingOfFunds.command.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatID;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfo;

public class ApplyRenamedCategoryCommandImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {

        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();
        sendMessage.setChatId(chatID);

        CallbackDataByChatID callbackDataByChatID = botService.findCallbackDataByChatID(chatID);

        if (callbackDataByChatID == null) {
            return null;
        }

        CategoryInfo currentCategoryInfo =
                botService.findCategoryInfoByID(callbackDataByChatID.getCallbackCategoryID());
        currentCategoryInfo.setReadableCategoryName(message);
        currentCategoryInfo.setCategoryKey(message.toLowerCase().replaceAll("\\s", ""));

        try {
            botService.saveCategoryInfo(currentCategoryInfo);
        } catch (DataIntegrityViolationException e) {
            sendMessage.setText(MessageName.UNIQUE_CONSTRAINT_ERROR_MESSAGE.getMessageName());
            return sendMessage;
        }

        sendMessage.setText(MessageName.CATEGORY_WAS_RENAMED_MESSAGE.getMessageName());

        botService.deleteCallbackDataByChatID(chatID);

        return sendMessage;
    }
}
