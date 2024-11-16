package ru.azhdankov.accountingOfFunds.commonData;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public record CommonDataForCommand(
        SendMessage sendMessage, String callbackData, String message, String chatID) {}
