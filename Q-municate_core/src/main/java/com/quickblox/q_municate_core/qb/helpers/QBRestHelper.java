package com.quickblox.q_municate_core.qb.helpers;

import android.content.Context;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.q_municate_core.utils.ConstsCore;
import com.quickblox.q_municate_core.utils.UserFriendUtils;
import com.quickblox.q_municate_db.managers.DataManager;
import com.quickblox.q_municate_db.models.User;
import com.quickblox.q_municate_user_service.QMUserService;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.quickblox.users.model.QBUser;

import java.util.Collection;

public class QBRestHelper extends BaseHelper {

    public QBRestHelper(Context context) {
        super(context);
    }

    public static QMUser loadUser(int userId) {
        QMUser resultUser;

        try {
            //QBUser user = QBUsers. getUser(userId).perform();
            QMUser user = QMUserService.getInstance().getUserSync(userId, true);
            resultUser = user;
        } catch (QBResponseException e) {
            // user not found
            resultUser = UserFriendUtils.createDeletedUser(userId);
        }

        return resultUser;
    }

    public static QMUser loadAndSaveUser(int userId) {
        QMUser resultUser = null;

        try {
            //QBUser user = QBUsers.getUser(userId).perform();
            QMUser user = QMUserService.getInstance().getUserSync(userId, true);
            resultUser = user;
        } catch (QBResponseException e) {
            // user not found
            resultUser = UserFriendUtils.createDeletedUser(userId);
        }

        DataManager.getInstance().getUserDataManager().createOrUpdate(resultUser, true);

        return resultUser;
    }

    public Collection<QMUser> loadUsers(Collection<Integer> usersIdsList) throws QBResponseException {
        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
        requestBuilder.setPage(ConstsCore.USERS_PAGE_NUM);
        requestBuilder.setPerPage(ConstsCore.USERS_PER_PAGE);
        //Collection<QBUser> usersList = QBUsers.getUsersByIDs(usersIdsList, requestBuilder, new Bundle()).perform();
        Collection<QMUser> usersList = QMUserService.getInstance().getUsersByIDsSync(usersIdsList, requestBuilder);
        return usersList;
    }
}