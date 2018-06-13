package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Observable;
import java.util.Observer;

import vn.edu.hcmute.ms14110050.chatproject.BR;
import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;

import static vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.MessageViewModel.TAG_LOAD_PROFILE_IMAGE;


/**
 * Created by Vo Ngoc Hanh on 5/14/2018.
 * base viewholder cho các message viewholder
 *
 * xử lý time
 */

public abstract class MessageViewHolder<DATABINDING extends ViewDataBinding,
                                        VIEWMODEL extends MessageViewModel>
                                                extends RecyclerView.ViewHolder implements Observer {
    protected DATABINDING binding;
    protected ImageView profileImageView;
    protected VIEWMODEL viewmodel;

    public MessageViewHolder(@NonNull DATABINDING binding, @NonNull String uid, String chatterUderUrlImage) {
        super(binding.getRoot());
        this.binding = binding;
        profileImageView = getProfileImageIfExist();
        viewmodel = initViewModel(uid, chatterUderUrlImage);
        if (viewmodel != null) {
            viewmodel.addObserver(this);
        }
        binding.setVariable(BR.resources, getResources());
    }

    protected abstract VIEWMODEL initViewModel(String uid, String chatterUderUrlImage);

    protected abstract ImageView getProfileImageIfExist();

    public void bind(Message prevMessage, Message message, Message nextMessage) {
        if (viewmodel != null) {
            viewmodel.setMessage(prevMessage, message, nextMessage);
            binding.setVariable(BR.viewmodel, viewmodel);
            binding.setVariable(BR.message, message);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof SendObject) {
            SendObject object = (SendObject) o;
            int tag = object.getTag();
            switch (tag) {
                case TAG_LOAD_PROFILE_IMAGE:
                    String imageUrl = (String) object.getValue();
                    loadProfileImage(imageUrl);
                    break;
            }
        }
    }

    private void loadProfileImage(String imageUrl) {
        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.loading_profile_message_animation)
                .error(R.drawable.ic_profile_36dp_4dp)
                .transform(new MessageProfileImageTransform(getResources(), R.dimen.size_image_profile_act_chat))
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
        return binding != null ? binding.getRoot().getContext() : null;
    }

    protected Resources getResources() {
        return binding != null ? binding.getRoot().getResources() : null;
    }
}
