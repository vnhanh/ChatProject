package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.item_image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.chatproject.R;

/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class ImageMessageTransform implements Transformation {
    WeakReference<Resources> weakResource;

    public ImageMessageTransform(Resources resource) {
        this.weakResource = new WeakReference<Resources>(resource);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.max(source.getWidth(), source.getHeight());
        int maxSizeAcceptable = weakResource.get().getDimensionPixelSize(R.dimen.max_size_image_message);
        int minSizeAcceptable = weakResource.get().getDimensionPixelSize(R.dimen.min_size_image_message);
        int actualSize = Math.min(size, maxSizeAcceptable);
        if (actualSize < minSizeAcceptable) {
            actualSize = minSizeAcceptable;
        }
        float scale = (float) (actualSize *1.0/ size);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap _bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);

        if (_bitmap != source) {
            source.recycle();
        }

        int imageWidth = (int) Math.floor(source.getWidth() * scale);
        int imageHeight = (int) Math.floor(source.getHeight() * scale);

        Bitmap bitmap = Bitmap.createBitmap(imageWidth, imageHeight, _bitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(_bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        int corner = weakResource.get().getDimensionPixelSize(R.dimen.corner_image_message);
        canvas.drawRoundRect(new RectF(0,0,imageWidth, imageHeight), corner,corner,paint);

        if (bitmap != _bitmap) {
            _bitmap.recycle();
        }

        return bitmap;
    }

    @Override
    public String key() {
        return "ImageMessageTransform";
    }
}
