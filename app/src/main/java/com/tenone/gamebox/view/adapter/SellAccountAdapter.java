package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.SellAccountModel;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class SellAccountAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<SellAccountModel> models;
    private LayoutInflater inflater;
    private OnStatusClickListener onStatusClickListener;

    public SellAccountAdapter(Context context, List<SellAccountModel> models) {
        this.context = context;
        this.models = models;
        this.inflater = LayoutInflater.from( context );
    }

    @Override
    public int getGroupCount() {
        return models.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return models.get( groupPosition ).getGameModels().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return models.get( groupPosition );
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return models.get( groupPosition ).getGameModels().get( childPosition );
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return Long.valueOf( "1" + groupPosition + "0" + childPosition ).longValue();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_select_account_group, parent, false );
            holder = new GroupViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        SellAccountModel model = models.get( groupPosition );
        holder.accountTv.setText( context.getString( R.string.account ) + (groupPosition + 1) + " : " + model.getAccount() );
        holder.accountTv.setSelected( isExpanded );
        holder.statusTv.setText( model.getStatus() == 0 ? "\u53d6\u6d88\u5173\u8054" : "\u4ea4\u6613\u4e2d" );
        final GroupViewHolder h = holder;
        holder.statusTv.setOnClickListener( v -> {
            if (onStatusClickListener != null) {
                onStatusClickListener.onStatusClick( h.statusTv, model );
            }
        } );
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_select_account_child, parent, false );
            holder = new ChildViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        GameModel model = models.get( groupPosition ).getGameModels().get( childPosition );
        holder.nameTv.setText( model.getName() );
        ImageLoadUtils.loadNormalImg( holder.logoIv, context, model.getImgUrl() );
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean isCustomerChildSelectable(int groupPosition) {
        return models.get( groupPosition ).getStatus() == 0;
    }

    private class GroupViewHolder {
        TextView accountTv, statusTv;

        public GroupViewHolder(View view) {
            accountTv = view.findViewById( R.id.id_item_select_account_group_accoutTv );
            statusTv = view.findViewById( R.id.id_item_select_account_group_statusTv );
        }
    }

    private class ChildViewHolder {
        TextView nameTv;
        ImageView logoIv;

        public ChildViewHolder(View view) {
            nameTv = view.findViewById( R.id.id_item_select_account_child_name );
            logoIv = view.findViewById( R.id.id_item_select_account_child_logo );
        }
    }

    public void setOnStatusClickListener(OnStatusClickListener onStatusClickListener) {
        this.onStatusClickListener = onStatusClickListener;
    }

    public interface OnStatusClickListener {
        void onStatusClick(View view, SellAccountModel model);
    }
}
