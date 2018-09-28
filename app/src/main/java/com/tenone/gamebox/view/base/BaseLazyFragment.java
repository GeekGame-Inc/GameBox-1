package com.tenone.gamebox.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

public abstract class BaseLazyFragment extends BaseFragment {
    /**
     * �����ع�
     */
    private boolean isLazyLoaded;

    private boolean isPrepared;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        isPrepared = true;
        lazyLoad( getUserVisibleHint() );
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        lazyLoad( isVisibleToUser );
        super.setUserVisibleHint( isVisibleToUser );
    }

    private void lazyLoad(boolean isVisibleToUser) {
        if (isVisibleToUser && isPrepared && !isLazyLoaded) {
            onLazyLoad();
            isLazyLoaded = true;
        }
    }

    @UiThread
    public abstract void onLazyLoad();

}
