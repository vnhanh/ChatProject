package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account.validation;

import vn.edu.hcmute.ms14110050.chatproject.common.validation.BaseValidation;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.PasswordValidator;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class VerifyAccountValidation extends BaseValidation{
    public PasswordValidator passwordValidator = new PasswordValidator();

    public VerifyAccountValidation() {
        registerValidator(passwordValidator);
    }
}
