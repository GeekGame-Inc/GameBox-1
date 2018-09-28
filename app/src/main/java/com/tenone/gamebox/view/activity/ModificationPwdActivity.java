package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.ModificationPwdView;
import com.tenone.gamebox.presenter.ModificationPwdPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

public class ModificationPwdActivity extends BaseActivity implements
        ModificationPwdView {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_modification_oldPwd)
    CustomizeEditText oldPwdEdit;
    @ViewInject(R.id.id_modification_newPwd)
    CustomizeEditText newPwdEdit;
    @ViewInject(R.id.id_modification_confirmPwd)
    CustomizeEditText confirmEdit;
    @ViewInject(R.id.id_modification_modificationBt)
    Button button;
    @ViewInject(R.id.id_title_underline)
    View underline;

    ModificationPwdPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_modification_pwd );
        ViewUtils.inject( this );
        underline.setVisibility( View.GONE );
        presenter = new ModificationPwdPresenter( this, this );
        presenter.initView();
        presenter.initListener();
    }

    @Override
    public TitleBarView getTitleBarView() {
        return titleBarView;
    }

    @Override
    public CustomizeEditText getOldPwdEditText() {
        return oldPwdEdit;
    }

    @Override
    public CustomizeEditText getNewPwdEditText() {
        return newPwdEdit;
    }

    @Override
    public CustomizeEditText getConfirmPwdEditText() {
        return confirmEdit;
    }

    @Override
    public Button getButton() {
        return button;
    }
}
