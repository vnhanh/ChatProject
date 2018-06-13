package vn.edu.hcmute.ms14110050.chatproject.module.authentication.register;

import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 5/30/2018.
 */

public interface RegisterContract {
    interface View extends LifeCycle.View{
        void clickLoginLink();

        void notGetAuth();

        void emailExist();

        void badFormatEmail();

        void weakPassword();

        void registerFailed();

        void notCreateUserData();

        void notGetFirebaseUser();

        void registerSuccess();

        void showProgress();

        void hideProgress();
    }
}
