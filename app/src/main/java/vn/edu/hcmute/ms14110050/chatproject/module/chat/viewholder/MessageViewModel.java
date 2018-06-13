package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder;

import android.content.res.Resources;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.edu.hcmute.ms14110050.chatproject.base.MyObservable;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;

/**
 * Created by Vo Ngoc Hanh on 6/6/2018.
 */

public abstract class MessageViewModel extends MyObservable {
    public static final int TAG_LOAD_PROFILE_IMAGE = 0;

    protected WeakReference<Resources> weakResources;

    protected Message prevMessage, message, nextMessage;
    protected String uid, chatterUserUrlImage;

    public ObservableField<String> createdTime = new ObservableField<>("");

    public MessageViewModel(@NonNull Resources resources, @NonNull String uid, String chatterUserUrlImage) {
        weakResources = new WeakReference<Resources>(resources);
        this.uid = uid;
        this.chatterUserUrlImage = chatterUserUrlImage;
    }

    public void setMessage(Message prevMessage, Message message, Message nextMessage) {
        this.prevMessage = prevMessage;
        this.message = message;
        this.nextMessage = nextMessage;

        showData();
    }

    protected void showData() {
        // nếu là message cuối cùng được nhận
        // khởi tạo instance "target" để load ảnh
        if (!StringUtils.isEmpty(chatterUserUrlImage) && !message.getSenderUid().equals(uid)) {
            // load ảnh profile
            SendObject sendObject = new SendObject(TAG_LOAD_PROFILE_IMAGE, chatterUserUrlImage);
            notify(sendObject);
        }

        // nếu là tin nhắn đầu tiên của mỗi người
        // hiện thời gian gửi
        createdTime.set(getCreatedTime());
    }

    private String getCreatedTime() {
        if (message == null || (prevMessage != null && message.getSenderUid().equals(prevMessage.getSenderUid()))) {
            return "";
        }

        Date now = new Date();
        long diff = now.getTime() - message.getCreateTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        SimpleDateFormat sdf = null;
        if (diffDays < 1) {
            sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else if (diffDays < 7) {
            sdf = new SimpleDateFormat("EEEE HH:mm", Locale.getDefault());
        } else if (diffDays < 366) {
            sdf = new SimpleDateFormat("dd MMM HH:mm", Locale.getDefault());
        } else {
            sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        }

        Date date = new Date(message.getCreateTime());
        return sdf.format(date);
    }

    protected Resources getResources() {
        return weakResources != null ? weakResources.get() : null;
    }
}
