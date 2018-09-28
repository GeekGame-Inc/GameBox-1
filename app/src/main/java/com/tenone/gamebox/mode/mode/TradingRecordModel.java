package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;

public class TradingRecordModel implements Serializable {
    private static final long serialVersionUID = -7552218913353710009L;
    private String gameName;
    private String title;
    private String money;
    private String time;
    private int status;
    private String reason;
    private String productId;
    private boolean isDelete = false;
    private boolean saleEnabled = true;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        if (!TextUtils.isEmpty( time )) {
            String text = "";
            try {
                long t = Long.valueOf( time ).longValue() * 1000;
                text = TimeUtils.formatDateMin( t );
            } catch (NumberFormatException e) {
            }
            return text;
        }
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isSaleEnabled() {
        return saleEnabled;
    }

    public void setSaleEnabled(boolean saleEnabled) {
        this.saleEnabled = saleEnabled;
    }
}
