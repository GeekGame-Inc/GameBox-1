package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.RebateHelp;
import com.tenone.gamebox.view.utils.BitmapUtils;

import java.util.List;

public class RebateHelpAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<RebateHelp> models;
	private LayoutInflater mInflater;

	public RebateHelpAdapter(Context cxt, List<RebateHelp> list) {
		this.mContext = cxt;
		this.models = list;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getGroupCount() {
		return models == null ? 0 : models.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return models.get(groupPosition).getArrays() == null ? 0 : 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return models == null ? null : models.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return models == null ? null
				: (models.get( groupPosition ).getArrays());
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		String id = "1" + groupPosition + "0" + childPosition;
		return Long.valueOf(id).longValue();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_rebate_help_group,
					parent, false);
			holder = new ViewHolder(convertView,
					R.id.id_item_rebate_help_group_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.icon_jt_y );
		float angle = 0;
		if (isExpanded) {
			angle = -90;
		}else {
			angle  = 90;
		}
		bitmap = BitmapUtils.rotate(bitmap, angle);
		Drawable drawable = new BitmapDrawable(bitmap);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		holder.textView.setCompoundDrawables(null, null, drawable, null);
		RebateHelp rebateHelp = models.get(groupPosition);
		holder.textView.setText(rebateHelp.getTitle());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_rebate_help_child,
					parent, false);
			holder = new ViewHolder(convertView,
					R.id.id_item_rebate_help_child_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String contents = models.get(groupPosition).getArrays();
		holder.textView.setText("\n"+contents+"\n");
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private class ViewHolder {
		TextView textView;

		public ViewHolder(View view, int rId) {
			textView = view.findViewById(rId);
		}
	}

}
