package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnRecyclerViewItemClickListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class JPGameAdapter extends RecyclerView.Adapter<JPGameAdapter.JPGAViewHolder> {
	private List<GameModel> items;
	private Context mContext;
	private LayoutInflater mInflater;
	private OnRecyclerViewItemClickListener<GameModel> onRecyclerViewItemClickListener;

	public JPGameAdapter(List<GameModel> items, Context mContext) {
		this.items = items;
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from( mContext );
	}


	@Override
	public JPGAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View convertView = mInflater.inflate( R.layout.item_new_game_jp,
				parent, false );
		JPGAViewHolder holder = new JPGAViewHolder( convertView );
		return holder;
	}

	@Override
	public void onBindViewHolder(JPGAViewHolder holder, int position) {
		GameModel gameModel = items.get( position );
		holder.textView.setText( gameModel.getName() );
		holder.typetv.setText( gameModel.getGameType() );
		String content = gameModel.getContent();
		if (TextUtils.isEmpty( content )) {
			holder.welfareTv.setVisibility( View.GONE );
		} else {
			holder.welfareTv.setVisibility( View.VISIBLE );
			holder.welfareTv.setText( content );
		}
		ImageLoadUtils.loadNormalImg( holder.imageView, mContext, gameModel.getImgUrl() );
		holder.rootView.setOnClickListener( v -> {
			if (onRecyclerViewItemClickListener != null) {
				onRecyclerViewItemClickListener.onRecyclerViewItemClick( gameModel );
			}
		} );

		/*ViewGroup.LayoutParams params = holder.rootView.getLayoutParams();
		params.width = DisplayMetricsUtils.getScreenWidth( mContext ) / 4 + DisplayMetricsUtils.dipTopx( mContext, 10 );
		holder.rootView.setLayoutParams( params );*/

		String discount = gameModel.getDis();
		if (!TextUtils.isEmpty( discount )) {
			float d = Float.valueOf( discount ).floatValue();
			if (d > 0) {
				holder.discountTv.setVisibility( View.VISIBLE );
				holder.discountTv.setText( d + "\u6298" );
			} else {
				holder.discountTv.setVisibility( View.GONE );
			}
		}
	}

	public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<GameModel> onRecyclerViewItemClickListener) {
		this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public int getItemCount() {
		return items.size();
	}

	public class JPGAViewHolder extends RecyclerView.ViewHolder {
		ImageView imageView;
		TextView textView, welfareTv, discountTv, typetv;
		RelativeLayout rootView;

		public JPGAViewHolder(View v) {
			super( v );
			initView( v );
		}

		private void initView(View v) {
			typetv = v.findViewById( R.id.id_jp_game_type );
			imageView = v.findViewById( R.id.id_jp_game_img );
			textView = v.findViewById( R.id.id_jp_game_text );
			welfareTv = v.findViewById( R.id.id_jp_game_welfare );
			rootView = v.findViewById( R.id.id_jp_game_root );
			discountTv = v.findViewById( R.id.id_jp_game_dis );
		}
	}
}
