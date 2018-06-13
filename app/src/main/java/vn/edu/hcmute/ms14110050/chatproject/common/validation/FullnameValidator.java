package vn.edu.hcmute.ms14110050.chatproject.common.validation;

import java.util.regex.Pattern;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.login.TextWatcherHandler;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class FullnameValidator extends TextWatcherHandler {

    public FullnameValidator() {
        this.errMsgId = R.string.validate_fullname;

        this.pattern = Pattern.compile(
                "[A-ZẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ\\s]{1,}",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }
}
