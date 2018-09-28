package com.tenone.gamebox.view.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.tenone.gamebox.mode.listener.PayResultCallback;
import com.tenone.gamebox.mode.mode.AlipayResult;

public class TradingPayTask extends AsyncTask<String, String, AlipayResult> {
    private PayResultCallback payResultCallback;
    private Context context;

    public TradingPayTask(PayResultCallback payResultCallback, Context context) {
        this.payResultCallback = payResultCallback;
        this.context = context;
    }

    @Override
    protected AlipayResult doInBackground(String... strings) {
        String tag = strings[0];
        return PayUtils.startAlipay( (Activity) context, tag );
    }

    @Override
    protected void onPostExecute(AlipayResult alipayResult) {
        super.onPostExecute( alipayResult );
        String resultStatus = alipayResult.getResultStatus();
        if (payResultCallback != null) {
            switch (resultStatus) {
							case "9000":
								// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。支付成功
								ToastUtils.showToast( context, "\u652f\u4ed8\u6210\u529f" );
								payResultCallback.onPaySuccess();
								break;
							case "6001":
								// 用户中途取消
								ToastUtils.showToast( context, "\u652f\u4ed8\u53d6\u6d88" );
								payResultCallback.onPayCancle();
								break;
							case "4000":
								// 订单支付失败
								ToastUtils.showToast( context, "\u652f\u4ed8\u5931\u8d25" );
								payResultCallback.onPayCancle();
								break;
							case "8000":
								// 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
								ToastUtils.showToast( context, "\u6b63\u5728\u5904\u7406\u4e2d" );
								payResultCallback.onPayFail();
								break;
							case "5000":
								//重复请求
								ToastUtils.showToast( context, "\u91cd\u590d\u8bf7\u6c42" );
								payResultCallback.onPayFail();
								break;
							case "6002":
								//网络连接出错
								ToastUtils.showToast( context, "\u7f51\u7edc\u8fde\u63a5\u51fa\u9519" );
								payResultCallback.onPayCancle();
								break;
							case "6004":
								//支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
								ToastUtils.showToast( context, "\u652f\u4ed8\u7ed3\u679c\u672a\u77e5" );
								payResultCallback.onPayFail();
								break;
							default:
								// 其它支付错误
								ToastUtils.showToast( context, "\u5176\u5b83\u652f\u4ed8\u9519\u8bef" );
								payResultCallback.onPayCancle();
								break;
            }
        }
    }
}
