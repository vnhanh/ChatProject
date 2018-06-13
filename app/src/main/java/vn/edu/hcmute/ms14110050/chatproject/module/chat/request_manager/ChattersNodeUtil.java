package vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;

import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.last_message.LastMessage;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/8/2018.
 */

public class ChattersNodeUtil {
    public static Chatter createChatterFromUser(User user, String chatID, boolean isFriend) {
        Chatter chatter = new Chatter();
        chatter.setChatID(chatID);
        chatter.setUid(user.getUid());
        chatter.setFullname(user.getFullname());
        chatter.setFullnameAcronym(user.getFullnameAcronym());
        chatter.setUrlImage(user.getUrlImage());
        chatter.setFriend(isFriend);

        return chatter;
    }

    public static void updateTwoChattersNode(DatabaseReference ref, User anotherUser) {
        ref.child(FIREBASE.NODE_FULLNAME).setValue(anotherUser.getFullname());
        ref.child(FIREBASE.NODE_FULLNAME_ACRONYM).setValue(anotherUser.getFullnameAcronym());
        ref.child(FIREBASE.NODE_URL_IMAGE).setValue(anotherUser.getUrlImage());
    }

    public static void createChatterNodeFromAnotherUser(DatabaseReference ref,
                                                        User anotherUser,
                                                        String chatID,
                                                        OnCompleteListener<Void> callback) {

        Chatter chatter = createChatterFromUser(anotherUser, chatID, false);

        ref.child(anotherUser.getUid()).setValue(chatter.toMap()).addOnCompleteListener(callback);
    }

    public static void updateLastMessage(DatabaseReference userRef,
                                         String userUid, String chatterUserUid,
                                         Message lastMessage,
                                         OnCompleteListener<Void> callback) {

        LastMessage _lastMessage = lastMessage.getLastMessage(userUid);

        userRef.child(FIREBASE.NODE_CHATTERS)
                .child(chatterUserUid)
                .child(FIREBASE.NODE_LAST_MESSAGE)
                .setValue(_lastMessage.toMap())
                .addOnCompleteListener(callback);
    }
}
