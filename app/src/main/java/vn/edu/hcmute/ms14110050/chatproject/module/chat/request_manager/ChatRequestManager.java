package vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;

import java.util.Observable;
import java.util.Observer;

import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;

import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_CREATED_OR_UPDATED_CHATTERS_NODE_SUCCESS;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_LOADED_TWO_USERS_SUCCESS;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_REQUEST_CREATE_CHATTERS_NODE_ON_FIRST_MESSAGE;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_REQUEST_UPDATE_CHATTERS_NODE_FOR_FIRST_MESSAGE;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_SEND_MESSAGE_FAILED;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_SEND_MESSAGE_SUCCESS;

/**
 * Created by Vo Ngoc Hanh on 6/7/2018.
 *
 * TODO: cần lưu giữ thông tin của bạn chat và người dùng từ lúc đầu
 */

public class ChatRequestManager extends Observable implements Observer{
    UsersNodeManager usersManager;
    ChatsNodeManager chatsManager;

    public ChatRequestManager(ChildEventListener messageHandler){
        createUsersManager();
        createChatsManager(messageHandler);
    }

    public void sendMessage(String message, @MESSAGE_TYPE int messageType) {
        chatsManager.sendMessage(message, messageType);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof SendObject) {
            SendObject object = (SendObject) o;
            int tag = object.getTag();
            switch (tag) {
                /*
                * NOTIFY FROM USERSNODE
                * */
                case TAG_LOADED_TWO_USERS_SUCCESS:
                    // notify load two users success
                    notify(object);
                    if (chatsManager != null) {
                        ResponseLoadUsers response = (ResponseLoadUsers) object.getValue();
                        String chatID = response.getChatID();
                        if (!StringUtils.isEmpty(chatID)) {
                            chatsManager.notifyGetChatIDWhenLoadTwoUsersInfo(response.getChatID());
                        }
                    }
                    break;

                case TAG_CREATED_OR_UPDATED_CHATTERS_NODE_SUCCESS:
                    if (chatsManager != null) {
                        Log.d("LOG", getClass().getSimpleName() + ":TAG_CREATED_OR_UPDATED_CHATTERS_NODE_SUCCESS");
                        String chatID = (String) object.getValue();
                        chatsManager.notifyUpdateTwoChattersNodeSuccess(chatID);
                    }
                    break;

                /*
                * NOTIFY FROM CHATSNODE
                * */

                case TAG_REQUEST_UPDATE_CHATTERS_NODE_FOR_FIRST_MESSAGE:
                    if (usersManager != null) {
                        Log.d("LOG", getClass().getSimpleName() + ":TAG_REQUEST_UPDATE_CHATTERS_NODE_FOR_FIRST_MESSAGE");
                        String chatID = (String) object.getValue();
                        usersManager.updateChattersNode(chatID);
                    }
                    break;


                case TAG_REQUEST_CREATE_CHATTERS_NODE_ON_FIRST_MESSAGE:
                    if (usersManager != null) {
                        Log.d("LOG", getClass().getSimpleName() + ":TAG_REQUEST_CREATE_CHATTERS_NODE_ON_FIRST_MESSAGE");
                        String chatID = (String) object.getValue();
                        usersManager.createChattersNode(chatID);
                    }
                    break;

                case TAG_SEND_MESSAGE_SUCCESS:
                    if (usersManager != null) {
                        Message lastMessage = (Message) object.getValue();
                        usersManager.notifySendMessageSuccess(lastMessage);
                    }
                    break;

                case TAG_SEND_MESSAGE_FAILED:
                    notify(object);
                    break;
            }
        }
    }

    // on update user uid
    public void receiveUserUid(String uid) {
        usersManager.onReceiveUserUid(uid);
        // chỉ nhận uid của user tại đây
        chatsManager.receiveUserUid(uid);
    }

    public void receiveChatterUserUid(String chatterUserUid) {
        usersManager.onReceiveChatterUserUid(chatterUserUid);
        // chỉ nhận uid của bạn chat tại đây
        chatsManager.receiveChatterUserUid(chatterUserUid);
    }

    private void createUsersManager() {
        if (usersManager == null) {
            usersManager = new UsersNodeManager();
            usersManager.addObserver(this);
        }
    }

    private void createChatsManager(ChildEventListener messageHandler) {
        if (chatsManager == null) {
            chatsManager = new ChatsNodeManager(messageHandler);
            chatsManager.addObserver(this);
        }
    }

    private void notify(Object object) {
        setChanged();
        notifyObservers(object);
    }
}
