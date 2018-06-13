package vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmute.ms14110050.chatproject.model.node_users.last_message.LastMessage;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public class ChatterFirebase extends ChatterPOJO{

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("chat_id", chatID);
        map.put("fullname", fullname);
        map.put("fullname_acronym", fullnameAcronym);
        map.put("url_image", urlImage);
        map.put("is_friend", isFriend);
        map.put("last_message", lastMessage);

        return map;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @PropertyName("chat_id")
    public String getChatID() {
        return chatID;
    }

    @PropertyName("chat_id")
    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @PropertyName("fullname_acronym")
    public String getFullnameAcronym() {
        return fullnameAcronym;
    }

    @PropertyName("fullname_acronym")
    public void setFullnameAcronym(String fullnameAcronym) {
        this.fullnameAcronym = fullnameAcronym;
    }

    @PropertyName("url_image")
    public String getUrlImage() {
        return urlImage;
    }

    @PropertyName("url_image")
    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @PropertyName("is_friend")
    public boolean isFriend() {
        return isFriend;
    }

    @PropertyName("is_friend")
    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    @PropertyName("last_message")
    public LastMessage getLastMessage() {
        return lastMessage;
    }

    @PropertyName("last_message")
    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
}
