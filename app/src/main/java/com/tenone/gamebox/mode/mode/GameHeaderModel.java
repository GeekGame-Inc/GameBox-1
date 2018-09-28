package com.tenone.gamebox.mode.mode;

import com.tenone.gamebox.view.utils.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameHeaderModel implements Serializable {
    private static final long serialVersionUID = 5869119354367566281L;
    private List<BannerModel> bannerModels = new ArrayList<BannerModel>();
    private JP_ZX_RM_Model jpModel,zxModel,rmModel;

    public List<BannerModel> getBannerModels() {
        return bannerModels;
    }

    public void setBannerModels(List<BannerModel> bannerModels) {
        this.bannerModels = bannerModels;
    }

    public JP_ZX_RM_Model getJpModel() {
        return jpModel;
    }

    public void setJpModel(JP_ZX_RM_Model jpModel) {
        this.jpModel = jpModel;
    }

    public JP_ZX_RM_Model getZxModel() {
        return zxModel;
    }

    public void setZxModel(JP_ZX_RM_Model zxModel) {
        this.zxModel = zxModel;
    }

    public JP_ZX_RM_Model getRmModel() {
        return rmModel;
    }

    public void setRmModel(JP_ZX_RM_Model rmModel) {
        this.rmModel = rmModel;
    }

    public void clear() {
        if (!BeanUtils.isEmpty( bannerModels )){
            bannerModels.clear();
        }
        if (!BeanUtils.isEmpty( jpModel )){
            jpModel.clear();
        }
        if (!BeanUtils.isEmpty( zxModel )){
            zxModel.clear();
        }
        if (!BeanUtils.isEmpty( rmModel )){
            rmModel.clear();
        }
    }
}
