package vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Observable;

import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;

import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_CREATED_OR_UPDATED_CHATTERS_NODE_SUCCESS;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_LOADED_TWO_USERS_SUCCESS;

/**
 * Created by Vo Ngoc Hanh on 6/7/2018.
 */

public class UsersNodeManager extends Observable {
    DatabaseReference userRef, chatterUserRef;
    String uid, chatterUid;
    User user, chatterUser;

    boolean isLoadUser = false;
    boolean isLoadChatterUser = false;

    public UsersNodeManager() {

    }
    ResponseLoadUsers lastResponse;
    // Kiểm tra đã load xong dữ liệu của người dùng và bạn chat chưa
    private void checkLoadTwoUsersSuccess() {
        if (isLoadUser && isLoadChatterUser) {
            // get chatID (nếu có)
            String chatId = getChatIDFromChatterNode();

            // notify observers đã load xong
            ResponseLoadUsers response = new ResponseLoadUsers(user.getUid(), chatterUser.getUrlImage(), chatId);
            if (lastResponse == null || !lastResponse.equals(response)) {
                lastResponse = response;
                SendObject sendObject = new SendObject();
                sendObject.setTag(TAG_LOADED_TWO_USERS_SUCCESS);
                sendObject.setValue(response);
                notify(sendObject);
            }
        }
    }

    public void onReceiveChatterUserUid(String chatterUid) {
        Log.d("LOG", getClass().getSimpleName() + ":onReceiveChatterUserUid():" + chatterUid);
        this.chatterUid = chatterUid;
        createAnotherUserReference();
        chatterUserRef.addValueEventListener(chatterUserValueListener);
    }

    private void createAnotherUserReference() {
        if (!StringUtils.isEmpty(chatterUid)) {
            chatterUserRef = FirebaseDatabase.getInstance().getReference().child(FIREBASE.NODE_USERS).child(chatterUid);
        }
    }


    public void onReceiveUserUid(String uid) {
        Log.d("LOG", getClass().getSimpleName() + ":onReceiveUserUid():" + uid);
        this.uid = uid;
        createUserReference();
        userRef.addValueEventListener(userValueListener);
    }

    private void createUserReference() {
        if (!StringUtils.isEmpty(uid)) {
            userRef = FirebaseDatabase.getInstance().getReference().child(FIREBASE.NODE_USERS).child(uid);
        }
    }

    private ValueEventListener userValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                isLoadUser = true;
                user = dataSnapshot.getValue(User.class);
                Log.d("LOG", getClass().getSimpleName() + ":get chatter user data:" + user.getUid());
            }else{
                user = null;
            }
            checkLoadTwoUsersSuccess();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            user = null;
            checkLoadTwoUsersSuccess();
        }
    };

    private ValueEventListener chatterUserValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                isLoadChatterUser = true;
                chatterUser = dataSnapshot.getValue(User.class);
                Log.d("LOG", getClass().getSimpleName() + ":get chatter user data:" + chatterUser.getUid());
            }else{
                chatterUser = null;
            }
            checkLoadTwoUsersSuccess();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            chatterUser = null;
            checkLoadTwoUsersSuccess();
        }
    };

    boolean isUpdatedChatterNode1Success = false;
    boolean isUpdatedChatterNode2Success = false;

    public void updateChattersNode(final String existedChatID) {
        Log.d("LOG", getClass().getSimpleName() + ":updateChattersNode()");
        DatabaseReference ref1 = userRef.child(FIREBASE.NODE_CHATTERS).child(chatterUser.getUid());
        ChattersNodeUtil.updateTwoChattersNode(ref1, chatterUser);

        DatabaseReference ref2 = chatterUserRef.child(FIREBASE.NODE_CHATTERS).child(user.getUid());
        ChattersNodeUtil.updateTwoChattersNode(ref2, user);

        isUpdatedChatterNode1Success = true;
        isUpdatedChatterNode2Success = true;
        checkCreateOrUpdateChattersNode(existedChatID);
    }

    public void createChattersNode(String chatID) {
        Log.d("LOG", getClass().getSimpleName() + ":createChattersNode()");
        DatabaseReference ref1 = userRef.child(FIREBASE.NODE_CHATTERS);
        ChattersNodeUtil.createChatterNodeFromAnotherUser(ref1, chatterUser, chatID, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    isUpdatedChatterNode1Success = true;
                    // tạo mới chatter với chatID lấy từ nguồn khác nên trả về null
                    // để đánh dấu
                    checkCreateOrUpdateChattersNode(null);
                }
            }
        });


        DatabaseReference ref2 = chatterUserRef.child(FIREBASE.NODE_CHATTERS);
        ChattersNodeUtil.createChatterNodeFromAnotherUser(ref2, user, chatID, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    isUpdatedChatterNode2Success = true;
                    // tạo mới chatter với chatID lấy từ nguồn khác nên trả về null
                    // để đánh dấu
                    checkCreateOrUpdateChattersNode(null);
                }
            }
        });
    }

    // Trả về chatID nếu chatter node đã tồn tại cho chatterUser và ngược lại trả về null
    private String getChatIDFromChatterNode() {
        if (user != null) {
            Map<String, Chatter> chatterMap = user.getChatters();
            if (chatterMap != null) {
                boolean chatterExist = chatterMap.containsKey(chatterUser.getUid());
                if (chatterExist) {
                    return chatterMap.get(chatterUser.getUid()).getChatID();
                }
            }
        }
        return null;
    }

    private void checkCreateOrUpdateChattersNode(String chatID) {
        Log.d("LOG", getClass().getSimpleName() + ":checkCreateOrUpdateChattersNode()");
        if (isUpdatedChatterNode1Success && isUpdatedChatterNode2Success) {
            SendObject sendObject = new SendObject(TAG_CREATED_OR_UPDATED_CHATTERS_NODE_SUCCESS, chatID);
            notify(sendObject);
        }
    }

    private void notify(Object object) {
        setChanged();
        notifyObservers(object);
    }

    public void notifySendMessageSuccess(Message lastMessage) {
        ChattersNodeUtil.updateLastMessage(userRef, user.getUid(), chatterUser.getUid(), lastMessage, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        ChattersNodeUtil.updateLastMessage(chatterUserRef, chatterUser.getUid(), user.getUid(), lastMessage, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}
