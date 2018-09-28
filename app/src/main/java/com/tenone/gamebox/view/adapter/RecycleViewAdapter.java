package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameClassify;
import com.tenone.gamebox.mode.mode.GameClassifyModel;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.activity.GameSearchActivity;
import com.tenone.gamebox.view.custom.MyGridView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("static-access")
public class RecycleViewAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int BASE_ITEM_TYPE_HEADER = 100000;
	public static List<GameClassifyModel> items = new ArrayList<GameClassifyModel>();
	private Context mContext;
	private OnItemClickListener mOnItemClickListener;
	private RecommendGameAdapter gridViewAdapter;
	private GameClassifyFragmentAdapter fragmentAdapter;
	private OnMoreClickListener mOnMoreClickListener;
	private OnGridItemClickListener mOnGridItemClickListener;
	private OnHeaderItemClickListener mOnHeaderItemClickListener;
	private List<GameClassify> classifys;
	private String topGameName;
	private int platform = 0;

	public RecycleViewAdapter(List<GameClassifyModel> lst, Context mContext,
														List<GameClassify> list) {
		this.items = lst;
		this.mContext = mContext;
		this.classifys = list;
	}

	public RecycleViewAdapter(List<GameClassifyModel> lst, Context mContext,
														List<GameClassify> list, String topGameName, int platform) {
		this.items = lst;
		this.mContext = mContext;
		this.classifys = list;
		this.topGameName = topGameName;
		this.platform = platform;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
																										int viewType) {
		if (viewType == BASE_ITEM_TYPE_HEADER) {
			View itemView = LayoutInflater.from( parent.getContext() ).inflate(
					R.layout.layout_classify_header, parent, false );
			HeaderHolder headerHolder = new HeaderHolder(
					itemView );
			return headerHolder;
		} else {
			View itemView = LayoutInflater.from( parent.getContext() ).inflate(
					R.layout.item_classify_recycler, parent, false );
			ItemHolder viewHolder = new ItemHolder(
					itemView, mOnItemClickListener, mOnMoreClickListener );
			return viewHolder;
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (getItemViewType( position ) == BASE_ITEM_TYPE_HEADER) {
			HeaderHolder headerHolder = (HeaderHolder) holder;
			fragmentAdapter = new GameClassifyFragmentAdapter( mContext,
					classifys );
			headerHolder.gridView.setAdapter( fragmentAdapter );
			setHeaderItemClick( headerHolder.gridView );
			initSearch( headerHolder );
		} else {
			ItemHolder itemHolder = (ItemHolder) holder;
			GameClassifyModel item = getItem( position - 1 );
			if (item != null) {
				if (itemHolder.titleTv != null) {
					itemHolder.titleTv.setText( item.getClassName() );
				}
				if (itemHolder.gridView != null) {
					List<GameModel> gameModels = item.getGameModels();
					gridViewAdapter = new RecommendGameAdapter( gameModels,
							mContext );
					itemHolder.gridView.setAdapter( gridViewAdapter );
					setGridViewOnclik( itemHolder.gridView, position );
				}
			}
		}
		return;
	}

	private void initSearch(HeaderHolder headerHolder) {
		if (!TextUtils.isEmpty( topGameName )) {
			headerHolder.searchTv.setText( topGameName );
			headerHolder.searchLayout.setOnClickListener( v ->
					mContext.startActivity( new Intent( mContext, GameSearchActivity.class )
							.putExtra( "platform", platform )
							.putExtra( "topGame", topGameName ) ) );
		}
	}

	@Override
	public int getItemCount() {
		if (items == null) {
			return 0;
		} else {
			return this.items.size();
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return BASE_ITEM_TYPE_HEADER;
		} else {
			return 2;
		}
	}

	public void setGridViewOnclik(GridView gridView, final int parentPosition) {
		if (gridViewAdapter != null) {
			GameClassifyModel item = getItem( parentPosition - 1 );
			final List<GameModel> gameModels = item.getGameModels();
			gridView.setOnItemClickListener( (parent, view, position, id) -> {
				if (mOnGridItemClickListener != null) {
					mOnGridItemClickListener.onGridItemClick( gameModels
							.get( position ) );
				}
			} );
		}
	}

	public void setHeaderItemClick(GridView gridView) {
		if (fragmentAdapter != null) {
			gridView.setOnItemClickListener( (parent, view, position, id) -> {
				if (mOnHeaderItemClickListener != null) {
					mOnHeaderItemClickListener.onHeaderItemClick( classifys
							.get( position ) );
				}
			} );
		}
	}

	public GameClassifyModel getItem(int position) {
		if (items == null) {
			return null;
		} else {
			return items.get( position );
		}
	}

	public void setOnItemClickListener(
			OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}


	public void setOnMoreClickListener(OnMoreClickListener m) {
		this.mOnMoreClickListener = m;
	}

	public void setOnGridItemClickListener(OnGridItemClickListener g) {
		this.mOnGridItemClickListener = g;
	}

	public void setOnHeaderItemClickListener(OnHeaderItemClickListener h) {
		this.mOnHeaderItemClickListener = h;
	}

	public interface OnItemClickListener {
		void onClick(View view, int position);
	}

	public interface OnMoreClickListener {
		void onMoreClick(GameClassifyModel classifyModel);
	}

	public interface OnGridItemClickListener {
		void onGridItemClick(GameModel model);
	}

	public interface OnHeaderItemClickListener {
		void onHeaderItemClick(GameClassify classify);
	}

	public static class ItemHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
		private OnItemClickListener mOnItemClickListener;
		private OnMoreClickListener mOnMoreClickListener;
		private MyGridView gridView;
		private TextView moreTv;
		private TextView titleTv;

		public ItemHolder(View itemView,
											OnItemClickListener onItemClickListener,
											OnMoreClickListener mOnMoreClickListener) {
			super( itemView );
			gridView = itemView.findViewById( R.id.id_classify_childGridView );
			moreTv = itemView.findViewById( R.id.id_classify_more );
			moreTv.setOnClickListener( this );
			titleTv = itemView.findViewById( R.id.id_classify_groupTv );
			this.mOnItemClickListener = onItemClickListener;
			this.mOnMoreClickListener = mOnMoreClickListener;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.id_classify_more:
					if (mOnMoreClickListener != null) {
						mOnMoreClickListener.onMoreClick( items.get( getPosition() - 1 ) );
					}
					break;
				default:
					if (mOnItemClickListener != null)
						mOnItemClickListener.onClick( v, getPosition() );
					break;
			}
		}
	}

	public static class HeaderHolder extends RecyclerView.ViewHolder {

		private MyGridView gridView;
		private TextView searchTv;
		private RelativeLayout searchLayout;

		public HeaderHolder(View itemView) {
			super( itemView );
			gridView = itemView.findViewById( R.id.id_classify_gridView );
			searchLayout = itemView.findViewById( R.id.id_new_search_layout );
			searchTv = itemView.findViewById( R.id.id_new_search_text );
		}
	}

}
