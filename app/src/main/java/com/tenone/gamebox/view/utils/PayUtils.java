package com.tenone.gamebox.view.utils;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tenone.gamebox.mode.mode.AlipayResult;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PayUtils {

    public static AlipayResult startAlipay(Activity activity, String orderInfo) {
        PayTask alipay = new PayTask( activity );
        Map<String, String> result = alipay.payV2( orderInfo, true );
        AlipayResult payResult = new AlipayResult( result );
        return payResult;
    }

    public static void startWXPay(ResultItem resultItem) {
        PayReq payReq = new PayReq();
        payReq.appId = "wx998abec7ee53ed78";
        payReq.partnerId = resultItem.getString( "partnerid" );
        payReq.prepayId = resultItem.getString( "prepayid" );
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = resultItem.getString( "noncestr" );
        payReq.timeStamp = String.valueOf( (System.currentTimeMillis() / 1000) );
        map.put( "appid", payReq.appId );
        map.put( "noncestr", payReq.nonceStr );
        map.put( "package", payReq.packageValue );
        map.put( "partnerid", payReq.partnerId );
        map.put( "prepayid", payReq.prepayId );
        map.put( "timestamp", payReq.timeStamp );
        payReq.sign = genAppSign();
        MyApplication.getIwxapi().sendReq( payReq );
    }

    private static String genAppSign() {
        ArrayList<String> keys = new ArrayList<String>();
        Set<String> set = map.keySet();
        for (String key : set) {
            keys.add( key );
        }
        Collections.sort( keys );
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < map.size(); i++) {
            sb.append( keys.get( i ) );
            sb.append( '=' );
            sb.append( map.get( keys.get( i ) ) );
            sb.append( '&' );
        }
        sb.append( "key=" );
        sb.append( "5fe35b38e8512d6d9eed5091cd4ae298" );
        String appSign = null;
        try {
            appSign = EncryptionUtils.getMD5( sb.toString() ).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return appSign;
    }

    private static Map<String, String> map = new HashMap<String, String>();
}
