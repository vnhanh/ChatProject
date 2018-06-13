package vn.edu.hcmute.ms14110050.chatproject.module.authentication.login;

import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 5/30/2018.
 */

public interface LoginContract {
    interface View extends LifeCycle.View{
        void notGetAuth();

        void loginSuccess();

        void notFoundUser();

        void loginFailed();

        void clickRegisterLink();

        void showProgress();

        void hideProgress();
    }
}
