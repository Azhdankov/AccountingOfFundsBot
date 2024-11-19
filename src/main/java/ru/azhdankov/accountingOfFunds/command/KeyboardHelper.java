package ru.azhdankov.accountingOfFunds.command;

import com.vdurmont.emoji.EmojiParser;
import lombok.Builder;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Builder
@Getter
public class KeyboardHelper {

    public static final int MAX_BUTTONS = 8;
    private String callBackDataPrefix;
    private HashMap<String, String> callBackDataMap;
    private int startIndex;
    private int endIndex;

    public ReplyKeyboardMarkup getReplyKeyboardMarkup(String... buttonsText) {

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

    public InlineKeyboardMarkup getInlineKeyboardMarkup(String... buttonsText) {

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        List<List<InlineKeyboardButton>> inlineKeyboardRows = new ArrayList<>();
        inlineKeyboardRows.add(inlineKeyboardButtons);
        boolean isCallBackDataSingle = callBackDataMap == null;

        int counter = 0;
        if (startIndex == 0 && endIndex == 0) {
            counter = Math.min(buttonsText.length, MAX_BUTTONS);
        } else {
            counter = endIndex;
        }

        for (int i = startIndex; i < counter; i++) {
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
                inlineKeyboardButton.setCallbackData(currentCallBackData);
            } else {
                inlineKeyboardButton.setCallbackData(
                        callBackDataPrefix + "_" + callBackDataMap.get(buttonsText[i]));
            }
            inlineKeyboardButtons.add(inlineKeyboardButton);
        }
        if ((buttonsText.length > MAX_BUTTONS) && startIndex == 0) {
            inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardRows.add(inlineKeyboardButtons);

            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(EmojiParser.parseToUnicode(":arrow_right:"));
            inlineKeyboardButton.setCallbackData("NextPage_"+callBackDataPrefix+":"+MAX_BUTTONS);

            inlineKeyboardButtons.add(inlineKeyboardButton);
        } else if (startIndex != 0 && endIndex != 0 && (buttonsText.length != endIndex)) {
            inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardRows.add(inlineKeyboardButtons);

            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            InlineKeyboardButton previousButton = new InlineKeyboardButton();
            nextButton.setText(EmojiParser.parseToUnicode(":arrow_right:"));
            previousButton.setText(EmojiParser.parseToUnicode(":arrow_left:"));
            nextButton.setCallbackData("NextPage_"+callBackDataPrefix+":"+(startIndex + MAX_BUTTONS));
            previousButton.setCallbackData("PreviousPage_"+callBackDataPrefix+":"+(startIndex - MAX_BUTTONS));

            inlineKeyboardButtons.add(previousButton);
            inlineKeyboardButtons.add(nextButton);
        } else if (buttonsText.length - startIndex <= MAX_BUTTONS && buttonsText.length > MAX_BUTTONS) {
            inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardRows.add(inlineKeyboardButtons);

            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(EmojiParser.parseToUnicode(":arrow_left:"));
            inlineKeyboardButton.setCallbackData("PreviousPage_"+callBackDataPrefix+":"+(startIndex - MAX_BUTTONS));

            inlineKeyboardButtons.add(inlineKeyboardButton);
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardRows);
        return inlineKeyboardMarkup;
    }
}
