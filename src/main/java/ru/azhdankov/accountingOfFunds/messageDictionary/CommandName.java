package ru.azhdankov.accountingOfFunds.messageDictionary;

import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;

@Getter
public enum CommandName {
    CHART_COMMAND("Диаграмма трат"),
    ADD_NEW_CATEGORY_COMMAND(
            "Добавить новую категорию " + EmojiParser.parseToUnicode(":heavy_plus_sign:")),
    ADD_SUM_TO_CATEGORY_COMMAND(
            "Добавить сумму на категорию " + EmojiParser.parseToUnicode(":moneybag:")),
    PRINT_ALL_CATEGORY_TO_SCREEN_COMMAND(
            "Вывести " + EmojiParser.parseToUnicode(":printer:") + " все категории на экран"),
    RENAME_CATEGORY_COMMAND("Переименовать категорию " + EmojiParser.parseToUnicode(":keyboard:")),
    ALL_CATEGORIES_ENTERED_COMMAND("Все категории введены"),
    OPEN_APPLICATION_COMMAND("Открыть приложение " + EmojiParser.parseToUnicode(":bar_chart:")),
    MONTH_SUM_COMMAND("Сумма за месяц"),
    REMOVE_ALL_DATA_COMMAND("Очистить все данные");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }
}
