
package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.PublishGameCommentBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.PublishGameCommentView;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.util.HashMap;
import java.util.Map;

public class PublishGameCommentPresenter extends BasePresenter implements
        OnClickListener, HttpResultListener {
    PublishGameCommentBiz commentBiz;
    PublishGameCommentView commentView;
    Context mContext;
    DynamicCommentModel model;
    DriverModel driverModel;

    public PublishGameCommentPresenter(Context cxt, PublishGameCommentView view) {
        this.commentBiz = new PublishGameCommentBiz();
        this.commentView = view;
        this.mContext = cxt;
    }

    public void initView() {
        getTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
			getTitleBarView().setTitleText( "\u53d1\u8868\u8bc4\u8bba" );
			getTitleBarView().setRightText( "\u53d1\u8868" );
        model = getCommentModel();
        if (model != null) {
            driverModel = getCommentModel().getDriverModel();
            if (driverModel != null) {
                getEditText().setHint( "@" + driverModel.getNick() + ":" );
            }
        }
    }

    public void initListener() {
        getTitleBarView().getLeftImg().setOnClickListener( this );
        getTitleBarView().getRightText().setOnClickListener( this );
    }

    public Intent getIntent() {
        return commentView.getIntent();
    }

    public TitleBarView getTitleBarView() {
        return commentView.getTitleBarView();
    }

    public CustomizeEditText getEditText() {
        return commentView.getEditText();
    }


    public DynamicCommentModel getCommentModel() {
        return commentBiz.getCommentModel( getIntent() );
    }

    private String getAction() {
        return commentBiz.getAction( getIntent() );
    }

    private String getGameId() {
        return commentBiz.getGameId( getIntent() );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_leftImg:
                close( mContext );
                break;
            case R.id.id_titleBar_rightText:
                buildProgressDialog( mContext );
                String comment = getEditText().getText().toString();
                if (TextUtils.isEmpty( comment )) {
									showToast( mContext, "\u8bf7\u8f93\u5165\u8bc4\u8bba\u5185\u5bb9" );
                    break;
                }
                String commentType = "2";
                int is_game_id = 0;
                if (!TextUtils.isEmpty( getAction() )) {
                    commentType = getAction();
                    is_game_id = 1;
                }
                HttpManager.doGameComment( 0, mContext, this,
                        driverModel == null ? "0" : driverModel.getDriverId(), getGameId(), comment,
                        model != null ? model.getIsFake() : 0, commentType ,is_game_id);
                break;
        }
    }


    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog();
        if (1 == resultItem.getIntValue( "status" )) {
					showToast( mContext, "\u53d1\u8868\u6210\u529f" );
            if (resultItem != null) {
                String coin = resultItem.getString( "data" );
                if (!TextUtils.isEmpty( coin )) {
                    ListenerManager.sendOnLoginStateChange( true );
									showToast( mContext, "\u60a8\u4eca\u65e5\u662f\u9996\u6b21\u8bc4\u8bba,\u83b7\u5f97" + coin + "\u91d1\u5e01" );
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( TrackingUtils.USERNAMEKEY, SpUtil.getAccount() );
            map.put( TrackingUtils.NICKNAMEKEY, SpUtil.getNick() );
            map.put( TrackingUtils.MOBILEKEY, SpUtil.getPhone() );
            TrackingUtils.setEvent( TrackingUtils.COMMENTSEVENT, map );
            close( mContext );
        } else {
            showToast( mContext, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog();
        showToast( mContext, error );
    }


}
