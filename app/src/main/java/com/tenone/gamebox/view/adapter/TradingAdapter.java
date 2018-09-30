package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnRecyclerViewItemClickListener;
import com.tenone.gamebox.mode.mode.TradingModel;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class TradingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int TYPE_FOOT = 56;
	private static final int TYPE_NORMAL = 88;

	private Context context;
	private List<TradingModel> models;
	private LayoutInflater inflater;
	private boolean isLoading = false;
	private OnRecyclerViewItemClickListener<TradingModel> onRecyclerViewItemClickListener;
	private int imgWidth, imgHeight;

	public TradingAdapter(Context context, List<TradingModel> models) {
		this.context = context;
		this.models = models;
		this.inflater = LayoutInflater.from( context );
		imgWidth = DisplayMetricsUtils.getScreenWidth( context ) / 3;
		imgHeight = imgWidth * 9 / 16;
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_FOOT) {
			View itemView = inflater.inflate( R.layout.layout_footer, parent, false );
			return new TAFViewHolder( itemView );
		} else {
			View itemView = inflater.inflate( R.layout.item_trading, parent, false );
			return new TAViewHolder( itemView );
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (TYPE_NORMAL == getItemViewType( position )) {
			TradingModel model = models.get( position );
			TAViewHolder tHolder = (TAViewHolder) holder;
			tHolder.nameTv.setText( model.getTitle() );
			tHolder.moenyTv.setText( "\uffe5" + model.getPrice() );
			tHolder.serverTv.setText( "\u533a\u670d:" + model.getServer() );
			tHolder.platformTv.setText( "\u5e73\u53f0:" + (1 == model.getPlatform() ? context.getString( R.string.android ) :
					(2 == model.getPlatform()) ? "ios" : context.getString( R.string.double_end )) );
			tHolder.gameNameTv.setText( model.getGameName() );
			ViewGroup.LayoutParams params = tHolder.img.getLayoutParams();
			params.width = imgWidth;
			params.height = imgHeight;
			tHolder.img.setLayoutParams( params );
			ImageLoadUtils.loadRoundGameDetailsImg( tHolder.img, context, model.getImgUrl(), 5 );
			String time = model.getStartTime();
			try {
				long t = Long.valueOf( time ).longValue() * 1000;
				time = TimeUtils.formatDateMin( t );
			} catch (NumberFormatException e) {
				time = "";
			}
			tHolder.timeTv.setText( time );
			tHolder.rootView.setOnClickListener( v -> {
				if (onRecyclerViewItemClickListener != null) {
					onRecyclerViewItemClickListener.onRecyclerViewItemClick( model );
				}
			} );
		}
	}


	public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<TradingModel> onRecyclerViewItemClickListener) {
		this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	public void setLoading(boolean loading) {
		isLoading = loading;
	}

	public boolean isLoading() {
		return isLoading;
	}

	@Override
	public int getItemCount() {
		if (models.size() > 0) {
			if (isLoading) {
				return models.size() + 1;
			} else {
				return models.size();
			}
		}
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		if (position >= models.size() && isLoading) {
			return TYPE_FOOT;
		}
		return TYPE_NORMAL;
	}


	public class TAViewHolder extends RecyclerView.ViewHolder {
		private ImageView img;
		private TextView nameTv, serverTv, platformTv, moenyTv, timeTv, gameNameTv;
		private LinearLayout rootView;

		public TAViewHolder(View itemView) {
			super( itemView );
			rootView = itemView.findViewById( R.id.id_item_trading_root );
			gameNameTv = itemView.findViewById( R.id.id_item_trading_gameName );
			img = itemView.findViewById( R.id.id_item_trading_img );
			nameTv = itemView.findViewById( R.id.id_item_trading_name );
			serverTv = itemView.findViewById( R.id.id_item_trading_server );
			platformTv = itemView.findViewById( R.id.id_item_trading_platform );
			moenyTv = itemView.findViewById( R.id.id_item_trading_money );
			timeTv = itemView.findViewById( R.id.id_item_trading_time );
		}
	}

	public class TAFViewHolder extends RecyclerView.ViewHolder {
		public TAFViewHolder(View itemView) {
			super( itemView );
		}
	}

}
