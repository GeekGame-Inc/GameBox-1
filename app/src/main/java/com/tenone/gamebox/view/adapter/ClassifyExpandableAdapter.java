package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.mode.GameClassifyModel;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.custom.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class ClassifyExpandableAdapter extends BaseExpandableListAdapter {

	List<GameClassifyModel> items = new ArrayList<GameClassifyModel>();
	Context context;
	LayoutInflater mInflater;
	GameItemClickListener clickListener;

	public ClassifyExpandableAdapter(List<GameClassifyModel> list, Context cxt) {
		this.items = list;
		this.context = cxt;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getGroupCount() {
		return items.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return items.get(groupPosition) == null ? 0 : 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return items.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return items.get(groupPosition).getGameModels() == null ? null : items
				.get(groupPosition).getGameModels().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition + childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_classify_group,
					parent, false);
			holder = new GroupHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (GroupHolder) convertView.getTag();
		}
		holder.textView.setText(items.get(groupPosition).getClassName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_classify_child,
					parent, false);
			holder = new ChildHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}
		final List<GameModel> gameModels = items.get(groupPosition)
				.getGameModels();
		initGridView(holder, gameModels);
		holder.myGridView.setOnItemClickListener( (parent1, view, position, id) -> {
			if (clickListener != null) {
				clickListener.gameItemClick(gameModels.get(position));
			}
		} );
		return convertView;
	}

	private void initGridView(ChildHolder holder, List<GameModel> gameModels) {
		RecommendGameAdapter gameAdapter = new RecommendGameAdapter(gameModels,
				context);
		holder.myGridView.setAdapter(gameAdapter);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void setGridItemClickListener(GameItemClickListener clickListener) {
		this.clickListener = clickListener;
	}

	class GroupHolder {
		TextView textView;

		public GroupHolder(View view) {
			initView(view);
		}

		private void initView(View view) {
			textView = view.findViewById(R.id.id_classify_groupTv);
		}
	}

	class ChildHolder {
		MyGridView myGridView;

		public ChildHolder(View view) {
			initView(view);
		}

		private void initView(View view) {
			myGridView = view
					.findViewById(R.id.id_classify_childGridView);
		}
	}

	class ChildGridHolder {
		ImageView imageView;
		TextView textView;

		public ChildGridHolder(View view) {
			initView(view);
		}

		private void initView(View view) {
			imageView = view.findViewById(R.id.id_noButtonGame_img);
			textView = view.findViewById(R.id.id_noButtonGame_text);
		}
	}

}
