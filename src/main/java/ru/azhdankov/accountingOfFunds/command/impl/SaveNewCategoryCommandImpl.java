package ru.azhdankov.accountingOfFunds.command.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatID;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfo;

@Getter
@Setter
@Component
public class SaveNewCategoryCommandImpl extends CommandHelper implements Command<SendMessage> {
    @Override
    public SendMessage run(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatID = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);

        String readableCategoryName = update.getMessage().getText();

        Pattern pattern = Pattern.compile("[^а-яa-z0-9/.,!№()\\s]");
        Matcher matcher = pattern.matcher(readableCategoryName);

        if (matcher.find()) {
            sendMessage.setText(MessageName.INVALID_CATEGORY_NAME.getMessageName());
            return sendMessage;
        }

        /*
         * readableCategoryName далее используется для отображения в mini app
         * категории в приложении отображаются согласно всем перечисленным readableCategoryName в query params
         * если там будет встречаться некорректный символ, то отображение в mini app поломается */

        String categoryKey = readableCategoryName.toLowerCase().replaceAll("\\s", "");

        CategoryInfo categoryInfo =
                CategoryInfo.builder()
                        .categoryKey(categoryKey)
                        .chatID(chatID)
                        .readableCategoryName(readableCategoryName)
                        .amount(0d)
                        .build();

        try {
            botService.saveCategoryInfo(categoryInfo);
        } catch (DataIntegrityViolationException e) {
            sendMessage.setText(MessageName.UNIQUE_CONSTRAINT_ERROR_MESSAGE.getMessageName());
            return sendMessage;
        }

        sendMessage.setText("Категория " + readableCategoryName + " успешно добавлена");

        CallbackDataByChatID callbackDataByChatID = botService.findCallbackDataByChatID(chatID);
        if (callbackDataByChatID != null
                && callbackDataByChatID.getCallbackData().equals("FromAddNewCategory")) {
            botService.deleteCallbackDataByChatID(chatID);
        }

        return sendMessage;
    }
}
