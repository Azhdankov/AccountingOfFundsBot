package ru.azhdankov.accountingOfFunds.command;

import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.botService.BotService;

@Component
public class CommandWrapper {

    @Autowired private BotService botService;

    @Autowired private CommandFactory commandFactory;

    public LinkedHashMap<Command<?>, Boolean> getCommandMap(Update update) {
        return commandFactory.getCommandMap(update, botService);
    }
}
