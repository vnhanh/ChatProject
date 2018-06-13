package vn.edu.hcmute.ms14110050.chatproject.common.validation;

import java.util.regex.Pattern;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.login.TextWatcherHandler;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class EmailValidator extends TextWatcherHandler {

    public EmailValidator() {
        this.errMsgId = R.string.validate_email;

        this.pattern = Pattern.compile(
                "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }
}
