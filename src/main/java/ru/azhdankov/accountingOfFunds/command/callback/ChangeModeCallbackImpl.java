package ru.azhdankov.accountingOfFunds.command.callback;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.azhdankov.accountingOfFunds.command.Command;
import ru.azhdankov.accountingOfFunds.command.CommandHelper;
import ru.azhdankov.accountingOfFunds.messageDictionary.MessageName;
import ru.azhdankov.accountingOfFunds.model.user.User;

import java.util.UUID;

public class ChangeModeCallbackImpl extends CommandHelper implements Command<SendMessage> {

    private final String SINGLE_MODE = "SingleMode";

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
                sendMessage.setText(MessageName.MODE_WASNT_CHANGED.getMessageName());
            } else {
                if (user.getPairID() != null || user.getPairID().isEmpty()) {
                    botService.saveUser(User.builder()
                            .id(chatID)
                            .isSingleMode(true)
                            .pairID(user.getPairID())
                            .build());
                }
                sendMessage.setText(MessageName.MODE_WAS_CHANGED.getMessageName());
            }
        } else {
            System.out.println("tbd");
        }

        return sendMessage;
    }
}
