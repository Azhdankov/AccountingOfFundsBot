package ru.azhdankov.accountingOfFunds.command;

import java.util.ArrayList;
import java.util.List;

import com.vdurmont.emoji.EmojiParser;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.azhdankov.accountingOfFunds.botService.BotService;

@Setter
@RequiredArgsConstructor
public abstract class CommandHelper {

    protected BotService botService;

}
