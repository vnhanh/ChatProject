package vn.edu.hcmute.ms14110050.chatproject.common.validation;

import android.text.Editable;

import java.util.Observable;
import java.util.Observer;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.login.TextWatcherHandler;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class ConfirmPasswordValidator extends TextWatcherHandler implements Observer{
    String password = "";

    public ConfirmPasswordValidator() {
        errMsgId = R.string.validate_confirm_password;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        super.afterTextChanged(editable);
        checkValid();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof SendObject) {
            SendObject sendObject = (SendObject) o;
            int tag = sendObject.getTag();
            if (tag == PasswordValidator.TAG_PASSWORD_CHANGE) {
                password = (String) sendObject.getValue();
                checkValid();
            }
        }
    }

    private void checkValid() {
        String confirmPassword = getContent();
        isValid = confirmPassword.equals(password);

        setError();
        updateValid();
    }
}
