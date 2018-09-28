package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.adapter.BGAPhotoPageAdapter;
import com.tenone.gamebox.view.custom.PhotoViewAttacher;
import com.tenone.gamebox.view.custom.bga.BGAHackyViewPager;
import com.tenone.gamebox.view.utils.BGAPhotoPickerUtil;

import java.util.ArrayList;

public class BGAPhotoPickerPreviewActivity extends BGAPPToolbarActivity implements PhotoViewAttacher.OnViewTapListener {
    private static final String EXTRA_PREVIEW_IMAGES = "EXTRA_PREVIEW_IMAGES";
    private static final String EXTRA_SELECTED_IMAGES = "EXTRA_SELECTED_IMAGES";
    private static final String EXTRA_MAX_CHOOSE_COUNT = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION";
    private static final String EXTRA_IS_FROM_TAKE_PHOTO = "EXTRA_IS_FROM_TAKE_PHOTO";

    private TextView mTitleTv;
    private TextView mSubmitTv;
    private BGAHackyViewPager mContentHvp;
    private RelativeLayout mChooseRl;
    private TextView mChooseTv;

    private ArrayList<String> mSelectedImages;
    private BGAPhotoPageAdapter mPhotoPageAdapter;
    private int mMaxChooseCount = 1;
    private String mTopRightBtnText;

    private boolean mIsHidden = false;
    private long mLastShowHiddenTime;
    private boolean mIsFromTakePhoto;

    public static Intent newIntent(Context context, int maxChooseCount, ArrayList<String> selectedImages, ArrayList<String> previewImages, int currentPosition, boolean isFromTakePhoto) {
        Intent intent = new Intent( context, BGAPhotoPickerPreviewActivity.class );
        intent.putStringArrayListExtra( EXTRA_SELECTED_IMAGES, selectedImages );
        intent.putStringArrayListExtra( EXTRA_PREVIEW_IMAGES, previewImages );
        intent.putExtra( EXTRA_MAX_CHOOSE_COUNT, maxChooseCount );
        intent.putExtra( EXTRA_CURRENT_POSITION, currentPosition );
        intent.putExtra( EXTRA_IS_FROM_TAKE_PHOTO, isFromTakePhoto );
        return intent;
    }

    public static ArrayList<String> getSelectedImages(Intent intent) {
        return intent.getStringArrayListExtra( EXTRA_SELECTED_IMAGES );
    }

    public static boolean getIsFromTakePhoto(Intent intent) {
        return intent.getBooleanExtra( EXTRA_IS_FROM_TAKE_PHOTO, false );
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setNoLinearContentView( R.layout.bga_pp_activity_photo_picker_preview );
        mContentHvp = getViewById( R.id.hvp_photo_picker_preview_content );
        mChooseRl = getViewById( R.id.rl_photo_picker_preview_choose );
        mChooseTv = getViewById( R.id.tv_photo_picker_preview_choose );
    }

    @Override
    protected void setListener() {
        mChooseTv.setOnClickListener( this );

        mContentHvp.addOnPageChangeListener( new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                handlePageSelectedStatus();
            }
        } );
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mMaxChooseCount = getIntent().getIntExtra( EXTRA_MAX_CHOOSE_COUNT, 1 );
        if (mMaxChooseCount < 1) {
            mMaxChooseCount = 1;
        }

        mSelectedImages = getIntent().getStringArrayListExtra( EXTRA_SELECTED_IMAGES );
        ArrayList<String> previewImages = getIntent().getStringArrayListExtra( EXTRA_PREVIEW_IMAGES );
        if (TextUtils.isEmpty( previewImages.get( 0 ) )) {
            previewImages.remove( 0 );
        }

        mIsFromTakePhoto = getIntent().getBooleanExtra( EXTRA_IS_FROM_TAKE_PHOTO, false );
        if (mIsFromTakePhoto) {
            mChooseRl.setVisibility( View.INVISIBLE );
        }
        int currentPosition = getIntent().getIntExtra( EXTRA_CURRENT_POSITION, 0 );

        mTopRightBtnText = getString( R.string.bga_pp_confirm );


        mPhotoPageAdapter = new BGAPhotoPageAdapter( this, this, previewImages );
        mContentHvp.setAdapter( mPhotoPageAdapter );
        mContentHvp.setCurrentItem( currentPosition );


        mToolbar.postDelayed( new Runnable() {
            @Override
            public void run() {
                hiddenToolbarAndChoosebar();
            }
        }, 2000 );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.bga_pp_menu_photo_picker_preview, menu );
        MenuItem menuItem = menu.findItem( R.id.item_photo_picker_preview_title );
        View actionView = menuItem.getActionView();

        mTitleTv = actionView.findViewById( R.id.tv_photo_picker_preview_title );
        mSubmitTv = actionView.findViewById( R.id.tv_photo_picker_preview_submit );
        mSubmitTv.setOnClickListener( this );

        renderTopRightBtn();
        handlePageSelectedStatus();

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_photo_picker_preview_submit) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra( EXTRA_SELECTED_IMAGES, mSelectedImages );
            intent.putExtra( EXTRA_IS_FROM_TAKE_PHOTO, mIsFromTakePhoto );
            setResult( RESULT_OK, intent );
            finish();
        } else if (v.getId() == R.id.tv_photo_picker_preview_choose) {
            String currentImage = mPhotoPageAdapter.getItem( mContentHvp.getCurrentItem() );
            if (mSelectedImages.contains( currentImage )) {
                mSelectedImages.remove( currentImage );
                mChooseTv.setCompoundDrawablesWithIntrinsicBounds( R.drawable.bga_pp_ic_cb_normal, 0, 0, 0 );
                renderTopRightBtn();
            } else {
                if (mMaxChooseCount == 1) {
                    mSelectedImages.clear();
                    mSelectedImages.add( currentImage );
                    mChooseTv.setCompoundDrawablesWithIntrinsicBounds( R.drawable.bga_pp_ic_cb_checked, 0, 0, 0 );
                    renderTopRightBtn();
                } else {
                    if (mMaxChooseCount == mSelectedImages.size()) {
                        BGAPhotoPickerUtil.show( this, getString( R.string.bga_pp_toast_photo_picker_max, mMaxChooseCount + "" ) );
                    } else {
                        mSelectedImages.add( currentImage );
                        mChooseTv.setCompoundDrawablesWithIntrinsicBounds( R.drawable.bga_pp_ic_cb_checked, 0, 0, 0 );
                        renderTopRightBtn();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra( EXTRA_SELECTED_IMAGES, mSelectedImages );
        intent.putExtra( EXTRA_IS_FROM_TAKE_PHOTO, mIsFromTakePhoto );
        setResult( RESULT_CANCELED, intent );
        finish();
    }

    private void handlePageSelectedStatus() {
        if (mTitleTv == null || mPhotoPageAdapter == null) {
            return;
        }

        mTitleTv.setText( (mContentHvp.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount() );
        if (mSelectedImages.contains( mPhotoPageAdapter.getItem( mContentHvp.getCurrentItem() ) )) {
            mChooseTv.setCompoundDrawablesWithIntrinsicBounds( R.drawable.bga_pp_ic_cb_checked, 0, 0, 0 );
        } else {
            mChooseTv.setCompoundDrawablesWithIntrinsicBounds( R.drawable.bga_pp_ic_cb_normal, 0, 0, 0 );
        }
    }

    private void renderTopRightBtn() {
        if (mIsFromTakePhoto) {
            mSubmitTv.setEnabled( true );
            mSubmitTv.setText( mTopRightBtnText );
        } else if (mSelectedImages.size() == 0) {
            mSubmitTv.setEnabled( false );
            mSubmitTv.setText( mTopRightBtnText );
        } else {
            mSubmitTv.setEnabled( true );
            mSubmitTv.setText( mTopRightBtnText + "(" + mSelectedImages.size() + "/" + mMaxChooseCount + ")" );
        }
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (System.currentTimeMillis() - mLastShowHiddenTime > 500) {
            mLastShowHiddenTime = System.currentTimeMillis();
            if (mIsHidden) {
                showTitlebarAndChoosebar();
            } else {
                hiddenToolbarAndChoosebar();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mTitleTv = null;
        mSubmitTv = null;
        mContentHvp = null;
        mChooseRl = null;
        mChooseTv = null;

        mSelectedImages = null;
        mPhotoPageAdapter = null;

        super.onDestroy();
    }

    private void showTitlebarAndChoosebar() {
        if (mToolbar != null) {
            ViewCompat.animate( mToolbar ).translationY( 0 ).setInterpolator( new DecelerateInterpolator( 2 ) ).setListener( new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = false;
                }
            } ).start();
        }

        if (!mIsFromTakePhoto && mChooseRl != null) {
            mChooseRl.setVisibility( View.VISIBLE );
            ViewCompat.setAlpha( mChooseRl, 0 );
            ViewCompat.animate( mChooseRl ).alpha( 1 ).setInterpolator( new DecelerateInterpolator( 2 ) ).start();
        }
    }

    private void hiddenToolbarAndChoosebar() {
        if (mToolbar != null) {
            ViewCompat.animate( mToolbar ).translationY( -mToolbar.getHeight() ).setInterpolator( new DecelerateInterpolator( 2 ) ).setListener( new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = true;
                    if (mChooseRl != null) {
                        mChooseRl.setVisibility( View.INVISIBLE );
                    }
                }
            } ).start();
        }

        if (!mIsFromTakePhoto) {
            if (mChooseRl != null) {
                ViewCompat.animate( mChooseRl ).alpha( 0 ).setInterpolator( new DecelerateInterpolator( 2 ) ).start();
            }
        }
    }

}