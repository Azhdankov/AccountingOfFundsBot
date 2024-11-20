package ru.azhdankov.accountingOfFunds.botService;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatID;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatIDDAO;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfo;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfoDAO;
import ru.azhdankov.accountingOfFunds.model.groupCategoryInfo.GroupCategoryInfo;
import ru.azhdankov.accountingOfFunds.model.groupCategoryInfo.GroupCategoryInfoDAO;
import ru.azhdankov.accountingOfFunds.model.user.User;
import ru.azhdankov.accountingOfFunds.model.user.UserDAO;

@Service
public class BotService {

    @Autowired private UserDAO userDao;
    @Autowired private CallbackDataByChatIDDAO callbackDataByChatIDDAODao;
    @Autowired private CategoryInfoDAO categoryInfoDAO;
    @Autowired private GroupCategoryInfoDAO groupCategoryInfoDAO;

    public List<User> findUsersByPairID(String pairID) {
        return userDao.findAllByPairID(pairID);
    }

    public User findUserByChatID(String chatID) {
        return userDao.findById(chatID).orElse(null);
    }

    public void saveUser(User user) {
        userDao.save(user);
    }

    public void saveCallbackData(CallbackDataByChatID callbackDataByChatID) {
        callbackDataByChatIDDAODao.save(callbackDataByChatID);
    }

    public List<CategoryInfo> findAllCategoryInfoByChatID(String chatID) {
        User user = findUserByChatID(chatID);
        if (!user.isSingleMode()) {
            List<CategoryInfo> categoryInfoList = new ArrayList<>();
            groupCategoryInfoDAO
                    .findAllByPairID(user.getPairID())
                    .forEach(
                            e ->
                                    categoryInfoList.add(
                                            CategoryInfo.builder()
                                                    .id(e.getId())
                                                    .categoryKey(e.getCategoryKey())
                                                    .chatID(e.getChatID())
                                                    .readableCategoryName(
                                                            e.getReadableCategoryName())
                                                    .amount(e.getAmount())
                                                    .build()));
            return categoryInfoList;
        }
        return categoryInfoDAO.findAllByChatID(chatID);
    }

    public void saveCategoryInfo(CategoryInfo categoryInfo) {
        User user = findUserByChatID(categoryInfo.getChatID());
        if (!user.isSingleMode()) {
            GroupCategoryInfo groupCategoryInfo =
                    GroupCategoryInfo.builder()
                            .id(categoryInfo.getId())
                            .readableCategoryName(categoryInfo.getReadableCategoryName())
                            .categoryKey(categoryInfo.getCategoryKey())
                            .amount(categoryInfo.getAmount())
                            .chatID(categoryInfo.getChatID())
                            .pairID(user.getPairID())
                            .build();
            groupCategoryInfoDAO.save(groupCategoryInfo);
        } else {
            categoryInfoDAO.save(categoryInfo);
        }
    }

    public CallbackDataByChatID findCallbackDataByChatID(String chatID) {
        return callbackDataByChatIDDAODao.findById(chatID).orElse(null);
    }

    public void deleteCallbackDataByChatID(String chatID) {
        callbackDataByChatIDDAODao.deleteById(chatID);
    }

    public CategoryInfo findCategoryInfoByID(String id) {
        if (categoryInfoDAO.findById(id).isPresent()) {
            return categoryInfoDAO.findById(id).get();
        } else {
            GroupCategoryInfo groupCategoryInfo = groupCategoryInfoDAO.findById(id).orElse(null);
            if (groupCategoryInfo != null) {
                return CategoryInfo.builder()
                        .id(groupCategoryInfo.getId())
                        .categoryKey(groupCategoryInfo.getCategoryKey())
                        .chatID(groupCategoryInfo.getChatID())
                        .readableCategoryName(groupCategoryInfo.getReadableCategoryName())
                        .amount(groupCategoryInfo.getAmount())
                        .build();
            }
        }
        return null;
    }

    public void deleteAllCategoryInfoByChatID(String chatID) {
        User user = findUserByChatID(chatID);
        if (!user.isSingleMode()) {
            groupCategoryInfoDAO.deleteAllByPairID(user.getPairID());
        } else {
            categoryInfoDAO.deleteAllByChatID(chatID);
        }
    }
}
