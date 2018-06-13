package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.item_image;

import android.content.res.Resources;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.squareup.picasso.Picasso;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.MessageViewModel;

/**
 * Created by Vo Ngoc Hanh on 6/6/2018.
 */

public class ImageMessageViewModel extends MessageViewModel {
    public final ObservableField<Drawable> messageDrawable = new ObservableField<>();
    BindableFieldTarget messageTarget;

    public ImageMessageViewModel(@NonNull Resources resources, @NonNull String uid, String chatterUserUrlImage) {
        super(resources, uid, chatterUserUrlImage);
        messageTarget = new BindableFieldTarget(messageDrawable, resources);
    }

    @Override
    protected void showData() {
        super.showData();
        // hiển thị ảnh tin nhắn
        Picasso.get()
                .load(message.getContent())
                .transform(new ImageMessageTransform(getResources()))
                .placeholder(R.drawable.ic_image_100dp_10dp)
                .error(R.drawable.ic_error_60dp_10dp)
                .into(messageTarget);
    }
}
