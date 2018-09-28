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
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddy on 2018/3/8.
 */

public class SelectAccountWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private Context mContext;
    private View view;
    private List<String> array = new ArrayList<String>();
    private ListView listView;
    private Adapter adapter;
    private OnActionSelectListener onActionSelectListener;

    public SelectAccountWindow(Context mContext, int type) {
        super( mContext );
        this.mContext = mContext;
        initArray( type );
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

    private void initArray(int type) {
        this.array.add( "ȡ������" );
        if (type == 1) {
            this.array.add( "�鿴����" );
        }
    }


    private void initView() {
        listView = view.findViewById( R.id.id_window_role_list );
        adapter = new Adapter();
        listView.setAdapter( adapter );
        listView.setOnItemClickListener( this );
    }

    public void setOnActionSelectListener(OnActionSelectListener onActionSelectListener) {
        this.onActionSelectListener = onActionSelectListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onActionSelectListener != null) {
            onActionSelectListener.onAction( position );
        }
        dismiss();
    }


    private class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return array.get( position );
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
            holder.textView.setText( array.get( position ) );
            return convertView;
        }

        private class ViewHolder {
            TextView textView;

            public ViewHolder(View view) {
                this.textView = view.findViewById( R.id.id_item_role_tv );
            }
        }
    }
    public interface OnActionSelectListener {
        void onAction(int postition);
    }
}
