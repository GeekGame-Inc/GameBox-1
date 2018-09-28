package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnRecyclerViewItemClickListener;
import com.tenone.gamebox.mode.mode.TaskCenterModel;

import java.util.List;

public class TaskCenterAdapter extends RecyclerView.Adapter<TaskCenterAdapter.TCAViewHolder> {

	private Context context;
	private List<TaskCenterModel> models;
	private LayoutInflater inflater;
	private OnRecyclerViewItemClickListener<Integer> onRecyclerViewItemClickListener;

	public TaskCenterAdapter(Context context, List<TaskCenterModel> models) {
		this.context = context;
		this.models = models;
		inflater = LayoutInflater.from( context );
	}


	@Override
	public TCAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate( R.layout.item_task_center, parent, false );
		TCAViewHolder holder = new TCAViewHolder( view );
		return holder;
	}

	@Override
	public void onBindViewHolder(TCAViewHolder holder, int position) {
		TaskCenterModel model = models.get( position );
		BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable( model.getIconId() );
		holder.iconIv.setImageBitmap( drawable.getBitmap() );
		holder.nameTv.setText( model.getItemName() );
		holder.introTv.setText( model.getIntro() );
		if (!model.isVisible()) {
			holder.pointV.setVisibility( View.GONE );
			holder.statusTv.setVisibility( View.GONE );
		} else {
			holder.statusTv.setVisibility( View.VISIBLE );
			holder.pointV.setVisibility( model.getStatus() == 0 ? View.VISIBLE : View.GONE );
			holder.statusTv.setText( model.getStatus() == 0 ? "\u672a\u5b8c\u6210" : "\u5df2\u5b8c\u6210 " );
		}
		holder.rootView.setOnClickListener( v -> {
			if (onRecyclerViewItemClickListener != null) {
				onRecyclerViewItemClickListener.onRecyclerViewItemClick( position );
			}
		} );
	}

	public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<Integer> onRecyclerViewItemClickListener) {
		this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
	}

	@Override
	public int getItemCount() {
		return models.size();
	}

	public class TCAViewHolder extends RecyclerView.ViewHolder {
		RelativeLayout rootView;
		ImageView iconIv;
		TextView nameTv, introTv, statusTv;
		View pointV;

		public TCAViewHolder(View itemView) {
			super( itemView );
			rootView = itemView.findViewById( R.id.id_item_task_root );
			iconIv = itemView.findViewById( R.id.id_item_task_icon );
			nameTv = itemView.findViewById( R.id.id_item_task_name );
			introTv = itemView.findViewById( R.id.id_item_task_intro );
			statusTv = itemView.findViewById( R.id.id_item_task_status );
			pointV = itemView.findViewById( R.id.id_item_task_point );
		}
	}
}
