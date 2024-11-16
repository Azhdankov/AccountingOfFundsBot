package ru.azhdankov.accountingOfFunds.command.callback;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.CommandName;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatID;

public class ApproveForClearDataCallbackImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {

        SendMessage sendMessage = new SendMessage();
        String callbackData = update.getCallbackQuery().getData();
        String chatID = update.getCallbackQuery().getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);
        String callbackAnswer = callbackData.substring(callbackData.indexOf("_") + 1);

        if (callbackAnswer.equals("yes")) {
            botService.deleteAllCategoryInfoByChatID(chatID);
            sendMessage.setText(MessageName.SUCCESS_REMOVED.getMessageName());
            sendMessage.setReplyMarkup(
                    getReplyKeyboardMarkup(
                            CommandName.ALL_CATEGORIES_ENTERED_COMMAND.getCommandName()));
            CallbackDataByChatID callbackDataByChatID =
                    CallbackDataByChatID.builder()
                            .chatID(chatID)
                            .callbackData("FromApproveForClearData")
                            .build();
            this.botService.saveCallbackData(callbackDataByChatID);
        } else {
            sendMessage.setText(MessageName.DISCARD_MESSAGE.getMessageName());
        }

        return sendMessage;
    }
}
