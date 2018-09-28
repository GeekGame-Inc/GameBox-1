package com.tenone.gamebox.view.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.sy.sdk.model.HttpUrls;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ProductModel;
import com.tenone.gamebox.mode.mode.UIStringBuilder;
import com.tenone.gamebox.mode.mode.VipModel;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.base.MyApplication;
import com.thoughtworks.xstream.mapper.Mapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressLint("DefaultLocale")
public class HttpManager {

	public static void startPay(int what, Context cxt,
															HttpResultListener listener, VipModel model) {
		String uid = SpUtil.getUserId();
		String url = MyApplication.getHttpUrl().getPayStart();
		String nickName = SpUtil.getNick();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "deviceType=1&" );
		builder.append( "appid=" + Constant.APPID + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "uid=" + uid + "&" );
		builder.append( "serverID=" + channel + "&" );
		builder.append( "serverNAME=" + "\u624b\u6e38\u76d2\u5b50" + "&" );
		builder.append( "roleID=" + uid + "&" );
		builder.append( "roleNAME=" + nickName + "&" );
		builder.append( "productID=" + model.getProductID() + "&" );
		builder.append( "productNAME=" + "\u5f00\u901aVIP" + "&" );
		builder.append( "payType=" + model.getPayStyle() + "&" );
		builder.append( "payMode=" + 3 + "&" );
		builder.append( "cardID=" + "&" );
		builder.append( "cardPass=" + "&" );
		builder.append( "cardMoney=" + "&" );
		builder.append( "amount=" + model.getMoney() + "&" );
		builder.append( "extend=" + "\u5f00\u901aVIP" );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "deviceType", 1 + "" )
				.add( "appid", TextUtils.isEmpty( Constant.APPID ) ? "" : Constant.APPID )
				.add( "channel", TextUtils.isEmpty( channel ) ? "" : channel )
				.add( "uid", TextUtils.isEmpty( uid ) ? "" : uid )
				.add( "serverID", channel + "" )
				.add( "serverNAME", "\u624b\u6e38\u76d2\u5b50" )
				.add( "roleID", uid + "" )
				.add( "roleNAME", TextUtils.isEmpty( nickName ) ? "" : nickName )
				.add( "productID", model.getProductID() + "" )
				.add( "productNAME", "\u5f00\u901aVIP" )
				.add( "payType", model.getPayStyle() + "" )
				.add( "payMode", "3" )
				.add( "cardID", "" )
				.add( "cardPass", "" ).add( "cardMoney", "" )
				.add( "amount", model.getMoney() + "" )
				.add( "extend", "\u5f00\u901aVIP" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void payQuery(int what, Context cxt,
															HttpResultListener listener, String orderId) {
		String url = MyApplication.getHttpUrl().getPayQuery();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "orderID=" + orderId );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "orderID", orderId + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void payReady(int what, Context cxt,
															HttpResultListener listener) {
		String uid = SpUtil.getUserId();
		String url = MyApplication.getHttpUrl().getPayReady();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "appid=" + Constant.APPID + "&" );
		builder.append( "uid=" + uid );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "appid",
						TextUtils.isEmpty( Constant.APPID ) ? "" : Constant.APPID )
				.add( "uid", TextUtils.isEmpty( uid ) ? "" : uid )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void platformCoinDetails(int what, Context cxt,
																				 HttpResultListener listener, int page, boolean type) {
		String url = type ? MyApplication.getHttpUrl().getGoldCoin()
				: MyApplication.getHttpUrl().getPlatformmoney();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "channel", TextUtils.isEmpty( channel ) ? "" : channel )
				.add( "uid", TextUtils.isEmpty( uid ) ? "" : uid )
				.add( "page", page + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.hasLoadingPostHttp( cxt, what, url, body, listener );
	}

	public static void login(int what, Context cxt,
													 HttpResultListener listener, String userName, String pwd) {
		String url = MyApplication.getHttpUrl().getLogin();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "username=" + userName + "&" );
		builder.append( "password=" + pwd + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "system=1&" );
		builder.append( "machine_code=" + TelephoneUtils.getImei( cxt ) );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = new FormBody.Builder()
				.add( "random", channel + "-" + Constant.getRandom() )
				.add( "username", userName + "" )
				.add( "password", pwd + "" )
				.add( "channel", channel )
				.add( "system", "1" )
				.add( "machine_code", TelephoneUtils.getImei( cxt ) + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void register(int what, Context cxt,
															HttpResultListener listener, String userName, String pwd,
															String code, String mobile, String type) {
		String url = MyApplication.getHttpUrl().getRegister();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "username=" + userName + "&" );
		builder.append( "code=" + code + "&" );
		builder.append( "mobile=" + mobile + "&" );
		builder.append( "password=" + pwd + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "system=1&" );
		builder.append( "maker=" + TelephoneUtils.getDeviceBrandName() + "&" );
		builder.append( "mobile_model=" + TelephoneUtils.getSystemModel() + "&" );
		builder.append( "machine_code=" + TelephoneUtils.getImei( cxt ) + "&" );
		builder.append( "system_version=" + TelephoneUtils.getSystemVersion()
				+ "&" );
		builder.append( "type=" + type );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "random", channel + "-" + Constant.getRandom() )
				.add( "username", userName + "" )
				.add( "code", code + "" )
				.add( "mobile", mobile + "" )
				.add( "password", pwd + "" )
				.add( "channel", channel )
				.add( "system", "1" )
				.add( "maker", TelephoneUtils.getDeviceBrandName() + "" )
				.add( "mobile_model", TelephoneUtils.getSystemModel() + "" )
				.add( "machine_code", TelephoneUtils.getImei( cxt ) + "" )
				.add( "system_version", TelephoneUtils.getSystemVersion() + "" )
				.add( "type", type )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		Log.i( "GameBox", "register random is " + Constant.getRandom() );
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void getCode(int what, Context cxt,
														 HttpResultListener listener, String mobile, int type) {
		String url = MyApplication.getHttpUrl().getSendCode();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "mobile=" + mobile + "&" );
		builder.append( "type=" + type );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "mobile", mobile + "" )
				.add( "type", type + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void check_smscode(int what, Context cxt,
																	 HttpResultListener listener, String mobile, String code) {
		String url = MyApplication.getHttpUrl().getCheckSmscode();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "mobile=" + mobile + "&" );
		builder.append( "code=" + code );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "mobile", mobile + "" )
				.add( "code", code + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void editDesc(int what, Context cxt,
															HttpResultListener listener, String path,
															String nickName, String sex, String address, String desc, String birth,
															String qq, String email) {
		String uid = SpUtil.getUserId();
		String url = MyApplication.getHttpUrl().getEditDesc();
		String channel = MyApplication.getConfigModle().getChannelID();
		int s = 0;
		if (!TextUtils.isEmpty( sex )) {
			s = cxt.getString( R.string.man ).equals( sex ) ? 1 : (cxt.getString( R.string.women ).equals( sex ) ? 2 : 3);
		}
		MediaType MEDIA_TYPE_JPEG = MediaType.parse( "image/png" );
		MultipartBody.Builder builder = new MultipartBody.Builder()
				.setType( MultipartBody.FORM );
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "channel=" + channel + "&" );
		uBuilder.append( "nick_name=" + nickName + "&" );
		uBuilder.append( "sex=" + s + "&" );
		uBuilder.append( "address=" + address + "&" );
		uBuilder.append( "desc=" + desc + "&" );
		uBuilder.append( "birth=" + birth + "&" );
		uBuilder.append( "qq=" + qq + "&" );
		uBuilder.append( "email=" + email );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!TextUtils.isEmpty( path )) {
			File file = new File( path );
			builder.addFormDataPart( "icon_url", file.getName(),
					RequestBody.create( MEDIA_TYPE_JPEG, file ) );
		}
		builder.addFormDataPart( "uid", uid );
		builder.addFormDataPart( "channel", channel );
		builder.addFormDataPart( "nick_name", nickName );
		builder.addFormDataPart( "sex", s + "" );
		builder.addFormDataPart( "address", address );
		builder.addFormDataPart( "desc", desc );
		builder.addFormDataPart( "birth", birth );
		builder.addFormDataPart( "qq", qq );
		builder.addFormDataPart( "email", email );
		builder.addFormDataPart( "sign", sign );
		Log.i( "editDesc", "sign is " + sign + " str is " + uBuilder.toString() );
		HttpUtils.postHttp( cxt, what, url, builder.build(), listener );
	}

	public static void getCoinInfo(int what, Context cxt,
																 HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getCoinInfo();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "channel", channel + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.hasLoadingPostHttp( cxt, what, url, body, listener );
	}

	public static void myCoin(int what, Context cxt, HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getMyCoin();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "channel", channel + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.hasLoadingPostHttp( cxt, what, url, body, listener );
	}

	public static void exchange(int what, Context cxt,
															HttpResultListener listener, String counts) {
		String url = MyApplication.getHttpUrl().getPlatExchange();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "platform_counts=" + counts );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "channel", channel + "" )
				.add( "platform_counts", counts + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.hasLoadingPostHttp( cxt, what, url, body, listener );
	}

	public static void customerService(int what, Context cxt,
																		 HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getCustomerService();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "channel", channel + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void changegame(int what, Context cxt,
																HttpResultListener listener, String origin_appname,
																String origin_servername, String origin_rolename,
																String new_appname, String new_servername, String new_rolename,
																String qq, String mobile) {
		String url = MyApplication.getHttpUrl().getChangegameApply();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "origin_appname=" + origin_appname + "&" );
		builder.append( "origin_servername=" + origin_servername + "&" );
		builder.append( "origin_rolename=" + origin_rolename + "&" );
		builder.append( "new_appname=" + new_appname + "&" );
		builder.append( "new_servername=" + new_servername + "&" );
		builder.append( "new_rolename=" + new_rolename + "&" );
		builder.append( "qq=" + qq + "&" );
		builder.append( "mobile=" + mobile );
		builder.append( Constant.APPKEY );

		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "channel", channel + "" )
				.add( "uid", uid + "" )
				.add( "origin_appname", origin_appname + "" )
				.add( "origin_servername", origin_servername + "" )
				.add( "origin_rolename", origin_rolename + "" )
				.add( "new_appname", new_appname + "" )
				.add( "new_servername", new_servername + "" )
				.add( "new_rolename", new_rolename + "" ).add( "qq", qq + "" )
				.add( "mobile", mobile + "" ).add( "sign", sign ).build();
		HttpUtils.hasLoadingPostHttp( cxt, what, url, body, listener );
	}


	public static void changegameLog(int what, Context cxt,
																	 HttpResultListener listener, int page) {
		String url = MyApplication.getHttpUrl().getChangegameLog();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "channel", channel + "" )
				.add( "uid", uid + "" ).add( "page", page + "" ).add( "sign", sign )
				.build();
		HttpUtils.hasLoadingPostHttp( cxt, what, url, body, listener );
	}

	public static void rebateNotice(int what, Context cxt,
																	HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getRebateNotice();
		RequestBody body = new FormBody.Builder().build();
		HttpUtils.hasLoadingPostHttp( cxt, what, url, body, listener );
	}

	public static void rebateInfo(int what, Context cxt,
																HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getRebateInfo();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void rebateInfo(int what, Context cxt,
																HttpResultListener listener, String uid) {
		String url = MyApplication.getHttpUrl().getRebateInfo();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void rebateApply(int what, Context cxt,
																 HttpResultListener listener, String appid, String rolename,
																 String roleid, String serverID) {
		String url = MyApplication.getHttpUrl().getRebateApply();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "appid=" + appid + "&" );
		builder.append( "rolename=" + rolename + "&" );
		builder.append( "roleid=" + roleid + "&" );
		builder.append( "serverID=" + serverID );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "appid", appid + "" ).add( "rolename", rolename + "" )
				.add( "serverID", serverID )
				.add( "roleid", roleid + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void rebateApply(int what, Context cxt,
																 HttpResultListener listener, String appid, String rolename,
																 String roleid, String serverID, String uid) {
		String url = MyApplication.getHttpUrl().getRebateApply();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "appid=" + appid + "&" );
		builder.append( "rolename=" + rolename + "&" );
		builder.append( "roleid=" + roleid + "&" );
		builder.append( "serverID=" + serverID );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "appid", appid + "" ).add( "rolename", rolename + "" )
				.add( "serverID", serverID )
				.add( "roleid", roleid + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void rebateRecord(int what, Context cxt,
																	HttpResultListener listener, int page) {
		String url = MyApplication.getHttpUrl().getRebateRecord();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "page", page + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void rebateRecord(int what, Context cxt,
																	HttpResultListener listener, int page, String uid) {
		String url = MyApplication.getHttpUrl().getRebateRecord();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "page", page + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void rebateKnow(int what, Context cxt,
																HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getRebateKnow();
		RequestBody body = new FormBody.Builder().build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void changeGameKnow(int what, Context cxt,
																		HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getChangegameKnow();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "channel", channel + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void modifyPassword(int what, Context cxt,
																		HttpResultListener listener, String oldPwd, String newPwd) {
		String url = MyApplication.getHttpUrl().getModifyPassword();
		String id = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "id=" + id + "&" );
		builder.append( "password=" + oldPwd + "&" );
		builder.append( "newpassword=" + newPwd );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = new FormBody.Builder().add( "id", id + "" )
				.add( "password", oldPwd + "" ).add( "newpassword", newPwd + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void forgetPassword(int what, Context cxt,
																		HttpResultListener listener, String token, String password) {
		String url = MyApplication.getHttpUrl().getForgetPassword();
		String id = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "id=" + id + "&" );
		builder.append( "password=" + password + "&" );
		builder.append( "token=" + token );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMd5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i( "sign", "string is " + builder.toString() );
		Log.i( "sign", "sign is " + sign );
		RequestBody body = new FormBody.Builder().add( "id", id + "" )
				.add( "password", password + "" ).add( "token", token + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void userCenter(int what, Context cxt,
																HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getUserCenter();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "channel", channel + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void signInit(int what, Context cxt,
															HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getSignInit();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "channel", channel + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void signIn(int what, Context cxt, HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getDoSign();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid + "" )
				.add( "channel", channel + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void getVipOption(int what, Context cxt,
																	HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getGetVipOption();
		RequestBody body = new FormBody.Builder().build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void modifyNickname(int what, Context cxt,
																		HttpResultListener listener, String nick_name) {
		String url = MyApplication.getHttpUrl().getModifyNicename();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "id=" + uid + "&" );
		builder.append( "nick_name=" + nick_name );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "id", uid )
				.add( "nick_name", nick_name ).add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void giveCoin(int what, Context cxt,
															HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getGiveCoin();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		url += "&uid=" + uid + "&sign=" + sign;
		HttpUtils.getHttp( cxt, what, url, listener );
	}

	public static void friendRecomInfo(int what, Context cxt,
																		 HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getFriendRecomInfo();
		String uid = SpUtil.getUserId();
		uid = "0".equals( uid ) ? "" : uid;
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid )
				.add( "channel", channel ).add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void prizes(int what, Context cxt, HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getPrizes();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void bindOrUnbingMobile(int what, Context cxt,
																				HttpResultListener listener, String mobile, String code,
																				boolean isBind) {
		String url = isBind ? MyApplication.getHttpUrl().getBindMobile()
				: MyApplication.getHttpUrl().getUnBindMobile();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "mobile=" + mobile + "&" );
		builder.append( "appid=" + Constant.APPID + "&" );
		builder.append( "code=" + code );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid )
				.add( "uid", uid ).add( "mobile", mobile )
				.add( "appid", Constant.APPID ).add( "code", code )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void getStartImgs(int what, Context cxt,
																	HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getGetStartImgs();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "system=1&" );
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "system", "1" )
				.add( "channel", channel ).add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void getNotice(int what, Context cxt,
															 HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getNotice();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "cid=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "cid", channel )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void getApatch(int what, Context cxt,
															 HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getGetApatch();
		String version = ApkUtils.getVersionCode( cxt ) + "";
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "version=" + version );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "version", version )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void boxInstallInfo(int what, Context cxt,
																		HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getBoxInstallInfo();
		String version = BeanUtils.getVersion( cxt );
		String channel = MyApplication.getConfigModle().getChannelID();
		RequestBody body = new FormBody.Builder()
				.add( "system", "1" )
				.add( "version", version )
				.add( "channel", channel )
				.add( "maker", TelephoneUtils.getDeviceBrand() )
				.add( "mobile_model", TelephoneUtils.getSystemModel() )
				.add( "code", TelephoneUtils.getImei( cxt ) )
				.add( "system_version", TelephoneUtils.getSystemVersion() )
				.add( "ip", NetworkUtils.getLocalIpAddress() )
				.add( "mac", TelephoneUtils.getMac() )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void boxStartInfo(int what, Context cxt,
																	HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getBoxStartInfo();
		String version = BeanUtils.getVersion( cxt );
		String channel = MyApplication.getConfigModle().getChannelID();
		RequestBody body = new FormBody.Builder().add( "system", "1" )
				.add( "version", version ).add( "channel", channel )
				.add( "code", TelephoneUtils.getImei( cxt ) )
				.add( "system_version", TelephoneUtils.getSystemVersion() )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void getMessageList(int what, Context cxt,
																		HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getMessageList();
		Log.i( "HttpManager", "url is " + url );
		String channel = MyApplication.getConfigModle().getChannelID();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid )
				.add( "channel", channel )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void messageInfo(int what, Context cxt,
																 HttpResultListener listener, int messageId) {
		String url = MyApplication.getHttpUrl().getMessageInfo();
		String channel = MyApplication.getConfigModle().getChannelID();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "user_message_id=" + messageId );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid )
				.add( "user_message_id", messageId + "" )
				.add( "channel", channel )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void deleteMessage(int what, Context cxt,
																	 HttpResultListener listener, int messageId) {
		String url = MyApplication.getHttpUrl().getDeleteMessage();
		String channel = MyApplication.getConfigModle().getChannelID();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "user_message_id=" + messageId );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid )
				.add( "user_message_id", messageId + "" )
				.add( "channel", channel )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void getBonus(int what, Context cxt,
															HttpResultListener listener, int messageId, String url) {
		String channel = MyApplication.getConfigModle().getChannelID();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "user_message_id=" + messageId );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid )
				.add( "user_message_id", messageId + "" )
				.add( "channel", channel )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void getUnreadCounts(int what, Context cxt,
																		 HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getUnreadCounts();
		String channel = MyApplication.getConfigModle().getChannelID();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder().add( "uid", uid )
				.add( "channel", channel )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void getDynamics(int what, Context cxt,
																 HttpResultListener listener, int page, int type) {
		String url = MyApplication.getHttpUrl().getGetDynamics();
		UIStringBuilder builder = new UIStringBuilder();
		String uid = BeanUtils.isLogin() ? SpUtil.getUserId() : "";
		builder.append( "type=" + type + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		if (!TextUtils.isEmpty( uid ))
			builder1.add( "uid", uid );
		builder1.add( "type", type + "" );
		builder1.add( "page", page + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void getDynamics(int what, Context cxt,
																 HttpResultListener listener, String buid, int page, int type) {
		String url = MyApplication.getHttpUrl().getGetDynamics();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "type=" + type + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		String uid = SpUtil.getUserId();
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		if (!TextUtils.isEmpty( uid ))
			builder1.add( "uid", uid );
		if (5 == type) {
			builder1.add( "buid", buid );
		}
		builder1.add( "type", type + "" );
		builder1.add( "page", page + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void publishDynamics(int what, Context cxt,
																		 HttpResultListener listener, String content, File[] files) {
		String url = MyApplication.getHttpUrl().getPublishDynamics();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "content=" + content );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		MediaType MEDIA_TYPE_JPEG = MediaType.parse( "image/png" );
		MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType( MultipartBody.FORM );

		if (files != null && files.length > 0) {
			int length = files.length;
			for (int i = 0; i < length; i++) {
				File f = files[i];
				if (f != null) {
					multipartBody.addFormDataPart( "imgs[]", f.getName(), RequestBody.create( MEDIA_TYPE_JPEG, f ) );
				}
			}
		}
		multipartBody.addFormDataPart( "uid", uid + "" );
		multipartBody.addFormDataPart( "content", content + "" );
		multipartBody.addFormDataPart( "sign", sign );
		MultipartBody requestBody = multipartBody.build();
		HttpUtils.postHttp( cxt, what, url, requestBody, listener );
	}


	public static void dynamicsLike(int what, Context cxt,
																	HttpResultListener listener, String dynamics_id, int type) {
		String url = MyApplication.getHttpUrl().getDynamicsLike();
		UIStringBuilder builder = new UIStringBuilder();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "dynamics_id=" + dynamics_id + "&" );
		builder.append( "type=" + type );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "uid", uid );
		builder1.add( "channel", channel + "" );
		builder1.add( "dynamics_id", dynamics_id + "" );
		builder1.add( "type", type + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void cancleDynamicsLike(int what, Context cxt,
																				HttpResultListener listener, String dynamics_id, int type) {
		String url = MyApplication.getHttpUrl().getCancelDynamicsLike();
		UIStringBuilder builder = new UIStringBuilder();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "dynamics_id=" + dynamics_id + "&" );
		builder.append( "type=" + type );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "uid", uid );
		builder1.add( "channel", channel + "" );
		builder1.add( "dynamics_id", dynamics_id + "" );
		builder1.add( "type", type + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void shareDynamics(int what, Context cxt,
																	 HttpResultListener listener, String dynamics_id) {
		String url = MyApplication.getHttpUrl().getShareDynamics();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "id=" + dynamics_id );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "id", dynamics_id );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void shareArticle(int what, Context cxt,
																	HttpResultListener listener, String article_id) {
		String url = MyApplication.getHttpUrl().getShareArticle();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "channel=" + channel + "&" );
		builder.append( "article_id=" + article_id );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "article_id", article_id );
		builder1.add( "channel", channel );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void commentList(int what, Context cxt,
																 HttpResultListener listener, String dynamics_id, int type, int page) {
		String url = MyApplication.getHttpUrl().getCommentList();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "dynamics_id=" + dynamics_id + "&" );
		builder.append( "type=" + type + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "uid", uid );
		builder1.add( "dynamics_id", dynamics_id );
		builder1.add( "type", type + "" );
		builder1.add( "page", page + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		Log.i( "commentList", "builder is " + builder.toString() + "   sign is " + sign + "   url is " + url );
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void gameCommentList(int what, Context cxt,
																		 HttpResultListener listener, String dynamics_id, int type, int page) {
		String url = MyApplication.getHttpUrl().getCommentList();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "dynamics_id=" + dynamics_id + "&" );
		builder.append( "type=" + type + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "uid", uid );
		builder1.add( "dynamics_id", dynamics_id );
		builder1.add( "type", type + "" );
		builder1.add( "page", page + "" );
		builder1.add( "comment_type", "2" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
		Log.i( "gameCommentList", "url is  " + url + "&" + builder.toString() + "&sign=" + sign + "&comment_type=2" );
	}


	public static void commentLike(int what, Context cxt,
																 HttpResultListener listener, String comment_id, int type) {
		String url = MyApplication.getHttpUrl().getCommentLike();
		UIStringBuilder builder = new UIStringBuilder();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "comment_id=" + comment_id + "&" );
		builder.append( "type=" + type );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "uid", uid );
		builder1.add( "channel", channel + "" );
		builder1.add( "comment_id", comment_id + "" );
		builder1.add( "type", type + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void cancleCommentLike(int what, Context cxt,
																			 HttpResultListener listener, String comment_id, int type) {
		String url = MyApplication.getHttpUrl().getCancelCommentLike();
		UIStringBuilder builder = new UIStringBuilder();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "comment_id=" + comment_id + "&" );
		builder.append( "type=" + type );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "uid", uid );
		builder1.add( "channel", channel + "" );
		builder1.add( "comment_id", comment_id + "" );
		builder1.add( "type", type + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void followOrCancel(int what, Context cxt,
																		HttpResultListener listener, String buid, int type) {
		String url = MyApplication.getHttpUrl().getFollowOrCancel();
		UIStringBuilder builder = new UIStringBuilder();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		builder.append( "uid=" + uid + "&" );
		builder.append( "buid=" + buid + "&" );
		builder.append( "type=" + type );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "uid", uid );
		builder1.add( "buid", buid + "" );
		builder1.add( "type", type + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
		Log.i( "followOrCancel", "url is " + url + " builder is " + builder.toString() + " sign is " + sign );
	}

	public static void doComment(int what, Context cxt,
															 HttpResultListener listener, String toUid, String dynamics_id, String content) {
		String url = MyApplication.getHttpUrl().getDoComment();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "to_uid=" + toUid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "dynamics_id=" + dynamics_id + "&" );
		builder.append( "content=" + content );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "uid", uid );
		builder1.add( "dynamics_id", dynamics_id );
		builder1.add( "to_uid", toUid + "" );
		builder1.add( "content", content + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void doGameComment(int what, Context cxt,
																	 HttpResultListener listener, String toUid,
																	 String dynamics_id, String content, int is_fake, String comment_type, int is_game_id) {
		String url = MyApplication.getHttpUrl().getDoComment();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "to_uid=" + toUid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "dynamics_id=" + dynamics_id + "&" );
		builder.append( "content=" + content );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "uid", uid );
		builder1.add( "dynamics_id", dynamics_id );
		builder1.add( "to_uid", toUid + "" );
		builder1.add( "content", content + "" );
		builder1.add( "comment_type", comment_type );
		builder1.add( "is_fake", is_fake + "" );
		builder1.add( " is_game_id", is_game_id + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
		Log.i( "doGameComment", "url is " + url + "&" + builder.toString() +
				"&comment_type=" + comment_type + "&sign=" + sign + "&is_fake=" + is_fake );
	}

	public static void deleteComment(int what, Context cxt,
																	 HttpResultListener listener, String comment_id) {
		String url = MyApplication.getHttpUrl().getDeleteComment();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "comment_id=" + comment_id );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "uid", uid );
		builder1.add( "comment_id", comment_id );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void userDesc(int what, Context cxt,
															HttpResultListener listener, String uid) {
		String url = MyApplication.getHttpUrl().getUserDesc();
		String visit_uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "visit_uid=" + visit_uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "field_type=" + 2 );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "uid", uid );
		builder1.add( "visit_uid", visit_uid );
		builder1.add( "field_type", "2" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void followList(int what, Context cxt,
																HttpResultListener listener, String visitUid, int page, int type) {
		String uid = SpUtil.getUserId();
		String url = MyApplication.getHttpUrl().getFollowList();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + visitUid + "&" );
		builder.append( "visit_uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "type=" + type + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "visit_uid", uid );
		builder1.add( "uid", visitUid );
		builder1.add( "type", "" + type );
		builder1.add( "page", "" + page );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void userNewUp(int what, Context cxt,
															 HttpResultListener listener) {
		String uid = SpUtil.getUserId();
		String url = MyApplication.getHttpUrl().getNewUpCounts();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "uid", uid );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
		Log.i( "userNewUp", "url is " + url + "&channel=" + channel + "&uid=" + uid + "&sign=" + sign );
	}

	public static void myCommentZan(int what, Context cxt,
																	HttpResultListener listener, int type, int page) {
		String uid = BeanUtils.isLogin() ? SpUtil.getUserId() : "";
		String url = MyApplication.getHttpUrl().getMyCommentZan();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "type=" + type + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "uid", uid );
		builder1.add( "type", type + "" );
		builder1.add( "page", page + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void doInit(int what, Context cxt,
														HttpResultListener listener) {
		String channel = MyApplication.getConfigModle().getChannelID();
		String url = MyApplication.getHttpUrl().getDoInit();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "channel=" + channel + "&" );
		uBuilder.append( "system=" + "1" );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "system", "1" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
		Log.i( "doInit", "url is " + url + "&" + uBuilder.toString() + "&sign=" + sign );
	}


	public static void doInitV2(int what, Context cxt,
															HttpResultListener listener, int isFirstBoot) {
		int version = ApkUtils.getVersionCode( cxt );
		String channel = MyApplication.getConfigModle().getChannelID();
		String maker = TelephoneUtils.getDeviceBrand();
		String mobileModel = TelephoneUtils.getSystemModel();
		String machineCode = TelephoneUtils.getImei( cxt );
		String systemVersion = TelephoneUtils.getSystemVersion();
		String mac = TelephoneUtils.getMac();
		String url = MyApplication.getHttpUrl().getDoInitV2();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "system=1&" );
		uBuilder.append( "version=" + version + "&" );
		uBuilder.append( "channel=" + channel + "&" );
		uBuilder.append( "maker=" + maker + "&" );
		uBuilder.append( "machine_code=" + machineCode + "&" );
		uBuilder.append( "mobile_model=" + mobileModel + "&" );
		uBuilder.append( "system_version=" + systemVersion + "&" );
		uBuilder.append( "mac=" + mac + "&" );
		uBuilder.append( "is_first_boot=" + isFirstBoot );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "system", "1" )
				.add( "version", version + "" )
				.add( "channel", channel )
				.add( "maker", maker )
				.add( "machine_code", machineCode )
				.add( "mobile_model", mobileModel )
				.add( "system_version", systemVersion )
				.add( "mac", mac )
				.add( "is_first_boot", isFirstBoot + "" )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void rankingList(int what, Context cxt,
																 HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getRankingList();
		HttpUtils.getHttp( cxt, what, url, listener );
	}

	public static void receiveReward(int what, Context cxt,
																	 HttpResultListener listener) {
		String uid = SpUtil.getUserId();
		String url = MyApplication.getHttpUrl().getReceiveReward();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "uid", uid );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void deleteDynamic(int what, Context cxt,
																	 HttpResultListener listener, String dynamicId) {
		String uid = SpUtil.getUserId();
		String url = MyApplication.getHttpUrl().getDelDynamic();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "id=" + dynamicId );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "uid", uid );
		builder1.add( "id", dynamicId );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void articleList(int what, Context cxt,
																 HttpResultListener listener, int type, int page) {
		String url = MyApplication.getHttpUrl().getArticleList();

		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "type", type + "" );
		builder1.add( "channel_id", MyApplication.getConfigModle().getChannelID() );
		builder1.add( "page", page + "" );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void articleListByGame(int what, Context cxt,
																			 HttpResultListener listener, int type, int page, String game_id) {
		String url = MyApplication.getHttpUrl().getArticleListByGame();
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "type", type + "" );
		builder1.add( "game_id", game_id );
		builder1.add( "channel_id", MyApplication.getConfigModle().getChannelID() );
		builder1.add( "page", page + "" );
		builder1.add( "uid", SpUtil.getUserId() );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void commentCounts(int what, Context cxt,
																	 HttpResultListener listener, String gameId) {
		String url = MyApplication.getHttpUrl().getCommentCount();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "channel=" + channel + "&" );
		uBuilder.append( "comment_type=" + "2&" );
		uBuilder.append( "dynamics_id=" + gameId );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "comment_type", "2" );
		builder1.add( "dynamics_id", gameId );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}


	public static void giftInfo(int what, Context cxt,
															HttpResultListener listener, String giftId) {
		String url = MyApplication.getHttpUrl().getPackInfo();
		String channel = MyApplication.getConfigModle().getChannelID();
		String username = SpUtil.getAccount();
		String code = TelephoneUtils.getImei( cxt );
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "pid=" + giftId + "&" );
		uBuilder.append( "username=" + username + "&" );
		uBuilder.append( "channel=" + channel + "&" );
		uBuilder.append( "machine_code=" + code + "&" );
		uBuilder.append( "terminal_type=" + "2&" );
		uBuilder.append( "system=" + "1" );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "pid", giftId );
		builder1.add( "username", username );
		builder1.add( "machine_code", code );
		builder1.add( "terminal_type", "2" );
		builder1.add( "system", "1" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void getPack(int what, Context context, HttpResultListener listener, String giftId) {
		String url = MyApplication.getHttpUrl().getGetPack();
		String uid = SpUtil.getAccount();
		String channelId = MyApplication.getConfigModle().getChannelID();
		String imei = TelephoneUtils.getImei( context );
		String ip = NetworkUtils.getLocalIpAddress();
		Map<String, String> map = new HashMap<String, String>();
		map.put( "username", uid );
		map.put( "ip", ip );
		map.put( "terminal_type", "2" );
		map.put( "pid", giftId );
		map.put( "device_id", imei );
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel_id", channelId )
				.add( "username", uid )
				.add( "device_id", imei )
				.add( "ip", ip )
				.add( "terminal_type", "2" )
				.add( "pid", giftId )
				.add( "sign", EncryptionUtils.getSingTure( map ) ).build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
		map = null;
	}

	public static void userRanking(int what, Context cxt,
																 HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getUserRanking();
		String uid = SpUtil.getUserId();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "type=" + "1" );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "uid", uid );
		builder1.add( "type", "1" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void rankNotice(int what, Context cxt,
																HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getRankNotice();
		FormBody.Builder builder1 = new FormBody.Builder();
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void replayComment(int what, Context cxt,
																	 HttpResultListener listener, int page) {
		String url = MyApplication.getHttpUrl().getReplayComment();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "comment_type=" + "2&" );
		uBuilder.append( "channel=" + channel + "&" );
		uBuilder.append( "page=" + page );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "uid", uid );
		builder1.add( "comment_type", "2" );
		builder1.add( "channel", channel );
		builder1.add( "page", page + "" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void userLoginApp(int what, Context cxt,
																	HttpResultListener listener, String appid) {
		String url = MyApplication.getHttpUrl().getUserLoginApp();
		String username = SpUtil.getAccount();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "username=" + username + "&" );
		uBuilder.append( "appid=" + appid );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "username", username );
		builder1.add( "appid", appid );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void reportData(Context context, int what, HttpResultListener listener, int type) {
		String conv_type = type == 0 ? "MOBILEAPP_ACTIVITE" : "MOBILEAPP_REGISTER";
		String url = MyApplication.getHttpUrl().getReportData();
		String deviceId = TelephoneUtils.getImei( context );
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "device_id=" + deviceId + "&" );
		uBuilder.append( "conv_type=" + conv_type + "&" );
		uBuilder.append( "app_type=" + "android" );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		url += "&device_id=" + deviceId + "&conv_type=" + conv_type + "&app_type=android" + "&sign=" + sign;
		HttpUtils.getHttp( context, what, url, listener );
	}


	public static void jrttReportData(Context context, int what, HttpResultListener listener, int type) {
		String url = MyApplication.getHttpUrl().getJrttReportData();
		String deviceId = TelephoneUtils.getImei( context );
		String mac = TelephoneUtils.getMac();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "device_id=" + deviceId + "&" );
		uBuilder.append( "mac=" + mac + "&" );
		uBuilder.append( "os=" + 0 + "&" );
		uBuilder.append( "source=" + "TD&" );
		uBuilder.append( "event_type=" + type );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		url += "&device_id=" + deviceId + "&mac=" + mac + "&os=0&source=TD&event_type=" + type + "&sign=" + sign;
		HttpUtils.getHttp( context, what, url, listener );
	}

	public static void gameIndex(Context context, int what, HttpResultListener listener, int page, int platform) {
		String url = MyApplication.getHttpUrl().getGameIndex();
		String channel = MyApplication.getConfigModle().getChannelID();
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel", channel )
				.add( "page", page + "" )
				.add( "system", "1" )
				.add( "platform", platform + "" )
				.build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
	}


	public static void newGameIndex(Context context, int what, HttpResultListener listener, int page, int platform) {
		String url = MyApplication.getHttpUrl().getNewIndex();
		String channel = MyApplication.getConfigModle().getChannelID();
		FormBody.Builder builder = new FormBody.Builder();
		builder.add( "channel", channel );
		builder.add( "page", page + "" );
		builder.add( "system", "1" );
		builder.add( "platform", platform + "" );
		builder.add( "uid", SpUtil.getUserId() );
		builder.add( "username", SpUtil.getAccount() );
		RequestBody requestBody = builder.build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
	}

	public static void boutiqueGame(Context context, int what, HttpResultListener listener, int page) {
		String url = MyApplication.getHttpUrl().getBoutiqueGame();
		String channel = MyApplication.getConfigModle().getChannelID();
		FormBody.Builder builder = new FormBody.Builder();
		builder.add( "channel", channel );
		builder.add( "page", page + "" );
		builder.add( "system", "1" );
		RequestBody requestBody = builder.build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
	}

	public static void taskCenter(Context context, int what, HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getTaskCenter();
		String uid = SpUtil.getUserId();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "channel=" + channel );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "uid", uid );
		builder1.add( "channel", channel );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}

	public static void getCommentNum(Context context, int what, HttpResultListener listener, int type, String dynamicsId) {
		String url = MyApplication.getHttpUrl().getCommentCount();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "channel=" + channel + "&" );
		uBuilder.append( "comment_type=" + type + "&" );
		uBuilder.append( "dynamics_id=" + dynamicsId );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "comment_type", type + "" );
		builder1.add( "dynamics_id", dynamicsId );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}

	public static void getCommitment(Context context, int what, HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getAppPromise();
		String channel = MyApplication.getConfigModle().getChannelID();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "channel=" + channel );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "channel", channel );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}

	public static void getNewGame(Context context, int what, HttpResultListener listener, int page, int platform) {
		String url = MyApplication.getHttpUrl().getGameType();
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel", MyApplication.getConfigModle().getChannelID() )
				.add( "page", page + "" )
				.add( "system", "1" )
				.add( "type", 1 + "" )
				.add( "platform", platform + "" )
				.build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
	}

	public static void getNewGame(Context context, int what, HttpResultListener listener, int page, int platform, int type) {
		String url = MyApplication.getHttpUrl().getGameType();
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel", MyApplication.getConfigModle().getChannelID() )
				.add( "page", page + "" )
				.add( "system", "1" )
				.add( "type", type + "" )
				.add( "platform", platform + "" )
				.build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
	}

	public static void newGetNewGame(Context context, int what, HttpResultListener listener, int page, int platform) {
		String url = MyApplication.getHttpUrl().getNewGameType();
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel", MyApplication.getConfigModle().getChannelID() )
				.add( "page", page + "" )
				.add( "system", "1" )
				.add( "type", 1 + "" )
				.add( "platform", platform + "" )
				.build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
	}

	public static void newGameType(Context context, int what, HttpResultListener listener, int page, int type, int platform) {
		String url = MyApplication.getHttpUrl().getNewGameType();
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel", MyApplication.getConfigModle().getChannelID() )
				.add( "page", page + "" )
				.add( "system", "1" )
				.add( "type", type + "" )
				.add( "platform", platform + "" )
				.build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
	}

	public static void newGameList(Context context, int what, HttpResultListener listener, int page, int type) {
		String url = MyApplication.getHttpUrl().getNewGameList();
		FormBody.Builder builder = new FormBody.Builder();
		builder.add( "channel", MyApplication.getConfigModle().getChannelID() );
		builder.add( "page", page + "" );
		builder.add( "system", "1" );
		builder.add( "type", type + "" );
		if (BeanUtils.isLogin()) {
			String uid = SpUtil.getUserId();
			builder.add( "uid", uid );
		}
		RequestBody requestBody = builder.build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
	}

	public static void reserveSuccess(Context context, int what, HttpResultListener listener, int gameId) {
		String url = MyApplication.getHttpUrl().getReserveSuccess();
		String uid = SpUtil.getUserId();
		FormBody.Builder builder = new FormBody.Builder();
		builder.add( "uid", uid );
		builder.add( "gid", gameId + "" );
		RequestBody requestBody = builder.build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
	}

	public static void newGameReserve(Context context, int what, HttpResultListener listener, int gameId) {
		String url = MyApplication.getHttpUrl().getNewgameReserve();
		String uid = SpUtil.getUserId();
		FormBody.Builder builder = new FormBody.Builder();
		builder.add( "uid", uid );
		builder.add( "gid", gameId + "" );
		RequestBody requestBody = builder.build();
		HttpUtils.postHttp( context, what, url, requestBody, listener );
	}

	public static void sendMessage(Context context, int what, HttpResultListener listener, String mobile, int type) {
		String url = MyApplication.getHttpUrl().getSendMessage();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "mobile=" + mobile + "&" );
		uBuilder.append( "type=" + type + "&" );
		uBuilder.append( "client=" + "1" );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder1 = new FormBody.Builder();
		builder1.add( "mobile", mobile );
		builder1.add( "type", type + "" );
		builder1.add( "client", "1" );
		builder1.add( "sign", sign );
		RequestBody body = builder1.build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}


	public static void tradingRegister(int what, Context cxt,
																		 HttpResultListener listener, String code, String mobile, String password) {
		String maker = TelephoneUtils.getDeviceBrand();
		String mobileModel = TelephoneUtils.getSystemModel();
		String machineCode = TelephoneUtils.getImei( cxt );
		String systemVersion = TelephoneUtils.getSystemVersion();
		String url = MyApplication.getHttpUrl().getTradingRegister();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "code=" + code + "&" );
		uBuilder.append( "mobile=" + mobile + "&" );
		uBuilder.append( "password=" + password + "&" );
		uBuilder.append( "system=1&" );
		uBuilder.append( "maker=" + maker + "&" );
		uBuilder.append( "mobile_model=" + mobileModel + "&" );
		uBuilder.append( "machine_code=" + machineCode + "&" );
		uBuilder.append( "system_version=" + systemVersion + "&" );
		uBuilder.append( "client=1" );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "code", code )
				.add( "mobile", mobile )
				.add( "password", password )
				.add( "maker", maker )
				.add( "machine_code", machineCode )
				.add( "mobile_model", mobileModel )
				.add( "system_version", systemVersion )
				.add( "system", "1" )
				.add( "client", "1" )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void tradingLogin(int what, Context cxt,
																	HttpResultListener listener, String password, String mobile) {
		String machineCode = TelephoneUtils.getImei( cxt );
		String url = MyApplication.getHttpUrl().getTradingLogin();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "username=" + mobile + "&" );
		uBuilder.append( "password=" + password + "&" );
		uBuilder.append( "system=1&" );
		uBuilder.append( "machine_code=" + machineCode + "&" );
		uBuilder.append( "client=1" );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "username", mobile )
				.add( "password", password )
				.add( "machine_code", machineCode )
				.add( "system", "1" )
				.add( "client", "1" )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void tradingForgetPwd(int what, Context cxt,
																			HttpResultListener listener, String password, String mobile, String code) {
		String url = MyApplication.getHttpUrl().getTradingForgetPassword();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "mobile=" + mobile + "&" );
		uBuilder.append( "code=" + code + "&" );
		uBuilder.append( "password=" + password );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "mobile", mobile )
				.add( "code", code )
				.add( "password", password )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void tradingResetPwd(int what, Context cxt,
																		 HttpResultListener listener, String pwd, String newpwd) {
		String url = MyApplication.getHttpUrl().getTradingModifyPassword();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "password=" + pwd + "&" );
		uBuilder.append( "newpassword=" + newpwd );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", uid )
				.add( "password", pwd )
				.add( "newpassword", newpwd )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void productList(int what, Context cxt,
																 HttpResultListener listener, String gameName, int order, int orderType, int page, int platform) {
		String url = MyApplication.getHttpUrl().getProductList();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "game_name=" + gameName + "&" );
		uBuilder.append( "system=" + platform + "&" );
		uBuilder.append( "order=" + order + "&" );
		uBuilder.append( "order_type=" + orderType + "&" );
		uBuilder.append( "page=" + page );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "game_name", gameName )
				.add( "system", platform + "" )
				.add( "order", order + "" )
				.add( "order_type", orderType + "" )
				.add( "page", page + "" )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void sdkUserList(int what, Context cxt, HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getSdkuserList();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", uid )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void unBindSdkUser(int what, Context cxt, HttpResultListener listener, String userName) {
		String url = MyApplication.getHttpUrl().getUnBindSdkUser();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "sdk_username=" + userName );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", uid )
				.add( "sdk_username", userName )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void bindSdkUser(int what, Context cxt, HttpResultListener listener, String userName, String password) {
		String url = MyApplication.getHttpUrl().getBindSdkUser();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "sdk_username=" + userName + "&" );
		uBuilder.append( "sdk_password=" + password );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", uid )
				.add( "sdk_username", userName )
				.add( "sdk_password", password )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void uploadPortrait(int what, Context cxt,
																		HttpResultListener listener, String path) {
		MediaType MEDIA_TYPE_JPEG = MediaType.parse( "image/png" );
		String url = MyApplication.getHttpUrl().getUploadPortrait();
		MultipartBody.Builder builder = new MultipartBody.Builder()
				.setType( MultipartBody.FORM );
		File file = new File( path );
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "id=" + SpUtil.getUserId() + "&" );
		uBuilder.append( "img=" + file.getName() );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		builder.addFormDataPart( "img", file.getName(),
				RequestBody.create( MEDIA_TYPE_JPEG, file ) );
		builder.addFormDataPart( "id", SpUtil.getUserId() );
		builder.addFormDataPart( "sign", sign );
		HttpUtils.uploadImg( cxt, what, url, builder, listener );
	}

	public static void sellProduct(int what, Context cxt, HttpResultListener listener, ProductModel productModel) {
		MediaType MEDIA_TYPE_JPEG = MediaType.parse( "image/png" );
		String url = MyApplication.getHttpUrl().getSellProduct();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "appid=" + productModel.getAppid() + "&" );
		uBuilder.append( "title=" + productModel.getTitle() + "&" );
		uBuilder.append( "sdk_username=" + productModel.getSdkUserName() + "&" );
		uBuilder.append( "price=" + productModel.getPrice() + "&" );
		uBuilder.append( "desc=" + productModel.getDesc() + "&" );
		uBuilder.append( "system=" + productModel.getSystem() + "&" );
		uBuilder.append( "server_name=" + productModel.getServer() + "&" );
		uBuilder.append( "end_time=" + productModel.getEndTime() );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String finalSign = sign;
		new AsyncTask<ProductModel, Mapper.Null, MultipartBody.Builder>() {
			@Override
			protected MultipartBody.Builder doInBackground(ProductModel... productModels) {
				ProductModel model = productModels[0];
				MultipartBody.Builder builder = new MultipartBody.Builder()
						.setType( MultipartBody.FORM );
				for (String path : model.getImgs()) {
					File file = new File( path );
					file = BitmapUtils.compressBmpToFile( BitmapUtils.fileToBitmap( path ),
							Configuration.cachepath, file.getName() );
					builder.addFormDataPart( "imgs[]", file.getName(),
							RequestBody.create( MEDIA_TYPE_JPEG, file ) );
				}
				for (String path : model.getTradeImgs()) {
					File file = new File( path );
					file = BitmapUtils.compressBmpToFile( BitmapUtils.fileToBitmap( path ),
							Configuration.cachepath, file.getName() );
					builder.addFormDataPart( "trade_imgs[]", file.getName(),
							RequestBody.create( MEDIA_TYPE_JPEG, file ) );
				}
				builder.addFormDataPart( "uid", uid )
						.addFormDataPart( "appid", model.getAppid() )
						.addFormDataPart( "title", model.getTitle() )
						.addFormDataPart( "sdk_username", model.getSdkUserName() )
						.addFormDataPart( "price", model.getPrice() )
						.addFormDataPart( "desc", model.getDesc() )
						.addFormDataPart( "system", model.getSystem() + "" )
						.addFormDataPart( "server_name", model.getServer() + "" )
						.addFormDataPart( "end_time", model.getEndTime() + "" )
						.addFormDataPart( "sign", finalSign )
						.build();
				return builder;
			}

			@Override
			protected void onPostExecute(MultipartBody.Builder builder) {
				HttpUtils.uploadImg( cxt, what, url, builder, listener );
			}
		}.execute( productModel );
	}


	public static void productInfo(int what, Context cxt, HttpResultListener listener, String productId, int type) {
		String url = MyApplication.getHttpUrl().getProductInfo();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "product_id=" + productId + "&" );
		uBuilder.append( "system=1&" );
		uBuilder.append( "uid=" + ((type == 0) ? "0" : uid) );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormBody.Builder builder = new FormBody.Builder();
		builder.add( "product_id", productId );
		builder.add( "system", "1" );
		builder.add( "uid", (type == 0) ? "0" : uid );
		builder.add( "sign", sign );
		RequestBody body = builder.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void tradingStartPayment(int what, Context cxt, HttpResultListener listener, String productId, int type) {
		String url = MyApplication.getHttpUrl().getTradingStartPayment();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "proid=" + productId + "&" );
		uBuilder.append( "buy_id=" + uid + "&" );
		uBuilder.append( "type=" + type );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "proid", productId )
				.add( "buy_id", uid )
				.add( "type", type + "" )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void tradingCancelPayment(int what, Context cxt, HttpResultListener listener, String id) {
		String url = MyApplication.getHttpUrl().getTradingCancelPayment();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "id=" + id );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "id", id )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void editUser(int what, Context cxt, HttpResultListener listener, String alipay, String realName) {
		String url = MyApplication.getHttpUrl().getEditUser();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "qq=&" );
		uBuilder.append( "alipay_account=" + alipay );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", uid )
				.add( "qq", "" )
				.add( "icon_url", "" )
				.add( "alipay_account", alipay )
				.add( "real_name", realName )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void customer(int what, Context cxt, HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getCustomer();
		HttpUtils.getHttp( cxt, what, url, listener );
	}

	public static void buyerRecord(int what, Context cxt, HttpResultListener listener, int type) {
		String url = MyApplication.getHttpUrl().getBuyerRecord();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "type=" + type );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", uid )
				.add( "type", type + "" )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void getProductByUser(int what, Context cxt, HttpResultListener listener, int status, int page) {
		String url = MyApplication.getHttpUrl().getGetProductByUser();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "status=" + status + "&" );
		uBuilder.append( "page=" + page );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", uid )
				.add( "status", status + "" )
				.add( "page", page + "" )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void sellerRecord(int what, Context cxt, HttpResultListener listener, int type) {
		String url = MyApplication.getHttpUrl().getSellerRecord();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "type=" + type );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", uid )
				.add( "type", type + "" )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void deleteProduct(int what, Context cxt, HttpResultListener listener, String productId) {
		String url = MyApplication.getHttpUrl().getDeleteProduct();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "product_id=" + productId );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", uid )
				.add( "product_id", productId )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void withdrawProduct(int what, Context cxt, HttpResultListener listener, String productId) {
		String url = MyApplication.getHttpUrl().getWithdrawProduct();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "product_id=" + productId );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", uid )
				.add( "product_id", productId )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
	}

	public static void applyOnsale(int what, Context cxt, HttpResultListener listener, ProductModel productModel) {
		String url = MyApplication.getHttpUrl().getApplyOnsale();
		String uid = SpUtil.getTradingUid();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "uid=" + uid + "&" );
		uBuilder.append( "product_id=" + productModel.getProductId() + "&" );
		uBuilder.append( "title=" + productModel.getTitle() + "&" );
		uBuilder.append( "price=" + productModel.getPrice() + "&" );
		uBuilder.append( "desc=" + productModel.getDesc() + "&" );
		uBuilder.append( "system=" + productModel.getSystem() + "&" );
		uBuilder.append( "server_name=" + productModel.getServer() + "&" );
		uBuilder.append( "end_time=" + productModel.getEndTime() );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String finalSign = sign;
		List<String> imgs = productModel.getImgs();
		if (imgs != null && !imgs.isEmpty()) {
			MediaType MEDIA_TYPE_JPEG = MediaType.parse( "image/png" );
			new AsyncTask<ProductModel, Mapper.Null, MultipartBody.Builder>() {
				@Override
				protected MultipartBody.Builder doInBackground(ProductModel... productModels) {
					ProductModel model = productModels[0];
					MultipartBody.Builder builder = new MultipartBody.Builder()
							.setType( MultipartBody.FORM );
					for (String path : model.getImgs()) {
						File file = new File( path );
						file = BitmapUtils.compressBmpToFile( BitmapUtils.fileToBitmap( path ),
								Configuration.cachepath, file.getName() );
						builder.addFormDataPart( "imgs[]", file.getName(),
								RequestBody.create( MEDIA_TYPE_JPEG, file ) );
					}
					for (String path : model.getTradeImgs()) {
						File file = new File( path );
						file = BitmapUtils.compressBmpToFile( BitmapUtils.fileToBitmap( path ),
								Configuration.cachepath, file.getName() );
						builder.addFormDataPart( "trade_imgs[]", file.getName(),
								RequestBody.create( MEDIA_TYPE_JPEG, file ) );
					}
					builder.addFormDataPart( "uid", uid )
							.addFormDataPart( "product_id", model.getProductId() )
							.addFormDataPart( "title", model.getTitle() )
							.addFormDataPart( "price", model.getPrice() )
							.addFormDataPart( "desc", model.getDesc() )
							.addFormDataPart( "system", model.getSystem() + "" )
							.addFormDataPart( "server_name", model.getServer() + "" )
							.addFormDataPart( "end_time", model.getEndTime() + "" )
							.addFormDataPart( "sign", finalSign )
							.build();
					return builder;
				}

				@Override
				protected void onPostExecute(MultipartBody.Builder builder) {
					HttpUtils.uploadImg( cxt, what, url, builder, listener );
				}
			}.execute( productModel );
		} else {
			new AsyncTask<ProductModel, Mapper.Null, RequestBody>() {
				@Override
				protected RequestBody doInBackground(ProductModel... productModels) {
					FormBody.Builder builder = new FormBody.Builder();
					RequestBody body = builder.add( "uid", uid )
							.add( "product_id", productModel.getProductId() )
							.add( "title", productModel.getTitle() )
							.add( "price", productModel.getPrice() )
							.add( "desc", productModel.getDesc() )
							.add( "system", productModel.getSystem() + "" )
							.add( "server_name", productModel.getServer() )
							.add( "end_time", productModel.getEndTime() )
							.add( "sign", finalSign )
							.build();
					return body;
				}

				@Override
				protected void onPostExecute(RequestBody body) {
					HttpUtils.postHttp( cxt, what, url, body, listener );
				}
			}.execute( productModel );
		}
	}


	public static void tradeNotes(int what, Context cxt, HttpResultListener listener) {
		String url = MyApplication.getHttpUrl().getTradeNotes();
		HttpUtils.getHttp( cxt, what, url, listener );
	}


	public static void exclusiveList(int what, Context cxt, HttpResultListener listener, int platform) {
		String url = MyApplication.getHttpUrl().getExclusiveList();
		UIStringBuilder uBuilder = new UIStringBuilder();
		uBuilder.append( "platform=" + platform );
		uBuilder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( uBuilder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "platform", platform + "" )
				.add( "sign", sign )
				.build();
		HttpUtils.postHttp( cxt, what, url, body, listener );
		MyLog.d( "url is " + url + "&platform=" + platform + "&sign=" + sign );
	}

	public static void sdkLogin(int what, Context context, HttpResultListener listener, String username,
															String pwd, String appId, String appKey) {
		String url = HttpUrls.getLogin();
		String channel = MyApplication.getConfigModle().getChannelID();
		String deviceID = TelephoneUtils.getImei( context );
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "username=" + username + "&" );
		builder.append( "type=1&" );
		builder.append( "password=" + pwd + "&" );
		builder.append( "appid=" + appId + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "system=1&" );
		builder.append( "machine_code=" + deviceID );
		builder.append( appKey );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "username", TextUtils.isEmpty( username ) ? "" : username )
				.add( "password", TextUtils.isEmpty( pwd ) ? "" : pwd )
				.add( "appid", TextUtils.isEmpty( appId ) ? "" : appId )
				.add( "system", "1" )
				.add( "type", "1" )
				.add( "random", channel + "-" + Constant.getRandom() )
				.add( "channel", TextUtils.isEmpty( channel ) ? "" : channel )
				.add( "machine_code",
						TextUtils.isEmpty( deviceID ) ? "" : deviceID )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();

		Log.i( "GameBox", "url is " + url + "&" + builder.toString() + "&sign=" + sign );
		HttpUtils.postHttp( context, what, url, body, listener );
	}

	public static void articleLike(int what, Context context, HttpResultListener listener, String articleId, int type) {
		String url = MyApplication.getHttpUrl().getArticleLike();
		String channel = MyApplication.getConfigModle().getChannelID();
		String uId = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uId + "&" );
		builder.append( "channel=" + channel + "&" );
		builder.append( "article_id=" + articleId + "&" );
		builder.append( "type=" + type );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "uid", TextUtils.isEmpty( uId ) ? "" : uId )
				.add( "channel", TextUtils.isEmpty( channel ) ? "" : channel )
				.add( "article_id", TextUtils.isEmpty( articleId ) ? "" : articleId )
				.add( "type", type + "" )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}

	public static void doReport(int what, Context context, HttpResultListener listener, String actions) {
		String url = MyApplication.getHttpUrl().getDoReport();
		String channel = MyApplication.getConfigModle().getChannelID();
		String machineCode = TelephoneUtils.getImei( context );
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "channel=" + channel + "&" );
		builder.append( "machine_code=" + machineCode + "&" );
		builder.append( "actions=" + actions );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "channel", TextUtils.isEmpty( channel ) ? "" : channel )
				.add( "machine_code", machineCode )
				.add( "actions", actions )
				.add( "sign", TextUtils.isEmpty( sign ) ? "" : sign ).build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}

	public static void consultList(int what, Context context, HttpResultListener listener, int page, String appid) {
		String url = MyApplication.getHttpUrl().getConsultList();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "appid=" + appid + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "appid", appid )
				.add( "page", page + "" )
				.add( "uid", TextUtils.isEmpty( uid ) ? "" : uid )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( context, what, url, body, listener );
		Log.i( "consultList", "url is " + url + "&" + builder.toString() + "&uid=" + uid + "&sign=" + sign );
	}

	public static void putQuestion(int what, Context context, HttpResultListener listener, String money, String content, String gameId) {
		String url = MyApplication.getHttpUrl().getPutQuestion();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "appid=" + gameId + "&" );
		builder.append( "content=" + content + "&" );
		builder.append( "money=" + money );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "content", content )
				.add( "appid", gameId )
				.add( "money", money + "" )
				.add( "uid", uid )
				.add( "sign", sign ).build();
		String text = "url is " + url + "&" + builder.toString() + "&sign=" + sign;
		HttpUtils.postHttp( context, what, url, body, listener );
	}

	public static void consultInfo(int what, Context context, HttpResultListener listener, String consultId, int page) {
		String url = MyApplication.getHttpUrl().getConsultInfo();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "consult_id=" + consultId + "&" );
		builder.append( "uid=" + uid + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "consult_id", consultId )
				.add( "uid", uid )
				.add( "page", page + "" )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}


	public static void doAnswer(int what, Context context, HttpResultListener listener, String consultId, String content) {
		String url = MyApplication.getHttpUrl().getDoAnswer();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "consult_id=" + consultId + "&" );
		builder.append( "uid=" + uid + "&" );
		builder.append( "content=" + content );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "consult_id", consultId )
				.add( "uid", uid )
				.add( "content", content )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}

	public static void doReward(int what, Context context, HttpResultListener listener, String consultId, String answerId) {
		String url = MyApplication.getHttpUrl().getDoReward();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "consult_id=" + consultId + "&" );
		builder.append( "id=" + answerId + "&" );
		builder.append( "uid=" + uid );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "consult_id", consultId )
				.add( "uid", uid )
				.add( "id", answerId )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}


	public static void myQuestions(int what, Context context, HttpResultListener listener, int page) {
		String url = MyApplication.getHttpUrl().getMyQuestions();
		String uid = SpUtil.getUserId();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "uid=" + uid + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "page", page + "" )
				.add( "uid", uid )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}


	public static void answerGame(int what, Context context, HttpResultListener listener, int page) {
		String url = MyApplication.getHttpUrl().getAnswerGame();
		String username = SpUtil.getAccount();
		UIStringBuilder builder = new UIStringBuilder();
		builder.append( "username=" + username + "&" );
		builder.append( "page=" + page );
		builder.append( Constant.APPKEY );
		String sign = "";
		try {
			sign = EncryptionUtils.getMD5( builder.toString() ).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestBody body = new FormBody.Builder()
				.add( "page", page + "" )
				.add( "username", username )
				.add( "sign", sign ).build();
		HttpUtils.postHttp( context, what, url, body, listener );
	}
}
