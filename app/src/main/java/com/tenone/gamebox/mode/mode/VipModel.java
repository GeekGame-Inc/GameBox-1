package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class VipModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3062791711705196512L;
    private String month; // 充值月数
    private String money; // 所需金额,元
    private String ptb; // 返还平台币
    private String vipCoin;//vip平台币
    private String productID;// 产品ID
    private String oldPrice;
    private int payStyle = 2;//支付方式

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPtb() {
        return ptb;
    }

    public void setPtb(String ptb) {
        this.ptb = ptb;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(int payStyle) {
        this.payStyle = payStyle;
    }

    public String getVipCoin() {
        return vipCoin;
    }

    public void setVipCoin(String vipCoin) {
        this.vipCoin = vipCoin;
    }
}
