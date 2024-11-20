package ru.azhdankov.accountingOfFunds.command.callback;

import com.vdurmont.emoji.EmojiParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.command.KeyboardHelper;
import ru.azhdankov.accountingOfFunds.model.user.User;

public class ChangeModeEmojiCallbackImpl extends CommandHelper
        implements Command<EditMessageReplyMarkup> {
    @Override
    public EditMessageReplyMarkup run(Update update) {

        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        String callbackData = update.getCallbackQuery().getData();
        String chatID = update.getCallbackQuery().getMessage().getChatId().toString();
        editMessageReplyMarkup.setChatId(chatID);

        User user = botService.findUserByChatID(chatID);
        String emoji = EmojiParser.parseToUnicode(":white_check_mark:");
        List<String> singleMultipleMode = new ArrayList<>();

        if (user.isSingleMode()) {
            singleMultipleMode.add("Одиночный " + emoji);
            singleMultipleMode.add("Группа");
        } else {
            singleMultipleMode.add("Одиночный");
            singleMultipleMode.add("Группа " + emoji);
        }
        String[] singleMultipleModeToInlineKeyboard = singleMultipleMode.toArray(new String[0]);

        HashMap<String, String> singleMultipleModeMap = new HashMap<>();
        singleMultipleModeMap.put(singleMultipleMode.getFirst(), "SingleMode");
        singleMultipleModeMap.put(singleMultipleMode.getLast(), "MultipleMode");
        // нужен рефакторинг

        KeyboardHelper keyboardHelper =
                KeyboardHelper.builder()
                        .callBackDataPrefix("ChangeMode")
                        .callBackDataMap(singleMultipleModeMap)
                        .build();

        editMessageReplyMarkup.setReplyMarkup(
                keyboardHelper.getInlineKeyboardMarkup(singleMultipleModeToInlineKeyboard));

        return editMessageReplyMarkup;
    }
}
