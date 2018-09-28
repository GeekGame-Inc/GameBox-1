package com.tenone.gamebox.view.custom.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.activity.BGAPhotoPickerActivity;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;


public class SelecteDynamicTypeWindow extends PopupWindow {
    private Context mContext;
    private ArrayList<String> paths = new ArrayList<String>();
    private View view;
    private float alpha = 1.0f;
    private int requestCode = 110;
    private OnTypeSelectListener listener;

    public SelecteDynamicTypeWindow(Context context, ArrayList<String> paths, int requestCode) {
        super( context );
        this.mContext = context;
        this.paths = paths;
        this.requestCode = requestCode;
        setOutsideTouchable( true );
        setBackgroundDrawable( new BitmapDrawable() );
        int width = DisplayMetricsUtils.getScreenWidth( mContext );
        setWidth( width );
        setHeight( ViewGroup.LayoutParams.WRAP_CONTENT );
        setAnimationStyle( R.style.PopupAnimation );
        setBackgroundDrawable( getBackground() );
        view = LayoutInflater.from( mContext ).inflate(
                R.layout.window_dynamic_type, null );
        setContentView( view );
        ViewUtils.inject( this, view );
    }

    @OnClick({R.id.id_window_select_dynamic_img, R.id.id_window_select_dynamic_gif})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_window_select_dynamic_img:
                if (listener != null) {
                    listener.onTypeSelect( 0 );
                }
                choicePhotoWrapper( 4 );
                break;
            case R.id.id_window_select_dynamic_gif:
                if (listener != null) {
                    listener.onTypeSelect( 1 );
                }
                choicePhotoWrapper( 1 );
                break;
        }
        dismiss();
    }

    public void setListener(OnTypeSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public void showAsDropDown(View anchor) {
        toDark();
        super.showAsDropDown( anchor );
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        toDark();
        super.showAsDropDown( anchor, xoff, yoff, gravity );
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        toDark();
        super.showAsDropDown( anchor, xoff, yoff );
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        toDark();
        super.showAtLocation( parent, gravity, x, y );
    }

    @Override
    public void dismiss() {
        toLight();
        super.dismiss();
    }

    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            backgroundAlpha( alpha );
            super.dispatchMessage( msg );
        }
    };


    public void toDark() {
        new Thread( () -> {
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
				} ).start();
    }


    public void toLight() {
        new Thread( () -> {
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
				} ).start();
    }

    public void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((BaseActivity) mContext).getWindow()
                .getAttributes();
        lp.alpha = alpha; // 0.0-1.0
        ((BaseActivity) mContext).getWindow().setAttributes( lp );
        ((BaseActivity) mContext).getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND );
    }

    @AfterPermissionGranted(110)
    private void choicePhotoWrapper(int maxImage) {
        File takePhotoDir = (1 == maxImage) ? null : FileUtils.getCacheDirectory( mContext );
        ((BaseActivity) mContext).startActivityForResult( BGAPhotoPickerActivity.newIntent( mContext, takePhotoDir, maxImage,
                paths, (1 == maxImage) ), requestCode );
    }


    public interface OnTypeSelectListener {
        void onTypeSelect(int type);
    }
}
