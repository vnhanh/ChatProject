package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.item_text;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import java.util.Observable;

import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemMessageTextReceivedBinding;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.MessageViewHolder;

/**
 * Created by Vo Ngoc Hanh on 6/7/2018.
 */

public class TextMessageVH<DATABINDING extends ViewDataBinding> extends MessageViewHolder<DATABINDING, TextMessageViewModel> {

    public TextMessageVH(@NonNull DATABINDING binding, @NonNull String uid, String chatterUderUrlImage) {
        super(binding, uid, chatterUderUrlImage);
        Log.d("LOG", getClass().getSimpleName() + ":constructor");
    }

    @Override
    protected ImageView getProfileImageIfExist() {
        if (binding instanceof ItemMessageTextReceivedBinding) {
            ItemMessageTextReceivedBinding _binding = (ItemMessageTextReceivedBinding) binding;
            return _binding.imgSender;
        }
        return null;
    }

    @Override
    protected TextMessageViewModel initViewModel(String uid, String chatterUderUrlImage) {
        return new TextMessageViewModel(getResources(), uid, chatterUderUrlImage);
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);

    }
}
