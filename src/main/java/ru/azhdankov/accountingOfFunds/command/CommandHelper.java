package ru.azhdankov.accountingOfFunds.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.azhdankov.accountingOfFunds.botService.BotService;

@Setter
@Getter
@RequiredArgsConstructor
public abstract class CommandHelper {

    protected BotService botService;
}
