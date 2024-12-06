package ru.azhdankov.accountingOfFunds.command.callbackImpl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;

public class ClearDataCallbackImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {
        return null;
    }
}
