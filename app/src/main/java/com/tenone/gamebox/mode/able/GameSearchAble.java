
package com.tenone.gamebox.mode.able;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.RecordMode;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;
public interface GameSearchAble {
	List<String> getAllGameNames();

	List<GameModel> getGameModels(List<ResultItem> resultItem);

	List<GameModel> getLabels(List<ResultItem> resultItem);

	List<RecordMode> getRecordModes(List<String> records);

	List<String> getRecords();
	
	void saveRecord(List<String> records);

	void clearRecord();
}
