
package com.tenone.gamebox.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class GameSearchLabelAdapter extends BaseAdapter {

    private List<GameModel> items;
    private Context mContext;
    private LayoutInflater mInflater;
    int[] colors = {R.color.label1, R.color.label2, R.color.label3,
            R.color.label4};

    public GameSearchLabelAdapter(List<GameModel> list, Context cxt) {
        this.mContext = cxt;
        this.items = list;
        this.mInflater = LayoutInflater.from( mContext );
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchGameListHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate( R.layout.item_search_game_label,
                    parent, false );
            holder = new SearchGameListHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (SearchGameListHolder) convertView.getTag();
        }
        GameModel gameModel = items.get( position );
        holder.textView.setText( gameModel.getName() );
        holder.textView.setTextColor( mContext.getResources().getColor( R.color.label3 ) );

		/*int index = position % 4;
		holder.textView.setTextColor(mContext.getResources().getColor(
				colors[index]));*/
        return convertView;
    }

    class MyTask extends AsyncTask<String, ImageView, Void> {
        ImageView imageView;

        public MyTask(ImageView v) {
            this.imageView = v;
        }

        @Override
        protected Void doInBackground(String... params) {
            Message message = new Message();
            message.obj = imageView;
            Bundle bundle = new Bundle();
            bundle.putString( "url", params[0] );
            message.setData( bundle );
            handler.sendMessage( message );
            return null;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ImageView imageView = (ImageView) msg.obj;
            String url = msg.getData().getString( "url" );
            ImageLoadUtils.loadNormalImg( imageView, mContext, url );
        }
    };

    class SearchGameListHolder {
        TextView textView;

        public SearchGameListHolder(View v) {
            initView( v );
        }

        private void initView(View v) {
            textView = v.findViewById( R.id.id_searchGameLable_text );
        }
    }
}
