package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.activity;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/11/2018.
 */

public class UserAccessor {
    public static void createUserInfo(@NonNull User user, OnCompleteListener<Void> callback) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE.NODE_USERS).child(user.getUid());

        reference.setValue(user.toMap()).addOnCompleteListener(callback);
    }
}
