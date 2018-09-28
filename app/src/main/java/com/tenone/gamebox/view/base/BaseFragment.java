package com.tenone.gamebox.view.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.receiver.DownActionReceiver;
import com.tenone.gamebox.view.receiver.WarnReceiver;

/**
 * Fragment基类
 * <p>
 * Created by Eddy on 2017/2/16.
 */

@SuppressLint("NewApi")
public abstract class BaseFragment extends Fragment {

    public <T> View findView(View parent, int childId) {
        return parent.findViewById( childId );
    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView( inflater, container, savedInstanceState );
    }


    private AlertDialog progressDialog;

    protected void buildProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AlertDialog.Builder( getActivity(), R.style.loadingStyle ).show();
        }
        progressDialog.setContentView( R.layout.loading_progress );
        progressDialog.setCancelable( false );
        TextView msg = progressDialog
                .findViewById( R.id.id_tv_loadingmsg );
        msg.setText( getResources().getString( R.string.loading ) + "..." );
        progressDialog.show();
    }


    protected void buildProgressDialog(String text) {
        if (progressDialog == null) {
            progressDialog = new AlertDialog.Builder( getActivity(), R.style.loadingStyle ).show();
        }
        progressDialog.setContentView( R.layout.loading_progress );
        progressDialog.setCancelable( false );
        TextView msg = progressDialog
                .findViewById( R.id.id_tv_loadingmsg );
        msg.setText( text + "..." );
        progressDialog.show();
    }

    protected void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void showToast(String txt) {
        ToastCustom.makeText( getActivity(), txt, ToastCustom.LENGTH_SHORT ).show();
    }

    public void registerWarnReceiver(Context mContext, WarnReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction( "open_notification" );
        mContext.registerReceiver( receiver, filter );
    }

    public void registerDownloadActionReceiver(Context mContext,
                                               DownActionReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction( "download_action" );
        mContext.registerReceiver( receiver, filter );
    }

}
