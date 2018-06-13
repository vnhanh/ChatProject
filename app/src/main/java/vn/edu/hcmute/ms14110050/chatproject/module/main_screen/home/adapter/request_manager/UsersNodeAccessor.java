package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.request_manager;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.chatproject.base.MyObservable;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.CheckCallBack;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.common.asynctask.ConvertFirebaseValue;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class UsersNodeAccessor extends MyObservable {
    User user;


    public void checkChatterExist(@NonNull final String chatterUserUid, final CheckCallBack<Void, ArrayList<String>, Void> callback) {
        if (checkUnavailable()) {
            return;
        }
        UsersNodeUtil.checkChatterExist(user.getUid(), chatterUserUid, new CheckCallBack<Void, Void, Void>() {
            @Override
            public void onRight(Void data) {
                callback.onRight(null);
            }

            @Override
            public void onWrong(Void data) {
                ArrayList<String> list = new ArrayList<>();
                list.add(user.getUid());
                list.add(chatterUserUid);
                callback.onWrong(list);
            }

            @Override
            public void onError(Void data) {
                callback.onError(null);
            }
        });
    }

    public void addFriendForChatter(String chatterUid, final SimpleCallback<Void> callback) {
        if (checkUnavailable()) {
            return;
        }

        final boolean[] checkUserFlags = {false, false};
        final boolean[] checkChatterUserFlags = {false, false};

        // add friend chatter user trong value của user
        UsersNodeUtil.addFriendForChatter(user.getUid(), chatterUid, new SimpleCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                checkUserFlags[0] = true;
                checkUserFlags[1] = true;
                checkFlags();
            }

            @Override
            public void onError() {
                checkUserFlags[0] = false;
                checkUserFlags[1] = true;
                checkFlags();
            }

            private void checkFlags() {
                if (checkUserFlags[1] && checkChatterUserFlags[1]) {
                    if (checkUserFlags[0] && checkChatterUserFlags[0]) {
                        callback.onSuccess(null);
                    }else{
                        callback.onError();
                    }
                }
            }
        });

        UsersNodeUtil.addFriendForChatter(chatterUid, user.getUid(),  new SimpleCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                checkChatterUserFlags[0] = true;
                checkChatterUserFlags[1] = true;
                checkFlags();
            }

            @Override
            public void onError() {
                checkChatterUserFlags[0] = false;
                checkChatterUserFlags[1] = true;
                checkFlags();
            }

            private void checkFlags() {
                if (checkUserFlags[1] && checkChatterUserFlags[1]) {
                    if (checkUserFlags[0] && checkChatterUserFlags[0]) {
                        callback.onSuccess(null);
                    }else{
                        callback.onError();
                    }
                }
            }
        });
    }

    // Tạo thông tin node chatters cho 2 user
    public void createChattersValue(String chatterUserUid, final String chatID, final SimpleCallback<Void> callback) {
        // load thông tin hồ sơ của bạn chat
        UsersNodeUtil.loadUserProfile(chatterUserUid, new  SimpleCallback<DataSnapshot>(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<User> type = new GenericTypeIndicator<User>(){};
                // asynctask convert DataSnapshot sang thông tin hồ sơ bạn chat
                new ConvertFirebaseValue<User>(new GetCallback<User>() {
                    @Override
                    public void onFinish(User chatterUser) {
                        onCreateChattersNodeForTwoUsers(chatterUser, chatID, callback);
                    }
                }, type)
                        .execute(dataSnapshot);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    private void onCreateChattersNodeForTwoUsers(User chatterUser, String chatID, final SimpleCallback<Void> callback) {
        // cờ kiểm tra đã tạo 2 chatters node thành công hay chưa
        // phần tử 0 và 1 lần lượt xác định tạo chatters node cho user có thành công hay không VÀ đã xong tác vụ tạo hay chưa
        final boolean[] checkUserFlags = {false, false};
        final boolean[] checkChatterUserFlags = {false, false};

        // tạo dữ liệu chatters node cho bạn chat
        UsersNodeUtil.createChatterNodeFromAnotherUser(user.getUid(), chatterUser, chatID, new SimpleCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                checkChatterUserFlags[0] = true;
                checkChatterUserFlags[1] = true;
                checkFlags();
            }

            @Override
            public void onError() {
                checkChatterUserFlags[0] = false;
                checkChatterUserFlags[1] = true;
                checkFlags();
            }

            private void checkFlags() {
                // đã hoàn thành tác vụ tạo cho 2 chatters
                if (checkUserFlags[1] && checkChatterUserFlags[1]) {
                    // tác vụ chỉ tính là thành công khi cả 2 chatters node được tạo thành công
                    if (checkUserFlags[0] && checkChatterUserFlags[0]) {
                        callback.onSuccess(null);
                    }else{
                        callback.onError();
                    }
                }
            }
        });

        // tạo dữ liệu chatters node cho bạn chat
        UsersNodeUtil.createChatterNodeFromAnotherUser(chatterUser.getUid(), user, chatID, new SimpleCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                checkUserFlags[0] = true;
                checkUserFlags[1] = true;
                checkFlags();
            }

            @Override
            public void onError() {
                checkUserFlags[0] = false;
                checkUserFlags[1] = true;
                checkFlags();
            }

            private void checkFlags() {
                // đã hoàn thành tác vụ tạo cho 2 chatters
                if (checkUserFlags[1] && checkChatterUserFlags[1]) {
                    // tác vụ chỉ tính là thành công khi cả 2 chatters node được tạo thành công
                    if (checkUserFlags[0] && checkChatterUserFlags[0]) {
                        callback.onSuccess(null);
                    }else{
                        callback.onError();
                    }
                }
            }
        });
    }

    public void onUpateUserProfile(User user) {
        this.user = user;
    }

    private boolean checkUnavailable() {
        return user == null || user.getUid() == null;
    }
}
