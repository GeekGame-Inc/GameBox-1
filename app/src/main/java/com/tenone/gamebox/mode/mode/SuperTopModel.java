package com.tenone.gamebox.mode.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SuperTopModel implements Serializable {

    private static final long serialVersionUID = 8064636239786095188L;
    private List<TopModel> models = new ArrayList<TopModel>();

    public SuperTopModel() {
    }

    public SuperTopModel(List<TopModel> models) {
        this.models = models;
    }

    public void setModels(List<TopModel> models) {
        this.models = models;
    }

    public List<TopModel> getModels() {
        return models;
    }
}
