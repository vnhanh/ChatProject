package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.chatproject.R;

/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class ProfileImageTransform implements Transformation {
    WeakReference<Resources> weakResources;

    public ProfileImageTransform(Resources resources) {
        weakResources = new WeakReference<Resources>(resources);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int maxSizeAcceptable = weakResources.get().getDimensionPixelSize(R.dimen.max_size_profile_image);
        int size = Math.max(source.getWidth(), source.getHeight());
        int actualSize = Math.min(size, maxSizeAcceptable);
        float scale = (float) (actualSize * 1.0 / size);
        int _width = (int) Math.floor(source.getWidth() * scale);
        int _height = (int) Math.floor(source.getHeight() * scale);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap _bitmap = Bitmap.createBitmap(source, 0, 0, _width, _height, matrix, false);
        if (_bitmap != source) {
            source.recycle();
        }
        return _bitmap;
    }

    @Override
    public String key() {
        return "ProfileImageTransform";
    }
}
