package com.tenone.gamebox.view.base;

import android.support.v7.app.AppCompatActivity;

import com.tenone.gamebox.view.custom.FloatingDragger;

public class BaseAppCompatActivity extends AppCompatActivity {


	@Override
	public void setContentView(int layoutResID) {
		super.setContentView( new FloatingDragger( this, layoutResID ).getView() );
	}
}
