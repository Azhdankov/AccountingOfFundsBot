package ru.azhdankov.accountingOfFunds.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.botService.BotService;
import ru.azhdankov.accountingOfFunds.command.callback.AddSumCallbackImpl;
import ru.azhdankov.accountingOfFunds.command.callback.ApproveForClearDataCallbackImpl;
import ru.azhdankov.accountingOfFunds.command.callback.CheckRenamedCategoryCallbackImpl;
import ru.azhdankov.accountingOfFunds.command.callback.ClearDataCallbackImpl;
import ru.azhdankov.accountingOfFunds.command.impl.*;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatIDDAO;

@Component
public class CommandFactory {

    @Autowired private CallbackDataByChatIDDAO callbackDataByChatIDDAO;

    public List<Command<?>> getCommandList(Update update, BotService botService) {

        List<Command<?>> commandList = new ArrayList<>();

        if ((update.hasMessage() && update.getMessage().hasText()) || update.hasCallbackQuery()) {
            if (update.hasCallbackQuery()) {
                String callbackDataFromButton = update.getCallbackQuery().getData();
                switch (callbackDataFromButton) {
                    case String s when s.startsWith("AddSum"):
                        commandList.add(new AddSumCallbackImpl());
                        break;
                    case String s when s.startsWith("CheckRenamedCategory"):
                        commandList.add(new CheckRenamedCategoryCallbackImpl());
                        break;
                    case String s when s.startsWith("ApproveForClearData"):
                        commandList.add(new ApproveForClearDataCallbackImpl());
                        break;
                    case String s when s.startsWith("Remove"):
                        commandList.add(new ClearDataCallbackImpl());
                        break;
                    default:
                        break;
                }
            } else {
                String message = update.getMessage().getText();
                switch (message) {
                    case "/start":
                        commandList.add(new StartCommandImpl());
                        break;
                    case String s when s.startsWith("Все категории введены"):
                        commandList.add(new AllCategoriesEnteredCommandImpl());
                        break;
                    case String s when s.startsWith("Добавить новую категорию"):
                        commandList.add(new AddNewCategoryCommandImpl());
                        break;
                    case String s when s.contains("Вывести")
                            && s.contains("все категории на экран"):
                        commandList.add(new ShowPreLoadEmojiImpl());
                        commandList.add(new ShowAllCategoriesCommandImpl());
                        break;
                    case String s when s.startsWith("Добавить сумму на категорию"):
                        commandList.add(new AddSumToCategoryCommandImpl());
                        break;
                    case String s when s.startsWith("Переименовать категорию"):
                        commandList.add(new ChooseCategoryToRenameCommandImpl());
                        break;
                    case "Сумма за месяц":
                        commandList.add(new MonthSumCommandImpl());
                        break;
                    case "Очистить все данные":
                        commandList.add(new ClearAllDataCommandImpl());
                        break;
                    default:
                        String chatID = update.getMessage().getChatId().toString();
                        if (callbackDataByChatIDDAO.findById(chatID).isEmpty()) {
                            break;
                        }
                        String callbackData =
                                callbackDataByChatIDDAO.findById(chatID).get().getCallbackData();
                        if (callbackData.equals("FromAddSum")) {
                            commandList.add(new EnterTheAmountCommandImpl());
                            break;
                        } else if (callbackData.equals("FromCheckRenamedCategory")) {
                            commandList.add(new ApplyRenamedCategoryCommandImpl());
                            break;
                        } else if (callbackData.equals("FromAddNewCategory")
                                || callbackData.equals("FromStartCommand")
                                || callbackData.equals("FromApproveForClearData")) {
                            commandList.add(new SaveNewCategoryCommandImpl());
                            break;
                        }
                }
            }
            commandList.forEach(
                    e -> {
                        Objects.requireNonNull(e).setBotService(botService);
                    });
        }
        return commandList;
    }
}
