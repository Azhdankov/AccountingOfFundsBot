package ru.azhdankov.accountingOfFunds.model.groupCategoryInfo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public interface GroupCategoryInfoDAO extends CrudRepository<GroupCategoryInfo, String> {
    List<GroupCategoryInfo> findAllByPairID(String pairID);

    @Transactional
    void deleteAllByPairID(String pairID);
}
