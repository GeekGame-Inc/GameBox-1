
package com.tenone.gamebox.mode.biz;

import android.text.TextUtils;

import com.tenone.gamebox.mode.able.DetailsGiftFragmentAble;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class DetailsGiftFragmentBiz implements DetailsGiftFragmentAble {

	@Override
	public List<GiftMode> constructArray(List<ResultItem> resultItems) {
		List<GiftMode> items = new ArrayList<GiftMode>();
		if (resultItems != null) {
			for (ResultItem data : resultItems) {
				GiftMode mode = new GiftMode();
				String id = data.getString( "id" );
				if (!TextUtils.isEmpty( id )) {
					mode.setGiftId( Integer.valueOf( id ) );
				}
				mode.setGiftImgUrl( MyApplication.getHttpUrl().getBaseUrl() + data.getString( "pack_logo" ) );
				mode.setGiftCounts( Long.valueOf( data.getString( "pack_counts" ) ) );
				mode.setGiftName( data.getString( "pack_name" ) );
				mode.setGiftContent( data.getString( "pack_abstract" ) );
				String counts = data.getString( "pack_counts" );
				String usedCounts = data.getString( "pack_used_counts" );
				if (!TextUtils.isEmpty( counts ) && !TextUtils.isEmpty( usedCounts )) {
					mode.setResidue( Integer.valueOf( counts ) - Integer.valueOf( usedCounts ) );
				}
				if (!TextUtils.isEmpty( data.getString( "card" ) )) {
					mode.setGiftCode( data.getString( "card" ) );
					mode.setState( 1 );
				} else {
					mode.setState( 0 );
				}
				items.add( mode );
			}
		}
		return items;
	}

	@Override
	public String getGiftCode(ResultItem resultItem) {
		String code = resultItem.getString( "data" );
		return code;
	}
}
