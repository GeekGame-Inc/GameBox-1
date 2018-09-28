package com.tenone.gamebox.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.AutoInstallApkThread;
import com.tenone.gamebox.mode.listener.ApkInstallListener;
import com.tenone.gamebox.mode.listener.GetGiftDialogConfirmListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.custom.CustomQBadgeView;
import com.tenone.gamebox.view.custom.DownloadProgressBar;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.custom.TwoStateButton;
import com.tenone.gamebox.view.custom.dialog.GetGiftDialog;
import com.tenone.gamebox.view.receiver.DownReceiver;
import com.tenone.gamebox.view.service.NoRequestHttpDownloadService;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.TrackingUtils;
import com.tenone.gamebox.view.utils.WindowUtils;
import com.tenone.gamebox.view.utils.database.GameDownloadTab;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftDetailsActivity extends BaseActivity implements HttpResultListener, GetGiftDialogConfirmListener, DownReceiver.DownStatusChangeListener, ApkInstallListener.InstallListener {
    @ViewInject(R.id.id_gift_detail_gameIcon)
    ImageView gameIconIv;
    @ViewInject(R.id.id_gift_detail_gameName)
    TextView gameNameTv;
    @ViewInject(R.id.id_gift_detail_gameIntro)
    TextView gameIntroTv;
    @ViewInject(R.id.id_gift_detail_gameSize)
    TextView gameSizeTv;
    @ViewInject(R.id.id_gift_detail_down)
    DownloadProgressBar downProgress;
    @ViewInject(R.id.id_gift_detail_giftName)
    TextView giftNameTv;
    @ViewInject(R.id.id_gift_detail_get)
    TwoStateButton getGittBt;
    @ViewInject(R.id.id_gift_detail_num)
    TextView numTv;
    @ViewInject(R.id.id_gift_detail_giftProgress)
    ProgressBar progressBar;
    @ViewInject(R.id.id_gift_detail_content)
    TextView giftContentTv;
    @ViewInject(R.id.id_gift_detail_use)
    TextView giftUseTv;
    @ViewInject(R.id.id_gift_detail_attention)
    TextView giftAttentionTv;
    @ViewInject(R.id.id_gift_detail_time)
    TextView giftTimeTv;
    @ViewInject(R.id.id_gift_detail_right_view)
    View badgViewTv;

    private int giftId;
    private String gameId, giftCode;
    private GetGiftDialog.Builder builder;
    private GameModel gameModel;
    private DownReceiver receiver;
    private ApkInstallListener installListener;
    private CustomQBadgeView downBadge;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_gift_detail );
        ViewUtils.inject( this );
        giftId = getIntent().getExtras().getInt( "giftId" );
        buildProgressDialog();
        HttpManager.giftInfo( 0, this, this, giftId + "" );
        receiver = new DownReceiver();
        installListener = new ApkInstallListener();
        registerDownReceiver( this, this, receiver );
        registerInstallReceiver( this, this, installListener );
    }


    @OnClick({R.id.id_imageView, R.id.id_gift_detail_right_img
            , R.id.id_gift_detail_down, R.id.id_gift_detail_get,
            R.id.id_gift_detail_game_root})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_imageView:
                finish();
                break;
            case R.id.id_gift_detail_right_img:
                startActivity( new Intent( this, ManagementActivity.class ) );
                break;
            case R.id.id_gift_detail_down:
                downloadGame();
                break;
            case R.id.id_gift_detail_get:
                if (TextUtils.isEmpty( giftCode )) {
                    buildProgressDialog();
                    HttpManager.getPack( 1, this, this, giftId + "" );
                } else {
                    if (CharSequenceUtils.CopyToClipboard( this, giftCode )) {
                        showDialog();
                    } else {
											Toast.makeText( this, "\u590d\u5236\u793c\u5305\u7801\u51fa\u9519", Toast.LENGTH_SHORT ).show();
                    }
                }
                break;
            case R.id.id_gift_detail_game_root:
                startActivity( new Intent( this, NewGameDetailsActivity.class ).putExtra( "id", gameId ) );
                break;
        }
    }

    private void downloadGame() {
        if (gameModel != null) {
            int status = gameModel.getStatus();
            switch (status) {
                case GameStatus.UNLOAD:
                    startDownload();
                    break;
                case GameStatus.LOADING:
                    gameModel.setStatus( GameStatus.PAUSEING );
                    openDownService( this, gameModel );
                    break;
                case GameStatus.PAUSEING:
                    gameModel.setStatus( GameStatus.LOADING );
                    openDownService( this, gameModel );
                    break;
                case GameStatus.COMPLETED:
                    String apkName = gameModel.getApkName();
                    ApkUtils.installApp( apkName, this );
                    break;
                case GameStatus.INSTALLING:
                    Toast.makeText( this, R.string.instaling_txt, Toast.LENGTH_SHORT )
                            .show();
                    break;
                case GameStatus.INSTALLCOMPLETED:
                    ApkUtils.doStartApplicationWithPackageName(
                            gameModel.getPackgeName(), this );
                    break;
                case GameStatus.DELETE:
                    gameModel.setStatus( GameStatus.LOADING );
                    openDownService( this, gameModel );
                case GameStatus.UNINSTALLING:
                    ApkUtils.installApp( gameModel.getApkName(), this );
                    break;
            }
        }
    }

    private void startDownload() {
        if (retuestStoragePermission( this )) {
            if (!TextUtils.isEmpty( gameModel.getUrl() )) {
                gameModel.setStatus( GameStatus.LOADING );
                openDownService( this, gameModel );
                WindowUtils.showAddDownloadWindow( this,
                        gameIconIv, 1500, getString( R.string.already_add_downlaod_list ) );
                sendDownloadActionBroadcast( this );
                Map<String, Object> map = new HashMap<String, Object>();
                map.put( TrackingUtils.USERNAMEKEY, SpUtil.getAccount() );
                map.put( TrackingUtils.NICKNAMEKEY, SpUtil.getNick() );
                map.put( TrackingUtils.MOBILEKEY, SpUtil.getPhone() );
                map.put( TrackingUtils.GAMENAME, gameModel.getName() );
                TrackingUtils.setEvent( TrackingUtils.DOWNLOADEVENT, map );
            } else {
                showToast( this.getResources().getString( R.string.no_download_path ) );
            }
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog();
        if (1 == resultItem.getIntValue( "status" )) {
            if (1 == what) {
                giftCode = resultItem.getString( "data" );
                if (!TextUtils.isEmpty( giftCode )) {
                    getGittBt.setState( 1 );
                }
                String str = resultItem.getString( "msg" );
							showToast( TextUtils.isEmpty( str ) ? "\u793c\u5305\u9886\u53d6\u6210\u529f" : str );
            } else {
                ResultItem data = resultItem.getItem( "data" );
                initView( data );
            }
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog();
        ToastCustom.makeText( this, error, ToastCustom.LENGTH_SHORT ).show();
    }

    private void initView(ResultItem resultItem) {
        String url = resultItem.getString( "logo" );
        ImageLoadUtils.loadImg( gameIconIv, this, url );
        giftNameTv.setText( resultItem.getString( "pack_name" ) );
        gameNameTv.setText( resultItem.getString( "game_name" ) );
        gameSizeTv.setText( resultItem.getString( "game_size" ) + "M" );
        gameIntroTv.setText( resultItem.getString( "game_content" ) );
        giftContentTv.setText( resultItem.getString( "pack_abstract" ) );
        giftAttentionTv.setText( resultItem.getString( "pack_notice" ) );
        String startTime = resultItem.getString( "start_time" );
        String endTime = resultItem.getString( "end_time" );
        long t1 = Long.valueOf( startTime ) * 1000;
        startTime = TimeUtils.formatDateMin( t1 );
        long t2 = Long.valueOf( endTime ) * 1000;
        endTime = TimeUtils.formatDateMin( t2 );
			giftTimeTv.setText( "\u5f00\u59cb\u65f6\u95f4:" + startTime + "\n" + "\u7ed3\u675f\u65f6\u95f4:" + endTime );
        giftUseTv.setText( resultItem.getString( "pack_method" ) );
        int count = resultItem.getIntValue( "pack_counts" );
        int use = resultItem.getIntValue( "pack_used_counts" );
        int num = count - use;
        int progress = (int) ((float) num * 100f / (float) count);
			numTv.setText( "(\u5269\u4f59" + progress + "%)" );
        progressBar.setProgress( progress );
        gameId = resultItem.getString( "game_id" );
        giftCode = resultItem.getString( "card" );
        getGittBt.setState( TextUtils.isEmpty( giftCode ) ? 0 : 1 );
        setGameModel( resultItem );
    }

    private void showDialog() {
        if (builder == null) {
            builder = new GetGiftDialog.Builder( this );
        }
        builder.setButtonText( this.getResources().getString(
                R.string.confirm ) );
        builder.setConfirmListener( this );
        builder.setTitle( this.getResources().getString(
                R.string.codeCopyHintTitle ) );
        builder.setMessage( this.getResources().getString(
                R.string.codeCopyHint ) );
        builder.showDialog();
    }

    @Override
    public void onConfirmClick(AlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        initDownBadge();
    }

    private void initDownBadge() {
        List<GameModel> gameModels = DatabaseUtils.getInstanse( this ).getDownloadList();
        int a = 0;
        for (GameModel mode : gameModels) {
            if (mode.getStatus() == GameStatus.LOADING
                    || mode.getStatus() == GameStatus.PAUSEING
                    || mode.getStatus() == GameStatus.COMPLETED) {
                a++;
            }
        }
        if (a > 0) {
            showDownBadge( a + "" );
        } else {
            if (downBadge != null) {
                downBadge.hide( true );
            }
        }
    }

    private void showDownBadge(String text) {
        if (downBadge == null) {
            downBadge = new CustomQBadgeView( this );
        }
        downBadge.bindTarget( badgViewTv )
                .setBadgeText( text )
                .setBadgeBackgroundColor( getResources().getColor( R.color.blue_40 ) )
                .setBadgeTextColor( getResources().getColor( R.color.white ) )
                .setBadgeGravity( Gravity.END | Gravity.TOP )
                .setBadgeTextSize( 7, true );
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver( receiver );
        unregisterReceiver( installListener );
        super.onDestroy();
    }

    private void setGameModel(ResultItem item) {
        if (gameModel == null) {
            gameModel = new GameModel();
        }
        gameModel.setGameTag( item.getString( "tag" ) );
        gameModel.setName( item.getString( "game_name" ) );
        String id = item.getString( "game_id" );
        if (!TextUtils.isEmpty( id )) {
            gameModel.setGameId( Integer.valueOf( id ) );
        }
        gameModel.setImgUrl( item.getString( "logo" ) );
        gameModel.setSize( item.getString( "game_size" ) );
        gameModel.setUrl( item.getString( "download_url" ) );
        gameModel.setPackgeName( item.getString( "android_pack" ) );
        String where = GameDownloadTab.GAMEID + "=? AND "
                + GameDownloadTab.GAMENAME + "=?";
        GameModel model = DatabaseUtils.getInstanse( this ).getGameModel(
                where,
                new String[]{(gameModel.getGameId() + ""),
                        gameModel.getName()} );
        gameModel.setApkName( model.getApkName() );
        gameModel.setStatus( model.getStatus() );
        gameModel.setProgress( model.getProgress() );
        ApkUtils.inspectApk( this, gameModel );
        downProgress.reset();
        downProgress.setStae( gameModel.getStatus() );
        downProgress.setProgress( gameModel.getProgress() );
    }


    protected void openDownService(Context mContext, GameModel gameModel) {
        Intent intent = new Intent( mContext, NoRequestHttpDownloadService.class );
        intent.putExtra( "gameModel", gameModel );
        mContext.startService( intent );
    }

    @Override
    public void onDownStatusChange(GameModel model) {
        if (model == null) {
            return;
        }
        if (model.getGameId() == gameModel.getGameId()) {
            gameModel.setProgress( model.getProgress() );
            gameModel.setStatus( model.getStatus() );
            gameModel.setUrl( model.getUrl() );
            switch (model.getStatus()) {
                case GameStatus.UNLOAD:
                    model.setProgress( 0 );
                    break;
                case GameStatus.COMPLETED:
                    if (SpUtil.getAutoInstall()) {
                        new AutoInstallApkThread( this, model.getApkName() ).start();
                    } else {
                        model.setStatus( GameStatus.UNINSTALLING );
                    }
                    break;

            }
            gameModel.setType( 1 );
            downProgress.reset();
            downProgress.setStae( model.getStatus() );
            downProgress.setProgress( model.getProgress() );
        }
    }

    @Override
    public void installed(String packgeName, int status) {
        if (gameModel != null) {
            if (packgeName.equals( gameModel.getPackgeName() )) {
                gameModel.setStatus( status );
                sendBroadcast( Configuration.completedFilter, gameModel, this );
            }
        }
    }

    @Override
    public void unInstall(String packgeName, int status) {
        if (packgeName.equals( gameModel.getPackgeName() )) {
            gameModel.setStatus( status );
            sendBroadcast( Configuration.deleteFilter, gameModel, this );
        }
    }
}
