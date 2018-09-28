
package com.tenone.gamebox.mode.able;

import com.tenone.gamebox.mode.mode.PrivilegeMode;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface PrivilegeAble {
	List<PrivilegeMode> getPrivilegeModes(List<ResultItem> resultItem);
}
