package com.tenone.gamebox.view.custom.pickers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.tenone.gamebox.R;

import java.util.ArrayList;

import cn.addapp.pickers.entity.Province;
import cn.addapp.pickers.listeners.OnLinkageListener;
import cn.addapp.pickers.picker.AddressPicker;
import cn.addapp.pickers.util.ConvertUtils;


public class AddressPickTask extends AsyncTask<String, Void, ArrayList<Province>> {
    private Activity activity;
    private ProgressDialog dialog;
    private Callback callback;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideProvince = false;
    private boolean hideCounty = false;

    public AddressPickTask(Activity activity) {
        this.activity = activity;
    }

    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show( activity, null, activity.getString( R.string.initing_data), true, true );
    }

    @Override
    protected ArrayList<Province> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }
        ArrayList<Province> data = new ArrayList<>();
        try {
            String json = ConvertUtils.toString( activity.getAssets().open( "city.json" ) );
            data.addAll( JSON.parseArray( json, Province.class ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<Province> result) {
        dialog.dismiss();
        if (result.size() > 0) {
            AddressPicker picker = new AddressPicker( activity, result );
            picker.setHideProvince( hideProvince );
            picker.setHideCounty( hideCounty );
            picker.setCanLoop( false );
            picker.setWheelModeEnable( true );
            if (hideCounty) {
                picker.setColumnWeight( 1 / 3.0f, 2 / 3.0f );//����Ļ��Ϊ3�ݣ�ʡ���͵ؼ��ı���Ϊ1:2
            } else {
                picker.setColumnWeight( 2 / 8.0f, 3 / 8.0f, 3 / 8.0f );//ʡ�����ؼ����ؼ��ı���Ϊ2:3:3
            }
            picker.setSelectedItem( selectedProvince, selectedCity, selectedCounty );
            picker.setOnLinkageListener( callback );
            picker.show();
        } else {
            callback.onAddressInitFailed();
        }
    }

    public interface Callback extends OnLinkageListener {

        void onAddressInitFailed();

    }

}

