package com.tenone.gamebox.share;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.DynamicModel;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.StrategyMode;
import com.tenone.gamebox.view.activity.MainActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class SharePopupWindow extends PopupWindow {
    private Context context;
    private PlatformActionListener platformActionListener;
    private ShareParams shareParams;
    private GameModel gameModel;
    private float alpha = 1.0f;
    private String url = "http://across.185sy.com";

    public SharePopupWindow(Context cx, GameModel model) {
        this.context = cx;
        this.gameModel = model;
        initShareParams( initShareModel( gameModel ) );
        showShareWindow();
    }

    public SharePopupWindow(Context cx, String url) {
        this.context = cx;
        initShareParams( initShareModel( url ) );
        showShareWindow();
    }

    public SharePopupWindow(Context cx, DynamicModel model) {
        this.context = cx;
        initShareParams( initShareModel( model ) );
        showShareWindow();
    }
	public SharePopupWindow(Context cx, StrategyMode model) {
		this.context = cx;
		initShareParams( initShareModel( model ) );
		showShareWindow();
	}


    public PlatformActionListener getPlatformActionListener() {
        return platformActionListener;
    }

    public void setPlatformActionListener(
            PlatformActionListener platformActionListener) {
        this.platformActionListener = platformActionListener;
    }

    @SuppressLint("InflateParams")
    public void showShareWindow() {
        View view = LayoutInflater.from( context ).inflate( R.layout.layout_share,
                null );
        setStyle( view );
        GridView gridView = view.findViewById( R.id.share_gridview );
        ShareAdapter adapter = new ShareAdapter( context );
        gridView.setAdapter( adapter );
        Button btn_cancel = view.findViewById( R.id.btn_cancel );
        btn_cancel.setOnClickListener( v -> dismiss() );
        gridView.setOnItemClickListener( new ShareItemClickListener() );
    }

    private void setStyle(View view) {
        this.setContentView( view );
        this.setWidth( LayoutParams.MATCH_PARENT );
        this.setHeight( LayoutParams.WRAP_CONTENT );
        this.setFocusable( true );
        ColorDrawable dw = new ColorDrawable( 0xb0000000 );
        this.setBackgroundDrawable( dw );
        setAnimationStyle( R.style.PopupAnimation );
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        toDark();
        super.showAtLocation( parent, gravity, x, y );
    }

    @Override
    public void showAsDropDown(View anchor) {
        toDark();
        super.showAsDropDown( anchor );
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        toDark();
        super.showAsDropDown( anchor, xoff, yoff );
    }

    @SuppressLint("NewApi")
    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        toDark();
        super.showAsDropDown( anchor, xoff, yoff, gravity );
    }

    @Override
    public void dismiss() {
        toLight();
        super.dismiss();
    }

    private class ShareItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            share( position );
            dismiss();
        }
    }

    public void share(int position) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( TrackingUtils.USERNAMEKEY, SpUtil.getAccount() );
        map.put( TrackingUtils.NICKNAMEKEY, SpUtil.getNick() );
        map.put( TrackingUtils.MOBILEKEY, SpUtil.getPhone() );
        switch (position) {
            case 0:
                wechat();
                map.put( TrackingUtils.SHARESTYLEKEY, "wechat" );
                break;
            case 1:
                WechatMoments();
                map.put( TrackingUtils.SHARESTYLEKEY, "wechatMoments" );
                break;
            case 2:
                qq();
                map.put( TrackingUtils.SHARESTYLEKEY, "qq" );
                break;
            case 3:
                qzone();
                map.put( TrackingUtils.SHARESTYLEKEY, "qzone" );
                break;
            default:
                Platform plat = null;
                plat = ShareSDK.getPlatform( context, getPlatform( position ) );
                if (platformActionListener != null) {
                    plat.setPlatformActionListener( getPlatformActionListener() );
                    plat.share( shareParams );
                }
                break;
        }
        TrackingUtils.setEvent( TrackingUtils.SHARESTYLEKEY, map );
    }

    public void initShareParams(ShareModel shareModel) {
        if (shareModel != null) {
            ShareParams sp = new ShareParams();
            sp.setContentType( Platform.SHARE_WEBPAGE );
            if (!TextUtils.isEmpty( shareModel.getTitle() ))
                sp.setTitle( (shareModel.getTitle().length() > 30)
                        ? shareModel.getTitle().substring( 0, 29 ) : shareModel.getTitle() );
            if (!TextUtils.isEmpty( shareModel.getText() ))
                sp.setText( (shareModel.getText().length() > 30)
                        ? shareModel.getText().substring( 0, 29 ) : shareModel.getText() );
            sp.setUrl( shareModel.getUrl() );
            sp.setTitleUrl( shareModel.getUrl() );
            sp.setImageUrl( shareModel.getImageUrl() );
            if (!TextUtils.isEmpty( shareModel.getComment() ))
                sp.setComment( (shareModel.getComment().length() > 30)
                        ? shareModel.getComment().substring( 0, 29 ) : shareModel.getComment() );
            sp.setSite( shareModel.getSite() );
            sp.setSiteUrl( shareModel.getSiteUrl() );
            BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable( context,R.drawable.icon_luncher );
            sp.setImageData( drawable.getBitmap() );
            this.shareParams = sp;
        }
    }

    public ShareModel initShareModel(GameModel gameModel) {
        ShareModel shareModel = new ShareModel();
        shareModel.setImageUrl( gameModel.getImgUrl() );
        shareModel.setSite( context.getResources().getString( R.string.app_name ) );
        shareModel.setSiteUrl( url );
        shareModel.setUrl( "http://m.185sy.com/Game-appGameinfo-id-" + gameModel.getGameId()
                + ".html" );
        shareModel.setTitle( gameModel.getName() );
        shareModel.setText( context.getResources().getString( R.string.gameShare1 ) + gameModel.getName() + context.getResources().getString( R.string.gameShare2 ) );
        return shareModel;
    }

	public ShareModel initShareModel(StrategyMode gameModel) {
		ShareModel shareModel = new ShareModel();
		shareModel.setImageUrl( gameModel.getStrategyImgUrl() );
		shareModel.setSite( context.getResources().getString( R.string.app_name ) );
		shareModel.setSiteUrl( url );
		shareModel.setUrl( gameModel.getUrl() );
		shareModel.setTitle( gameModel.getStrategyName() );
		shareModel.setText(gameModel.getStrategyName());
		return shareModel;
	}

    public ShareModel initShareModel(String url) {
        ShareModel shareModel = new ShareModel();
        shareModel
                .setImageUrl( "http://www.185sy.com/themes/template/Public/img/c_icon.png" );
        shareModel.setSite( context.getResources().getString( R.string.app_name ) );
        shareModel.setSiteUrl( "http://www.185sy.com/" );
        shareModel.setUrl( url );
        shareModel.setTitle( context.getResources().getString( R.string.boxShare1 ) );
        shareModel.setText( context.getResources().getString( R.string.boxShare2 ) );
        return shareModel;
    }

    public ShareModel initShareModel(DynamicModel model) {
        ShareModel shareModel = new ShareModel();
        ArrayList<String> arrayList = model.getDynamicImg();
        if (arrayList != null && arrayList.size() > 0) {
            String path = arrayList.get( 0 );
            shareModel
                    .setImageUrl( path );
        } else {
            shareModel
                    .setImageUrl( "http://www.185sy.com/themes/template/Public/img/c_icon.png" );
        }
        shareModel.setSite( context.getResources().getString( R.string.app_name ) );
        shareModel.setSiteUrl(url );
        shareModel.setUrl( MyApplication.getHttpUrl().getDynamicsWapInfo() + "?id=" + model.getDynamicModelId() );
        shareModel.setTitle( model.getContent() );
        shareModel.setText( model.getContent() );
        return shareModel;
    }

    public String getPlatform(int position) {
        String platform = "";
        switch (position) {
            case 0:
                platform = "Wechat";
                break;
            case 1:
                platform = "WechatMoments";
                break;
            case 2:
                platform = "QQ";
                break;
            case 3:
                platform = "QZone";
                break;
        }
        return platform;
    }

    private void qzone() {
        ShareParams sp = new ShareParams();
        sp.setContentType( Platform.SHARE_WEBPAGE );
        sp.setTitle( shareParams.getTitle() );
        sp.setTitleUrl( shareParams.getUrl() );
        sp.setText( shareParams.getText() );
        sp.setImageUrl( shareParams.getImageUrl() );
        sp.setComment( context.getResources().getString( R.string.say_about ) );
        sp.setSite( shareParams.getSite() );
        sp.setSiteUrl( shareParams.getSiteUrl() );
        sp.setImageData( shareParams.getImageData() );
        Platform qzone = ShareSDK.getPlatform( context, QZone.NAME );
        qzone.setPlatformActionListener( getPlatformActionListener() );
        qzone.share( sp );
    }

    private void qq() {
        ShareParams sp = new ShareParams();
        sp.setContentType( Platform.SHARE_WEBPAGE );
        sp.setTitle( shareParams.getTitle() );
        sp.setTitleUrl( shareParams.getUrl() );
        sp.setText( shareParams.getText() );
        sp.setImageUrl( shareParams.getImageUrl() );
        sp.setSite( shareParams.getSite() );
        sp.setSiteUrl( shareParams.getSiteUrl() );
        sp.setImageData( shareParams.getImageData() );
        Platform qq = ShareSDK.getPlatform( context, QQ.NAME );
        qq.setPlatformActionListener( getPlatformActionListener() );
        qq.share( sp );
    }

    public void wechat() {
        ShareParams sp = new ShareParams();
        sp.setShareType( Platform.SHARE_WEBPAGE );
        sp.setTitle( shareParams.getTitle() );
        sp.setText( shareParams.getText() );
        sp.setImageUrl( shareParams.getImageUrl() );
        sp.setUrl( shareParams.getUrl() );
        sp.setImageData( shareParams.getImageData() );
        Platform wechat = ShareSDK.getPlatform( context, Wechat.NAME );
        wechat.setPlatformActionListener( getPlatformActionListener() );
        wechat.share( sp );
    }

    public void WechatMoments() {
        ShareParams sp = new ShareParams();
        sp.setShareType( Platform.SHARE_WEBPAGE );
        sp.setTitle( shareParams.getTitle() );
        sp.setText( shareParams.getText() );
        sp.setImageUrl( shareParams.getImageUrl() );
        sp.setUrl( shareParams.getUrl() );
        sp.setImageData( shareParams.getImageData() );
        Platform wechatMoments = ShareSDK.getPlatform( context,
                WechatMoments.NAME );
        wechatMoments.setPlatformActionListener( getPlatformActionListener() );
        wechatMoments.share( sp );
    }

    private void toDark() {
       
        Message msg = new Message();
        msg.what = 1;
        alpha = 0.5f;
        msg.obj = alpha;
        mHandler.sendMessage( msg );
    }

    private void toLight() {
      

        Message msg = new Message();
        msg.what = 1;
        alpha = 1f;
        msg.obj = alpha;
        mHandler.sendMessage( msg );
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
        Activity activity = null;
        if (((Activity) context).getParent() != null) {
            activity = ((Activity) context).getParent();
            if (activity.getClass().getName().contains( "MainActivity" )) {
                activity = ((MainActivity) context);
            }
        } else {
            activity = ((Activity) context);
        }
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = alpha;
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND );
        activity.getWindow().setAttributes( lp );
    }


}
