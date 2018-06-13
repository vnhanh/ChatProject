package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewmodel;

import android.content.res.Resources;

import com.google.firebase.database.DatabaseReference;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.Constant;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.request_manager.ChatItemRequestManager;

import static vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.ChatItemTags.TAG_ADD_FRIEND_FAILED;
import static vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.ChatItemTags.TAG_ADD_FRIEND_SUCCESS;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public class NotFriendViewModel extends ChatItemViewModel {
    DatabaseReference reference;

    public NotFriendViewModel(Resources resources) {
        super(resources);

    }

    public void onClickAddFriend(Chatter chatter) {
        showProgress();

        ChatItemRequestManager.getInstance().addFriend(chatter.getUid(), new SimpleCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgress();
                onAddFriendSuccess();
            }

            @Override
            public void onError() {
                hideProgress();
                onAddFriendFailed();
            }
        });
    }

    private void onAddFriendSuccess() {
        notify(TAG_ADD_FRIEND_SUCCESS);
    }

    private void onAddFriendFailed() {
        notify(TAG_ADD_FRIEND_FAILED);
    }

    private void showProgress() {
        notify(Constant.SHOW_PROGRESS);
    }

    private void hideProgress() {
        notify(Constant.HIDE_PROGRESS);
    }
}
