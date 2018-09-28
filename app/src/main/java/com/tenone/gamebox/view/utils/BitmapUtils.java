package com.tenone.gamebox.view.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.tenone.gamebox.view.base.BaseActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BitmapUtils {

    /**
     * 根据原图和边长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source, int min) {
        Paint paint = new Paint();
        // paint.setStyle(Paint.Style.STROKE);
        // paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias( true );
        paint.setFilterBitmap( true );
        Bitmap target = Bitmap.createBitmap( min, min, source.getConfig() );
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas( target );

        /**
         * 首先绘制圆形
         */
        canvas.drawCircle( min / 2, min / 2, min / 2, paint );
        /**
         * 使用SRC_IN
         */
        paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN ) );

        /**
         * 给Canvas加上抗锯齿标志
         */
        canvas.setDrawFilter( new PaintFlagsDrawFilter( 0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG ) );

        /**
         * 绘制图片
         */
        canvas.drawBitmap( source, 0, 0, paint );
        return target;
    }

    /**
     * 获取位图 getSmallBitmap:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    @SuppressWarnings("deprecation")
    public static Bitmap getSmallBitmap(String filePath, int reqWidth,
                                        int reqHeight) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( filePath, options );
        options.inSampleSize = calculateInSampleSize( options, reqWidth,
                reqHeight );
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        // 避免出现内存溢出的情况，进行相应的属性设置。
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        return BitmapFactory.decodeFile( filePath, options );
    }

    /**
     * 获取压缩值 calculateInSampleSize:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static int calculateInSampleSize(Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round( (float) height
                    / (float) reqHeight );
            final int widthRatio = Math.round( (float) width / (float) reqWidth );
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 质量压缩
     *
     * @param image
     * @param scanle 压缩比例
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int scanle) {
        Bitmap bitmap = null;
        if (image != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress( Bitmap.CompressFormat.JPEG, 100, baos );// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = scanle;
            while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                image.compress( Bitmap.CompressFormat.JPEG, options, baos );// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(
                    baos.toByteArray() );// 把压缩后的数据baos存放到ByteArrayInputStream中
            try {
                bitmap = BitmapFactory.decodeStream( isBm, null, null );// 把ByteArrayInputStream数据生成图片
            } catch (OutOfMemoryError e) {
                Log.e( "compressImage", e.toString() );
            }
            try {
                baos.close();
                isBm.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    /**
     * 图片按比例大小压缩（根据路径获取图片并压缩）
     *
     * @param srcPath
     * @param height  屏幕高度
     * @param width   屏幕宽度
     * @param scanle  压缩比例
     * @return
     */
    public static Bitmap getimage(String srcPath, float height, float width,
                                  int scanle) {
        Options newOpts = new Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inTempStorage = new byte[12 * 1024];
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile( srcPath, newOpts );// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / width);
        } else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / height);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        try {
            bitmap = BitmapFactory.decodeFile( srcPath, newOpts );
        } catch (OutOfMemoryError e) {
        }
        return compressImage( bitmap, scanle );// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片按比例大小压缩（根据Bitmap图片压缩）
     *
     * @param image
     * @param height 屏幕高度
     * @param width  屏幕宽度
     * @param scanle 压缩比例
     * @return
     */
    public static Bitmap comp(Bitmap image, float height, float width,
                              int scanle) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress( Bitmap.CompressFormat.JPEG, 100, baos );
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress( Bitmap.CompressFormat.JPEG, scanle, baos );// 这里压缩scanle%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream( baos.toByteArray() );
        Options newOpts = new Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream( isBm, null, newOpts );
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / width);
        } else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / height);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream( baos.toByteArray() );
        bitmap = BitmapFactory.decodeStream( isBm, null, newOpts );
        return compressImage( bitmap, scanle );// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 保存图片到相册
     *
     * @param mContext
     * @param str
     * @param name
     * @return
     */
    public static String saveAlbum(Context mContext, String str, String name) {
        ContentResolver cr = mContext.getContentResolver();
        String uri = null;
        try {
            uri = MediaStore.Images.Media.insertImage( cr, str, name, "" );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return uriToPath( mContext, uri );
    }

    /**
     * 保存图片到相册
     *
     * @param mContext
     * @param bitmap
     * @param name
     * @return
     */
    public static String saveAlbum(Context mContext, Bitmap bitmap,
                                   String name, String description) {
        ContentResolver cr = mContext.getContentResolver();
        String uri = null;
        uri = MediaStore.Images.Media
                .insertImage( cr, bitmap, name, description );
        return uriToPath( mContext, uri );
    }

    /**
     * 将uri转换成手机的图片Media的绝对路径
     *
     * @param urlString
     * @return
     */
    private static String uriToPath(Context mContext, String urlString) {
        if (null == urlString || "".equals( urlString )) {
            return "";
        }

        String path = "";
        Uri uri = Uri.parse( urlString );
        Cursor cur = mContext.getContentResolver().query( uri, null, null, null,
                null );
        if (null != cur && cur.moveToNext()) {
            int index = cur.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
            cur.moveToFirst();
            path = cur.getString( index );
        }
        if (null != cur) {
            cur.close();
        }
        return path;
    }

    /**
     * 扫描某个文件
     *
     * @param filePath
     */
    public static void fileScan(Context mContext, String filePath) {
        Uri data = Uri.parse( "file://" + filePath );
        mContext.sendBroadcast( new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data ) );
    }

    /**
     * 扫描整个SD卡
     */
    public static void sdScan(Context mContext) {
        mContext.sendBroadcast( new Intent( Intent.ACTION_MEDIA_MOUNTED, Uri
                .parse( "file://" + Environment.getExternalStorageDirectory() ) ) );
    }

    /**
     * 压缩图片并保存压缩后的图片
     *
     * @param bmp
     * @param path
     * @param name
     * @return
     */
    public static File compressBmpToFile(Bitmap bmp, String path, String name) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bmp.compress( Bitmap.CompressFormat.JPEG, options, baos );
        while (baos.toByteArray().length / 1024 > 350) {
            baos.reset();
            options -= 5;
            bmp.compress( Bitmap.CompressFormat.JPEG, options, baos );
        }
        File file = new File( path, name + ".jpg" );
        try {
            FileOutputStream fos = new FileOutputStream( file );
            fos.write( baos.toByteArray() );
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Context cxt, Uri uri, int output_X, int output_Y,
                             int REQUEST) {
        Intent intent = new Intent( "com.android.camera.action.CROP" );
        intent.setDataAndType( uri, "image/*" );
        // 把裁剪的数据填入里面
        // 设置裁剪
        intent.putExtra( "crop", "true" );
        // aspectX , aspectY :宽高的比例
        intent.putExtra( "aspectX", 1 );
        intent.putExtra( "aspectY", 1 );
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra( "outputX", output_X );
        intent.putExtra( "outputY", output_Y );
        intent.putExtra( "return-data", true );
        ((BaseActivity) cxt).startActivityForResult( intent, REQUEST );
    }

    @SuppressWarnings("deprecation")
    public static Options getBitmapOption(int inSampleSize) {
        System.gc();
        Options options = new Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }


    /**
     * file转bitmap fileToBitmap:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param path
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static Bitmap fileToBitmap(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile( path, getBitmapOption( 1 ) );
        return bitmap;
    }

    /**
     * 不压缩bitmap保存到file saveBitmapToFile:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param bitmap
     * @param path
     * @param name
     * @author John Lie
     * @since JDK 1.6
     */
    public static String saveBitmapToFile(Bitmap bitmap, String path,
                                          String name) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress( Bitmap.CompressFormat.JPEG, options, baos );
        File file = new File( path, name + UUID.randomUUID().toString() + ".png" );
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream( file );
            fos.write( baos.toByteArray() );
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /**
     * 旋转图片
     *
     * @param bitmap 源图片
     * @param angle  旋转角度(90为顺时针旋转,-90为逆时针旋转)
     * @param x      中心x坐标
     * @param y      中心y坐标
     * @return Bitmap
     */
    public static Bitmap rotate(Bitmap bitmap, float angle, float x, float y) {
        Matrix matrix = new Matrix();
        matrix.postRotate( angle, x, y );
        Bitmap map = Bitmap.createBitmap( bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true );
        return map;
    }

    /**
     * 旋转图片
     *
     * @param bitmap 源图片
     * @param angle  旋转角度(90为顺时针旋转,-90为逆时针旋转)
     * @return Bitmap
     */
    public static Bitmap rotate(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate( angle );
        Bitmap map = Bitmap.createBitmap( bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true );
        return map;
    }

    public static Bitmap readDrawableBitMap(Context context, int resId) {
        Options opt = new Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
