package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 顶部有进度条的webview ClassName: ProgressWebView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017-4-13 下午4:58:51 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {
	private ProgressBar progressbar;

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		progressbar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				3, 0, 0));
		addView(progressbar);
		setWebChromeClient(new WebChromeClient());
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(GONE);
			} else {
				if (progressbar.getVisibility() == GONE)
					progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
