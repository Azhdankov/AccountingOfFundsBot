package ru.azhdankov.accountingOfFunds.command.impl;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.command.KeyboardHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;
import ru.azhdankov.accountingOfFunds.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChangeModeCommandImpl extends CommandHelper implements Command<SendMessage> {
    private final String SINGLE_MODE = "SingleMode";
    private final String MULTIPLE_MODE = "MultipleMode";
    @Override
    public SendMessage run(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);
        sendMessage.setText(MessageName.CHANGE_MODE.getMessageName());
        botService.deleteCallbackDataByChatID(chatID);

        User user = botService.findUserByChatID(chatID);
        String currentMode = user.getPairID() == null || user.getPairID().isEmpty() ? SINGLE_MODE: MULTIPLE_MODE;
        String emoji = EmojiParser.parseToUnicode(":white_check_mark:");
        // нужен рефакторинг
        List<String> singleMultipleMode = new ArrayList<>();

        if (currentMode.equals(SINGLE_MODE)) {
            singleMultipleMode.add("Режим для одного пользователя " + emoji);
            singleMultipleMode.add("Группа пользователей");
        } else {
            singleMultipleMode.add("Режим для одного пользователя");
            singleMultipleMode.add("Группа пользователей " + emoji);
        }

        String[] singleMultipleModeToInlineKeyboard = singleMultipleMode.toArray(new String[0]);

        HashMap<String,String> singleMultipleModeMap = new HashMap<>();
        singleMultipleModeMap.put("Режим для одного пользователя", "SingleMode");
        singleMultipleModeMap.put("Группа пользователей", "MultipleMode");
        // нужен рефакторинг

        KeyboardHelper keyboardHelper = KeyboardHelper.builder()
                .callBackDataPrefix("ChangeMode")
                .callBackDataMap(singleMultipleModeMap)
                .build();

        sendMessage.setReplyMarkup(
                keyboardHelper.getInlineKeyboardMarkup(singleMultipleModeToInlineKeyboard));

        return sendMessage;
    }
}
