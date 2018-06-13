package vn.edu.hcmute.ms14110050.chatproject.model.node_users.user;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public class UserFirebase extends UserPOJO {

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("uid", uid);
        map.put("username", username);
        map.put("fullname", fullname);
        map.put("fullname_acronym", fullnameAcronym);
        map.put("email", email);
        map.put("phone", phone);
        map.put("gender", gender);
        map.put("birth_date", birthDate);
        map.put("url_image", urlImage);

        Map<String, Object> chattersMap = new HashMap<>();
        for (Map.Entry<String, Chatter> chatter : chatters.entrySet()) {
            chattersMap.put(chatter.getKey(), chatter.getValue().toMap());
        }
        map.put("chatters", chattersMap);

        return map;
    }

    public boolean equal(Object object) {
        if (object instanceof User) {
            return equal((User)object);
        }
        return false;
    }

    public boolean equal(User another) {
        return another != null && uid.equals(another.getUid()) && username.equals(another.getUsername())
                && ( (fullname == null && another.getFullname() == null) || fullname.equals(another.getFullname()))
                && ((email == null && another.getEmail() == null) || email.equals(another.getEmail()))
                && ((phone == null && another.getPhone() == null) || phone.equals(another.getPhone()))
                && ((urlImage == null && another.getUrlImage() == null) || urlImage.equals(another.getUrlImage()))
                && gender == another.isGender()
                && birthDate == another.getBirthDate();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    // important
    public void setUsername(String username) {
        this.username = username.trim();
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname.trim().replaceAll(" +", " ");

        // write fullname_acronym
        fullnameAcronym = StringUtils.deAccent(this.fullname).toLowerCase();
    }

    @PropertyName("fullname_acronym")
    public String getFullnameAcronym() {
        return fullnameAcronym;
    }

    // only use for convert object from firebase
    @PropertyName("fullname_acronym")
    public void setFullnameAcronym(String fullnameAcronym) {
        this.fullnameAcronym = fullnameAcronym;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @PropertyName("birth_date")
    public long getBirthDate() {
        return birthDate;
    }

    @PropertyName("birth_date")
    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    @PropertyName("url_image")
    public String getUrlImage() {
        return urlImage;
    }

    @PropertyName("url_image")
    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Map<String, Chatter> getChatters() {
        return chatters;
    }

    public void setChatters(Map<String, Chatter> chatters) {
        this.chatters = chatters;
    }
}
