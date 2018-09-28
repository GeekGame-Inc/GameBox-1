/**
 * Project Name:GameBox
 * File Name:MineItemAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-3-13����6:07:12
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.SpUtil;

public class MineItemAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private String[] itemArray;
	int[] imgIdArray = {R.drawable.icon_qiandao, R.drawable.icon_meiripinglun,
			R.drawable.icon_yaoqinghaoyou, R.drawable.icon_share_top, R.drawable.icon_driver, R.drawable.icon_jbdh,
			R.drawable.icon_jbcj, R.drawable.icon_hbmx, R.drawable.icon_flsq,
			R.drawable.icon_shenqingzhuanyou, R.drawable.icon_yygl,
			R.drawable.icon_wodelibao, R.drawable.icon_wodeshouchang,
			R.drawable.icon_wdhd, R.drawable.icon_wdxx, R.drawable.icon_kefu,
			R.drawable.icon_xgmm, R.drawable.icon_bdsj, R.drawable.icon_gy};
	private int size = 0;

	private String signGold, vipSignGold, commentGold, shareGold, exchangeGold,
			deplete_coin, mobile, topGold;

	public MineItemAdapter(Context cxt, int size) {
		this.size = size;
		this.mContext = cxt;
		this.mInflater = LayoutInflater.from( cxt );
		itemArray = mContext.getResources().getStringArray( R.array.mine_item );
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int position) {
		return itemArray == null ? null : itemArray[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MineViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate( R.layout.item_mine_grid, parent,
					false );
			holder = new MineViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			holder = (MineViewHolder) convertView.getTag();
		}
		holder.textView.setText( itemArray[position] );
		BitmapDrawable drawable = (BitmapDrawable) mContext.getResources()
				.getDrawable( imgIdArray[position] );
		holder.imageView.setImageBitmap( drawable.getBitmap() );
		holder.line
				.setVisibility( (position == 5 || position == 8 || position == 10
						|| position == 13 || position == 15) ? View.VISIBLE
						: View.GONE );
		String str = "";
		if (!TextUtils.isEmpty( signGold )) {
			switch (position) {
				case 0:
					holder.textView1.setText(
							Html.fromHtml( mContext.getResources()
									.getString( R.string.sign_text,
											signGold, vipSignGold ) ) );
					break;
				case 1:
					if (!TextUtils.isEmpty( commentGold )) {
						commentGold = commentGold.replace( "-", "~" );
						holder.textView1.setText(
								Html.fromHtml( mContext
										.getResources().getString( R.string.comment_text,
												commentGold ) ) );
					}
					break;
				case 2:
					holder.textView1.setText(
							Html.fromHtml( mContext.getResources()
									.getString( R.string.share_text,
											shareGold ) ) );
					break;
				case 3:
					holder.textView1.setText(
							Html.fromHtml( mContext.getResources()
									.getString( R.string.top_text,
											topGold ) ) );
					break;

				case 4:
					holder.textView1.setText(
							Html.fromHtml( mContext.getResources()
									.getString( R.string.dirver_text,
											"5~30" ) ) );
					break;

				case 5:
					holder.textView1.setText(
							Html.fromHtml( mContext.getResources()
									.getString( R.string.exchange_text,
											exchangeGold, "10", "1" ) ) );
					break;
				case 6:
					holder.textView1.setText(
							Html.fromHtml( mContext.getResources()
									.getString( R.string.lucky_text,
											deplete_coin ) ) );
					break;
				case 8:
					str = "\u5145\u503c\u6709\u5956,\u5143\u5b9d\u8fd4\u8fd8";
					holder.textView1.setText( str );
					break;
				case 15:
					str = "\u5bfb\u6c42\u5e2e\u52a9,\u95ee\u9898\u53cd\u9988";
					holder.textView1.setText( str );
					break;
				case 17:
					str = isLogin() ? (isBind() ? CharSequenceUtils
							.getVisibilyPhone( mobile ) : "\u53bb\u7ed1\u5b9a") : "";
					holder.textView1.setText( str );
					break;
				default:
					holder.textView1.setText( "" );
					break;
			}
		}
		return convertView;
	}

	private boolean isBind() {
		boolean isBind = (!TextUtils.isEmpty( mobile ))
				&& BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, mobile );
		return isBind;
	}

	private boolean isLogin() {
		return !TextUtils.isEmpty( SpUtil.getUserId() )
				&& !"0".equals( SpUtil.getUserId() );
	}

	public void setInstruction(String sign, String comment, String share,
														 String exchange, String vipSign, String mobile, String deplete_coin, String topGold) {
		this.shareGold = share;
		this.signGold = sign;
		this.vipSignGold = vipSign;
		this.commentGold = comment;
		this.exchangeGold = exchange;
		this.mobile = mobile;
		this.topGold = topGold;
		this.deplete_coin = deplete_coin;
		notifyDataSetChanged();
	}

	private class MineViewHolder {
		TextView textView, textView1;
		ImageView imageView;
		View line;

		public MineViewHolder(View convertView) {
			textView1 = convertView.findViewById( R.id.id_mine_tv1 );
			textView = convertView.findViewById( R.id.id_mine_tv );
			imageView = convertView.findViewById( R.id.id_mine_iv );
			line = convertView.findViewById( R.id.id_mine_line );
		}
	}
}
