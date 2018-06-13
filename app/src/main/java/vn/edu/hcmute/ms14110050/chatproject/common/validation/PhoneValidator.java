package vn.edu.hcmute.ms14110050.chatproject.common.validation;

import java.util.regex.Pattern;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.login.TextWatcherHandler;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class PhoneValidator extends TextWatcherHandler {

    public PhoneValidator() {
        this.errMsgId = R.string.validate_phone;

        this.pattern = Pattern.compile(
                "^[0-9]{10,11}$");
    }

}
