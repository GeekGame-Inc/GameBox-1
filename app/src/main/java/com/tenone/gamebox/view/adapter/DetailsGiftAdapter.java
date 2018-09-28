package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.GameGiftButtonClickListener;
import com.tenone.gamebox.mode.listener.OnGiftItemClickListener;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.view.custom.TwoStateButton;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;
public class DetailsGiftAdapter extends RecyclerView.Adapter<DetailsGiftAdapter.GiftViewHolder> {
	List<GiftMode> items = new ArrayList<GiftMode>();
	Context mContext;
	LayoutInflater mInflater;
	GameGiftButtonClickListener buttonClickListener;
	OnGiftItemClickListener onGiftItemClickListener;

	public DetailsGiftAdapter(List<GiftMode> list, Context cxt) {
		this.items = list;
		this.mContext = cxt;
		this.mInflater = LayoutInflater.from( mContext );
	}

	@Override
	public GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate( R.layout.item_gift_list, parent,
				false );
		GiftViewHolder holder = new GiftViewHolder( view );
		return holder;
	}

	@Override
	public void onBindViewHolder(GiftViewHolder holder, int position) {
		final GiftMode giftMode = items.get( position );
		holder.giftNum.setText( ":" + giftMode.getGiftCounts() );
		holder.gameName.setText( giftMode.getGiftName() );
		holder.stateButton.setState( giftMode.getState() );
		holder.residueTv.setText( "\u5269\u4f59: " + giftMode.getResidue() );
		holder.stateButton.setOnClickListener( v -> {
			if (buttonClickListener != null) {
				buttonClickListener.onButtonClick( giftMode );
			}
		} );
		holder.giftContentTv.setText( giftMode.getGiftContent() );
		ImageLoadUtils.loadNormalImg( holder.imageView, mContext,
				giftMode.getGiftImgUrl() );
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public void setData(List<GiftMode> list) {
		this.items = list;
		notifyDataSetChanged();
	}

	public void setButtonClickListener(
			GameGiftButtonClickListener buttonClickListener) {
		this.buttonClickListener = buttonClickListener;
	}

	public void setOnGiftItemClickListener(OnGiftItemClickListener onGiftItemClickListener) {
		this.onGiftItemClickListener = onGiftItemClickListener;
	}

	class GiftViewHolder extends RecyclerView.ViewHolder {
		ImageView imageView;
		TextView gameName, residueTv, giftContentTv;
		TextView giftNum;
		RelativeLayout rootView;
		TwoStateButton stateButton;

		public GiftViewHolder(View view) {
			super( view );
			init( view );
		}
	@SuppressWarnings( "deprecation" )
		private void init(View view) {
			giftContentTv = view.findViewById( R.id.id_gift_list_giftContent );
			rootView = view.findViewById( R.id.id_gift_list_root );
			imageView = view.findViewById( R.id.id_gift_list_giftImg );
			gameName = view.findViewById( R.id.id_gift_list_giftName );
			giftNum = view.findViewById( R.id.id_gift_list_giftNum );
			residueTv = view.findViewById( R.id.id_gift_list_residue );
			stateButton = view.findViewById( R.id.id_gift_list_button );
			rootView.setOnClickListener( v -> {
				if (onGiftItemClickListener != null) {
					onGiftItemClickListener.onGiftItemClick( items.get( getPosition() ) );
				}
			} );
		}
	}
}
