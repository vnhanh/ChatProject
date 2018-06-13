package vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter;

import android.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.chatproject.model.node_users.last_message.LastMessage;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public class ChatterPOJO extends BaseObservable implements Cloneable{
    protected String uid;

    @SerializedName("chat_id")
    @Expose
    protected String chatID;

    protected String fullname;

    @SerializedName("fullname_acronym")
    @Expose
    protected String fullnameAcronym;

    @SerializedName("url_image")
    @Expose
    protected String urlImage;

    @SerializedName("is_friend")
    @Expose
    protected boolean isFriend = false;

    @SerializedName("last_message")
    @Expose
    protected LastMessage lastMessage;

    @Override
    public Object clone() throws CloneNotSupportedException {
        ChatterPOJO clone = (ChatterPOJO) super.clone();
        if (lastMessage != null) {
            clone.lastMessage = (LastMessage) lastMessage.clone();
        }
        return clone;
    }
}
