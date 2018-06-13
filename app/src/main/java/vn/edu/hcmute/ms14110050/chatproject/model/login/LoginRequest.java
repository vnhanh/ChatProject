package vn.edu.hcmute.ms14110050.chatproject.model.login;

/**
 * Created by Vo Ngoc Hanh on 5/19/2018.
 */

public class LoginRequest {
    String email;
    String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void trimSelft() {
        email = email.trim();
    }
}
