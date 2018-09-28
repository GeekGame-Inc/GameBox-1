

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class BannerModel implements Serializable {
    private static final long serialVersionUID = -592752519309287727L;

    private int gameId;
    private int type;
    private String url;
    private String imageUrl;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
