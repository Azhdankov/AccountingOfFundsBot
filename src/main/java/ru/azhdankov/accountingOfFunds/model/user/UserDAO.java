package ru.azhdankov.accountingOfFunds.model.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserDAO extends CrudRepository<User, String> {
    List<User> findAllByPairID(String pairID);
}
