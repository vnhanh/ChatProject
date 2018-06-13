package vn.edu.hcmute.ms14110050.chatproject.module.chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.chatproject.base.MyObservable;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.common.asynctask.ConvertBundleFirebaseValue;
import vn.edu.hcmute.ms14110050.chatproject.common.asynctask.ConvertFirebaseValue;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags;

import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.MESSAGE_EVENT.TAG_CANCELED_MESSAGE;

/**
 * Created by Vo Ngoc Hanh on 6/8/2018.
 */

public class MessagesHandler extends MyObservable implements ChildEventListener {
    private int index = 0;
    private volatile boolean isHandlingMessage = false;
    // hàng chờ các gói DataSnapshot
    private ArrayList<SendObject> waitedDataSnapshotQueue = new ArrayList<>();
    // hàng chờ các gói tin nhắn đã được convert
    private ArrayList<SendObject> waitedMessagesQueue = new ArrayList<>();
    // mảng ghi chép các index đã được gửi
    private ArrayList<Integer> sendedIndex = new ArrayList<>();

    private synchronized boolean isHandlingMessage() {
        return isHandlingMessage;
    }

    private synchronized void setHandlingMessageStatus(boolean status) {
        isHandlingMessage = status;
    }

    // Nhận và xử lý gói tin nhắn mới được convert
    private synchronized void handleMessage(SendObject sendObject) {
        isHandlingMessage = true;
        int newIndex = sendObject.getTag();
        // nếu chỉ số bằng với kích thước sendedIndex tức là nằm ngay sau gói được gửi gần nhất
        if (sendedIndex != null || index == sendedIndex.size()) {
            // gửi luôn
            Message message = (Message) sendObject.getValue();
            sendMessage(message, ChatTags.MESSAGE_EVENT.TAG_ADD_MESSAGE);
            sendedIndex.add(index);
        }else{
            // duyệt hàng chờ các tin nhắn đã được convert
            // gửi tất cả các tin nhắn có thể gửi được
            // duyệt từ tin nhắn có index nhỏ nhất
            // thoát ngay nếu duyệt đến tin nhắn không thể gửi được
            // vì những tin nhắn xếp sau chắc chắn cũng không thể gửi được
            int size = waitedMessagesQueue.size();
            boolean isNeedHandleMessage = true;
            boolean isNotYetAddNewMessageBundle = true;
            int i = 0;
            do {
                SendObject _item = waitedMessagesQueue.get(i);
                if (isNeedHandleMessage && checkAndSendMessageSendable(_item)) {
                    waitedMessagesQueue.remove(i);
                    if (sendedIndex == null) {
                        sendedIndex = new ArrayList<>();
                    }
                    sendedIndex.add(_item.getTag());
                } else {
                    isNeedHandleMessage = false;
                    if (i == size) {
                        waitedMessagesQueue.add(sendObject);
                        isNotYetAddNewMessageBundle = false;
                    } else if (_item.getTag() > newIndex) {
                        waitedMessagesQueue.add(i, sendObject);
                        isNotYetAddNewMessageBundle = false;
                    }
                    i ++;
                }
            } while (isNeedHandleMessage || isNotYetAddNewMessageBundle);
        }
        isHandlingMessage = false;
    }

    private synchronized boolean checkAndSendMessageSendable(SendObject sendObject) {
        int _index = sendObject.getTag();
        if (sendedIndex != null && _index == sendedIndex.size()) {
            // gửi luôn
            Message message = (Message) sendObject.getValue();
            sendMessage(message, ChatTags.MESSAGE_EVENT.TAG_ADD_MESSAGE);
            sendedIndex.add(_index);
            return true;
        }
        return false;
    }

    private void sendMessage(Message message, @ChatTags.MESSAGE_EVENT int tag) {
        SendObject _sendObject = new SendObject(tag, message);
        notify(_sendObject);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (dataSnapshot.getValue() != null) {
            new ConvertBundleFirebaseValue<Message>(new GetCallback<SendObject>() {
                @Override
                public void onFinish(SendObject sendObject) {
                    // chờ cho đến khi xử lý xong hàng đợi tin nhắn
                    while (isHandlingMessage) {
                    }
                    handleMessage(sendObject);
                }
            }, new GenericTypeIndicator<Message>(){}).execute(new SendObject(index++,dataSnapshot));
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (dataSnapshot.getValue() != null) {
            new ConvertFirebaseValue<Message>(new GetCallback<Message>() {
                @Override
                public void onFinish(Message message) {
                    sendMessage(message, ChatTags.MESSAGE_EVENT.TAG_CHANGE_MESSAGE);
                }
            }, new GenericTypeIndicator<Message>(){}).execute(dataSnapshot);
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            new ConvertFirebaseValue<Message>(new GetCallback<Message>() {
                @Override
                public void onFinish(Message message) {
                    sendMessage(message, ChatTags.MESSAGE_EVENT.TAG_REMOVE_MESSAGE);
                }
            }, new GenericTypeIndicator<Message>(){}).execute(dataSnapshot);
        }
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        SendObject sendObject = new SendObject(TAG_CANCELED_MESSAGE, "");
        notify(sendObject);
    }
}
