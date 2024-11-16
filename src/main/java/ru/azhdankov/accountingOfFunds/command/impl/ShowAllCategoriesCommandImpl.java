package ru.azhdankov.accountingOfFunds.command.impl;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.CommandName;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfo;

public class ShowAllCategoriesCommandImpl extends CommandHelper
        implements Command<EditMessageText> {
    @Override
    public EditMessageText run(Update update) {

        EditMessageText editMessage = new EditMessageText();
        String chatID = update.getMessage().getChatId().toString();
        editMessage.setChatId(chatID);
        botService.deleteCallbackDataByChatID(chatID);

        StringBuilder sb = new StringBuilder();
        List<CategoryInfo> categoryInfoList = botService.findAllCategoryInfoByChatID(chatID);

        if (categoryInfoList.isEmpty()) {
            editMessage.setText(MessageName.THERE_ARE_NO_CATEGORIES_TO_SHOW.getMessageName());
            return editMessage;
        }

        categoryInfoList.forEach(
                e ->
                        sb.append(e.getReadableCategoryName())
                                .append(": ")
                                .append(e.getAmount())
                                .append("\n"));

        String[] chart = {CommandName.CHART_COMMAND.getCommandName()};

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        List<List<InlineKeyboardButton>> inlineKeyboardRows = new ArrayList<>();
        inlineKeyboardRows.add(inlineKeyboardButtons);

        StringBuilder categories = new StringBuilder();
        StringBuilder amounts = new StringBuilder();

        botService
                .findAllCategoryInfoByChatID(chatID)
                .forEach(
                        e -> {
                            categories.append(e.getReadableCategoryName()).append(",");
                            amounts.append(e.getAmount()).append(",");
                        });

        String queryCategories = categories.substring(0, categories.length() - 1);
        String queryAmounts = amounts.substring(0, amounts.length() - 1);
        String queryParams = URLDecoder.decode(queryCategories, StandardCharsets.UTF_8);
        String queryValues = URLDecoder.decode(queryAmounts, StandardCharsets.UTF_8);

        String url =
                "https://azhdankov.github.io/AccountingOfFundsBotMiniApp/index.html"
                        + "?categories="
                        + queryParams
                        + "&amounts="
                        + queryValues;

        WebAppInfo webAppInfo = WebAppInfo.builder().url(url).build();

        InlineKeyboardButton inlineKeyboardButton =
                InlineKeyboardButton.builder()
                        .webApp(webAppInfo)
                        .text(CommandName.OPEN_APPLICATION_COMMAND.getCommandName())
                        .build();

        inlineKeyboardButtons.add(inlineKeyboardButton);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardRows);

        editMessage.setReplyMarkup(inlineKeyboardMarkup);
        editMessage.setText(sb.toString());
        return editMessage;
    }
}
