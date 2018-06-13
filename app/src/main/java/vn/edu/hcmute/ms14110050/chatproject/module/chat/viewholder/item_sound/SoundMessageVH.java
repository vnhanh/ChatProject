package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.item_sound;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Observable;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemMessageSoundReceivedBinding;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.MessageViewHolder;

import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_MEDIA_PLAYER_IS_RUNNING;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_PREPARE_MEDIA_PLAYER_FAILED;

/**
 * Created by Vo Ngoc Hanh on 6/6/2018.
 */

public class SoundMessageVH<DATABINDING extends ViewDataBinding> extends MessageViewHolder<DATABINDING, SoundMessageViewModel> {

    public SoundMessageVH(@NonNull DATABINDING binding, @NonNull String uid, String chatterUderUrlImage) {
        super(binding, uid, chatterUderUrlImage);
    }

    @Override
    protected ImageView getProfileImageIfExist() {
        if (binding instanceof ItemMessageSoundReceivedBinding) {
            ItemMessageSoundReceivedBinding _binding = (ItemMessageSoundReceivedBinding) binding;
            return _binding.imgSender;
        }
        return null;
    }

    @Override
    protected SoundMessageViewModel initViewModel(String uid, String chatterUderUrlImage) {
        return new SoundMessageViewModel(getResources(), uid, chatterUderUrlImage);
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);
        if (o instanceof SendObject) {
            SendObject sendObject = (SendObject) o;
            int tag = sendObject.getTag();

            switch (tag) {
                case TAG_PREPARE_MEDIA_PLAYER_FAILED:
                    Toast.makeText(getContext(), getResources().getString(R.string.play_audio_failed), Toast.LENGTH_SHORT).show();
                    break;

                case TAG_MEDIA_PLAYER_IS_RUNNING:
                    Toast.makeText(getContext(), getResources().getString(R.string.file_audio_is_running), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
