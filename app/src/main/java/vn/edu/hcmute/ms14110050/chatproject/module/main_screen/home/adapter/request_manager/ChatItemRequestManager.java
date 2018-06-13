package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.request_manager;

import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import vn.edu.hcmute.ms14110050.chatproject.base.MyObservable;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.CheckCallBack;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;

import static vn.edu.hcmute.ms14110050.chatproject.common.constant.NODE_ACCESSOR.TAG_CHATTER_EXIST;
import static vn.edu.hcmute.ms14110050.chatproject.common.constant.NODE_ACCESSOR.TAG_CHATTER_NOT_EXIST;

/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class ChatItemRequestManager extends MyObservable implements Observer{
    private static ChatItemRequestManager INSTANCE;

    UsersNodeAccessor usersAccessor;
    ChatsNodeAccessor chatsAccessor;

    private ChatItemRequestManager() {
        createUsersManager();
        createChatsManager();
    }

    public static ChatItemRequestManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatItemRequestManager();
        }
        return INSTANCE;
    }

    private void createChatsManager() {
        if (chatsAccessor == null) {
            chatsAccessor = new ChatsNodeAccessor();
            chatsAccessor.addObserver(this);
        }
    }

    private void createUsersManager() {
        if (usersAccessor == null) {
            usersAccessor = new UsersNodeAccessor();
            usersAccessor.addObserver(this);
        }
    }

    public void onUpdateUserProfile(User user) {
        createUsersManager();
        usersAccessor.onUpateUserProfile(user);
    }

    /*
    * MAIN METHODS
    * */

    public void addFriend(final String chatterUid, final SimpleCallback<Void> callback) {
        // kiểm tra đã tồn tại value chatter node của bạn chat trong hồ sơ người dùng hay chưa
        usersAccessor.checkChatterExist(chatterUid, new CheckCallBack<Void, ArrayList<String>, Void>() {
            // đã tồn tại value của chatter node
            @Override
            public void onRight(Void aVoid) {
                usersAccessor.addFriendForChatter(chatterUid, callback);
            }

            // chưa tồn tại value chatter node của 2 user
            @Override
            public void onWrong(ArrayList<String> list) {
                String userUid = list.get(0);
                final String chatterUserUid = list.get(1);

                // tạo phòng chat
                chatsAccessor.createChatRoom(userUid, chatterUserUid, new SimpleCallback<String>() {
                    // tạo phòng chat thành công và lấy được chatID
                    @Override
                    public void onSuccess(String chatID) {
                        // tạo value chatter node cho người dùng và bạn chat
                        usersAccessor.createChattersValue(chatterUserUid, chatID, callback);
                    }

                    @Override
                    public void onError() {
                        Log.d("LOG", ChatItemRequestManager.this.getClass().getSimpleName()
                                + ":addFriend:checkChatterExist is false:createChatRoom():onError");
                        callback.onError();
                    }
                });
            }

            @Override
            public void onError(Void data) {
                Log.d("LOG", ChatItemRequestManager.this.getClass().getSimpleName() + ":checkChatterExist:onError");
                callback.onError();
            }
        });
    }

    /*
    * END MAIN METHODS
    * */

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof SendObject) {
            SendObject sendObject = (SendObject) o;
            int tag = sendObject.getTag();
            switch (tag) {
                case TAG_CHATTER_EXIST:

                    break;

                case TAG_CHATTER_NOT_EXIST:


                    break;
            }
        }
    }
}
