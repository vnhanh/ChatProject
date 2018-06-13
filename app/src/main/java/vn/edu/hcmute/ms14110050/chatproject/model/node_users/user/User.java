package vn.edu.hcmute.ms14110050.chatproject.model.node_users.user;

import android.databinding.Bindable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;
import vn.edu.hcmute.ms14110050.chatproject.model.register.RegisterRequest;

/**
 * Created by Vo Ngoc Hanh on 5/19/2018.
 * Không khai báo @EqualsAndHashCode(callSuper=false) với @Data -> có thể dẫn đến lỗi
 */

public class User extends UserFirebase{

    public User() {

    }

    @Bindable
    public String getBirthDateStrValue() {
        Date date = new Date(birthDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    @Bindable
    public void setBirthDateStrValue(String text) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = sdf.parse(text);
            birthDate = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("LOG", getClass().getSimpleName() + ":couldn't convert string to birthdate:string:" + text);
        }
    }

    public static User getUser(RegisterRequest input) {
        User user = new User();
        user.setUsername(input.getUsername());
        user.setFullname(input.getFullname());
        user.setEmail(input.getEmail());
        user.setGender(input.isGender());
        user.setPhone(input.getPhone());

        return user;
    }

    // Tạo 1 object Chatter từ dữ liệu đã có của người dùng
    public Chatter createChatter() {
        Chatter chatter = new Chatter();
        chatter.setFullname(fullname);
        chatter.setFullnameAcronym(fullnameAcronym);
        chatter.setUid(uid);
        chatter.setUrlImage(urlImage);

        return chatter;
    }
}
