package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.DimenRes;

import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class MessageProfileImageTransform implements Transformation {
    WeakReference<Resources> weakResource;
    int size = 0;

    public MessageProfileImageTransform(Resources resource, @DimenRes int dimenRes) {
        this.weakResource = new WeakReference<Resources>(resource);
        size = weakResource.get().getDimensionPixelSize(dimenRes);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int sourceSize = Math.min(source.getWidth(), source.getHeight());
        if (size < 20) {
            size = 20;
        }
        float scale = (float) (size *1.0/ sourceSize);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

//        int _x = source.getWidth() - maxSourceSize;
//        int _y = source.getHeight() - maxSourceSize;

        Bitmap _bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);

        if (_bitmap != source) {
            source.recycle();
        }

//        Bitmap __bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
//        Canvas _canvas = new Canvas(__bitmap);
//        _canvas.drawColor(Color.WHITE);
//        _canvas.drawBitmap(_bitmap, (size-_bitmap.getWidth())/2, (size-_bitmap.getHeight())/2, null);
//
//        if (_bitmap != __bitmap) {
//            __bitmap.recycle();
//        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(_bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        float r = size / 2f;
        canvas.drawCircle(r,r,r,paint);
        if (bitmap != _bitmap) {
            _bitmap.recycle();
        }
        return bitmap;
    }

    @Override
    public String key() {
        return "MessageProfileImageTransform";
    }
}
