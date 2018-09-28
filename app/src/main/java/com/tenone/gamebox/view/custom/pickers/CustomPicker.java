package com.tenone.gamebox.view.custom.pickers;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnWheeledListener;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.SinglePicker;

/**
 * �Զ��嶥�����ײ�
 *
 * @author matt
 *         blog: addapp.cn
 */
public class CustomPicker extends SinglePicker<String> implements OnSingleWheelListener {
    private TextView titleView;
    private String titleText;
    private LineConfig lineConfig;
    private OnWheeledListener onWheeledListener;
    private String seleteItem;

    public CustomPicker(Activity activity, String[] array, String titleText) {
        super( activity, array );
        this.titleText = titleText;
        lineConfig = new LineConfig( 0.06f );
        lineConfig.setColor( activity.getResources().getColor( R.color.blue_40 ) );
        setSelectedTextColor( activity.getResources().getColor( R.color.blue_40 ) );
        setTextSize( 14 );
        setItemWidth( 80 );
        setLineConfig( lineConfig );
        setOnSingleWheelListener( this );
    }

    @Override
    protected void showAfter() {
     /*   View rootView = getRootView();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat( rootView, "alpha", 0, 1 );
        ObjectAnimator translation = ObjectAnimator.ofFloat( rootView, "translationY", 300, 0 );
        animatorSet.playTogether( alpha, translation );
        animatorSet.setDuration( 200 );
        animatorSet.setInterpolator( new AccelerateInterpolator() );
        animatorSet.start();*/
    }

    @Override
    public void dismiss() {
       /* View rootView = getRootView();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat( rootView, "alpha", 1, 0 );
        ObjectAnimator translation = ObjectAnimator.ofFloat( rootView, "translationX", 0, rootView.getWidth() );
        ObjectAnimator rotation = ObjectAnimator.ofFloat( rootView, "rotation", 0, 120 );
        animatorSet.playTogether( alpha, translation, rotation );
        animatorSet.setDuration( 200 );
        animatorSet.setInterpolator( new AccelerateInterpolator() );
        animatorSet.addListener( new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismissImmediately();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        } );
        animatorSet.start();*/
        dismissImmediately();
    }

    @Nullable
    @Override
    protected View makeHeaderView() {
        View view = LayoutInflater.from( activity ).inflate( R.layout.picker_header, null );
        titleView = view.findViewById( R.id.picker_title );
        titleView.setText( titleText );
        view.findViewById( R.id.picker_close ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        } );
        return view;
    }


    @Nullable
    @Override
    protected View makeFooterView() {
        View view = LayoutInflater.from( activity ).inflate( R.layout.picker_footer, null );
        Button submitView = view.findViewById( R.id.picker_submit );
        submitView.setText( submitText );
        submitView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSubmit();
            }
        } );
        Button cancelView = view.findViewById( R.id.picker_cancel );
        cancelView.setText( cancelText );
        cancelView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onCancel();
            }
        } );
        return view;
    }


    @Override
    public void onWheeled(int index, String item) {
        seleteItem = item;
    }


    @Override
    public void onSubmit() {
        if (onWheeledListener != null) {
            onWheeledListener.onWheeled( seleteItem );
        }
        super.onSubmit();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
    }

    public void setOnWheeledListener(OnWheeledListener onWheeledListener) {
        this.onWheeledListener = onWheeledListener;
    }
}