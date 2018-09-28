package com.tenone.gamebox.view.custom.popupwindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.ListenerManager;


public class AboutMeTypeWindow extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private View view;
    private String[] types;
    private float alpha = 1.0f;
    private TextView typeTv1, typeTv2, typeTv3;
    private int type;

    public AboutMeTypeWindow(Context context, String[] types, int type) {
        super( context );
        this.mContext = context;
        this.types = types;
        this.type = type;
        // ���õ�����ɵ��
        setOutsideTouchable( false );
        setBackgroundDrawable( new BitmapDrawable() );
        setWidth( ViewGroup.LayoutParams.WRAP_CONTENT );
        setHeight( ViewGroup.LayoutParams.WRAP_CONTENT );
        setBackgroundDrawable( getBackground() );
        view = LayoutInflater.from( mContext ).inflate(
                R.layout.window_about_for_me, null );
        setContentView( view );
        initView();
    }

    private void initView() {
        typeTv1 = view.findViewById( R.id.id_window_about_type1 );
        typeTv2 = view.findViewById( R.id.id_window_about_type2 );
        typeTv3 = view.findViewById( R.id.id_window_about_type3 );
        typeTv1.setText( types[0] );
        typeTv2.setText( types[1] );
        typeTv3.setText( types[2] );
        switch (type) {
            case 1:
                typeTv1.setSelected( true );
                typeTv2.setSelected( false );
                typeTv3.setSelected( false );
                break;
            case 2:
                typeTv2.setSelected( true );
                typeTv1.setSelected( false );
                typeTv3.setSelected( false );
                break;
            case 3:
                typeTv3.setSelected( true );
                typeTv1.setSelected( false );
                typeTv2.setSelected( false );
                break;
        }
        typeTv3.setOnClickListener( this );
        typeTv2.setOnClickListener( this );
        typeTv1.setOnClickListener( this );
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation( parent, gravity, x, y );
        toDark();
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown( anchor );
        toDark();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown( anchor, xoff, yoff );
        toDark();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown( anchor, xoff, yoff, gravity );
        toDark();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        toLight();
    }

    private void toDark() {
        new Thread( new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        Thread.sleep( 4 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    alpha -= 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage( msg );
                }
            }

        } ).start();
    }

    private void toLight() {
        new Thread( new Runnable() {
            @Override
            public void run() {
                while (alpha < 1f) {
                    try {
                        Thread.sleep( 4 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    alpha += 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage( msg );
                }
            }
        } ).start();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            float alpha = (float) msg.obj;
            backgroundAlpha( alpha );
            super.dispatchMessage( msg );
        }
    };

    public void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = alpha;
        ((Activity) mContext).getWindow().setAttributes( lp );
        ((Activity) mContext).getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_window_about_type1:
                ListenerManager.sendOnTypeSelecteListener( 1 );
                break;
            case R.id.id_window_about_type2:
                ListenerManager.sendOnTypeSelecteListener( 2 );
                break;
            case R.id.id_window_about_type3:
                ListenerManager.sendOnTypeSelecteListener( 3 );
                break;
        }
        dismiss();
    }
}
