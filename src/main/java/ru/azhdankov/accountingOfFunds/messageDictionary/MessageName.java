package ru.azhdankov.accountingOfFunds.messageDictionary;

import lombok.Getter;

@Getter
public enum MessageName {
    WAIT_NEW_CATEGORY_MESSAGE("Ожидаю введения новой категории"),
    NO_CATEGORY_FOR_RENAME_MESSAGE("Нет категорий для переименования"),
    INVALID_CATEGORY_NAME("Присутствуют невалидные символы в названии категории"),
    THERE_ARE_NO_CATEGORIES_TO_SHOW("Нет трат для отображения"),
    NO_CATEGORY_FOR_ADD_SUM_MESSAGE("Нет категории для добавления суммы трат"),
    FIRST_MESSAGE_BOT_MESSAGE(
            "Давайте добавим категории трат..."
                    + "\n"
                    + "Отправляйте по одной название категории. По завершению, нажмите кнопку \"Все категории введены\" "),
    AFTER_CATEGORIES_ADDED_MESSAGE(
            "Отлично, я всё добавил. Если захотите добавить что-то новое, просто нажмите на соответствующую кнопку"),
    CHOOSE_CATEGORY_NAME_FOR_NEXT_RENAMING("Выберите название категории для переименования"),
    MONTH_AMOUNT_MESSAGE("Всего за месяц потрачено"),
    CATEGORY_WAS_RENAMED_MESSAGE("Категория переименована"),
    ARE_YOU_SURE_DELETE_ALL_AMOUNTS_MESSAGE(
            "Вы действительно хотите удалить все данные по тратам?"),
    DISCARD_MESSAGE("Отменено"),
    SUCCESS_REMOVED(
            "Успешно очищено." + "\n" + "Теперь вы можете снова добавить необходимые категории"),
    AMOUNT_SUCCESSFULLY_WAS_ADDED("Сумма успешно добавлена в категорию "),
    UNIQUE_CONSTRAINT_ERROR_MESSAGE(
            "Данная категория уже существует. Давайте попробуем что-то ещё \uD83D\uDE43"),
    CHOOSE_CATEGORY_NAME_MESSAGE("Выберите название категории"),
    AMOUNT_IS_NOT_CORRECT("Некорректный формат суммы");

    private final String messageName;

    MessageName(String messageName) {
        this.messageName = messageName;
    }
}
