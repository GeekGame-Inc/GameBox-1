package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class VipModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3062791711705196512L;
    private String month; // ��ֵ����
    private String money; // ������,Ԫ
    private String ptb; // ����ƽ̨��
    private String vipCoin;//vipƽ̨��
    private String productID;// ��ƷID
    private String oldPrice;
    private int payStyle = 2;//֧����ʽ

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
