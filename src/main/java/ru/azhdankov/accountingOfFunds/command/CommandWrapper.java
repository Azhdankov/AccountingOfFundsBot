package ru.azhdankov.accountingOfFunds.command;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.botService.BotService;

@Component
public class CommandWrapper {

    @Autowired private BotService botService;

    @Autowired private CommandFactory commandFactory;

    public List<Command<?>> getSendObject(Update update) {
        List<Command<?>> commandList = commandFactory.getCommandList(update, botService);
        return commandList;
    }
}
