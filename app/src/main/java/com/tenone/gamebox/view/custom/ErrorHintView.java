package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.IStrategy;


/**
 * @author longtao.li
 */
public class ErrorHintView extends RelativeLayout {

    RelativeLayout mContainer;
    LayoutParams layoutParams;

    private AnimationDrawable animationDrawable;

    private ErrorHandler mErrorHandler = new ErrorHandler();

    public interface OperateListener {
        void operate();
    }

    private OperateListener mOperateListener;

    public ErrorHintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ErrorHintView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.custom_error_hint_view, this);
        mContainer = findViewById(R.id.container);
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void close() {
        setVisibility(View.GONE);
    }

    class ErrorHandler {

        public ErrorHandler() {
        }

        public void operate(IStrategy strategy) {
            show();
            strategy.operate();
        }

    }

    public void loadFailure(OperateListener Listener) {
        this.mOperateListener = Listener;
        mErrorHandler.operate(new LoadFailure());
    }

    View loadFailure;
    class LoadFailure implements IStrategy {

        @Override
        public void operate() {
            if (loadFailure == null) {
                loadFailure = View.inflate(getContext(),
                        R.layout.layout_load_failure, null);
                View view = loadFailure.findViewById(R.id.load_retry);
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mOperateListener.operate();
                    }
                });
            }
            mContainer.removeAllViews();
            mContainer.addView(loadFailure, layoutParams);
        }
    }

    public void netError(OperateListener Listener) {
        this.mOperateListener = Listener;
        mErrorHandler.operate(new NetWorkError());
    }

    View netError;

    class NetWorkError implements IStrategy {

        @Override
        public void operate() {
            if (netError == null) {
                netError = View.inflate(getContext(),
                        R.layout.layout_load_wifi_failure, null);
                View view = netError.findViewById(R.id.wifi_retry);
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mOperateListener.operate();
                    }
                });
            }
            mContainer.removeAllViews();
            mContainer.addView(netError, layoutParams);
        }

    }

    public void noData() {
        mErrorHandler.operate(new NoDataError());
    }

    View noData;

    class NoDataError implements IStrategy {

        @Override
        public void operate() {
            if (noData == null) {
                noData = View.inflate(getContext(),
                        R.layout.layout_load_noorder, null);
            }
            mContainer.removeAllViews();
            mContainer.addView(noData, layoutParams);
        }

    }

    View loadingdata;

    class LoadingData implements IStrategy {

        @Override
        public void operate() {
            if (loadingdata == null) {
                loadingdata = View.inflate(getContext(),
                        R.layout.layout_load_loading, null);
            }
            ImageView iv = loadingdata
                    .findViewById(R.id.loading_iv);
            mContainer.removeAllViews();
            mContainer.addView(loadingdata, layoutParams);
            showLoading(iv);
        }

    }
    public void loadingData() {
        mErrorHandler.operate(new LoadingData());
    }

    public void showLoading(final ImageView iv) {
        animationDrawable = (AnimationDrawable) iv.getBackground();
        animationDrawable.start();
    }

    public void hideLoading() {
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        close();
    }

}
