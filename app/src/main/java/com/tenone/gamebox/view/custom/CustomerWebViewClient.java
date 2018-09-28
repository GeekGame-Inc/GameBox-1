package com.tenone.gamebox.view.custom;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomerWebViewClient extends WebViewClient {

    private OnLoadWebCallback callback;

    private boolean isError = false;

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished( view, url );
        if (!isError) {
            //�ص��ɹ������ز���
            if (callback != null) {
                callback.onLoadSuccess();
            }
        }
        isError = false;
    }

    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError( view, request, error );
        isError = true;
        //�ص�ʧ�ܵ���ز���
    }

    public void setCallback(OnLoadWebCallback callback) {
        this.callback = callback;
    }

    public CustomerWebViewClient(OnLoadWebCallback callback) {
        this.callback = callback;
    }

    public interface OnLoadWebCallback {

        void onLoadSuccess();

        void onLoadError();
    }
}
