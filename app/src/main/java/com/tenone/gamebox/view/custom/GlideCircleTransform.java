package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;


public class GlideCircleTransform extends BitmapTransformation {

    private String imgCacheUri = "";

    public GlideCircleTransform(Context context) {
        super(context);
    }

    public GlideCircleTransform(Context context, String cacheUri) {
        super(context);
        imgCacheUri = cacheUri;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return cirleCrop(pool, toTransform);
    }

    private Bitmap cirleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }

        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null)
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        if (!TextUtils.isEmpty(imgCacheUri)) {
            File file = new File(imgCacheUri);
            if (file.exists())
                file.delete();
            if (result != null) {
                try {
                    FileOutputStream fileStream = new FileOutputStream(file);
                    result.compress(Bitmap.CompressFormat.PNG, 100, fileStream);
                    fileStream.flush();
                    fileStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
