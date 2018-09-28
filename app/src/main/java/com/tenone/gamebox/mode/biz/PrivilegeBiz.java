package com.tenone.gamebox.mode.biz;

import android.text.TextUtils;

import com.tenone.gamebox.mode.able.PrivilegeAble;
import com.tenone.gamebox.mode.mode.PrivilegeMode;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.ArrayList;
import java.util.List;

public class PrivilegeBiz implements PrivilegeAble {

    @Override
    public List<PrivilegeMode> getPrivilegeModes(List<ResultItem> resultItem) {
        return sort( resultItem );
    }

    private List<PrivilegeMode> sort(List<ResultItem> resultItem) {
        List<PrivilegeMode> items = new ArrayList<PrivilegeMode>();
        for (ResultItem result : resultItem) {
            PrivilegeMode mode = new PrivilegeMode();
            String privilegeId = result.getString( "article_id" );
            if (!TextUtils.isEmpty( privilegeId )) {
                mode.setPrivilegeId( Integer.valueOf( privilegeId )
                        .intValue() );
            }
            String title = result.getString( "title" );
            mode.setPrivilegeName( title );
            mode.setDetailsUrl( result.getString( "url" ) );
            mode.setPrivilegeImgUrl( result.getString( "slide_pic" ) );
            items.add( mode );
        }
        return items;
    }
}
