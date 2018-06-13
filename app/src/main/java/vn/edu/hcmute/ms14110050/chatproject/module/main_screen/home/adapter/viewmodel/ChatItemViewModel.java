package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewmodel;

import android.content.res.Resources;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.MyObservable;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;


/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 *
 * base viewmodel cho Chatter Item
 */

public class ChatItemViewModel extends MyObservable {
    public static final int TAG_LOAD_PROFILE_IMAGE = 0;

    protected WeakReference<Resources> resourcesWeakReference;
    protected BindableFieldTarget target;
    public ObservableField<Drawable> profileDrawable = new ObservableField<>();

    Chatter chatter;

    public ChatItemViewModel(Resources resources) {
        // init
        resourcesWeakReference = new WeakReference<Resources>(resources);
        target = new BindableFieldTarget(profileDrawable, resourcesWeakReference.get());
    }

    public void setChatter(Chatter chatter) {
        this.chatter = chatter;
        showData();
    }

    protected void showData() {
        // load ảnh profile
        int size = resourcesWeakReference.get().getDimensionPixelSize(R.dimen.size_img_item_chatter);
        Picasso.get().load(chatter.getUrlImage())
                .resize(size, size)
                .into(target);

        SendObject sendObject = new SendObject(TAG_LOAD_PROFILE_IMAGE, chatter.getUrlImage());
        notify(sendObject);
    }

    public Chatter getData() {
        return chatter;
    }

    protected String getString(@StringRes int idRes) {
        return resourcesWeakReference.get().getString(idRes);
    }
}
