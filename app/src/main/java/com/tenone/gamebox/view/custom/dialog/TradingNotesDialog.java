package com.tenone.gamebox.view.custom.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.adapter.TradingNotesAdapter;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.custom.MyListView;

public class TradingNotesDialog extends AlertDialog {
    protected TradingNotesDialog(Context context) {
        super( context );
    }

    public static class TradingNotesBuilder {
        private Context mContext;
        private int type;
        private TradingNotesDialog dialog;
        private OnAgreeClickListener onAgreeClickListener;

        public TradingNotesBuilder(Context mContext) {
            this.mContext = mContext;
        }

        public TradingNotesBuilder setType(int type) {
            this.type = type;
            return this;
        }

        public TradingNotesBuilder setOnAgreeClickListener(OnAgreeClickListener onAgreeClickListener) {
            this.onAgreeClickListener = onAgreeClickListener;
            return this;
        }

        private boolean isClickable = false;

        public void showDialog() {
            dialog = new TradingNotesDialog( mContext );
            dialog.show();
            dialog.setCanceledOnTouchOutside( false );
            View view = LayoutInflater.from( mContext ).inflate(
                    R.layout.window_trading_notes, null );
            dialog.setContentView( view );
            TextView tv1 = view.findViewById( R.id.id_window_trading_notes_title1 );
            tv1.setText( type == 0 ? "\u51fa\u552e\u6d41\u7a0b" : "\u8d2d\u4e70\u6d41\u7a0b" );
            ImageView img = view.findViewById( R.id.id_window_trading_notes_img );
            img.setSelected( type == 0 );
            TextView tv2 = view.findViewById( R.id.id_window_trading_notes_title2 );
            tv2.setText( type == 0 ? "\u5356\u5bb6\u987b\u77e5" : "\u4e70\u5bb6\u987b\u77e5" );
            MyListView listView = view.findViewById( R.id.id_window_trading_notes_listview );
            TradingNotesAdapter adapter1 = new TradingNotesAdapter( mContext,
                    type == 0 ? Constant.getSellerNotes() : Constant.getBuyerNotes() );
            listView.setAdapter( adapter1 );
            MyListView listView2 = view.findViewById( R.id.id_window_trading_notes_listview2 );
            TradingNotesAdapter adapter2 = new TradingNotesAdapter( mContext, Constant.getBusinessNotice() );
            listView2.setAdapter( adapter2 );
            TextView agree = view.findViewById( R.id.id_window_trading_notes_agree );
            agree.setSelected( true );
            CheckBox checkBox = view.findViewById( R.id.id_window_trading_notes_check );
            checkBox.setOnCheckedChangeListener( (buttonView, isChecked) -> {
                agree.setSelected( !isChecked );
                isClickable = isChecked;
            } );
            agree.setOnClickListener( v -> {
                if (onAgreeClickListener != null && isClickable) {
                    onAgreeClickListener.onAgreeClick();
                }
            } );
        }

        public void dismiss() {
            if (dialog != null) {
                isClickable = false;
                dialog.dismiss();
                dialog = null;
            }
        }
    }


    public interface OnAgreeClickListener {
        void onAgreeClick();
    }
}
