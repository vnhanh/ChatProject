package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewholder;

import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Observable;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.common.custom_view.MyProgressDialog;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemChatterNotFriendBinding;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewmodel.NotFriendViewModel;

import static vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.ChatItemTags.TAG_ADD_FRIEND_FAILED;
import static vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.ChatItemTags.TAG_ADD_FRIEND_SUCCESS;
import static vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.ChatItemTags.TAG_HIDE_PROGRESS;
import static vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.ChatItemTags.TAG_SHOW_PROGRESS;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public class NotFriendChatterItemVH extends ChatItemVH<ItemChatterNotFriendBinding, NotFriendViewModel> {

    public NotFriendChatterItemVH(ItemChatterNotFriendBinding binding) {
        super(binding);
    }

    @Override
    protected ImageView getProfileImageView() {
        return binding.imageProfile;
    }

    @Override
    protected NotFriendViewModel initViewModel() {
        return new NotFriendViewModel(binding.getRoot().getResources());
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);
        if (o instanceof Integer) {
            int tag = (int) o;
            switch (tag) {
                case TAG_SHOW_PROGRESS:
                    showProgress();
                    break;

                case TAG_HIDE_PROGRESS:
                    hideProgress();
                    break;

                case TAG_ADD_FRIEND_SUCCESS:
                    onAddFriendSuccess();
                    break;

                case TAG_ADD_FRIEND_FAILED:
                    onAddFriendFailed();
                    break;
            }
        }
    }

    private void onAddFriendSuccess() {
        Toast.makeText(getContext(), getString(R.string.message_add_friend_success), Toast.LENGTH_SHORT).show();
    }

    private void onAddFriendFailed() {
        Toast.makeText(getContext(), getString(R.string.message_add_friend_failed), Toast.LENGTH_SHORT).show();
    }

    AlertDialog progressDialog;

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = MyProgressDialog.create(binding.getRoot().getContext(), R.string.message_loading_add_friend);
        }
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
