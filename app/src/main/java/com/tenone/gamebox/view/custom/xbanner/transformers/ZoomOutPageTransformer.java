/** 
 * Project Name:GameBox 
 * File Name:ZoomOutPageTransformer.java 
 * Package Name:com.tenone.gamebox.view.custom.xbanner.transformers 
 * Date:2017-4-11����10:52:17 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom.xbanner.transformers;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ClassName:ZoomOutPageTransformer <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-11 ����10:52:17 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
	private static final float MAX_SCALE = 1.2f;
	private static final float MIN_SCALE = 1.0f;// 0.85f

	@Override
	public void transformPage(View view, float position) {
		// setScaleYֻ֧��api11����
		if (position < -1) {
			view.setScaleX(MIN_SCALE);
			view.setScaleY(MIN_SCALE);
		} else if (position <= 1) // aҳ������bҳ �� aҳ�� 0.0 -1 ��bҳ��1 ~ 0.0
		{ // [-1,1]
			// Log.e("TAG", view + " , " + position + "");
			float scaleFactor = MIN_SCALE + (1 - Math.abs(position))
					* (MAX_SCALE - MIN_SCALE);
			view.setScaleX(scaleFactor);
			// ÿ�λ��������΢С���ƶ�Ŀ����Ϊ�˷�ֹ�����ǵ�ĳЩ�ֻ��ϳ������ߵ�ҳ��Ϊ��ʾ�����
			if (position > 0) {
				view.setTranslationX(-scaleFactor * 2);
			} else if (position < 0) {
				view.setTranslationX(scaleFactor * 2);
			}
			view.setScaleY(scaleFactor);

		} else { // (1,+Infinity]
			view.setScaleX(MIN_SCALE);
			view.setScaleY(MIN_SCALE);

		}
	}
}
