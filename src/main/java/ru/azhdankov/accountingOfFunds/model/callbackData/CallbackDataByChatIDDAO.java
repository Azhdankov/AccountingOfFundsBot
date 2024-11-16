package ru.azhdankov.accountingOfFunds.model.callbackData;

import org.springframework.data.repository.CrudRepository;

public interface CallbackDataByChatIDDAO extends CrudRepository<CallbackDataByChatID, String> {}
