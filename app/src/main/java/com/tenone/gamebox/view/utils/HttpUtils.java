package com.tenone.gamebox.view.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求工具类
 */
public class HttpUtils {

	private static OkHttpClient client;


	/* 请求次数 */
	private static int loadTimes;
	/* 最多请求多少次 */
	private static int maxLoadTimes = 3;

	protected final static int SUCCESS = 0x012;
	protected final static int ERROR = 0x013;
	/* 到主线程 */
	private static UIHandler mHandler;

	public static OkHttpClient getClient(Context cxt) {
		if (client == null)
			client = new OkHttpClient.Builder()
					.connectTimeout( 5, TimeUnit.SECONDS )
					.readTimeout( 5, TimeUnit.SECONDS ).build();
		return client;
	}

	/**
	 * get方式请求
	 */
	public static void getHttp(final Context cxt, final int what, String url,
														 final HttpResultListener listener) {
		final OkHttpClient client = getClient( cxt );
		Request.Builder requestBuilder = new Request.Builder().url( url );
		requestBuilder.addHeader( "Cache-Control", "max-age=0" );
		requestBuilder
				.addHeader( "Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" );
		requestBuilder.addHeader( "Accept-Charset",
				"utf-8, iso-8859-1, utf-16, *;q=0.7" );
		requestBuilder.addHeader( "Accept-Language", "zh-CN, en-US" );
		requestBuilder.addHeader( "X-Requested-With", "com.android.browser" );
		loadTimes = 0;
		Request request = requestBuilder.build();
		client.newCall( request ).enqueue( new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				if (SocketTimeoutException.class.equals( e.getCause() ))// 如果超时并未超过指定次数，则重新连接
				{
					if (loadTimes < maxLoadTimes) {
						loadTimes++;
						call.enqueue( this );
					} else {
						if (listener != null) {
							mHandler = new UIHandler( listener );
							Message message = new Message();
							message.what = ERROR;
							message.arg1 = what;
							message.obj = cxt.getResources().getString( R.string.timeout );
							mHandler.sendMessage( message );
						} else {
							call.cancel();
						}
						e.printStackTrace();
					}
				} else {
					if (listener != null) {
						mHandler = new UIHandler( listener );
						Message message = new Message();
						message.what = ERROR;
						message.arg1 = what;
						message.obj = cxt.getResources().getString( R.string.noNetwork );
						if (mHandler != null)
							mHandler.sendMessage( message );
					}else {
						call.cancel();
					}
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.code() == 200) {
					String str = response.body().string();
					ResultItem resultItem = BeanUtils.getResultItemByJson( str );
					if (listener != null) {
						mHandler = new UIHandler( listener );
						Message message = new Message();
						message.what = SUCCESS;
						message.arg1 = what;
						message.obj = resultItem;
						mHandler.sendMessage( message );
					}else {
						call.cancel();
					}
				}
				if (response.code() == 404) {
					if (listener != null) {
						mHandler = new UIHandler( listener );
						Message message = new Message();
						message.what = ERROR;
						message.arg1 = what;
						message.obj = cxt.getResources().getString( R.string.notfound );
						mHandler.sendMessage( message );
					}else {
						call.cancel();
					}
				}
			}
		} );
	}

	/**
	 * post方式获取数据
	 */
	public static void postHttp(final Context cxt, final int what, String url,
															RequestBody requestBody, final HttpResultListener listener) {
		try {
			final OkHttpClient client = getClient( cxt );
			Request.Builder requestBuilder = new Request.Builder().url( url )
					.post( requestBody );
			requestBuilder.addHeader( "Cache-Control", "max-age=0" );
			requestBuilder
					.addHeader( "Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" );
			requestBuilder.addHeader( "Accept-Charset",
					"utf-8, iso-8859-1, utf-16, *;q=0.7" );
			requestBuilder.addHeader( "Accept-Language", "zh-CN, en-US" );
			requestBuilder.addHeader( "X-Requested-With", "com.android.browser" );
			client.newCall( requestBuilder.build() ).enqueue( new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					if (SocketTimeoutException.class.equals( e.getCause() ))// 如果超时并未超过指定次数，则重新连接
					{
						if (loadTimes < maxLoadTimes) {
							loadTimes++;
							call.enqueue( this );
						} else {
							if (listener != null) {
								mHandler = new UIHandler( listener );
								Message message = new Message();
								message.what = ERROR;
								message.arg1 = what;
								message.obj = cxt.getResources().getString( R.string.timeout );
								mHandler.sendMessage( message );
							}else {
								call.cancel();
							}
							e.printStackTrace();
						}
					} else {
						if (listener != null) {
							mHandler = new UIHandler( listener );
							Message message = new Message();
							message.what = ERROR;
							message.arg1 = what;
							message.obj = cxt.getResources().getString( R.string.noNetwork );
							if (mHandler != null)
								mHandler.sendMessage( message );
						}else {
							call.cancel();
						}
					}
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if (response.code() == 200) {
						String str = response.body().string();
						ResultItem resultItem = BeanUtils
								.getResultItemByJson( str );
						if (listener != null) {
							mHandler = new UIHandler( listener );
							Message message = mHandler.obtainMessage();
							message.what = SUCCESS;
							message.arg1 = what;
							message.obj = resultItem;
							mHandler.sendMessage( message );
						}else {
							call.cancel();
						}
					}
					if (response.code() == 404) {
						if (listener != null) {
							if (mHandler == null) {
								mHandler = new UIHandler( listener );
							}
							Message message = mHandler.obtainMessage();
							message.what = ERROR;
							message.arg1 = what;
							message.obj = cxt.getResources().getString( R.string.notfound );
							if (mHandler != null)
								mHandler.sendMessage( message );
						}else {
							call.cancel();
						}
					}
				}
			} );
		} catch (Exception e2) {
		}
	}

	/**
	 * post方式获取数据
	 */
	public static void hasLoadingPostHttp(final Context cxt, final int what,
																				String url, RequestBody requestBody,
																				final HttpResultListener listener) {
		try {
			final OkHttpClient client = getClient( cxt );
			Request.Builder requestBuilder = new Request.Builder().url( url )
					.post( requestBody );
			requestBuilder.addHeader( "Cache-Control", "max-age=0" );
			requestBuilder
					.addHeader( "Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" );
			requestBuilder.addHeader( "Accept-Charset",
					"utf-8, iso-8859-1, utf-16, *;q=0.7" );
			requestBuilder.addHeader( "Accept-Language", "zh-CN, en-US" );
			requestBuilder.addHeader( "X-Requested-With", "com.android.browser" );

			client.newCall( requestBuilder.build() ).enqueue( new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					try {
						if (e != null
								&& e.getCause().equals(
								SocketTimeoutException.class )
								&& loadTimes < maxLoadTimes)// 如果超时并未超过指定次数，则重新连接
						{
							loadTimes++;
							if (listener != null) {
								mHandler = new UIHandler( listener );
								Message message = new Message();
								message.what = ERROR;
								message.arg1 = what;
								message.obj = cxt.getResources().getString( R.string.agin );
								mHandler.sendMessage( message );
							}else {
								call.cancel();
							}
							call.enqueue( this );
						} else {
							if (listener != null) {
								mHandler = new UIHandler( listener );
								Message message = new Message();
								message.what = ERROR;
								message.arg1 = what;
								message.obj = cxt.getResources().getString( R.string.timeout );
								mHandler.sendMessage( message );
							}else {
								call.cancel();
							}
						}
					} catch (NullPointerException e2) {
					}
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if (response.code() == 200) {
						String str = response.body().string();
						ResultItem resultItem = BeanUtils
								.getResultItemByJson( str );
						if (listener != null) {
							mHandler = new UIHandler( listener );
							Message message = mHandler.obtainMessage();
							message.what = SUCCESS;
							message.arg1 = what;
							message.obj = resultItem;
							mHandler.sendMessage( message );
						}else {
							call.cancel();
						}
					}
					if (response.code() == 404) {
						if (listener != null) {
							if (mHandler == null) {
								mHandler = new UIHandler( listener );
							}
							Message message = mHandler.obtainMessage();
							message.what = ERROR;
							message.arg1 = what;
							message.obj = cxt.getResources().getString( R.string.notfound );
							mHandler.sendMessage( message );
						}else {
							call.cancel();
						}
					}
				}


			} );
		} catch (Exception e2) {
		}
	}

	public static void uploadImg(final Context cxt, final int what, String url,
															 MultipartBody.Builder builder, final HttpResultListener listener) {
		final OkHttpClient client = getClient( cxt );
		MultipartBody requestBody = builder.build();
		// 构建请求
		Request request = new Request.Builder().url( url )// 地址
				.post( requestBody )// 添加请求体
				.build();
		client.newCall( request ).enqueue( new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				try {
					if (e != null
							&& e.getCause()
							.equals( SocketTimeoutException.class )
							&& loadTimes < maxLoadTimes)// 如果超时并未超过指定次数，则重新连接
					{
						loadTimes++;
						call.enqueue( this );
					} else {
						if (listener != null) {
							mHandler = new UIHandler( listener );
							Message message = new Message();
							message.what = ERROR;
							message.arg1 = what;
							message.obj = cxt.getResources().getString( R.string.timeout );
							mHandler.sendMessage( message );
						}
					}
				} catch (NullPointerException e2) {
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.code() == 200) {
					String str = response.body().string();
					ResultItem resultItem = BeanUtils.getResultItemByJson( str );
					if (listener != null) {
						mHandler = new UIHandler( listener );
						Message message = mHandler.obtainMessage();
						message.what = SUCCESS;
						message.arg1 = what;
						message.obj = resultItem;
						mHandler.sendMessage( message );
					}
				}
				if (response.code() == 404) {
					if (listener != null) {
						if (mHandler == null) {
							mHandler = new UIHandler( listener );
						}
						Message message = mHandler.obtainMessage();
						message.what = ERROR;
						message.arg1 = what;
						message.obj = cxt.getResources().getString( R.string.notfound );
						mHandler.sendMessage( message );
					}
				}
			}

		} );
	}

	@SuppressLint("HandlerLeak")
	static class UIHandler extends Handler {
		@SuppressWarnings("rawtypes")
		private WeakReference mWeakReference;
		HttpResultListener listener;

		public UIHandler(HttpResultListener listener) {
			super( Looper.getMainLooper() );
			mWeakReference = new WeakReference<HttpResultListener>( listener );
		}

		@Override
		public void handleMessage(Message msg) {
			listener = ((HttpResultListener) mWeakReference.get());
			switch (msg.what) {
				case SUCCESS:
					if (listener != null) {
						listener.onSuccess( msg.arg1, (ResultItem) msg.obj );
					}
					break;
				case ERROR:
					if (listener != null) {
						listener.onError( msg.arg1, msg.obj.toString() );
					}
					break;
				default:
					super.handleMessage( msg );
					break;
			}
			mHandler = null;
			listener = null;
		}
	}

	public static void downFile(Context cxt, String url,
															final Handler handler) {
		final OkHttpClient client = getClient( cxt );
		// 构建请求
		Request request = new Request.Builder().get().url( url )// 地址
				.build();
		client.newCall( request ).enqueue( new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Message message = handler.obtainMessage();
				if (response.isSuccessful()) {
					message.what = 1;
					message.obj = response.body().bytes();
					handler.sendMessage( message );
				} else {
					handler.sendEmptyMessage( 0 );
				}
			}


		} );
	}

	/*AsyncHttpClient 方式请求网络*/
	private static AsyncHttpClient sClient = null;
	private static PersistentCookieStore myCookieStore;

	private static AsyncHttpClient getClient(Context context, boolean mobile) {
		if (sClient == null) {
			sClient = new AsyncHttpClient(); // 实例话对象
			sClient.setEnableRedirects( false );
			sClient.setCookieStore( new PersistentCookieStore( context ) );
			sClient.addHeader( "Cache-Control", "max-age=0" );
			sClient.addHeader( "Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" );
			sClient.addHeader( "Accept-Charset",
					"utf-8, iso-8859-1, utf-16, *;q=0.7" );
			sClient.addHeader( "Accept-Language", "zh-CN, en-US" );
			sClient.addHeader( "X-Requested-With", "com.android.browser" );
			sClient.setTimeout( 20000000 );
		}
		if (mobile)
			sClient.setUserAgent( "Mozilla/5.0 (Linux; U; Android 4.2.1; en-us; M040 Build/JOP40D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30" );
		else
			sClient.setUserAgent( "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko" );
		return sClient;
	}

	private static AsyncHttpClient getAsyncHttpClient(Context context) {
		return getClient( context, true );
	}


	public static void requestOnceWithURLString(final Context cxt, String url,
																							final com.loopj.android.http.RequestParams requestParams,
																							final HttpResultListener callBack, final int successCode) {
		if (myCookieStore == null) {
			myCookieStore = new PersistentCookieStore( cxt );
		}
		AsyncHttpClient client = getAsyncHttpClient( cxt );

		client.post( url, requestParams, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (statusCode == 200) {
					String str = new String( responseBody );
					ResultItem resultItem = BeanUtils.getResultItemByJson( str );
					callBack.onSuccess( successCode, resultItem );
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				if (responseBody != null) {
					String str = new String( responseBody );
					if (statusCode == 404) {
						str = "\u8def\u5f84\u9519\u8bef";
					} else if (statusCode == 500) {
						str = "\u670d\u52a1\u5668\u5f02\u5e38";
					}
					callBack.onError( successCode, str );
				}
			}
		} );
	}


}
