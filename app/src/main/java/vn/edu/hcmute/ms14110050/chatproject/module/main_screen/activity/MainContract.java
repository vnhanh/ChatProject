package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.activity;

import android.content.Context;

import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.LifeCycle;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;

/**
 * Created by Vo Ngoc Hanh on 5/30/2018.
 */

public interface MainContract {
    interface View extends LifeCycle.View{

        Context getContext();

        void onLoadUserData(User user);

        void showProgressLoadUserProfile();

        void hideProgressLoadUserProfile();

        void logout();

        void showMessageLogoutFailed();
    }
}
