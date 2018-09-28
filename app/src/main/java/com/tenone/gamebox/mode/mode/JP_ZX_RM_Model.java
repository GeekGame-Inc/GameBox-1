package com.tenone.gamebox.mode.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JP_ZX_RM_Model implements Serializable {
    private static final long serialVersionUID = -8251528600617178016L;
    private List<GameModel> jpModels = new ArrayList<GameModel>();
    private BannerModel ggModel;

    public List<GameModel> getJpModels() {
        return jpModels;
    }

    public void setJpModels(List<GameModel> jpModels) {
        this.jpModels = jpModels;
    }

    public BannerModel getGgModel() {
        return ggModel;
    }

    public void setGgModel(BannerModel ggModel) {
        this.ggModel = ggModel;
    }

    public void clear() {
        jpModels.clear();
        ggModel = null;
    }
}
