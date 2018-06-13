package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewholder;

import android.widget.ImageView;

import java.util.Observable;

import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemChatterMyFriendBinding;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewmodel.FriendViewModel;


/**
 * Created by Vo Ngoc Hanh on 5/14/2018.
 */

public class FriendChatItemVH extends ChatItemVH<ItemChatterMyFriendBinding, FriendViewModel> {

    public FriendChatItemVH(ItemChatterMyFriendBinding binding) {
        super(binding);
    }

    @Override
    protected ImageView getProfileImageView() {
        return binding.imageProfile;
    }

    @Override
    protected FriendViewModel initViewModel() {
        return new FriendViewModel(binding.getRoot().getResources());
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);
    }
}
