package vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;

/**
 * Created by Vo Ngoc Hanh on 6/8/2018.
 */

public class ChatsNodeUtil {

    public static void createChatRoom(String chatID, String userUid, String chatterUserUid, OnCompleteListener<Void> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE.CHATS);

        Map<String, Object> map = new HashMap<>();

        map.put(FIREBASE.CHAT_ID, chatID);
        map.put(FIREBASE.CHATS_UID1, userUid);
        map.put(FIREBASE.CHATS_UID2, chatterUserUid);

        ref.child(chatID).setValue(map).addOnCompleteListener(callback);
    }

    public static void createChatRoom(String userUid, String chatterUserUid, final SimpleCallback<String> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE.CHATS);
        final String chatID = ref.push().getKey();
        Map<String, Object> map = new HashMap<>();

        map.put(FIREBASE.CHAT_ID, chatID);
        map.put(FIREBASE.CHATS_UID1, userUid);
        map.put(FIREBASE.CHATS_UID2, chatterUserUid);

        ref.child(chatID).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess(chatID);
                }else{
                    callback.onError();
                }
            }
        });
    }

    public static void createMessage(DatabaseReference messageRef, String chatID,
                                        String senderUid,
                                        String message,
                                        @MESSAGE_TYPE int messageType,
                                        final SimpleCallback<Message> callback) {
        final String key = messageRef.push().getKey();

        Message newMessage = new Message();
        newMessage.setId(key);
        newMessage.setChatID(chatID);
        newMessage.setContent(message);
        newMessage.setSenderUid(senderUid);
        newMessage.setMessageType(messageType);

        messageRef.child(key).setValue(newMessage.toMap(true), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable final DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Message _message = dataSnapshot.getValue(Message.class);
                                callback.onSuccess(_message);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    callback.onError();
                }
            }
        });
    }
}
