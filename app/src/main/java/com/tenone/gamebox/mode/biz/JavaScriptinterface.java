
package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaScriptinterface {
	Context mContext;

	public JavaScriptinterface(Context c) {
		mContext = c;
	}

	@JavascriptInterface
	public void showToast(String ssss) {
		Toast.makeText(mContext, ssss, Toast.LENGTH_LONG).show();
	}
}
