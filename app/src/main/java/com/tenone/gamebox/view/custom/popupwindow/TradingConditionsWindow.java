package com.tenone.gamebox.view.custom.popupwindow;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnPlatformCallback;
import com.tenone.gamebox.mode.listener.OnTradingConditionsCallback;
import com.tenone.gamebox.mode.mode.CommonViewHolder;
import com.tenone.gamebox.view.adapter.CommonAdapter;

import java.util.List;


public class TradingConditionsWindow extends PopupWindow {
    private Context mContext;
    private View view;
    private List<String> conditios;
    private float alpha = 1.0f;
    private ListView listView;
    private CommonAdapter<String> adapter;
    private OnTradingConditionsCallback onTradingConditionsCallback;
    private OnPlatformCallback onPlatformCallback;
    private int type;

    public TradingConditionsWindow(Context context, List<String> conditios, int type) {
        super( context );
        this.mContext = context;
        this.type = type;
        this.conditios = conditios;
        setOutsideTouchable( false );
        setBackgroundDrawable( new BitmapDrawable() );
        setWidth( ViewGroup.LayoutParams.MATCH_PARENT );
        setHeight( ViewGroup.LayoutParams.MATCH_PARENT );
        setBackgroundDrawable( getBackground() );
        view = LayoutInflater.from( mContext ).inflate(
                R.layout.window_trading_conditions, null );
        setContentView( view );
        initView();
    }

    private void initView() {
        listView = view.findViewById( R.id.id_window_trading_list );
        view.findViewById( R.id.id_window_trading_close ).setOnClickListener( v -> {
            dismiss();
        } );
        adapter = new CommonAdapter<String>( mContext, conditios, R.layout.item_window_trading_conditions ) {
            @Override
            public void convert(CommonViewHolder holder, String s) {
                holder.setText( R.id.id_item_window_trading_text, s );
            }
        };
        listView.setAdapter( adapter );
        listView.setOnItemClickListener( (parent, view, position, id) -> {
            if (type == 0) {
                if (onTradingConditionsCallback != null) {
                    onTradingConditionsCallback.onTradingConditions( conditios.get( position ) );
                }
            } else {
                if (onPlatformCallback != null) {
                    onPlatformCallback.onPlatform( conditios.get( position ) );
                }
            }
            dismiss();
        } );
    }

    public void setOnTradingConditionsCallback(OnTradingConditionsCallback onTradingConditionsCallback) {
        this.onTradingConditionsCallback = onTradingConditionsCallback;
    }

    public void setOnPlatformCallback(OnPlatformCallback onPlatformCallback) {
        this.onPlatformCallback = onPlatformCallback;
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect( rect );
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight( h );
        }
        super.showAsDropDown( anchor );
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect( rect );
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight( h );
        }
        super.showAsDropDown( anchor, xoff, yoff );
    }
}
