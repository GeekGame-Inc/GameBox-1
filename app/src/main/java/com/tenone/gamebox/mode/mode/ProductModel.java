package com.tenone.gamebox.mode.mode;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {
    private static final long serialVersionUID = 7549453247029436947L;
    private String appid="";
    private String title="";
    private String sdkUserName="";
    private String price="";
    private String desc="";
    private int system;
    private String server="";
    private String endTime="";
    private List<String> imgs;
    private List<String> tradeImgs;
    private String productId="";
    private String gameName="";
    private String size="";
    private String icon="";
    private int  platform;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSdkUserName() {
        return sdkUserName;
    }

    public void setSdkUserName(String sdkUserName) {
        this.sdkUserName = sdkUserName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getTradeImgs() {
        return tradeImgs;
    }

    public void setTradeImgs(List<String> tradeImgs) {
        this.tradeImgs = tradeImgs;
    }
}
