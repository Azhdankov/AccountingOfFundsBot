package ru.azhdankov.accountingOfFunds.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfo;

public class MonthSumCommandImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);
        botService.deleteCallbackDataByChatID(chatID);

        double sum =
                botService.findAllCategoryInfoByChatID(chatID).stream()
                        .mapToDouble(CategoryInfo::getAmount)
                        .sum();

        sendMessage.setText(MessageName.MONTH_AMOUNT_MESSAGE.getMessageName() + ": " + sum);
        return sendMessage;
    }
}
