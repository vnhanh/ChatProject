package vn.edu.hcmute.ms14110050.chatproject.model.register;

import lombok.Data;

/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

@Data
public class RegisterRequest {
    String email;
    String username;
    String fullname;
    String password;
    String phone;
    boolean gender = true;

    public void selfTrim() {
        email = email.trim();
        username = username.trim();
        fullname = fullname.trim();
        password = password.trim();
    }
}
