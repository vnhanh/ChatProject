package vn.edu.hcmute.ms14110050.chatproject.module.chat;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemMessageImageReceivedBinding;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemMessageImageSendBinding;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemMessageSoundReceivedBinding;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemMessageSoundSendBinding;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemMessageTextReceivedBinding;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemMessageTextSendBinding;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.MessageViewHolder;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.item_image.ImageMessageVH;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.item_sound.SoundMessageVH;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.item_text.TextMessageVH;


/**
 * Created by Vo Ngoc Hanh on 5/14/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private final int MULTIPLES = 10;

    String uid, chatterUserUrlImage;
    ArrayList<Message> list = new ArrayList<>();

    public MessageAdapter(@NonNull String uid, @NonNull String chatterUserUrlImage) {
        this.uid = uid;
        this.chatterUserUrlImage = chatterUserUrlImage;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Log.d("LOG", getClass().getSimpleName() + ":onCreateViewHolder():viewType:" + viewType);
        switch (viewType) {
            case MESSAGE_TYPE.IMAGE * MULTIPLES:
                Log.d("LOG", getClass().getSimpleName() + ":onCreateViewHolder():item_message_image_received");
                ItemMessageImageReceivedBinding imageReceivedBinding =
                        DataBindingUtil.inflate(inflater, R.layout.item_message_image_received, parent, false);

                return new ImageMessageVH<ItemMessageImageReceivedBinding>(imageReceivedBinding, uid, chatterUserUrlImage);

            case MESSAGE_TYPE.IMAGE * MULTIPLES + 1:
                Log.d("LOG", getClass().getSimpleName() + ":onCreateViewHolder():item_message_image_send");
                ItemMessageImageSendBinding imageSendBinding =
                        DataBindingUtil.inflate(inflater, R.layout.item_message_image_send, parent, false);

                return new ImageMessageVH<ItemMessageImageSendBinding>(imageSendBinding, uid, null);

            case MESSAGE_TYPE.SOUND * MULTIPLES:
                Log.d("LOG", getClass().getSimpleName() + ":onCreateViewHolder():item_message_sound_received");
                ItemMessageSoundReceivedBinding soundReceivedBinding =
                        DataBindingUtil.inflate(inflater, R.layout.item_message_sound_received, parent, false);
                return new SoundMessageVH<ItemMessageSoundReceivedBinding>(soundReceivedBinding, uid, chatterUserUrlImage);

            case MESSAGE_TYPE.SOUND * MULTIPLES + 1:
                Log.d("LOG", getClass().getSimpleName() + ":onCreateViewHolder():item_message_sound_send");
                ItemMessageSoundSendBinding soundSendBinding =
                        DataBindingUtil.inflate(inflater, R.layout.item_message_sound_send, parent, false);
                return new SoundMessageVH<ItemMessageSoundSendBinding>(soundSendBinding, uid, null);

            case MESSAGE_TYPE.TEXT * MULTIPLES:
                Log.d("LOG", getClass().getSimpleName() + ":onCreateViewHolder():item_message_text_received");
                ItemMessageTextReceivedBinding textReceivedBinding =
                        DataBindingUtil.inflate(inflater, R.layout.item_message_text_received, parent, false);

                return new TextMessageVH<ItemMessageTextReceivedBinding>(textReceivedBinding, uid, chatterUserUrlImage);

            case MESSAGE_TYPE.TEXT * MULTIPLES + 1:
                Log.d("LOG", getClass().getSimpleName() + ":onCreateViewHolder():item_message_text_send");
                ItemMessageTextSendBinding textSendBinding =
                        DataBindingUtil.inflate(inflater, R.layout.item_message_text_send, parent, false);

                return new TextMessageVH<ItemMessageTextSendBinding>(textSendBinding, uid, null);

            default:
                throw new IllegalStateException("Couldn't recognize this viewtype for Message Chat Item:" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Log.d("LOG", getClass().getSimpleName() + ":onBindViewHolder():position:" + position);
        Message prevMessage = position > 0 ? list.get(position - 1) : null;
        Message nextMessage = position < getItemCount() - 1 ? list.get(position + 1) : null;
        holder.bind(prevMessage, list.get(position), nextMessage);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = list.get(position);
        String senderUid = message.getSenderUid();
        if (StringUtils.isEmpty(senderUid)) {
            throw new IllegalStateException("Message item has not contained sender uid");
        }
        // nếu bạn là người gửi
        if (message.getSenderUid().equals(uid)) {
            return list.get(position).getMessageType() * MULTIPLES + 1;
        }
        // nếu là người nhận
        else{
            return list.get(position).getMessageType() * MULTIPLES;
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void addMessage(Message message) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(message);
        notifyItemInserted(list.size()-1);
    }

    public void changeMessage(Message message) {
        if (list == null) {
            list = new ArrayList<>();
            return;
        }

        int size = list.size();
        for (int i = 0; i < size; i++) {
            String id = list.get(i).getId();
            if (id != null && id.equals(message.getId())) {
                list.set(i, message);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void removeMessage(Message message) {
        if (list == null) {
            list = new ArrayList<>();
            return;
        }

        int size = list.size();
        for (int i = 0; i < size; i++) {
            String id = list.get(i).getId();
            if (id != null && id.equals(message.getId())) {
                list.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }
}
