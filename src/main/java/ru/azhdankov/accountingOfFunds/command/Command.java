package ru.azhdankov.accountingOfFunds.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.botService.BotService;

public interface Command<T> {

    void setBotService(BotService botService);

    T run(Update update);
}
