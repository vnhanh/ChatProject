package vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;

import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_REQUEST_CREATE_CHATTERS_NODE_ON_FIRST_MESSAGE;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_REQUEST_UPDATE_CHATTERS_NODE_FOR_FIRST_MESSAGE;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_SEND_MESSAGE_FAILED;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_SEND_MESSAGE_SUCCESS;

/**
 * Created by Vo Ngoc Hanh on 6/7/2018.
 */

public class ChatsNodeManager extends Observable {
    /**
     * cờ xác định đã có thể gửi tin nhắn được chưa
     */
    boolean sendable = false;

    DatabaseReference messageRef;
    ChildEventListener messagesValueListener;

    String uid, chatterUserUid;

    // cờ đánh dấu đã yêu cầu tạo chatters node cho 2 user
    boolean isRequestedUpatedTwoChattersNode = false;
    ArrayList<Map<Integer,String>> waitedMessages = new ArrayList<>();
    String chatID;

    // cờ đánh dấu đang duyệt hàng chờ tin nhắn
    boolean isBrowsingWaitedMessage = false;

    public ChatsNodeManager(ChildEventListener messagesListener) {
        messagesValueListener = messagesListener;
    }

    private void requestCreateOrUpdateChattersNode() {
        Log.d("LOG", getClass().getSimpleName() + ":sendMessage():not sendable");
        if (!isRequestedUpatedTwoChattersNode) {
            SendObject sendObject = null;
            if (chatID == null) {
                // chưa có phòng chat
                chatID = FirebaseDatabase.getInstance().getReference().child(FIREBASE.CHATS).push().getKey();
                sendObject = new SendObject(TAG_REQUEST_CREATE_CHATTERS_NODE_ON_FIRST_MESSAGE, chatID);
            }else{
                // đã có chatID tức là đã có phòng chat
                // gửi yêu cầu update chatters node khi lần đầu gửi message (từ khi mở screen chat)
                sendObject = new SendObject(TAG_REQUEST_UPDATE_CHATTERS_NODE_FOR_FIRST_MESSAGE, chatID);
            }

            notify(sendObject);
            isRequestedUpatedTwoChattersNode = true;
        }
    }

    public void sendMessage(String message, @MESSAGE_TYPE int messageType) {
        Log.d("LOG", getClass().getSimpleName() + ":sendMessage()");
        if (!sendable) {
            requestCreateOrUpdateChattersNode();

            // thêm message vào hàng chờ
            addWaitedMessage(message, messageType);
            return;
        }

        Log.d("LOG", getClass().getSimpleName() + ":sendMessage():sendable:addWaitedMessage");

        // thêm message vào hàng chờ
        addWaitedMessage(message, messageType);
        // gửi tin nhắn trong hàng chờ (nếu có và đang trong trạng thái không duyệt)
        browseWaitedMessages();
    }

    // thêm message vào hàng chờ
    private void addWaitedMessage(String message, int messageType) {
        Map<Integer, String> map = new HashMap<>();
        map.put(messageType, message);
        waitedMessages.add(map);
    }

    // Đã update 2 chatters node của 2 user
    // và có thể gửi tin nhắn
    // chỉ setup messageRef tại đây
    public void notifyUpdateTwoChattersNodeSuccess(final String chatID) {
        Log.d("LOG", getClass().getSimpleName() + ":notifyUpdateTwoChattersNodeSuccess():chatID:" + chatID);
        if (!StringUtils.isEmpty(chatID)) {
            Log.d("LOG", getClass().getSimpleName() + ":notifyUpdateTwoChattersNodeSuccess():chat room was existed");
            // phòng chat đã có
            this.chatID = chatID;
            onNotifySendable();
        }else{
            Log.d("LOG", getClass().getSimpleName() + ":notifyUpdateTwoChattersNodeSuccess():chat room was not existed");
            // phòng chat chưa được tạo nhưng đã có chatID
            // tạo phòng chat
            ChatsNodeUtil.createChatRoom(this.chatID, uid, chatterUserUid, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        onNotifySendable();
                    }
                }
            });
        }
    }

    // Nhận biết đã có thể gửi tin nhắn
    private void onNotifySendable() {
        Log.d("LOG", getClass().getSimpleName() + ":onNotifySendable()");
        if (!sendable) {
            // bật cờ đánh dấu đã có thể gửi tin nhắn được
            sendable = true;

            messageRef = FirebaseDatabase.getInstance().getReference().child(FIREBASE.CHATS)
                    .child(this.chatID).child(FIREBASE.CHATS_MESSAGES);
            Log.d("LOG", getClass().getSimpleName() + ":onNotifySendable():messageRef: " + messageRef.toString());

            messageRef.addChildEventListener(messagesValueListener);

            // gửi tin nhắn ở hàng chờ (nếu có)
            browseWaitedMessages();
        }
    }

    private void browseWaitedMessages() {
        if (waitedMessages == null || isBrowsingWaitedMessage) {
            return;
        }
        Log.d("LOG", getClass().getSimpleName() + ":browseWaitedMessages:size of waited queue:" + waitedMessages.size());

        while (waitedMessages.size() > 0) {
            isBrowsingWaitedMessage = true;
            Map<Integer, String> map = waitedMessages.get(0);
            waitedMessages.remove(0);
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                Log.d("LOG", getClass().getSimpleName() + ":browseWaitedMessages():waitedMessages:for map:" + entry.getValue());
                _sendMessage(entry.getValue(), entry.getKey());
            }
        }

        isBrowsingWaitedMessage = false;
    }

    private void _sendMessage(String message, @MESSAGE_TYPE int messageType) {
        Log.d("LOG", getClass().getSimpleName() + ":_sendMessage():message:" + message);

        ChatsNodeUtil.createMessage(messageRef, chatID, uid, message, messageType, new SimpleCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                onSendMessageSuccess(message);
            }

            @Override
            public void onError() {
                onSendMessageFailed();
            }
        });
    }

    private void onSendMessageSuccess(Message message) {
        Log.d("LOG", getClass().getSimpleName() + ":onSendMessageSuccess():lastMessage:" + message.getContent());
        SendObject sendObject = new SendObject(TAG_SEND_MESSAGE_SUCCESS, message);
        notify(sendObject);
    }

    private void onSendMessageFailed() {
        SendObject sendObject = new SendObject();
        sendObject.setTag(TAG_SEND_MESSAGE_FAILED);
        notify(sendObject);
    }

    public void receiveUserUid(String uid) {
        this.uid = uid;
    }

    public void receiveChatterUserUid(String uid) {
        this.chatterUserUid = uid;
    }

    private void notify(Object object) {
        setChanged();
        notifyObservers(object);
    }

    // Get được chatID từ thông tin tài khoản người dùng
    public void notifyGetChatIDWhenLoadTwoUsersInfo(String chatID) {
        this.chatID = chatID;
        // yêu cầu tạo hoặc cập nhật dữ liệu của chatters
        if (!StringUtils.isEmpty(chatID)) {
            requestCreateOrUpdateChattersNode();
        }
    }
}
