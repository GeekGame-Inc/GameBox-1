
package com.tenone.gamebox.mode.able;

import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface AlreadyOpenFragmentAble {
	List<OpenServerMode> getModes(List<ResultItem> resultItem);
	OpenServiceNotificationMode getNotificationMode(OpenServerMode mode);
}
