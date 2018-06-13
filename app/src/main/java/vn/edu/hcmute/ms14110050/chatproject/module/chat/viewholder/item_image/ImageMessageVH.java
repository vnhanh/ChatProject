package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.item_image;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import java.util.Observable;

import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemMessageImageReceivedBinding;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.MessageViewHolder;

/**
 * Created by Vo Ngoc Hanh on 6/6/2018.
 */

public class ImageMessageVH<DATABINDING extends ViewDataBinding> extends MessageViewHolder<DATABINDING, ImageMessageViewModel> {

    public ImageMessageVH(@NonNull DATABINDING binding, @NonNull String uid, String chatterUderUrlImage) {
        super(binding, uid, chatterUderUrlImage);
        Log.d("LOG", getClass().getSimpleName() + ":constructor");
    }

    @Override
    protected ImageView getProfileImageIfExist() {
        if (binding instanceof ItemMessageImageReceivedBinding) {
            ItemMessageImageReceivedBinding _binding = (ItemMessageImageReceivedBinding) binding;
            return _binding.imgSender;
        }
        return null;
    }

    @Override
    protected ImageMessageViewModel initViewModel(String uid, String chatterUderUrlImage) {
        return new ImageMessageViewModel(getResources(), uid, chatterUderUrlImage);
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);
    }

    @Override
    public void bind(Message prevMessage, Message message, Message nextMessage) {
        super.bind(prevMessage, message, nextMessage);
    }
}
