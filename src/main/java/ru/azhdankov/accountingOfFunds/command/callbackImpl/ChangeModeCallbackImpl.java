package ru.azhdankov.accountingOfFunds.command.callbackImpl;

import static ru.azhdankov.accountingOfFunds.messageDictionary.MessageName.*;

import java.util.UUID;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.model.user.User;

public class ChangeModeCallbackImpl extends CommandHelper implements Command<SendMessage> {

    private final String SINGLE_MODE = "SingleMode";
    private final String URL = "https://t.me/AccountingOfFundsBot?start=";

    @Override
    public SendMessage run(Update update) {

        SendMessage sendMessage = new SendMessage();
        String callbackData = update.getCallbackQuery().getData();
        String chatID = update.getCallbackQuery().getMessage().getChatId().toString();
        sendMessage.setChatId(chatID);

        String modeFromCallbackData = callbackData.substring(callbackData.indexOf("_") + 1);
        User user = botService.findUserByChatID(chatID);

        if (modeFromCallbackData.equals(SINGLE_MODE)) {
            if (user.isSingleMode()) {
                sendMessage.setText(MODE_WASNT_CHANGED.getMessageName());
            } else {
                botService.saveUser(
                        User.builder()
                                .id(chatID)
                                .isSingleMode(true)
                                .pairID(user.getPairID())
                                .build());
                sendMessage.setText(MODE_WAS_CHANGED_TO_SINGLE.getMessageName());
            }
        } else {
            if (user.isSingleMode()) {
                user.setSingleMode(false);
                if (user.getPairID() == null || user.getPairID().isEmpty()) {
                    user.setPairID(UUID.randomUUID().toString());
                }
                botService.saveUser(user);
                String message =
                        MODE_WAS_CHANGED_TO_GROUP.getMessageName() + "\n" + URL + user.getPairID();
                sendMessage.setText(message);
            } else {
                sendMessage.setText(MODE_WASNT_CHANGED.getMessageName());
            }
        }
        return sendMessage;
    }
}
