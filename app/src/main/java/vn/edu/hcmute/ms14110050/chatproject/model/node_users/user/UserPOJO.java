package vn.edu.hcmute.ms14110050.chatproject.model.node_users.user;

import android.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public class UserPOJO extends BaseObservable implements Cloneable{

    protected String uid = "";

    protected String username = "";

    protected String fullname = "";

    @SerializedName("fullname_acronym")
    @Expose
    protected String fullnameAcronym = "";

    protected String email = "";

    protected String phone = "";

    protected boolean gender = true;

    @SerializedName("birth_date")
    @Expose
    protected long birthDate;

    @SerializedName("url_image")
    @Expose
    protected String urlImage;

    /*
    * key: uid bạn chat
    * value: bạn chat có thể đã kết bạn hoặc chưa
    * */
    protected Map<String,Chatter> chatters = new HashMap<>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        UserPOJO clone = (UserPOJO) super.clone();
        if (chatters != null) {
            Map<String, Chatter> chattersClone = new HashMap<>();
            for (Map.Entry<String, Chatter> entry : chatters.entrySet()) {
                chattersClone.put(entry.getKey(), (Chatter) entry.getValue().clone());
            }
            clone.chatters = chattersClone;
        }
        return clone;
    }
}
