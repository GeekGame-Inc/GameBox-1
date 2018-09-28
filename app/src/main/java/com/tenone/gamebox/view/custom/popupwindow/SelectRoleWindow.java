package com.tenone.gamebox.view.custom.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.RoleModel;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.List;

/**
 * Created by Eddy on 2018/3/8.
 */

public class SelectRoleWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private Context mContext;
    private View view;
    private List<RoleModel> roleModels;
    private ListView listView;
    private Adapter adapter;
    private OnRoleSelectListener onRoleSelectListener;

    public SelectRoleWindow(Context mContext, List<RoleModel> roleModels) {
        super( mContext );
        this.mContext = mContext;
        this.roleModels = roleModels;
        // ���õ�����ɵ��
        setOutsideTouchable( true );
        setBackgroundDrawable( new BitmapDrawable() );
        setWidth( DisplayMetricsUtils.getScreenWidth( mContext ) / 3 );
        setHeight( ViewGroup.LayoutParams.WRAP_CONTENT );
        setBackgroundDrawable( getBackground() );
        view = LayoutInflater.from( mContext ).inflate(
                R.layout.window_select_role, null );
        setContentView( view );
        initView();
    }


    private void initView() {
        listView = view.findViewById( R.id.id_window_role_list );
        adapter = new Adapter();
        listView.setAdapter( adapter );
        listView.setOnItemClickListener( this );
    }

    public void setOnRoleSelectListener(OnRoleSelectListener onRoleSelectListener) {
        this.onRoleSelectListener = onRoleSelectListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onRoleSelectListener != null) {
            onRoleSelectListener.onRoleSelect( roleModels.get( position ) );
        }
        dismiss();
    }


    private class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return roleModels.size();
        }

        @Override
        public Object getItem(int position) {
            return roleModels.get( position );
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from( mContext ).inflate( R.layout.item_select_role, parent, false );
                holder = new ViewHolder( convertView );
                convertView.setTag( holder );
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            RoleModel model = roleModels.get( position );
            if (model != null)
                holder.textView.setText( model.getServerName() + "�� - " + model.getRoleName() );
            return convertView;
        }

        private class ViewHolder {
            TextView textView;

            public ViewHolder(View view) {
                this.textView = view.findViewById( R.id.id_item_role_tv );
            }
        }
    }

    public interface OnRoleSelectListener {
        void onRoleSelect(RoleModel model);
    }
}
