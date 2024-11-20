package ru.azhdankov.accountingOfFunds.model.groupCategoryInfo;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GroupCategoryInfoDAO extends CrudRepository<GroupCategoryInfo, String> {
    List<GroupCategoryInfo> findAllByPairID(String pairID);

    @Transactional
    void deleteAllByPairID(String pairID);
}
