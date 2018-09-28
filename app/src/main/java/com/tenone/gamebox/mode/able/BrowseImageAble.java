
package com.tenone.gamebox.mode.able;

import android.content.Intent;

import java.util.List;

public interface BrowseImageAble {
	
	List<String> getImageUrls(Intent intent);

	String getCurrentUrl(Intent intent);
}
