package vn.edu.hcmute.ms14110050.chatproject.common.databinding;

import android.databinding.BindingAdapter;
import android.support.annotation.IdRes;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account.ProfileImageTransform;

/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class ImageViewBinding {
    @BindingAdapter("loadProfileImage")
    public static void loadProfileImage(ImageView view, String url) {
        Picasso.get().load(url).transform(new ProfileImageTransform(view.getResources()))
                .into(view);
    }

    @BindingAdapter({"loadImage","placeholder","error","transform"})
    public static void loadImage(ImageView view, String url, @IdRes int placeholder, @IdRes int error, Transformation transformation) {
        Picasso.get().load(url).transform(transformation)
                .into(view);
    }
}
