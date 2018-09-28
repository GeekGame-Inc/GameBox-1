package com.tenone.gamebox.view.custom.pickers;

import com.jzxiang.pickerview.ControllerImpl;
import com.jzxiang.pickerview.config.PickerConfig;


public class MyControllerImpl extends ControllerImpl {
    public MyControllerImpl(PickerConfig pickerConfig) {
        super( pickerConfig );
    }

    @Override
    public int getMinYear() {
        return super.getMinYear();
    }

    @Override
    public int getMaxYear() {
        return getMinYear() + 200;
    }
}
