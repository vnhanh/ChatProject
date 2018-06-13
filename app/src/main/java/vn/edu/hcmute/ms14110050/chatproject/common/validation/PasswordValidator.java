package vn.edu.hcmute.ms14110050.chatproject.common.validation;

import android.text.Editable;

import java.util.regex.Pattern;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.common.IntegerUnique;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.login.TextWatcherHandler;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class PasswordValidator extends TextWatcherHandler{
    public static final int TAG_PASSWORD_CHANGE = IntegerUnique.generate();

    public PasswordValidator() {
        this.errMsgId = R.string.validate_password;

        this.pattern = Pattern.compile(
                "^(?=.*\\d)(?=.*[a-zA-Z])(?=(.*[\\da-zA-Z]){6}).{6,}$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        super.afterTextChanged(editable);

        SendObject newPasswordObject = new SendObject(TAG_PASSWORD_CHANGE, editable.toString());
        setChanged();
        notifyObservers(newPasswordObject);
    }
}
