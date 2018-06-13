package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account.validation;

import vn.edu.hcmute.ms14110050.chatproject.common.validation.BaseValidation;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.ConfirmPasswordValidator;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.PasswordValidator;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class ChangePasswordValidation extends BaseValidation{
    public PasswordValidator passwordValidator = new PasswordValidator();
    public ConfirmPasswordValidator confirmPasswordValidator = new ConfirmPasswordValidator();

    public ChangePasswordValidation() {
        registerValidator(passwordValidator);
        passwordValidator.addObserver(confirmPasswordValidator);
        registerValidator(confirmPasswordValidator);
    }

    @Override
    public void destroy() {
        passwordValidator.destroy();
        confirmPasswordValidator.destroy();
    }
}
