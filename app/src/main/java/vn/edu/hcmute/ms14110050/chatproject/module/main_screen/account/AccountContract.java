package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account;

import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.LifeCycle;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;

/**
 * Created by Vo Ngoc Hanh on 5/30/2018.
 */

public interface AccountContract {
    interface View extends LifeCycle.View{
        // chuyển đổi các layout: EDIT_ACCOUNT, CHANGE_PASSWORD
        void onSwitchViewGroup();

        // show DatePickerDialog
        void showDatePickerDialog();

        void uploadImageFailed();

        void userProfileNotChanged();

        void updateUserProfileSuccess();

        void updateUserProfileFailed();

        void updateEmailFailed();

        void verifyPasswordFailed();

        void updatePasswordSuccess();

        void updatePasswordFailed();

        void loadUserProfile(User user);

        // thông báo gặp lỗi khi xử lý dữ liệu
        void messageCountErrorProcessData();

        void reloadUserProfile(User profile);

        // show dialog xác nhận mật khẩu
        void showDialogVerifyAccount();

        // show dialog thay đổi mật khẩu
        void showDialogChangePassword();

        void clearAllError();
    }
}
