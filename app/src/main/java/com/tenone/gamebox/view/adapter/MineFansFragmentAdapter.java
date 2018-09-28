package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnDriverItemClickListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.view.custom.AttentionTextView;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;


public class MineFansFragmentAdapter extends RecyclerView.Adapter<MineFansFragmentAdapter.MineFansViewHolder> {
	private Context context;
	private List<DriverModel> models = new ArrayList<DriverModel>();
	private LayoutInflater inflater;
	private OnDriverItemClickListener onDriverItemClickListener;
	private String userId;

	public MineFansFragmentAdapter(Context context, List<DriverModel> models) {
		this.context = context;
		this.models = models;
		this.inflater = LayoutInflater.from( context );
	}

	@Override
	public MineFansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate( R.layout.item_fans_attention_list, parent, false );
		MineFansViewHolder holder = new MineFansViewHolder( view );
		return holder;
	}

	@Override
	public void onBindViewHolder(MineFansViewHolder holder, int position) {
		DriverModel model = models.get( position );
		ImageLoadUtils.loadNormalImg( holder.headerView, context, model.getHeader() );
		holder.nameTv.setText( model.getNick() );
		holder.vipIv.setVisibility( model.isVip() ? View.VISIBLE : View.GONE );

		if ("\u672a\u8bbe\u7f6e".equals( model.getSex() )) {
			holder.sexIv.setVisibility( View.GONE );
		} else {
			holder.sexIv.setVisibility( View.VISIBLE );
			holder.sexIv.setSelected( !context.getString( R.string.man ).equals( model.getSex() ) );
		}

		holder.fansTv.setText( model.getFansNum() + "\u7c89\u4e1d" );
		if (model.getDriverId().equals( SpUtil.getUserId() )) {
			holder.attentionIv.setVisibility( View.GONE );
		} else {
			holder.attentionIv.setVisibility( View.VISIBLE );
			holder.attentionIv.setStatus( model.isAttention() );
		}
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public int getItemCount() {
		return models.size();
	}

	public void setOnDriverItemClickListener(OnDriverItemClickListener onDriverItemClickListener) {
		this.onDriverItemClickListener = onDriverItemClickListener;
	}

	public class MineFansViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private CircleImageView headerView;
		private TextView nameTv, fansTv;
		private ImageView sexIv, vipIv;
		private DriverModel model;
		private AttentionTextView attentionIv;

		public MineFansViewHolder(View itemView) {
			super( itemView );
			this.headerView = itemView.findViewById( R.id.id_item_fans_attention_header );
			this.attentionIv = itemView.findViewById( R.id.id_item_fans_attention_attention );
			this.nameTv = itemView.findViewById( R.id.id_item_fans_attention_name );
			this.sexIv = itemView.findViewById( R.id.id_item_fans_attention_sexImg );
			this.vipIv = itemView.findViewById( R.id.id_item_fans_attention_vipImg );
			this.fansTv = itemView.findViewById( R.id.id_item_fans_attention_fans );
			headerView.setOnClickListener( this );
			attentionIv.setOnClickListener( this );
		}

		@Override
		public void onClick(View v) {
			if (models.size() > 0 && getPosition() > -1 && getPosition() < models.size()) {
				model = models.get( getPosition() );
				switch (v.getId()) {
					case R.id.id_item_fans_attention_header:
						if (onDriverItemClickListener != null) {
							onDriverItemClickListener.onDriverHeaderClick( model );
						}
						break;
					case R.id.id_item_fans_attention_attention:
						if (onDriverItemClickListener != null) {
							onDriverItemClickListener.onAttentionClick( model );
						}
						break;
				}
			}
		}
	}
}
