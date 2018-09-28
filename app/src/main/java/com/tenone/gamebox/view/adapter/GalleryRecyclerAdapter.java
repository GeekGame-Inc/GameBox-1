package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.GRAViewHolder> {

	private List<String> imgUrls;
	private Context mContext;
	private ImageClickListener listener;
	private LayoutInflater inflater;
	private int imgWidth = 0, imgHeight;

	public GalleryRecyclerAdapter(List<String> imgUrls, Context mContext, int imgWidth, int imgHeight) {
		this.imgUrls = imgUrls;
		this.mContext = mContext;
		this.inflater = LayoutInflater.from( mContext );
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
	}

	@Override
	public GRAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate( R.layout.item_image, parent, false );
		GRAViewHolder holder = new GRAViewHolder( view );
		return holder;
	}

	public void setImageClickListener(ImageClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void onBindViewHolder(GRAViewHolder holder, int position) {
		ViewGroup.LayoutParams params = holder.rootView.getLayoutParams();
		params.width = imgWidth;
		params.height = imgHeight;
		holder.rootView.setLayoutParams( params );
		Log.i( "GlideRoundTransform", " image width is " + imgWidth + " image height is " + imgHeight );
		ImageLoadUtils.loadRoundGameDetailsImg( holder.imageView, mContext, imgUrls.get( position ), 8 );
		holder.imageView.setOnClickListener( v -> {
			if (listener != null) {
				listener.onImageClick( imgUrls.get( position ) );
			}
		} );
	}

	@Override
	public int getItemCount() {
		return imgUrls.size();
	}

	public class GRAViewHolder extends RecyclerView.ViewHolder {
		ImageView imageView;
		LinearLayout rootView;

		public GRAViewHolder(View itemView) {
			super( itemView );
			imageView = itemView.findViewById( R.id.id_item_imageView );
			rootView = itemView.findViewById( R.id.id_item_imageView_root );
		}
	}

	public interface ImageClickListener {
		void onImageClick(String url);
	}
}
