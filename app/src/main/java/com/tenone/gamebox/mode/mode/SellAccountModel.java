package com.tenone.gamebox.mode.mode;

import java.io.Serializable;
import java.util.List;

public class SellAccountModel implements Serializable {
    private static final long serialVersionUID = 5340916985139883984L;
    private String id;
    private String account;
    private List<GameModel> gameModels;
    private int status;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<GameModel> getGameModels() {
        return gameModels;
    }

    public void setGameModels(List<GameModel> gameModels) {
        this.gameModels = gameModels;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
