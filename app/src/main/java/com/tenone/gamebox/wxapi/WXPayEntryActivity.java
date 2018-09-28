package com.tenone.gamebox.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.MyLog;
import com.tenone.gamebox.view.utils.ToastUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        MyLog.d( "WXPayEntryActivity onCreate " );
        MyApplication.getIwxapi().handleIntent( getIntent(), this );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent( intent );
        setIntent( intent );
        MyApplication.getIwxapi().handleIntent( intent, this );
        MyLog.d( "WXPayEntryActivity onNewIntent " );
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    public void onResp(BaseResp resp) {
        MyLog.d( "WXPayEntryActivity errCode is  " + resp.errCode );
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:
									ToastUtils.showToast( this, "\u652f\u4ed8\u6210\u529f" );
                    ListenerManager.sendPayResultCallback( 0 );
                    break;
                case -1://���ܵ�ԭ��ǩ������δע��APPID����Ŀ����APPID����ȷ��ע���APPID�����õĲ�ƥ�䡢�����쳣�ȡ�
                    ListenerManager.sendPayResultCallback( -1 );
									ToastUtils.showToast( this, "\u652f\u4ed8\u51fa\u9519" );
                    break;
                case -2://���账�������������û���֧���ˣ����ȡ��������APP��
                    ListenerManager.sendPayResultCallback( -2 );
									ToastUtils.showToast( this, "\u652f\u4ed8\u53d6\u6d88" );
                    break;
            }
            finish();
        }
    }
}
