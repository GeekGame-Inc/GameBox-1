package com.tenone.gamebox.view.base;


public class Global {
    private static Global instance = null;

    public static synchronized Global getInstance() {
        if (null == instance) {
            instance = new Global();
        }
        return instance;
    }
}
