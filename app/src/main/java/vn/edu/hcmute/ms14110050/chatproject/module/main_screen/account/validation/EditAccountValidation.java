package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account.validation;

import android.content.Context;
import android.content.res.Resources;

import vn.edu.hcmute.ms14110050.chatproject.common.validation.BaseValidation;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.BirthDateValidator;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.EmailValidator;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.FullnameValidator;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.PhoneValidator;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.UsernameValidator;

// nếu không thể sử dụng các biến của BR
// hãy import trước
// rồi clean -> rebuild

/**
 * Created by Vo Ngoc Hanh on 5/24/2018.
 */

public class EditAccountValidation extends BaseValidation{
    Resources resources;

    public EmailValidator emailValidator = new EmailValidator();

    public FullnameValidator fullnameValidator = new FullnameValidator();

    public UsernameValidator usernameValidator = new UsernameValidator();

    public PhoneValidator phoneValidator = new PhoneValidator();

    public final BirthDateValidator birthDateValidator;

    public EditAccountValidation(Context context) {
        this.resources = context.getResources();

        registerValidator(emailValidator);
        registerValidator(fullnameValidator);
        registerValidator(usernameValidator);
        registerValidator(phoneValidator);

        birthDateValidator = new BirthDateValidator(resources);
        registerValidator(birthDateValidator);
    }

    public void destroy() {
        resources = null;
        emailValidator.destroy();
        fullnameValidator.destroy();
        usernameValidator.destroy();
        phoneValidator.destroy();
        birthDateValidator.destroy();
    }
}
