package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.lang.reflect.Field;


public class CoordinatorTabLayout extends CoordinatorLayout {

    private Context mContext;
    private Toolbar mToolbar;
    private ActionBar mActionbar;
    private TabLayout mTabLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private RelativeLayout mGruopView;
    private ImageView imageView, rightImageView;
    private View rightView;

    public CoordinatorTabLayout(Context context) {
        this( context, null );
    }

    public CoordinatorTabLayout(Context context, AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public CoordinatorTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        mContext = context;
        if (!isInEditMode()) {
            initView( context );
            initWidget( context, attrs );
        }
    }

    private void initView(Context context) {
        LayoutInflater.from( context ).inflate( R.layout.view_coordinatortablayout, this, true );
        initToolbar();
        mCollapsingToolbarLayout = findViewById( R.id.collapsingtoolbarlayout );
        mTabLayout = findViewById( R.id.tabLayout );
    }

    private void initWidget(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes( attrs
                , R.styleable.CoordinatorTabLayout );
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute( R.color.titleBarColor, typedValue, true );
        int contentScrimColor = typedArray.getColor(
                R.styleable.CoordinatorTabLayout_contentScrim, typedValue.data );
        mCollapsingToolbarLayout.setContentScrimColor( contentScrimColor );
        int tabIndicatorColor = typedArray.getColor( R.styleable.CoordinatorTabLayout_tabIndicatorColor, context.getResources().getColor( R.color.rebateColor ) );
        mTabLayout.setSelectedTabIndicatorColor( tabIndicatorColor );
        typedArray.recycle();
    }

    private void initToolbar() {
        mToolbar = findViewById( R.id.toolbar );
        mGruopView = findViewById( R.id.id_group_view );
        imageView = findViewById( R.id.id_imageView );
        rightImageView = findViewById( R.id.id_right_img );
        rightView = findViewById( R.id.id_right_view );
        imageView.setOnClickListener( v -> ((AppCompatActivity) mContext).finish() );
        ((AppCompatActivity) mContext).setSupportActionBar( mToolbar );
        mActionbar = ((AppCompatActivity) mContext).getSupportActionBar();
        mActionbar.setDisplayShowTitleEnabled( false );
    }

    /**
     * 设置Toolbar标题
     *
     * @param title 标题
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setTitle(String title) {
        if (mActionbar != null) {
            mActionbar.setTitle( title );
        }
        return this;
    }

    public CoordinatorTabLayout setBackEnable(Boolean canBack) {
        if (canBack && mActionbar != null) {
            // mActionbar.setDisplayHomeAsUpEnabled( true );
            //  mActionbar.setHomeAsUpIndicator( R.drawable.ic_arrow_white_24dp );
        }
        return this;
    }


    /**
     * 设置与该组件搭配的ViewPager
     *
     * @param viewPager 与TabLayout结合的ViewPager
     * @return CoordinatorTabLayout
     */
    public CoordinatorTabLayout setupWithViewPager(ViewPager viewPager) {
        mTabLayout.setupWithViewPager( viewPager );
        return this;
    }

    /**
     * 获取该组件中的ActionBar
     */
    public ActionBar getActionBar() {
        return mActionbar;
    }


    public Toolbar getmToolbar() {
        return mToolbar;
    }

    /**
     * 获取该组件中的TabLayout
     */
    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    public RelativeLayout getGruopView() {
        return mGruopView;
    }

    public ImageView getRightImageView() {
        return rightImageView;
    }

    public View getRightView() {
        return rightView;
    }




    public void setIndicatorWidth(TabLayout tabLayout) {
        tabLayout.post( () -> {
           try {
                LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt( 0 );
                mTabStrip.setBackgroundColor( getResources().getColor( R.color.white ) );
                int dp10 = DisplayMetricsUtils.dipTopx( tabLayout.getContext(), 10 );
                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt( i );
                    Field mTextViewField = tabView.getClass().getDeclaredField( "mTextView" );
                    mTextViewField.setAccessible( true );
                    TextView mTextView = (TextView) mTextViewField.get( tabView );
                    tabView.setPadding( 0, 0, 0, 0 );
                    int width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure( 0, 0 );
                        width = mTextView.getMeasuredWidth();
                    }
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width;
                    params.leftMargin = dp10;
                    params.rightMargin = dp10;
                    tabView.setLayoutParams( params );
                    tabView.invalidate();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } );
    }
}