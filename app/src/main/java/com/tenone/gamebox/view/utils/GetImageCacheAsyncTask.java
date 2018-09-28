package com.tenone.gamebox.view.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.tenone.gamebox.view.custom.ToastCustom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GetImageCacheAsyncTask extends AsyncTask<String, Void, File> {

    private Context mContext;

    private String imgUrl;

    public GetImageCacheAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected File doInBackground(String... strings) {
        imgUrl = strings[0];
        try {
            return Glide.with( mContext )
                    .load( imgUrl )
                    .downloadOnly( Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL )
                    .get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(File file) {
        if (file == null) {
            return;
        }
        File tagFile = null;
        try {
            int index = imgUrl.lastIndexOf( "/" );
            //String name = imgUrl.substring(index + 1);
            char[] ch = imgUrl.toCharArray();
            String name = String.copyValueOf( ch, index + 1, ch.length - index - 1 );
					tagFile = FileUtils.createFile( FileUtils.getCacheDirectory( mContext ).getPath() + "/\u4fdd\u5b58\u7684\u56fe\u7247/", name );
            FileUtils.copyFile( file, tagFile );
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tagFile != null) {
            try {
                MediaStore.Images.Media.insertImage( mContext.getContentResolver(),
										tagFile.getAbsolutePath(), tagFile.getName(), "\u624b\u6e38\u76d2\u5b50\u4fdd\u5b58\u7684\u56fe\u7247" );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mContext.sendBroadcast( new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse( "file://" + tagFile.getAbsolutePath() ) ) );
        }
			ToastCustom.makeText( mContext, "\u56fe\u7247\u6210\u529f\u4fdd\u5b58\u5230" + tagFile.getAbsolutePath(), ToastCustom.LENGTH_SHORT ).show();
    }
}
