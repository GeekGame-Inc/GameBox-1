package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class TaskCenterModel implements Serializable {
    private static final long serialVersionUID = 5606187260588084553L;
    private int iconId;
    private String itemName = "";
    private String intro = "";
    private int status = 0;
    private boolean isVisible = true;

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
