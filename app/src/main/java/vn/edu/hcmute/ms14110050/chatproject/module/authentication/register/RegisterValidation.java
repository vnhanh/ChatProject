package vn.edu.hcmute.ms14110050.chatproject.module.authentication.register;

import vn.edu.hcmute.ms14110050.chatproject.common.validation.ConfirmPasswordValidator;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.FullnameValidator;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.UsernameValidator;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.login.LoginValidation;

/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class RegisterValidation extends LoginValidation{

    public UsernameValidator usernameValidator = new UsernameValidator();

    public FullnameValidator fullnameValidator = new FullnameValidator();

    public ConfirmPasswordValidator confirmPasswordValidator = new ConfirmPasswordValidator();

    public RegisterValidation() {
        registerValidator(usernameValidator);
        registerValidator(fullnameValidator);
        registerValidator(confirmPasswordValidator);

        passwordValidator.addObserver(confirmPasswordValidator);
    }

    @Override
    public void destroy() {
        super.destroy();
        usernameValidator.destroy();
        fullnameValidator.destroy();
        confirmPasswordValidator.destroy();
    }
}
