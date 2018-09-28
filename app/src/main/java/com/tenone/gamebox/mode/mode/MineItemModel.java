package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class MineItemModel implements Serializable {
    private static final long serialVersionUID = -6328183858023067708L;
    private int drawableId;
    private String itemName;
    private String text;
    private boolean isShowLine;
    private boolean isHtml;

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isShowLine() {
        return isShowLine;
    }

    public void setShowLine(boolean showLine) {
        isShowLine = showLine;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }
}
