package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.request_manager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.CheckCallBack;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChattersNodeUtil;

import static vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE.NODE_CHATTERS_IS_FRIEND;

/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class UsersNodeUtil {
    public static void checkChatterExist(String userUid, String chatterUserUid, final CheckCallBack<Void, Void, Void> callback) {
        getReference(userUid, chatterUserUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            callback.onRight(null);
                        }else{
                            callback.onWrong(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError(null);
                    }
                });
    }

    public static void addFriendForChatter(String userUid, String chatterUserUid, final SimpleCallback<Void> callback) {
        getReference(userUid, chatterUserUid)
                .child(NODE_CHATTERS_IS_FRIEND)
                .setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(null);
                        }else{
                            callback.onError();
                        }
                    }
                });
    }

    public static void createChatterNodeFromAnotherUser(String userUid, User chatterUser,
                                                        String chatID,
                                                        final SimpleCallback<Void> callback) {
        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE.NODE_USERS).child(userUid)
                .child(FIREBASE.NODE_CHATTERS).child(chatterUser.getUid());

        Chatter chatter = ChattersNodeUtil.createChatterFromUser(chatterUser, chatID, true);

        reference.setValue(chatter.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess(null);
                }else{
                    callback.onError();
                }
            }
        });
    }


    private static DatabaseReference getReference(String userUid, String chatterUserUid) {
        return FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE.NODE_USERS).child(userUid)
                .child(FIREBASE.NODE_CHATTERS).child(chatterUserUid);
    }

    public static void loadUserProfile(String userUid, final SimpleCallback<DataSnapshot> callback) {
        FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE.NODE_USERS)
                .child(userUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            callback.onSuccess(dataSnapshot);
                        }else{
                            callback.onError();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("LOG", getClass().getSimpleName()+":loadUserProfile():");
                        callback.onError();
                    }
                });
    }
}
