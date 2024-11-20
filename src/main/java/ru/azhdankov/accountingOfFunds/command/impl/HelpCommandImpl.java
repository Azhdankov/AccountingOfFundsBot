package ru.azhdankov.accountingOfFunds.command.impl;

import static ru.azhdankov.accountingOfFunds.messageDictionary.MessageName.HELP_MESSAGE;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;

public class HelpCommandImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);
        botService.deleteCallbackDataByChatID(chatID);

        sendMessage.setText(HELP_MESSAGE.getMessageName());
        return sendMessage;
    }
}
