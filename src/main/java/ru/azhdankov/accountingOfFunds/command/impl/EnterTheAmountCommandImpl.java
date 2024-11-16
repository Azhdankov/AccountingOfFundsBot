package ru.azhdankov.accountingOfFunds.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatID;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfo;

public class EnterTheAmountCommandImpl extends CommandHelper implements Command<SendMessage> {
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
        Double amount = 0d;
        if (message.contains(",")) {
            message = message.replaceAll(",", ".");
        }
        if (message.matches("[^.0-9]")) {
            sendMessage.setText(MessageName.AMOUNT_IS_NOT_CORRECT.getMessageName());
            return sendMessage;
        }
        try {
            amount = Double.parseDouble(message);
        } catch (NumberFormatException e) {
            sendMessage.setText(MessageName.AMOUNT_IS_NOT_CORRECT.getMessageName());
            return sendMessage;
        }
        currentCategoryInfo.setAmount(
                currentCategoryInfo.getAmount() + Double.parseDouble(message));
        botService.saveCategoryInfo(currentCategoryInfo);

        sendMessage.setText(
                MessageName.AMOUNT_SUCCESSFULLY_WAS_ADDED.getMessageName()
                        + currentCategoryInfo.getReadableCategoryName());

        botService.deleteCallbackDataByChatID(chatID);
        return sendMessage;
    }
}
