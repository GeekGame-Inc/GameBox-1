package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.GameClassifyTabAble;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class GameClassifyTabBiz implements GameClassifyTabAble {

	@Override
	public List<GameModel> constructArray(Context context,
																				List<ResultItem> resultItem) {
		List<GameModel> items = new ArrayList<GameModel>();
		for (ResultItem data : resultItem) {
			GameModel model = new GameModel();
			model.setGameTag( data.getString( "tag" ) );
			model.setType( 1 );
			model.setOperate( data.getIntValue( "operate" ) );
			model.setDis( data.getString( "discount" ) );
			model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl() + data.getString( "logo" ) );
			model.setName( data.getString( "gamename" ) );
			String id = data.getString( "id" );
			if (!TextUtils.isEmpty( id )) {
				model.setGameId( Integer.valueOf( id ).intValue() );
			}
			model.setSize( data.getString( "size" ) );
			model.setUrl( data.getString( "android_url" ) );
			int download = 0;
			String str = data.getString( "download" );
			if (!TextUtils.isEmpty( str )) {
				download = Integer.valueOf( str ).intValue();
			}
			if (download > 10000) {
				download = download / 10000;
				model.setTimes( download + "\u4e07+" );
			} else {
				model.setTimes( download + "" );
			}
			model.setPackgeName( data.getString( "android_pack" ) );
			model.setStatus( GameStatus.UNLOAD );
			model.setContent( data.getString( "content" ) );
			String types = data.getString( "label" );
			if (!TextUtils.isEmpty( types )) {
				String[] lableArray = types.split( "," );
				model.setLabelArray( lableArray );
			}
			items.add( model );
		}
		return items;
	}

	@Override
	public String getTitle(Intent intent) {
		return intent.getExtras().getString( "name" );
	}

	@Override
	public String getClassifyId(Intent intent) {
		return intent.getExtras().getString( "id" );
	}
}
