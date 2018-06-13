package vn.edu.hcmute.ms14110050.chatproject.module.authentication.login;

import vn.edu.hcmute.ms14110050.chatproject.common.validation.BaseValidation;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.EmailValidator;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.PasswordValidator;

/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class LoginValidation extends BaseValidation{
    public EmailValidator emailValidator;
    public PasswordValidator passwordValidator;

    public LoginValidation() {
        emailValidator = new EmailValidator();
        passwordValidator = new PasswordValidator();

        registerValidator(emailValidator);
        registerValidator(passwordValidator);
    }

    @Override
    public void destroy() {
        emailValidator.destroy();
        passwordValidator.destroy();
    }
}
