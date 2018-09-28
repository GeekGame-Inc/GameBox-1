package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzxiang.pickerview.data.Type;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.EditDialogConfrimListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnTimePickerSeleteListener;
import com.tenone.gamebox.mode.listener.OnWheeledListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.dialog.EditDialog;
import com.tenone.gamebox.view.custom.pickers.AddressPickTask;
import com.tenone.gamebox.view.custom.pickers.CustomPicker;
import com.tenone.gamebox.view.custom.pickers.TimePickerDialog;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.TrackingUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.io.File;
import java.util.ArrayList;

import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;
import pub.devrel.easypermissions.AfterPermissionGranted;


public class UserMessageActivity extends BaseActivity implements HttpResultListener, EditDialogConfrimListener, OnWheeledListener, OnTimePickerSeleteListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_user_message_header)
	CircleImageView headerIv;
	@ViewInject(R.id.id_next)
	ImageView newxtIv;
	@ViewInject(R.id.id_user_message_nick)
	TextView nickTv;
	@ViewInject(R.id.id_user_message_sex)
	TextView sexTv;
	@ViewInject(R.id.id_user_message_loc)
	TextView locTv;
	@ViewInject(R.id.id_user_message_intro)
	TextView introTv;
	@ViewInject(R.id.id_user_message_birthday)
	TextView birthdayTv;
	@ViewInject(R.id.id_user_message_qq)
	TextView qqTv;
	@ViewInject(R.id.id_user_message_email)
	TextView emailTv;
	@ViewInject(R.id.id_user_message_time)
	TextView timeTv;

	CustomPicker picker;
	TimePickerDialog timePickerDialog;
	private EditDialog.EditDialogBuilder builder;

	private String uid, nickName = "", sex = "", address = "", desc = "", birth = "",
			qq = "", email = "";
	private boolean isUs = false;
	private DriverModel model;
	private ArrayList<String> paths = new ArrayList<String>();
	private String[] sexArray = {"\u7537", "\u5973", "\u5176\u4ed6"};


	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		Intent intent = getIntent();
		if (intent.hasExtra( "model" )) {
			model = (DriverModel) intent.getExtras().get( "model" );
			uid = model.getDriverId();
			isUs = SpUtil.getUserId().equals( uid );
			setContentView( R.layout.activity_user_message );
			ViewUtils.inject( this );
			initTitle();
			initView();
		}
	}

	@OnClick({R.id.id_titleBar_leftImg, R.id.id_user_message_headerLayout,
			R.id.id_user_message_nickLayout, R.id.id_user_message_sexLayout,
			R.id.id_user_message_locLayout, R.id.id_user_message_introLayout,
			R.id.id_user_message_birthdayLayout, R.id.id_user_message_qqLayout,
			R.id.id_user_message_emailLayout})
	public void onClick(View view) {
		if (view.getId() == R.id.id_titleBar_leftImg) {
			finish();
		} else {
			if (view.getId() == R.id.id_user_message_introLayout) {
				startActivityForResult( new Intent( this, UserIntroActivity.class )
						.putExtra( "intro", model.getIntro() )
						.putExtra( "uid", model.getDriverId() ), 2330 );
			}
			if (!isUs) {
				return;
			}
			switch (view.getId()) {
				case R.id.id_user_message_headerLayout:
					choicePhotoWrapper( 1 );
					break;
				case R.id.id_user_message_nickLayout:
					if (builder == null) {
						builder = new EditDialog.EditDialogBuilder( this ).setConfrimListener( this );
					}
					builder.setHint( getString( R.string.please_input_nick) );
					builder.setTitle( getString( R.string.change_nick) );
					builder.setAction( "nick" );
					builder.showDialog();
					break;
				case R.id.id_user_message_sexLayout:
					showSexPicker();
					break;
				case R.id.id_user_message_locLayout:
					showAddressPicker();
					break;
				case R.id.id_user_message_birthdayLayout:
					showBirthPicker();
					break;
				case R.id.id_user_message_qqLayout:
					if (builder == null) {
						builder = new EditDialog.EditDialogBuilder( this ).setConfrimListener( this );
					}
					builder.setHint( getString( R.string.please_input_qq) );
					builder.setTitle( getString( R.string.change_qq) );
					builder.setAction( "qq" );
					builder.showDialog();
					break;
				case R.id.id_user_message_emailLayout:
					if (builder == null) {
						builder = new EditDialog.EditDialogBuilder( this ).setConfrimListener( this );
					}
					builder.setHint( getString( R.string.please_input_mail) );
					builder.setTitle( getString( R.string.change_mail) );
					builder.setAction( "email" );
					builder.showDialog();
					break;
			}
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case 2330:
					if (data != null) {
						if (data.hasExtra( "intro" )) {
							desc = data.getExtras().getString( "intro" );
							introTv.setText( desc );
						}
					}
					break;
				case 200:
					paths.clear();
					paths.addAll( BGAPhotoPickerActivity.getSelectedImages( data ) );
					startActivityForResult(
							new Intent( this, TailorImageActivity.class )
									.putExtra( "path", paths.get( 0 ) ), 500 );
					break;
				case 500:
					if (data != null) {
						String path = data.getExtras().getString( "path" );
						paths.clear();
						paths.add( path );
						ImageLoadUtils.loadLocImg( this, paths.get( 0 ), headerIv );
					}
					break;
			}
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		cancelProgressDialog();
		showToast( resultItem.getString( "msg" ) );
		if (1 == resultItem.getIntValue( "status" )) {
			if (!TextUtils.isEmpty( nickName )) {
				SpUtil.setNick( nickName );
			}
			String data = resultItem.getString( "data" );
			if (!TextUtils.isEmpty( data )) {
				SpUtil.setHeaderUrl( data );
			}
			setResult( RESULT_OK );
			ListenerManager.sendOnUserInfoChangeListener();
			finish();
		}
	}

	@Override
	public void onError(int what, String error) {
		cancelProgressDialog();
	}

	private void initTitle() {
		titleBarView.setTitleText( isUs ? getString( R.string.eidt_message) : getString( R.string.driver_message) );
		titleBarView.setLeftImg( R.drawable.icon_back_grey );
		if (isUs) {
			titleBarView.setRightText( getString( R.string.save) );
			titleBarView.setOnClickListener( R.id.id_titleBar_rightText, v -> editDesc( HttpType.REFRESH,
					(paths.size() > 0) ? paths.get( 0 ) : "", nickName, sex, address, desc, birth, qq, email ) );
		}
	}

	private void initView() {
		ImageLoadUtils.loadNormalImg( headerIv, this, model.getHeader() );
		nickTv.setText( getText( model.getNick(), getString( R.string.change_your_nick), getString( R.string.have_no_nick) ) );
		sexTv.setText( getText( model.getSex(), getString( R.string.choice_your_sex), getString( R.string.not_set) ) );
		introTv.setText( getText( model.getIntro(), getString( R.string.edit_your_intro), getString( R.string.not_intro) ) );
		locTv.setText( getText( model.getAddress(), getString( R.string.choice_your_loc), getString( R.string.not_loc) ) );
		timeTv.setText( model.getRegTime() );
		emailTv.setText( getText( model.getEmail(), getString( R.string.input_your_mail), getString( R.string.not_mail) ) );
		qqTv.setText( getText( model.getQq(), getString( R.string.input_your_qq), getString( R.string.not_qq) ) );
		birthdayTv.setText( getText( model.getBirth(), getString( R.string.choice_your_birthday), getString( R.string.not_birthday) ) );
		if (!isUs) {
			newxtIv.setImageBitmap( null );
			nickTv.setCompoundDrawables( null, null, null, null );
			sexTv.setCompoundDrawables( null, null, null, null );
			locTv.setCompoundDrawables( null, null, null, null );
			qqTv.setCompoundDrawables( null, null, null, null );
			emailTv.setCompoundDrawables( null, null, null, null );
			birthdayTv.setCompoundDrawables( null, null, null, null );
		}
	}


	private String getText(String str, String hint, String hint2) {
		return TextUtils.isEmpty( str ) ? (isUs ? hint : hint2) : str;
	}

	@AfterPermissionGranted(110)
	private void choicePhotoWrapper(int maxImage) {
		File takePhotoDir = (1 == maxImage) ? null : FileUtils.getCacheDirectory( this );
		startActivityForResult( BGAPhotoPickerActivity.newIntent( this, takePhotoDir, maxImage,
				paths ), 200 );
	}


	private void editDesc(int what, String path, String nickName, String sex, String address, String desc, String birth,
												String qq, String email) {
		buildProgressDialog();
		HttpManager.editDesc( what, this, this, path, nickName, sex, address, desc, birth, qq, email );
		TrackingUtils.getUserInfoMap().put( TrackingUtils.GENDERKEY, sex );
		TrackingUtils.getUserInfoMap().put( TrackingUtils.LOCKEY, address );
		TrackingUtils.getUserInfoMap().put( TrackingUtils.BIRTHDAYKEY, birth );
		TrackingUtils.setEvent( TrackingUtils.EDITUSERINFOKEY, TrackingUtils.getUserInfoMap() );
	}

	@Override
	public void onConfrimClick(String action, String message) {
		switch (action) {
			case "nick":
				nickName = message;
				nickTv.setText( nickName );
				break;
			case "qq":
				qq = message;
				qqTv.setText( qq );
				break;
			case "email":
				email = message;
				emailTv.setText( email );
				break;
		}
	}

	@Override
	public void onWheeled(String text) {
		sex = text;
		sexTv.setText( sex );
	}

	private void showSexPicker() {
		if (picker == null) {
			picker = new CustomPicker( this, sexArray, getString( R.string.choice_sex) );
			picker.setOnWheeledListener( this );
			picker.setGravity( Gravity.CENTER );//居中
		}
		picker.setOffset( 1 );
		picker.show();
	}

	public void showAddressPicker() {
		AddressPickTask task = new AddressPickTask( this );
		task.setHideCounty( true );
		task.setCallback( new AddressPickTask.Callback() {
			@Override
			public void onAddressInitFailed() {
				showToast( getString( R.string.init_data_error) );
			}

			@Override
			public void onAddressPicked(Province province, City city, County county) {
				address = province.getAreaName() + "-" + city.getAreaName();
				locTv.setText( address );
			}
		} );
		task.execute( getString( R.string.sichuan), getString( R.string.chengdu) );
	}


	public void showBirthPicker() {
		if (timePickerDialog == null) {
			timePickerDialog = new TimePickerDialog.Builder()
					.setCancelStringId( getString( R.string.cancle) )
					.setSureStringId( getString( R.string.confirm) )
					.setTitleStringId( getString( R.string.choice_sex) )
					.setCyclic( true )
					.setThemeColor( getResources().getColor( R.color.blue_40 ) )
					.setWheelItemTextSelectorColorId( getResources().getColor( R.color.blue_40 ) )
					.setWheelItemTextNormalColorId( getResources().getColor( R.color.gray_9a ) )
					.setType( Type.YEAR_MONTH_DAY )
					.setWheelItemTextSize( 12 )
					.setMinMillseconds( TimeUtils.getDateForm( "1900-01-01 00-00-00" ) )
					.build();
			timePickerDialog.setListener( this );
		}
		timePickerDialog.show( getSupportFragmentManager().beginTransaction(), "time" );
	}

	@Override
	public void onTimePickerSelete(String year, String month, String day) {
		birth = year + "-" + month + "-" + day;
		birthdayTv.setText( birth );
	}
}
