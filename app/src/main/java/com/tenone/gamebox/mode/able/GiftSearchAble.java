
package com.tenone.gamebox.mode.able;

import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.mode.mode.RecordMode;

import java.util.List;
public interface GiftSearchAble {

	List<RecordMode> getRecordModes(List<String> records);

	List<String> getRecord(Context mContext);

	List<String> getAllGameName();

	String getHint(Intent intent);

	void saveRecord(List<String> records);

	void clearRecord();
}
