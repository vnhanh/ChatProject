package vn.edu.hcmute.ms14110050.chatproject.common.databinding;

import android.content.res.Resources;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

/**
 * Created by Vo Ngoc Hanh on 5/27/2018.
 */

public class BindableFieldTarget implements Target {

    private ObservableField<Drawable> observableDrawable;
    private WeakReference<Resources> resources;
    private ImageView imageView;

    public BindableFieldTarget(ObservableField<Drawable> observableDrawable, Resources resources) {
        this.observableDrawable = observableDrawable;
        this.resources = new WeakReference<Resources>(resources);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        observableDrawable.set(new BitmapDrawable(resources.get(), bitmap));
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        observableDrawable.set(errorDrawable);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        observableDrawable.set(placeHolderDrawable);
    }
}
