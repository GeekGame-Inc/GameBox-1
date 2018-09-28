package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnHeaderClickListener;
import com.tenone.gamebox.mode.mode.AboutForMeCommentModel;
import com.tenone.gamebox.mode.mode.AboutForMeModel;
import com.tenone.gamebox.mode.mode.AboutForMePublicModel;
import com.tenone.gamebox.mode.mode.AboutForMeZanCommenModel;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

public class AboutForMeAdapter extends BaseAdapter {
	private OnHeaderClickListener onHeaderClickListener;
	private AboutForMeModel aboutForMeModel;
	private Context mContext;
	private LayoutInflater inflater;
	private int type = 1;

	public AboutForMeAdapter(AboutForMeModel aboutForMeModel, Context mContext) {
		this.aboutForMeModel = aboutForMeModel;
		this.mContext = mContext;
		this.inflater = LayoutInflater.from( mContext );
	}

	@Override
	public int getCount() {
		int count = 0;
		switch (type) {
			case 1:
				count = aboutForMeModel.getCommentModels().size();
				break;
			case 2:
				count = aboutForMeModel.getZanCommenModels().size();
				break;
			case 3:
				count = aboutForMeModel.getZanDynamicModels().size();
				break;
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		Object object = null;
		switch (type) {
			case 1:
				object = aboutForMeModel.getCommentModels().get( position );
				break;
			case 2:
				object = aboutForMeModel.getZanCommenModels().get( position );
				break;
			case 3:
				object = aboutForMeModel.getZanDynamicModels().get( position );
				break;
		}
		return object;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.item_about_for_me, parent, false );
			holder = new ViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		switch (type) {
			case 1:
				showType1( holder, position );
				break;
			case 2:
				showType2( holder, position );
				break;
			case 3:
				showType3( holder, position );
				break;
		}
		return convertView;
	}

	public void setType(int type) {
		this.type = type;
	}


	private void showType1(ViewHolder holder, int position) {
		final AboutForMeCommentModel model = aboutForMeModel.getCommentModels().get( position );
		ImageLoadUtils.loadNormalImg( holder.headerIv, mContext, model.getcUHedaer() );
		if (TextUtils.isEmpty( model.getDyImg() )) {
			holder.dyIv.setVisibility( View.GONE );
		} else {
			holder.dyIv.setVisibility( View.VISIBLE );
			ImageLoadUtils.loadNormalImg( holder.dyIv, mContext, model.getDyImg() );
		}
		holder.cNickTv.setText( model.getcUNick() );
		holder.vipIv.setVisibility( model.iscUIsVip() ? View.VISIBLE : View.GONE );
		holder.timeTv.setText( model.getTime() );
		if (!TextUtils.isEmpty( model.gettUserId() ) && !"0".equals( model.gettUserId() )) {
			holder.contentTv.setText( getText( R.string.dynamic_comment, model.gettNick(), model.getcContent() ) );
		} else {
			holder.contentTv.setText( model.getcContent() );
		}
		holder.dyContentTv.setText( model.getDyContent() );
		holder.dyNickTv.setText( model.getDyUNick() );
		holder.dyCommentTv.setVisibility( View.GONE );
		holder.headerIv.setOnClickListener( v -> {
			if (onHeaderClickListener != null) {
				DriverModel driverModel = new DriverModel();
				driverModel.setDriverId( model.getcUId() );
				onHeaderClickListener.onHeaderClick( driverModel );
			}
		} );
	}

	private void showType2(ViewHolder holder, int position) {
		final AboutForMeZanCommenModel model = aboutForMeModel.getZanCommenModels().get( position );
		ImageLoadUtils.loadNormalImg( holder.headerIv, mContext, model.getcUHedaer() );
		if (TextUtils.isEmpty( model.getDyImg() )) {
			holder.dyIv.setVisibility( View.GONE );
		} else {
			holder.dyIv.setVisibility( View.VISIBLE );
			ImageLoadUtils.loadNormalImg( holder.dyIv, mContext, model.getDyImg() );
		}
		holder.cNickTv.setText( model.getcUNick() );
		holder.vipIv.setVisibility( model.iscUIsVip() ? View.VISIBLE : View.GONE );
		holder.timeTv.setText( model.getTime() );
		holder.dyContentTv.setText( model.getDyContent() );
		holder.dyNickTv.setText( model.getDyUNick() );
		holder.dyCommentTv.setVisibility( View.VISIBLE );

		if (!TextUtils.isEmpty( model.gettUId() ) && !"0".equals( model.gettUId() )) {
			holder.dyCommentTv.setText( getText( R.string.about_for_me_dy_comment,
					model.getcDyUNick(), model.gettUNick(), model.getcDyContent() ) );
		} else {
			holder.dyCommentTv.setText( model.getcDyUNick() + ":" + model.getcDyContent() );
		}

		holder.contentTv.setText( ((model.getType() == 1) ? "\u8d5e" : "\u8e29") + "\u4e86\u8fd9\u6761\u8bc4\u8bba" );
		holder.headerIv.setOnClickListener( v -> {
			if (onHeaderClickListener != null) {
				DriverModel driverModel = new DriverModel();
				driverModel.setDriverId( model.getcUId() );
				onHeaderClickListener.onHeaderClick( driverModel );
			}
		} );
	}

	private void showType3(ViewHolder holder, int position) {
		final AboutForMePublicModel model = aboutForMeModel.getZanDynamicModels().get( position );
		ImageLoadUtils.loadNormalImg( holder.headerIv, mContext, model.getcUHedaer() );
		if (TextUtils.isEmpty( model.getDyImg() )) {
			holder.dyIv.setVisibility( View.GONE );
		} else {
			holder.dyIv.setVisibility( View.VISIBLE );
			ImageLoadUtils.loadNormalImg( holder.dyIv, mContext, model.getDyImg() );
		}
		holder.cNickTv.setText( model.getcUNick() );
		holder.vipIv.setVisibility( model.iscUIsVip() ? View.VISIBLE : View.GONE );
		holder.timeTv.setText( model.getTime() );
		holder.dyContentTv.setText( model.getDyContent() );
		holder.dyNickTv.setText( model.getDyUNick() );
		holder.dyCommentTv.setVisibility( View.GONE );
		holder.contentTv.setText( ((model.getType() == 1) ? "\u8d5e" : "\u8e29") + "\u4e86\u8fd9\u6761\u52a8\u6001" );
		holder.headerIv.setOnClickListener( v -> {
			if (onHeaderClickListener != null) {
				DriverModel driverModel = new DriverModel();
				driverModel.setDriverId( model.getcUId() );
				onHeaderClickListener.onHeaderClick( driverModel );
			}
		} );
	}

	private Spanned getText(int id, String str, String str2) {
		return Html.fromHtml( mContext.getResources().getString( id,
				str, str2 ) );
	}

	private Spanned getText(int id, String str, String str2, String str3) {
		return Html.fromHtml( mContext.getResources().getString( id,
				str, str2, str3 ) );
	}

	public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
		this.onHeaderClickListener = onHeaderClickListener;
	}

	private class ViewHolder {
		CircleImageView headerIv;
		ImageView dyIv, sexIv, vipIv;
		TextView cNickTv, timeTv, contentTv, dyCommentTv, dyNickTv, dyContentTv;

		public ViewHolder(View view) {
			this.headerIv = view.findViewById( R.id.id_item_for_me_header );
			this.timeTv = view.findViewById( R.id.id_item_for_me_time );
			this.cNickTv = view.findViewById( R.id.id_item_for_me_nicktv );
			this.sexIv = view.findViewById( R.id.id_item_for_me_sexIv );
			this.vipIv = view.findViewById( R.id.id_item_for_me_vipIv );
			this.contentTv = view.findViewById( R.id.id_item_for_me_content );

			this.dyCommentTv = view.findViewById( R.id.id_item_for_me_dynamic_textview );
			this.dyIv = view.findViewById( R.id.id_item_for_me_dynamicIv );
			this.dyNickTv = view.findViewById( R.id.id_item_for_me_dynamic_nickTv );
			this.dyContentTv = view.findViewById( R.id.id_item_for_me_dynamic_content );
		}
	}
}
