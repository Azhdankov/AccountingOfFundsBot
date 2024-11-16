package ru.azhdankov.accountingOfFunds.model.categoryInfo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public interface CategoryInfoDAO extends CrudRepository<CategoryInfo, String> {
    List<CategoryInfo> findAllByChatID(String chatID);

    CategoryInfo getCategoryInfoByChatIDAndCategoryKey(String chatID, String categoryKey);

    @Transactional
    void deleteAllByChatID(String chatID);
}
