package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewholder;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Observable;

import vn.edu.hcmute.ms14110050.chatproject.BR;
import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.recyclerview.BaseViewHolder;
import vn.edu.hcmute.ms14110050.chatproject.base.recyclerview.OnClickItemVHListener;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.MessageProfileImageTransform;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewmodel.ChatItemViewModel;

import static vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewmodel.ChatItemViewModel.TAG_LOAD_PROFILE_IMAGE;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public abstract class ChatItemVH<DATABINDING extends ViewDataBinding, VIEWMODEL extends ChatItemViewModel>
        extends BaseViewHolder<DATABINDING, VIEWMODEL> {
    ImageView profileImageView;

    public ChatItemVH(DATABINDING binding) {
        super(binding);
        profileImageView = getProfileImageView();

        binding.setVariable(BR.resources, binding.getRoot().getResources());
        binding.setVariable(BR.viewmodel, viewmodel);

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(viewmodel.getData());
            }
        });
    }

    protected abstract ImageView getProfileImageView();

    public void bind(Chatter data) {
        viewmodel.setChatter(data);
        binding.setVariable(BR.data, data);
        binding.getRoot().startAnimation(AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.slide_in_top));
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof SendObject) {
            SendObject sendObject = (SendObject) o;
            int tag = sendObject.getTag();
            switch (tag) {
                case TAG_LOAD_PROFILE_IMAGE:
                    String imageUrl = (String) sendObject.getValue();
                    loadProfileImage(imageUrl);
                    break;
            }
        }
    }

    protected void loadProfileImage(String imageUrl) {
        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.loading_profile_item_chatter_animation)
                .error(R.drawable.ic_profile_60dp_6dp)
                .transform(new MessageProfileImageTransform(getResources(), R.dimen.size_img_item_chatter))
                .into(profileImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        profileImageView
                                .setAnimation(AnimationUtils.loadAnimation(profileImageView.getContext(),
                                        R.anim.fade_in));
                    }

                    @Override
                    public void onError(Exception e) {
                        profileImageView
                                .setAnimation(AnimationUtils.loadAnimation(profileImageView.getContext(),
                                        R.anim.fade_in));
                    }
                });
    }

    protected Context getContext() {
        if (binding == null) {
            return null;
        }
        return binding.getRoot().getContext();
    }

    protected String getString(@StringRes int idRes) {
        return getContext().getString(idRes);
    }

    private OnClickItemVHListener<Chatter> listener;

    public void setListener(OnClickItemVHListener<Chatter> listener) {
        this.listener = listener;
    }
}
