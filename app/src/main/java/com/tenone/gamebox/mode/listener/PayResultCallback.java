package com.tenone.gamebox.mode.listener;

public interface PayResultCallback {
    
    void onPaySuccess();

    
    void onPayFail();

    
    void onPayCancle();
}
