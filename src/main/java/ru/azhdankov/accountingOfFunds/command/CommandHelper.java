package ru.azhdankov.accountingOfFunds.command;

import java.util.ArrayList;
import java.util.List;
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

    protected ReplyKeyboardMarkup getReplyKeyboardMarkup(String... buttonsText) {

        KeyboardRow keyboardRow = new KeyboardRow();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardRow);

        for (int i = 0; i < buttonsText.length; i++) {
            if (i % 2 == 0 && i != 0) {
                keyboardRow = new KeyboardRow();
                keyboardRows.add(keyboardRow);
            }
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText(buttonsText[i]);
            keyboardRow.add(keyboardButton);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    protected InlineKeyboardMarkup getInlineKeyboardMarkup(
            String callBackDataPrefix,
            List<String> callBackDataList,
            String chatID,
            String... buttonsText) {

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        List<List<InlineKeyboardButton>> inlineKeyboardRows = new ArrayList<>();
        inlineKeyboardRows.add(inlineKeyboardButtons);
        Boolean isCallBackDataSingle = callBackDataList == null;

        for (int i = 0; i < buttonsText.length; i++) {
            if (i % 2 == 0 && i != 0) {
                inlineKeyboardButtons = new ArrayList<>();
                inlineKeyboardRows.add(inlineKeyboardButtons);
            }
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(buttonsText[i]);
            inlineKeyboardButton.setCallbackData(callBackDataPrefix);

            if (isCallBackDataSingle) {
                String currentCallBackData =
                        callBackDataPrefix
                                + "_"
                                + buttonsText[i].toLowerCase().replaceAll("\\s", "");
                currentCallBackData =
                        currentCallBackData.substring(
                                0, Math.min(currentCallBackData.length(), 40));
                System.out.println("currentCallBackData: " + currentCallBackData);
                inlineKeyboardButton.setCallbackData(currentCallBackData);
            } else {
                inlineKeyboardButton.setCallbackData(
                        callBackDataPrefix + "_" + callBackDataList.get(i));
            }
            inlineKeyboardButtons.add(inlineKeyboardButton);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardRows);
        return inlineKeyboardMarkup;
    }
}
