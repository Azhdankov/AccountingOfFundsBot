package ru.azhdankov.accountingOfFunds.command.callback;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatID;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfo;

public class CheckRenamedCategoryCallbackImpl extends CommandHelper
        implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {

        SendMessage sendMessage = new SendMessage();
        String callbackData = update.getCallbackQuery().getData();
        String chatID = update.getCallbackQuery().getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);
        String categoryID = callbackData.substring(callbackData.indexOf("_") + 1);

        CategoryInfo currentCategoryInfo = botService.findCategoryInfoByID(categoryID);

        sendMessage.setText(
                "Выбрана категория "
                        + currentCategoryInfo.getReadableCategoryName()
                        + ". Введите новое имя");

        CallbackDataByChatID callbackDataByChatID =
                CallbackDataByChatID.builder()
                        .chatID(chatID)
                        .callbackData("FromCheckRenamedCategory")
                        .callbackCategoryID(categoryID)
                        .build();

        botService.saveCallbackData(callbackDataByChatID);
        return sendMessage;
    }
}
