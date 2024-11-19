package ru.azhdankov.accountingOfFunds.botService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatID;
import ru.azhdankov.accountingOfFunds.model.callbackData.CallbackDataByChatIDDAO;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfo;
import ru.azhdankov.accountingOfFunds.model.categoryInfo.CategoryInfoDAO;
import ru.azhdankov.accountingOfFunds.model.user.User;
import ru.azhdankov.accountingOfFunds.model.user.UserDAO;

@Service
public class BotService {

    @Autowired private UserDAO userDao;
    @Autowired private CallbackDataByChatIDDAO callbackDataByChatIDDAODao;
    @Autowired private CategoryInfoDAO categoryInfoDAO;

    private boolean isSingleMode() {
        return false;
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
        return categoryInfoDAO.findAllByChatID(chatID);
    }

    public void saveCategoryInfo(CategoryInfo categoryKey) {
        categoryInfoDAO.save(categoryKey);
    }

    public CallbackDataByChatID findCallbackDataByChatID(String chatID) {
        return callbackDataByChatIDDAODao.findById(chatID).orElse(null);
    }

    public void deleteCallbackDataByChatID(String chatID) {
        callbackDataByChatIDDAODao.deleteById(chatID);
    }

    public CategoryInfo findCategoryInfoByID(String id) {
        return categoryInfoDAO.findById(id).orElse(null);
    }

    public void deleteAllCategoryInfoByChatID(String chatID) {
        categoryInfoDAO.deleteAllByChatID(chatID);
    }
}
