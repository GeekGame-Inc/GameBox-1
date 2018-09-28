package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RebateModel implements Serializable {

    private static final long serialVersionUID = -2451408638823202524L;
    private String gameName;
    private String gameServer;
    private String gameRoleName;
    private String roleId;
    private String money;
    private String time;
    private int state;
    private String gameId;

    private List<RoleModel> roleModels = new ArrayList<RoleModel>();

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameServer() {
        return gameServer;
    }

    public void setGameServer(String gameServer) {
        this.gameServer = gameServer;
    }

    public String getGameRoleName() {
        return gameRoleName;
    }

    public void setGameRoleName(String gameRoleName) {
        this.gameRoleName = gameRoleName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        if (!TextUtils.isEmpty( time )) {
            try {
                long t = Long.valueOf( time );
                t *= 1000;
                time = TimeUtils.formatData( t, "yyyy-MM-dd HH:mm:ss" );
            } catch (NumberFormatException e) {
                return time;
            }
        }
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<RoleModel> getRoleModels() {
        return roleModels;
    }

    public void setRoleModels(List<RoleModel> roleModels) {
        this.roleModels = roleModels;
    }
}
