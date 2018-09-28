package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tenone.gamebox.R;

import java.math.BigDecimal;

public class CustomerRatingBar extends LinearLayout {
    /**
     * �Ƿ�ɵ��
     */
    private boolean mClickable;
    /**
     * ��������
     */
    private int starCount;
    /**
     * ���ǵĵ���¼�
     */
    private OnRatingChangeListener onRatingChangeListener;
    /**
     * ÿ�����ǵĴ�С
     */
    private float starImageSize;
    /**
     * ÿ�����ǵļ��
     */
    private float starPadding;
    /**
     * ���ǵ���ʾ������֧��С����
     */
    private float starStep;
    /**
     * �հ׵�Ĭ������ͼƬ
     */
    private Drawable starEmptyDrawable;
    /**
     * ѡ�к���������ͼƬ
     */
    private Drawable starFillDrawable;
    /**
     * ����ǵ�ͼƬ
     */
    private Drawable starHalfDrawable;
    /**
     * ÿ�ε�����������ӵ������������ǰ��
     */
    private StepSize stepSize;

    /**
     * ���ð��ǵ�ͼƬ��Դ�ļ�
     */
    public void setStarHalfDrawable(Drawable starHalfDrawable) {
        this.starHalfDrawable = starHalfDrawable;
    }

    /**
     * �������ǵ�ͼƬ��Դ�ļ�
     */
    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    /**
     * ���ÿհ׺�Ĭ�ϵ�ͼƬ��Դ�ļ�
     */
    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    /**
     * ���������Ƿ���Ե������
     */
    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    /**
     * �������ǵ���¼�
     */
    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    /**
     * �������ǵĴ�С
     */
    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    public void setStepSize(StepSize stepSize) {
        this.stepSize = stepSize;
    }

    /**
     * ���캯��
     * ��ȡxml�����õ���Դ�ļ�
     */
    public CustomerRatingBar(Context context, AttributeSet attrs) {
        super( context, attrs );
        setOrientation( LinearLayout.HORIZONTAL );
        TypedArray mTypedArray = context.obtainStyledAttributes( attrs, R.styleable.RatingBar );
        starImageSize = mTypedArray.getDimension( R.styleable.RatingBar_starImageSize, 20 );
        starPadding = mTypedArray.getDimension( R.styleable.RatingBar_starPadding, 10 );
        starStep = mTypedArray.getFloat( R.styleable.RatingBar_starStep, 1.0f );
        stepSize = StepSize.fromStep( mTypedArray.getInt( R.styleable.RatingBar_stepSize, 1 ) );
        starCount = mTypedArray.getInteger( R.styleable.RatingBar_starCount, 5 );
        starEmptyDrawable = mTypedArray.getDrawable( R.styleable.RatingBar_starEmpty );
        starFillDrawable = mTypedArray.getDrawable( R.styleable.RatingBar_starFill );
        starHalfDrawable = mTypedArray.getDrawable( R.styleable.RatingBar_starHalf );
        mClickable = mTypedArray.getBoolean( R.styleable.RatingBar_clickable, true );
        mTypedArray.recycle();
        for (int i = 0; i < starCount; ++i) {
            final ImageView imageView = getStarImageView();
            imageView.setImageDrawable( starEmptyDrawable );
            imageView.setOnClickListener(
								v -> {
										if (mClickable) {
												//����������������
												int fint = (int) starStep;
												BigDecimal b1 = new BigDecimal( Float.toString( starStep ) );
												BigDecimal b2 = new BigDecimal( Integer.toString( fint ) );
												//��������С������
												float fPoint = b1.subtract( b2 ).floatValue();
												if (fPoint == 0) {
														fint -= 1;
												}

												if (indexOfChild( v ) > fint) {
														setStar( indexOfChild( v ) + 1 );
												} else if (indexOfChild( v ) == fint) {
														if (stepSize == StepSize.Full) {//��������� �Ͳ����ǰ������
																return;
														}
														//���֮��Ĭ��ÿ��������һ���ǣ��ٴε����Ϊ�����
														if (imageView.getDrawable().getCurrent().getConstantState().equals( starHalfDrawable.getConstantState() )) {
																setStar( indexOfChild( v ) + 1 );
														} else {
																setStar( indexOfChild( v ) + 0.5f );
														}
												} else {
														setStar( indexOfChild( v ) + 1f );
												}

										}
								}
						);
            addView( imageView );
        }
        setStar( starStep );
    }

    /**
     * ����ÿ�����ǵĲ���
     */
    private ImageView getStarImageView() {
        ImageView imageView = new ImageView( getContext() );

        LayoutParams layout = new LayoutParams(
                Math.round( starImageSize ), Math.round( starImageSize ) );//����ÿ�����������Բ��ֵĴ�С
        layout.setMargins( 0, 0, Math.round( starPadding ), 0 );//����ÿ�����������Բ��ֵļ��
        imageView.setLayoutParams( layout );
        imageView.setAdjustViewBounds( true );
        imageView.setScaleType( ImageView.ScaleType.CENTER_CROP );
        imageView.setImageDrawable( starEmptyDrawable );
        imageView.setMinimumWidth( 10 );
        imageView.setMaxHeight( 10 );
        return imageView;

    }

    /**
     * �������ǵĸ���
     */

    public void setStar(float rating) {

        if (onRatingChangeListener != null) {
            onRatingChangeListener.onRatingChange( rating );
        }
        this.starStep = rating;
        //����������������
        int fint = (int) rating;
        BigDecimal b1 = new BigDecimal( Float.toString( rating ) );
        BigDecimal b2 = new BigDecimal( Integer.toString( fint ) );
        //��������С������
        float fPoint = b1.subtract( b2 ).floatValue();

        //����ѡ�е�����
        for (int i = 0; i < fint; ++i) {
            ((ImageView) getChildAt( i )).setImageDrawable( starFillDrawable );
        }
        //����û��ѡ�е�����
        for (int i = fint; i < starCount; i++) {
            ((ImageView) getChildAt( i )).setImageDrawable( starEmptyDrawable );
        }
        //С����Ĭ�����Ӱ����
        if (fPoint > 0) {
            ((ImageView) getChildAt( fint )).setImageDrawable( starHalfDrawable );
        }
    }

    /**
     * �������ǵĵ���¼�
     */
    public interface OnRatingChangeListener {
        /**
         * ѡ�е����ǵĸ���
         */
        void onRatingChange(float ratingCount);

    }

    /**
     * ����ÿ�����ӵķ�ʽ���ǻ��ǰ��ǣ�ö������
     * ������View.GONE
     */
    public enum StepSize {
        Half( 0 ), Full( 1 );
        int step;

        StepSize(int step) {
            this.step = step;
        }

        public static StepSize fromStep(int step) {
            for (StepSize f : values()) {
                if (f.step == step) {
                    return f;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
