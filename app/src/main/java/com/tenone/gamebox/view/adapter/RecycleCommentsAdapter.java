package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.superadapter.SuperAdapter;
import com.john.superadapter.SuperViewHolder;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnDynamicCommentItemClickListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class RecycleCommentsAdapter extends SuperAdapter<DynamicCommentModel> {
	private List<DynamicCommentModel> models;
	private Context mContext;
	private OnDynamicCommentItemClickListener onDynamicCommentItemClickListener;

	public RecycleCommentsAdapter(List<DynamicCommentModel> list, Context cxt) {
		super( cxt, list, R.layout.item_dynamic_comment );
		this.mContext = cxt;
		this.models = list;
	}

	@Override
	public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, DynamicCommentModel item) {
		if (holder == null){
			return;
		}
		final DynamicCommentModel model = models.get( layoutPosition );
		CircleImageView headerView;
		ImageView vipIv, topIv;
		TextView nameTv, commentIv, contentTv, timeTv, praiseIv, deleteTv;
		headerView = holder.findViewById( R.id.id_item_dynamic_comment_header );
		vipIv = holder.findViewById( R.id.id_item_dynamic_comment_vipImg );
		nameTv = holder.findViewById( R.id.id_item_dynamic_comment_name );
		deleteTv = holder.findViewById( R.id.id_item_dynamic_comment_delete );
		commentIv = holder.findViewById( R.id.id_item_dynamic_comment_comment );
		praiseIv = holder.findViewById( R.id.id_item_dynamic_comment_praise );
		contentTv = holder.findViewById( R.id.id_item_dynamic_comment_content );
		timeTv = holder.findViewById( R.id.id_item_dynamic_comment_time );
		topIv = holder.findViewById( R.id.id_item_dynamic_comment_top );
		if (null != model) {
			DriverModel driverModel = model.getDriverModel();
			if (driverModel != null) {
				ImageLoadUtils.loadNormalImg( headerView, mContext, driverModel.getHeader() );
				vipIv.setVisibility( driverModel.isVip() ? View.VISIBLE : View.GONE );
				nameTv.setText( driverModel.getNick() );
				String uid = SpUtil.getUserId();
				String id = driverModel.getDriverId();
				if (!TextUtils.isEmpty( uid ) && !"0".equals( uid ) && uid.equals( id )) {
					deleteTv.setVisibility( View.VISIBLE );
					deleteTv.setOnClickListener( v -> {
						if (null != onDynamicCommentItemClickListener) {
							onDynamicCommentItemClickListener.onDeleteClick( model );
						}
					} );
					commentIv.setVisibility( View.GONE );
				} else {
					deleteTv.setVisibility( View.GONE );
					commentIv.setVisibility( View.VISIBLE );
				}
			}
			if (!TextUtils.isEmpty( model.getToUserId() ) && !"0".equals( model.getToUserId() )) {
				contentTv.setText(
						getString( R.string.dynamic_comment, model.getToUserNick(), model.getCommentContent() ) );
			} else {
				contentTv.setText( model.getCommentContent() );
			}
			timeTv.setText( model.getCommentTime() );
			topIv.setVisibility( model.getOrder() == 1 ? View.VISIBLE : View.GONE );
			int type = model.getLikeTy();
			Drawable drawable = mContext.getResources().getDrawable( R.drawable.selector_good );
			drawable.setBounds( 0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight() );
			praiseIv.setCompoundDrawables( drawable, null, null, null );
			praiseIv.setText( model.getCommentLikes() );
			praiseIv.setSelected( 1 == type );
			praiseIv.setOnClickListener( v -> {
				if (null != onDynamicCommentItemClickListener) {
					onDynamicCommentItemClickListener.onPraiseClick( model );
				}
			} );

			commentIv.setOnClickListener( v -> {
				if (null != onDynamicCommentItemClickListener) {
					onDynamicCommentItemClickListener.onCommentClick( model );
				}
			} );
			headerView.setOnClickListener( v -> {
				if (null != onDynamicCommentItemClickListener) {
					onDynamicCommentItemClickListener.onHeaderClick( model );
				}
			} );
		}
	}

	@Override
	public int getItemCount() {
		return models.size();
	}

	public void setOnDynamicCommentItemClickListener(OnDynamicCommentItemClickListener
																											 onDynamicCommentItemClickListener) {
		this.onDynamicCommentItemClickListener = onDynamicCommentItemClickListener;
	}

	@SuppressWarnings("deprecation")
	private Spanned getString(int id, String str, String str2) {
		return Html.fromHtml( mContext.getResources().getString( id,
				str, str2 ) );
	}
}
