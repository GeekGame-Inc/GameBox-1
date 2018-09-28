package com.tenone.gamebox.mode.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AboutForMeModel implements Serializable {

    private static final long serialVersionUID = -5380642247212825809L;

    private List<AboutForMeCommentModel> commentModels = new ArrayList<AboutForMeCommentModel>();

    private List<AboutForMeZanCommenModel> zanCommenModels = new ArrayList<AboutForMeZanCommenModel>();

    private List<AboutForMePublicModel> zanDynamicModels = new ArrayList<AboutForMePublicModel>();


    public List<AboutForMeCommentModel> getCommentModels() {
        return commentModels;
    }

    public void setCommentModels(List<AboutForMeCommentModel> commentModels) {
        this.commentModels .addAll( commentModels ) ;
    }

    public List<AboutForMeZanCommenModel> getZanCommenModels() {
        return zanCommenModels;
    }

    public void setZanCommenModels(List<AboutForMeZanCommenModel> zanCommenModels) {
        this.zanCommenModels.addAll( zanCommenModels );
    }


    public List<AboutForMePublicModel> getZanDynamicModels() {
        return zanDynamicModels;
    }

    public void setZanDynamicModels(List<AboutForMePublicModel> zanDynamicModels) {
        this.zanDynamicModels.addAll( zanDynamicModels ) ;
    }


    public void clearCommentModels() {
        commentModels.clear();
    }

    public void clearZanCommenModels() {
        zanCommenModels.clear();
    }

    public void clearZanDynamicModels() {
        zanDynamicModels.clear();
    }
}
