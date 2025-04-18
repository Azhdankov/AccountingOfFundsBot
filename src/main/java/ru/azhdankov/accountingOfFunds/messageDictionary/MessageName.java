package ru.azhdankov.accountingOfFunds.messageDictionary;

import lombok.Getter;

@Getter
public enum MessageName {
    WAIT_NEW_CATEGORY_MESSAGE("Ожидаю введения новой категории"),
    NO_CATEGORY_FOR_RENAME_MESSAGE("Нет категорий для переименования"),
    THERE_IS_NO_PAIR_FOR_START_MESSAGE("Ссылка для запуска невалидна"),
    CURRENT_MODE_WAS_CHANGED_TO_GROUP_MESSAGE("Режим был изменен на групповой"),
    GROUP_MODE_WAS_APPLIED_MESSAGE("Групповой режим был применен"),
    PREVIOUS_GROUP_WAS_REMOVED_MESSAGE(
            "Привязка была изменена на новую группу. "
                    + "Если хотите вернуть предыдущую группу, то запросите ссылку у членов предыдущей группы"),
    WELCOME_FROM_LINK(
            "Добро пожаловать! "
                    + "\n"
                    + "Вы можете использовать команду /help, чтобы узнать больше об этом боте"),
    MODE_WASNT_CHANGED("Режим не был изменён"),
    MODE_WAS_CHANGED_TO_SINGLE("Режим был изменён на одиночное использование"),
    MODE_WAS_CHANGED_TO_GROUP(
            "Режим был изменен на групповое использование"
                    + "\n"
                    + "Отправьте ссылку пользователям, которые будут использовать бота вместе с вами"),
    CHANGE_MODE(
            "Вы можете использовать режим для одного пользователя или для группы"
                    + "\n"
                    + "Соответствующий режим можете выбрать ниже"),
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
    AMOUNT_IS_NOT_CORRECT("Некорректный формат суммы"),
    HELP_MESSAGE(
            "Это бот для учёта трат \uD83D\uDCB8\n"
                    + "\n"
                    + "Теперь вы сможете вести учёт своих расходов в одном месте! Просто добавляйте сюда информацию о своих затратах, и в любой момент сможете проанализировать свои расходы\n"
                    + "\n"
                    + "Если вы были приглашены через ссылку, то режим по умолчанию будет групповым. Это значит, что все добавленные категории, "
                    + "изменение трат и переименования будут видны остальным пользователям, которые находятся в общей группе с вами. Если "
                    + "вы захотите сменить режим на одиночный, используйте команду из меню - /mode"
                    + "\n"
                    + "\n"
                    + "Если вы сами хотите создать новую группу для учёта совместных трат, то вы так же можете переключить режим через команду из меню - /mode"
                    + "\n"
                    + "Поехали!\uD83E\uDD11");

    private final String messageName;

    MessageName(String messageName) {
        this.messageName = messageName;
    }
}
