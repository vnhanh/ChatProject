package vn.edu.hcmute.ms14110050.chatproject.model.node_chats;

import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Vo Ngoc Hanh on 6/2/2018.
 */

public class ChatPOJO {
    @SerializedName("chat_id")
    @Expose
    String chatID;

    String uid1;

    String uid2;

    Map<String,Message> messages;


    @PropertyName("chat_id")
    public String getChatID() {
        return chatID;
    }

    @PropertyName("chat_id")
    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public Map<String, Message> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Message> messages) {
        this.messages = messages;
    }
}
