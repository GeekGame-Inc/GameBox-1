package com.tenone.gamebox.view.base;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.tenone.gamebox.R;

@SuppressWarnings( "deprecation" )
public class GlideCache implements GlideModule {

	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
		String downloadDirectoryPath = Configuration.getCachepath();
		int cacheSize = 100 * 1000 * 1000;
		builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath,
				context.getString( R.string.image_cache), cacheSize));
	}

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }
}
