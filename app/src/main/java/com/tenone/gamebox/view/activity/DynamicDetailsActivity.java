package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDynamicCommentItemClickListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.DynamicModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.adapter.DynamicDetailsCommentAdapter;
import com.tenone.gamebox.view.adapter.ImageGridAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.AttentionTextView;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.MyListView;
import com.tenone.gamebox.view.custom.MyRefreshListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


public class DynamicDetailsActivity extends BaseActivity implements HttpResultListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, PlatformActionListener, OnDynamicCommentItemClickListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_dynamic_details_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_dynamic_details_listView)
    MyRefreshListView listView;
    @ViewInject(R.id.id_dynamic_details_praise)
    TextView praiseTv;
    @ViewInject(R.id.id_dynamic_details_stepOn)
    TextView stepOnTv;
    @ViewInject(R.id.id_dynamic_details_share)
    TextView shareTv;
    @ViewInject(R.id.id_dynamic_details_comment)
    TextView commentTv;
    @ViewInject(R.id.id_dynamic_details_editLayout)
    RelativeLayout editLayout;
    @ViewInject(R.id.id_dynamic_details_edit)
    EditText editText;
    private static final int PRAISE = 58;
    private static final int CANCLEPRAISE = 60;
    private static final int STEPON = 59;
    private static final int CANCLESTEPON = 61;

    private DynamicDetailsCommentAdapter adapter, headerHotAdapter;
    private ImageGridAdapter headerImgAdapter;

    private List<DynamicCommentModel> models = new ArrayList<DynamicCommentModel>();
    private List<DynamicCommentModel> hotComments = new ArrayList<DynamicCommentModel>();
    private List<String> imgs = new ArrayList<String>();

    private String dynamicId = "1";
    private String toUid;
    private int page = 1, type = 2;
    private View headerView;
    private CircleImageView headerIv;
    private AttentionTextView attentionIv;
    private ImageView sexIv, vipIv;
    private TextView nameTv, timeTv, contentTv, allTv;
    private MyGridView gridView;
    private MyListView myListView;
    private LinearLayout hotLayout;
    private int[] rId = {R.drawable.selector_good, R.drawable.selector_disgood,
            R.drawable.icon_dynamic_share, R.drawable.icon_dynamic_comment};
    private boolean isPraise, isStepOn, isFollow = false, isUs;
    private SharePopupWindow sharePopupWindow;
    private DynamicModel dynamicModel;
    private int dp40, anInt, scrWidth;
    private Context mContent;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_dynamic_details );
        context = this;
        ViewUtils.inject( this );
        initTitle();
        if (!getIntent().hasExtra( "dynamicId" )) {
            return;
        }
        dynamicId = getIntent().getExtras().getString( "dynamicId" );
        initHeaderView();
        initView();
    }


    private void initTitle() {
        titleBarView.setTitleText( "\u8be6\u60c5" );
        titleBarView.setLeftImg( R.drawable.icon_back_grey );
        dp40 = DisplayMetricsUtils.dipTopx( this, 40 );
        scrWidth = DisplayMetricsUtils.getScreenWidth( this );
        anInt = scrWidth - dp40;
    }

    private void initHeaderView() {
        headerView = LayoutInflater.from( this ).inflate( R.layout.layout_dynamic_details_header, listView, false );
        headerIv = headerView.findViewById( R.id.id_dynamic_details_header_header );
        attentionIv = headerView.findViewById( R.id.id_dynamic_details_header_attention );
        sexIv = headerView.findViewById( R.id.id_dynamic_details_header_sexImg );
        vipIv = headerView.findViewById( R.id.id_dynamic_details_header_vipImg );
        nameTv = headerView.findViewById( R.id.id_dynamic_details_header_name );
        timeTv = headerView.findViewById( R.id.id_dynamic_details_header_time );
        contentTv = headerView.findViewById( R.id.id_dynamic_details_header_content );
        allTv = headerView.findViewById( R.id.id_dynamic_details_header_all );
        gridView = headerView.findViewById( R.id.id_dynamic_details_header_grid );
        myListView = headerView.findViewById( R.id.id_dynamic_details_header_listview );
        hotLayout = headerView.findViewById( R.id.id_dynamic_details_header_hotLayout );
        headerHotAdapter = new DynamicDetailsCommentAdapter( this, hotComments );
        myListView.setAdapter( headerHotAdapter );
        headerImgAdapter = new ImageGridAdapter( this, imgs );
        gridView.setAdapter( headerImgAdapter );
        attentionIv.setOnClickListener( v -> {
            if (!BeanUtils.isLogin()) {
                startActivity( new Intent( DynamicDetailsActivity.this, LoginActivity.class ) );
                return;
            }
            if (dynamicModel != null) {
                DriverModel m = dynamicModel.getModel();
                if (m != null) {
                    String mId = m.getDriverId();
                    HttpManager.followOrCancel( 20036, mContent,
                            new HttpResultListener() {
                                @Override
                                public void onSuccess(int what, ResultItem resultItem) {
                                    if (1 == resultItem.getIntValue( "status" )) {
                                        ToastCustom.makeText( mContent,
																						"\u64cd\u4f5c\u6210\u529f", ToastCustom.LENGTH_SHORT ).show();
                                        isFollow = !isFollow;
                                        attentionIv.setSelected( isFollow );
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        ToastCustom.makeText( mContent,
																						"\u64cd\u4f5c\u5931\u8d25", ToastCustom.LENGTH_SHORT ).show();
                                    }
                                }

                                @Override
                                public void onError(int what, String error) {
                                    ToastCustom.makeText( mContent, error, ToastCustom.LENGTH_SHORT ).show();
                                }
                            }, mId, isFollow ? 2 : 1 );
                }
            }
        } );
        headerIv.setOnClickListener( v -> {
            if (BeanUtils.isLogin()) {
                startActivity( new Intent( mContent, UserInfoActivity.class )
                        .putExtra( "uid", dynamicModel.getModel().getDriverId() ) );
            } else {
                startActivity( new Intent( mContent, LoginActivity.class ) );
            }
        } );

        gridView.setOnItemClickListener( (parent, view, position, id) -> startActivity(
                new Intent( mContent, BrowseImageActivity.class )
                        .putStringArrayListExtra( "urls", dynamicModel.getDynamicImg() ).putExtra(
                        "url", dynamicModel.getDynamicImg().get( position ) ) ) );
    }

    private void initView() {
        mContent = this;
        adapter = new DynamicDetailsCommentAdapter( this, models );
        listView.addHeaderView( headerView );
        listView.setAdapter( adapter );
        adapter.setOnDynamicCommentItemClickListener( this );
        buildProgressDialog();
        HttpManager.commentList( HttpType.REFRESH, this, this, dynamicId, type, page );
        refreshLayout.setOnRefreshListener( this );
        refreshLayout.setOnLoadListener( this );
        listView.setOnTouchListener( (v, event) -> {
            if (editLayout.getVisibility() == View.VISIBLE) {
                editLayout.setVisibility( View.GONE );
                editText.setText( "" );
                toUid = "";
                BeanUtils.packUpKeyboard( mContent, editText );
            }
            return false;
        } );
    }


    @OnClick({R.id.id_titleBar_leftImg, R.id.id_dynamic_details_praiseLayout,
            R.id.id_dynamic_details_stepOnLayout, R.id.id_dynamic_details_shareLayout,
            R.id.id_dynamic_details_commentLayout, R.id.id_dynamic_details_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_dynamic_details_send:
                if (BeanUtils.isLogin()) {
                    String content = editText.getText().toString();
                    if (TextUtils.isEmpty( content )) {
                        ToastCustom.makeText( this, "\u8bf4\u70b9\u4ec0\u4e48\u5427...", ToastCustom.LENGTH_SHORT ).show();
                        return;
                    }
                    if (TextUtils.isEmpty( toUid ) || "0".equals( toUid )) {
                        toUid = "0";
                    }
                    HttpManager.doComment( 2006, this, new HttpResultListener() {
                        @Override
                        public void onSuccess(int what, ResultItem resultItem) {
                            if (1 == resultItem.getIntValue( "status" )) {
                                int d = resultItem.getIntValue( "data" );
                                String text = "\u8bc4\u8bba\u6210\u529f";
                                if (d > 0) {
                                    text += (",\u5e76\u83b7\u5f97" + d + "\u91d1\u5e01");
                                }
                                ToastCustom.makeText( mContent,
                                        text, ToastCustom.LENGTH_SHORT ).show();
                                page = 1;
                                refreshLayout.setRefreshing( true );
                                HttpManager.commentList( HttpType.REFRESH,
                                        mContent, DynamicDetailsActivity.this, dynamicId, type, page );
                                editText.setText( "" );
                                editLayout.setVisibility( View.GONE );
                                BeanUtils.packUpKeyboard( mContent, editText );
                            } else {
                                ToastCustom.makeText( mContent,
                                        resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
                            }
                        }

                        @Override
                        public void onError(int what, String error) {
                            ToastCustom.makeText( mContent, error, ToastCustom.LENGTH_SHORT ).show();
                        }
                    }, toUid, dynamicId, content );
                } else {
                    startActivity( new Intent( this, LoginActivity.class ) );
                }
                break;
            case R.id.id_titleBar_leftImg:
                finish();
                break;
            case R.id.id_dynamic_details_praiseLayout:
                if (BeanUtils.isLogin()) {
                    if (dynamicModel.isDisGood()) {
                        HttpManager.cancleDynamicsLike( CANCLESTEPON, context, new HttpResultListener() {
                            @Override
                            public void onSuccess(int what, ResultItem resultItem) {
                                if (1 == resultItem.getIntValue( "status" )) {
                                    ResultItem item = resultItem.getItem( "data" );
                                    if (item != null && 2 == item.getIntValue( "operate" )) {
                                        dynamicModel.setDisGood( false );
                                        String txt = dynamicModel.getDislikes();
                                        try {
                                            int num = Integer.valueOf( txt ).intValue();
                                            num--;
                                            dynamicModel.setDislikes( num + "" );
                                            stepOnTv.setSelected( false );
                                            stepOnTv.setText( num + "" );
                                        } catch (NumberFormatException e) {
                                        }
                                        HttpManager.dynamicsLike( PRAISE, context,
                                                new HttpResultListener() {
                                                    @Override
                                                    public void onSuccess(int what, ResultItem resultItem) {
                                                        if (1 == resultItem.getIntValue( "status" )) {
                                                            ResultItem item = resultItem.getItem( "data" );
                                                            if (item != null && 1 == item.getIntValue( "operate" )) {
                                                                String txt = "";
                                                                int num = 0;
                                                                dynamicModel.setGood( true );
                                                                txt = dynamicModel.getLikes();
                                                                try {
                                                                    num = Integer.valueOf( txt );
                                                                } catch (NumberFormatException e) {
                                                                }
                                                                num++;
                                                                dynamicModel.setLikes( num + "" );
                                                                praiseTv.setSelected( true );
                                                                praiseTv.setText( num + "" );
                                                                int bonus = item.getIntValue( "bonus" );
                                                                if (bonus > 0)
                                                                    ToastCustom.makeText( context,
                                                                            "\u6bcf\u65e5\u9996\u6b21\u70b9\u8d5e\u6210\u529f\uff0c\u5956\u52b1" +
																																								"" + bonus + "\u91d1\u5e01",
                                                                            ToastCustom.LENGTH_SHORT ).show();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(int what, String error) {
                                                    }
                                                }, dynamicModel.getDynamicModelId(), 1 );
                                    }
                                }
                            }

                            @Override
                            public void onError(int what, String error) {
                            }
                        }, dynamicModel.getDynamicModelId(), 0 );
                    } else if (dynamicModel.isGood()) {
                        HttpManager.cancleDynamicsLike( CANCLEPRAISE, context,
                                new HttpResultListener() {
                                    @Override
                                    public void onSuccess(int what, ResultItem resultItem) {
                                        if (1 == resultItem.getIntValue( "status" )) {
                                            ResultItem item = resultItem.getItem( "data" );
                                            if (item != null && 2 == item.getIntValue( "operate" )) {
                                                dynamicModel.setGood( false );
                                                String txt = dynamicModel.getLikes();
                                                try {
                                                    int num = Integer.valueOf( txt ).intValue();
                                                    num--;
                                                    dynamicModel.setLikes( num + "" );
                                                    praiseTv.setText( num + "" );
                                                    praiseTv.setSelected( false );
                                                } catch (NumberFormatException e) {
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(int what, String error) {
                                    }
                                }, dynamicModel.getDynamicModelId(), 1 );
                    } else {
                        HttpManager.dynamicsLike( 3669, this, new HttpResultListener() {
                            @Override
                            public void onSuccess(int what, ResultItem resultItem) {
                                if (1 == resultItem.getIntValue( "status" )) {
                                    ResultItem item = resultItem.getItem( "data" );
                                    if (item != null && 1 == item.getIntValue( "operate" )) {
                                        int bonus = item.getIntValue( "bonus" );
                                        if (bonus > 0)
                                            ToastCustom.makeText( context,
                                                    "\u6bcf\u65e5\u9996\u6b21\u70b9\u8d5e\u6210\u529f\uff0c\u5956\u52b1"
																												+ bonus + "\u91d1\u5e01",
                                                    ToastCustom.LENGTH_SHORT ).show();
                                        dynamicModel.setGood( true );
                                        String num = dynamicModel.getLikes();
                                        if (!TextUtils.isEmpty( num )) {
                                            try {
                                                int a = Integer.valueOf( num ).intValue();
                                                a++;
                                                dynamicModel.setLikes( a + "" );
                                                praiseTv.setText( a + "" );
                                                praiseTv.setSelected( true );
                                            } catch (NumberFormatException e) {
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onError(int what, String error) {
                                cancelProgressDialog();
                                ToastCustom.makeText( mContent, error,
                                        ToastCustom.LENGTH_SHORT ).show();
                            }
                        }, dynamicModel.getDynamicModelId(), 1 );
                    }
                } else {
                    startActivity( new Intent( mContent, LoginActivity.class ) );
                }
                break;
            case R.id.id_dynamic_details_stepOnLayout:
                if (BeanUtils.isLogin()) {
                    if (dynamicModel.isGood()) {
                        HttpManager.cancleDynamicsLike( 5585, context, new HttpResultListener() {
                                    @Override
                                    public void onSuccess(int what, ResultItem resultItem) {
                                        if (1 == resultItem.getIntValue( "status" )) {
                                            ResultItem item = resultItem.getItem( "data" );
                                            if (item != null && 2 == item.getIntValue( "operate" )) {
                                                dynamicModel.setGood( false );
                                                String txt = dynamicModel.getLikes();
                                                try {
                                                    int num = Integer.valueOf( txt ).intValue();
                                                    num--;
                                                    dynamicModel.setLikes( num + "" );
                                                    praiseTv.setSelected( false );
                                                    praiseTv.setText( num + "" );
                                                } catch (NumberFormatException e) {
                                                }
                                                HttpManager.dynamicsLike( STEPON, context,
                                                        new HttpResultListener() {
                                                            @Override
                                                            public void onSuccess(int what, ResultItem resultItem) {
                                                                if (1 == resultItem.getIntValue( "status" )) {
                                                                    ResultItem item = resultItem.getItem( "data" );
                                                                    if (item != null && 0 == item.getIntValue( "operate" )) {
                                                                        dynamicModel.setDisGood( true );
                                                                        String txt = dynamicModel.getDislikes();
                                                                        try {
                                                                            int num = Integer.valueOf( txt ).intValue();
                                                                            num++;
                                                                            dynamicModel.setDislikes( num + "" );
                                                                            stepOnTv.setText( num + "" );
                                                                            stepOnTv.setSelected( true );
                                                                        } catch (NumberFormatException e) {
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onError(int what, String error) {
                                                            }
                                                        }, dynamicModel.getDynamicModelId(), 0 );
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(int what, String error) {
                                    }
                                },
                                dynamicModel.getDynamicModelId(), 1 );
                    } else if (dynamicModel.isDisGood()) {
                        HttpManager.cancleDynamicsLike( CANCLESTEPON, context,
                                new HttpResultListener() {
                                    @Override
                                    public void onSuccess(int what, ResultItem resultItem) {
                                        if (1 == resultItem.getIntValue( "status" )) {
                                            ResultItem item = resultItem.getItem( "data" );
                                            if (item != null && 2 == item.getIntValue( "operate" )) {
                                                dynamicModel.setDisGood( false );
                                                String txt = dynamicModel.getDislikes();
                                                try {
                                                    int num = Integer.valueOf( txt ).intValue();
                                                    num--;
                                                    dynamicModel.setDislikes( num + "" );
                                                    stepOnTv.setText( num + "" );
                                                    stepOnTv.setSelected( false );
                                                } catch (NumberFormatException e) {
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(int what, String error) {
                                    }
                                }, dynamicModel.getDynamicModelId(), 0 );

                    } else {
                        HttpManager.dynamicsLike( 3669, this, new HttpResultListener() {
                                    @Override
                                    public void onSuccess(int what, ResultItem resultItem) {
                                        if (1 == resultItem.getIntValue( "status" )) {
                                            ResultItem item = resultItem.getItem( "data" );
                                            if (item != null && 0 == item.getIntValue( "operate" )) {
                                                dynamicModel.setDisGood( true );
                                                String num = dynamicModel.getDislikes();
                                                if (!TextUtils.isEmpty( num )) {
                                                    try {
                                                        int a = Integer.valueOf( num ).intValue();
                                                        a++;
                                                        dynamicModel.setDislikes( a + "" );
                                                        stepOnTv.setText( a + "" );
                                                        stepOnTv.setSelected( true );
                                                    } catch (NumberFormatException e) {
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(int what, String error) {
                                    }
                                },
                                dynamicModel.getDynamicModelId(), 0 );
                    }
                } else {
                    startActivity( new Intent( mContent, LoginActivity.class ) );
                }
                break;
            case R.id.id_dynamic_details_shareLayout:
                if (dynamicModel != null) {
                    if (sharePopupWindow == null) {
                        sharePopupWindow = new SharePopupWindow( mContent, dynamicModel );
                        sharePopupWindow.setPlatformActionListener( DynamicDetailsActivity.this );
                    }
                    sharePopupWindow.showAtLocation( titleBarView, Gravity.BOTTOM, 0, 0 );
                }
                break;
            case R.id.id_dynamic_details_commentLayout:
                if (BeanUtils.isLogin()) {
                    editLayout.setVisibility( View.VISIBLE );
                    editText.requestFocus();
                    toUid = "0";
                } else {
                    startActivity( new Intent( mContent, LoginActivity.class ) );
                }
                break;
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog();
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
        if (1 == resultItem.getIntValue( "status" )) {
            switch (what) {
                case HttpType.REFRESH:
                    models.clear();
                    imgs.clear();
                    hotComments.clear();
                    break;
            }
            ResultItem item = resultItem.getItem( "data" );
            if (null != item) {
                if (what == HttpType.REFRESH) {
                    setHeaderData( item );
                }
                setData( item );
            }
        } else {
            ToastCustom.makeText( this, resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
        }
    }

    private void setHeaderData(ResultItem resultItem) {
        ResultItem dynamic = resultItem.getItem( "dynamics_info" );
        if (dynamic != null) {
            dynamicModel = new DynamicModel();
            dynamicModel.setComments( dynamic.getString( "comment" ) );
            dynamicModel.setContent( dynamic.getString( "content" ) );
            dynamicModel.setLikes( dynamic.getString( "likes" ) );
            dynamicModel.setDislikes( dynamic.getString( "dislike" ) );
            dynamicModel.setTime( dynamic.getString( "create_time" ) );
            dynamicModel.setDynamicModelId( dynamic.getString( "id" ) );
            dynamicModel.setShares( dynamic.getString( "share" ) );
            dynamicModel.setGood( "1".equals( dynamic.getString( "operate" ) ) );
            dynamicModel.setDisGood( "0".equals( dynamic.getString( "operate" ) ) );
            List<ResultItem> imgs = dynamic.getItems( "imgs" );
            if (null != imgs && imgs.size() > 0) {
                ArrayList<String> imgsArray = new ArrayList<String>();
                int size = imgs.size();
                for (int i = 0; i < size; i++) {
                    String img = String.valueOf( imgs.get( i ) );
                    imgsArray.add( String.valueOf( img ) );
                }
                dynamicModel.setDynamicImg( imgsArray );
                int imgHeight = 0;
                gridView.setVisibility( View.VISIBLE );
                switch (size) {
                    case 1:
                        gridView.setNumColumns( 1 );
                        imgHeight = anInt / 2;
                        break;
                    case 2:
                        gridView.setNumColumns( 2 );
                        imgHeight = anInt / 2;
                        break;
                    default:
                        gridView.setNumColumns( 3 );
                        imgHeight = anInt / 3;
                        break;
                }
                headerImgAdapter.setImgHeight( imgHeight );
            } else {
                gridView.setVisibility( View.GONE );
            }
            DriverModel driverModel = new DriverModel();
            driverModel.setNick( dynamic.getString( "nick_name" ) );
            driverModel.setSex( dynamic.getString( "sex" ) );
            driverModel.setVip( dynamic.getBooleanValue( "vip", 1 ) );
            driverModel.setHeader( dynamic.getString( "icon_url" ) );
            driverModel.setDriverId( dynamic.getString( "uid" ) );
            dynamicModel.setModel( driverModel );
            isFollow = dynamic.getBooleanValue( "is_follow", 1 );
            attentionIv.setSelected( isFollow );
        }

        if (dynamicModel != null) {
            String uid = SpUtil.getUserId();
            String id = dynamic.getString( "uid" );
            if (!TextUtils.isEmpty( uid ) && !"0".equals( uid ) && uid.equals( id )) {
                isUs = true;
                attentionIv.setVisibility( View.GONE );
            } else {
                isUs = false;
                attentionIv.setSelected( dynamic.getBooleanValue( "is_follow", 1 ) );
            }
            DriverModel driverModel = dynamicModel.getModel();
            if (driverModel != null) {
                ImageLoadUtils.loadNormalImg( headerIv, this, driverModel.getHeader() );
                nameTv.setText( driverModel.getNick() );
                vipIv.setVisibility( driverModel.isVip() ? View.VISIBLE : View.GONE );
            }
            sexIv.setSelected( getString( R.string.women ).equals( driverModel.getSex() ) );
            vipIv.setVisibility( driverModel.isVip() ? View.VISIBLE : View.GONE );
            timeTv.setText( dynamicModel.getTime() );
            contentTv.setText( dynamicModel.getContent() );
            imgs.addAll( dynamicModel.getDynamicImg() );
            headerImgAdapter.notifyDataSetChanged();
            allTv.setText( "\u5168\u90e8\u8bc4\u8bba(" + dynamicModel.getComments() + ")" );
            Drawable drawable = this.getResources().getDrawable( rId[0] );
            drawable.setBounds( 0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight() );
            praiseTv.setCompoundDrawables( drawable, null, null, null );
            praiseTv.setText( dynamicModel.getLikes() );
            praiseTv.setSelected( dynamicModel.isGood() );
            Drawable drawable1 = this.getResources().getDrawable( rId[1] );
            drawable1.setBounds( 0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight() );
            stepOnTv.setCompoundDrawables( drawable1, null, null, null );
            stepOnTv.setText( dynamicModel.getDislikes() );
            stepOnTv.setSelected( dynamicModel.isDisGood() );
            Drawable drawable2 = this.getResources().getDrawable( rId[2] );
            drawable2.setBounds( 0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight() );
            shareTv.setCompoundDrawables( drawable2, null, null, null );
            shareTv.setText( dynamicModel.getShares() );
            Drawable drawable3 = this.getResources().getDrawable( rId[3] );
            drawable3.setBounds( 0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight() );
            commentTv.setCompoundDrawables( drawable3, null, null, null );
            commentTv.setText( dynamicModel.getComments() );
        }

        List<ResultItem> comments = resultItem.getItems( "hot_list" );
        if (null != comments && comments.size() > 0) {
            for (ResultItem item : comments) {
                DynamicCommentModel model = new DynamicCommentModel();
                model.setCommentContent( item.getString( "content" ) );
                model.setCommentId( item.getString( "id" ) );
                model.setCommentTime( item.getString( "create_time" ) );
                model.setCommentDislike( item.getString( "dislike" ) );
                model.setCommentLikes( item.getString( "likes" ) );
                model.setDynamicId( item.getString( "dynamics_id" ) );
                model.setToUserId( item.getString( "to_uid" ) );
                model.setToUserIsVip( item.getBooleanValue( "touid_vip", 1 ) );
                model.setToUserNick( item.getString( "touid_nickname" ) );
                model.setToUserHeader( item.getString( "touid_iconurl" ) );
                model.setLikeTy( item.getIntValue( "like_type" ) );
                DriverModel driverModel = new DriverModel();
                driverModel.setDriverId( item.getString( "uid" ) );
                driverModel.setHeader( item.getString( "uid_iconurl" ) );
                driverModel.setVip( item.getBooleanValue( "uid_vip", 1 ) );
                driverModel.setNick( item.getString( "uid_nickname" ) );
                model.setDriverModel( driverModel );
                hotComments.add( model );
            }
            headerHotAdapter.notifyDataSetChanged();
            headerHotAdapter.setOnDynamicCommentItemClickListener( this );
        } else {
            hotLayout.setVisibility( View.GONE );
        }
    }

    private void setData(ResultItem resultItem) {
        List<ResultItem> comments = resultItem.getItems( "list" );
        if (null != comments) {
            for (ResultItem item : comments) {
                DynamicCommentModel model = new DynamicCommentModel();
                model.setCommentContent( item.getString( "content" ) );
                model.setCommentId( item.getString( "id" ) );
                model.setCommentTime( item.getString( "create_time" ) );
                model.setCommentDislike( item.getString( "dislike" ) );
                model.setCommentLikes( item.getString( "likes" ) );
                model.setDynamicId( item.getString( "dynamics_id" ) );
                model.setToUserId( item.getString( "to_uid" ) );
                model.setToUserIsVip( item.getBooleanValue( "touid_vip", 1 ) );
                model.setToUserNick( item.getString( "touid_nickname" ) );
                model.setToUserHeader( item.getString( "touid_iconurl" ) );
                model.setLikeTy( item.getIntValue( "like_type" ) );
                model.setOrder( item.getIntValue( "order" ) );
                DriverModel driverModel = new DriverModel();
                driverModel.setDriverId( item.getString( "uid" ) );
                driverModel.setHeader( item.getString( "uid_iconurl" ) );
                driverModel.setVip( item.getBooleanValue( "uid_vip", 1 ) );
                driverModel.setNick( item.getString( "uid_nickname" ) );
                model.setDriverModel( driverModel );
                models.add( model );
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog();
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
        ToastCustom.makeText( this, error, ToastCustom.LENGTH_SHORT ).show();
    }

    @Override
    public void onRefresh() {
        page = 1;
        HttpManager.commentList( HttpType.REFRESH, this, this, dynamicId, type, page );
    }

    @Override
    public void onLoad() {
        page++;
        HttpManager.commentList( HttpType.LOADING, this, this, dynamicId, type, page );
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        HttpManager.shareDynamics( 15, this, new HttpResultListener() {
            @Override
            public void onSuccess(int what, ResultItem resultItem) {
                Log.i( "share_dynamics", resultItem.getString( "msg" ) );
            }

            @Override
            public void onError(int what, String error) {
                Log.i( "share_dynamics", error );
            }
        }, dynamicId );
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
    }

    @Override
    public void onCancel(Platform platform, int i) {
    }

    @Override
    public void onCommentClick(DynamicCommentModel model) {
        if (BeanUtils.isLogin()) {
            DriverModel model1 = model.getDriverModel();
            editLayout.setVisibility( View.VISIBLE );
            editText.requestFocus();
            toUid = model1.getDriverId();
        } else {
            startActivity( new Intent( mContent, LoginActivity.class ) );
        }
    }

    @Override
    public void onPraiseClick(final DynamicCommentModel model) {
        if (!BeanUtils.isLogin()) {
            startActivity( new Intent( mContent, LoginActivity.class ) );
            return;
        }
        if (!(model.getLikeTy() == 1)) {
            HttpManager.commentLike( 2500, this, new HttpResultListener() {
                @Override
                public void onSuccess(int what, ResultItem resultItem) {
                    if (1 == resultItem.getIntValue( "status" )) {
                        model.setLikeTy( 1 );
                        String likes = model.getCommentLikes();
                        if (!TextUtils.isEmpty( likes )) {
                            try {
                                int l = Integer.valueOf( likes ).intValue();
                                l++;
                                model.setCommentLikes( l + "" );
                            } catch (NumberFormatException e) {
                            }
                        }
                        if (models.indexOf( model ) > -1)
                            adapter.notifyDataSetChanged();
                        if (hotComments.indexOf( model ) > -1)
                            headerHotAdapter.notifyDataSetChanged();
                    }
                    ToastCustom.makeText( mContent,
                            resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
                }

                @Override
                public void onError(int what, String error) {
                    ToastCustom.makeText( mContent, error, ToastCustom.LENGTH_SHORT ).show();
                }
            }, model.getCommentId(), 1 );
        } else {
            ToastCustom.makeText( mContent, "\u60a8\u5df2\u7ecf\u8d5e\u8fc7\u4e86", ToastCustom.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onHeaderClick(DynamicCommentModel model) {
        if (BeanUtils.isLogin()) {
            startActivity( new Intent( mContent, UserInfoActivity.class )
                    .putExtra( "uid", model.getDriverModel().getDriverId() ) );
        } else {
            startActivity( new Intent( mContent, LoginActivity.class ) );
        }
    }

    @Override
    public void onDeleteClick(final DynamicCommentModel model) {
        buildProgressDialog();
        HttpManager.deleteComment( 2006, this, new HttpResultListener() {
            @Override
            public void onSuccess(int what, ResultItem resultItem) {
                cancelProgressDialog();
                if (1 == resultItem.getIntValue( "status" )) {
                    if (models.indexOf( model ) > -1) {
                        models.remove( model );
                        adapter.notifyDataSetChanged();
                    }

                    if (hotComments.indexOf( model ) > -1) {
                        hotComments.remove( model );
                        headerHotAdapter.notifyDataSetChanged();
                    }
                    String c = dynamicModel.getComments();
                    try {
                        int n = Integer.valueOf( c ).intValue();
                        n--;
                        dynamicModel.setComments( n + "" );
                        allTv.setText( "\u5168\u90e8\u8bc4\u8bba(" + dynamicModel.getComments() + ")" );
                    } catch (NumberFormatException e) {
                    }
                }
                ToastCustom.makeText( mContent,
                        resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
            }

            @Override
            public void onError(int what, String error) {
                cancelProgressDialog();
                ToastCustom.makeText( mContent, error, ToastCustom.LENGTH_SHORT ).show();
            }
        }, model.getCommentId() );
    }
}
