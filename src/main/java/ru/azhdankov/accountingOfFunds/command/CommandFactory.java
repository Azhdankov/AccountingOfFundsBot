package ru.azhdankov.accountingOfFunds.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.botService.BotService;
import ru.azhdankov.accountingOfFunds.command.callback.*;
import ru.azhdankov.accountingOfFunds.command.impl.*;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatIDDAO;

@Component
public class CommandFactory {

    @Autowired private CallbackDataByChatIDDAO callbackDataByChatIDDAO;

    public HashMap<Command<?>, Boolean> getCommandMap(Update update, BotService botService) {

        HashMap<Command<?>, Boolean> commandMap = new HashMap<>();

        if ((update.hasMessage() && update.getMessage().hasText()) || update.hasCallbackQuery()) {
            if (update.hasCallbackQuery()) {
                String callbackDataFromButton = update.getCallbackQuery().getData();
                switch (callbackDataFromButton) {
                    case String s when s.startsWith("AddSum"):
                        commandMap.put(new AddSumCallbackImpl(), false);
                        break;
                    case String s when s.startsWith("CheckRenamedCategory"):
                        commandMap.put(new CheckRenamedCategoryCallbackImpl(), false);
                        break;
                    case String s when s.startsWith("ApproveForClearData"):
                        commandMap.put(new ApproveForClearDataCallbackImpl(), false);
                        break;
                    case String s when s.startsWith("Remove"):
                        commandMap.put(new ClearDataCallbackImpl(), false);
                        break;
                    case String s when s.startsWith("NextPage_") || s.startsWith("PreviousPage_"):
                        commandMap.put(new NextPreviousInlineKeyboardCallbackImpl(), false);
                        break;
                    case String s when s.startsWith("ChangeMode"):
                        commandMap.put(new ChangeModeCallbackImpl(), false);
                        commandMap.put(new ChangeModeEmojiCallbackImpl(), false);
                        break;
                    default:
                        break;
                }
            } else {
                String message = update.getMessage().getText();
                switch (message) {
                    case "/start":
                        commandMap.put(new StartCommandImpl(), false);
                        break;
                    case String s when s.startsWith("/start"):
                        commandMap.put(new StartFromLinkCommandImpl(), false);
                        break;
                    case "/mode":
                        commandMap.put(new ChangeModeCommandImpl(), false);
                        break;
                    case "/help":
                        commandMap.put(new HelpCommandImpl(), false);
                        break;
                    case String s when s.startsWith("Все категории введены"):
                        commandMap.put(new AllCategoriesEnteredCommandImpl(), false);
                        break;
                    case String s when s.startsWith("Добавить новую категорию"):
                        commandMap.put(new AddNewCategoryCommandImpl(), false);
                        break;
                    case String s when s.contains("Вывести")
                            && s.contains("все категории на экран"):
                        commandMap.put(new ShowPreLoadEmojiImpl(), true);
                        commandMap.put(new ShowAllCategoriesCommandImpl(), false);
                        break;
                    case String s when s.startsWith("Добавить сумму на категорию"):
                        commandMap.put(new AddSumToCategoryCommandImpl(), false);
                        break;
                    case String s when s.startsWith("Переименовать категорию"):
                        commandMap.put(new ChooseCategoryToRenameCommandImpl(), false);
                        break;
                    case "Сумма за месяц":
                        commandMap.put(new MonthSumCommandImpl(), false);
                        break;
                    case "Очистить все данные":
                        commandMap.put(new ClearAllDataCommandImpl(), false);
                        break;
                    default:
                        String chatID = update.getMessage().getChatId().toString();
                        if (callbackDataByChatIDDAO.findById(chatID).isEmpty()) {
                            break;
                        }
                        String callbackData =
                                callbackDataByChatIDDAO.findById(chatID).get().getCallbackData();
                        if (callbackData.equals("FromAddSum")) {
                            commandMap.put(new EnterTheAmountCommandImpl(), false);
                            break;
                        } else if (callbackData.equals("FromCheckRenamedCategory")) {
                            commandMap.put(new ApplyRenamedCategoryCommandImpl(), false);
                            break;
                        } else if (callbackData.equals("FromAddNewCategory")
                                || callbackData.equals("FromStartCommand")
                                || callbackData.equals("FromApproveForClearData")) {
                            commandMap.put(new SaveNewCategoryCommandImpl(), false);
                            break;
                        }
                }
            }
            commandMap.forEach(
                    (k,v) -> {
                        Objects.requireNonNull(k).setBotService(botService);
                    });
        }
        return commandMap;
    }
}
