package com.tenone.gamebox.mode.biz;

import android.text.TextUtils;

import com.tenone.gamebox.mode.able.MineAble;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;

public class MineBiz implements MineAble {

    @Override
    public String getHeaderUrl() {
        return SpUtil.getHeaderUrl();
    }

    @Override
    public String getNick() {
        return TextUtils.isEmpty( SpUtil.getNick() ) ? SpUtil.getAccount() : SpUtil.getNick();
    }

    @Override
    public ArrayList<String> getAlbumList() {
        ArrayList<String> defultArray = new ArrayList<String>();
        return defultArray;
    }

    @Override
    public boolean isLogin() {
        return !TextUtils.isEmpty( SpUtil.getUserId() )
                && !"0".equals( SpUtil.getUserId() );
    }
}
