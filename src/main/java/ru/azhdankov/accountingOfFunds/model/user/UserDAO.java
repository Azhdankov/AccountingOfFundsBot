package ru.azhdankov.accountingOfFunds.model.user;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserDAO extends CrudRepository<User, String> {
    List<User> findAllByPairID(String pairID);
}
