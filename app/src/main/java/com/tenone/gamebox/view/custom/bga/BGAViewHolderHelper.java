
package com.tenone.gamebox.view.custom.bga;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.mode.listener.BGAOnItemChildClickListener;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildCheckedChangeListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
public class BGAViewHolderHelper implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    protected final SparseArray<View> mViews;
    protected BGAOnItemChildClickListener mOnItemChildClickListener;
    protected BGAOnItemChildLongClickListener mOnItemChildLongClickListener;
    protected BGAOnItemChildCheckedChangeListener mOnItemChildCheckedChangeListener;
    protected View mConvertView;
    protected Context mContext;
    protected int mPosition;
    protected BGARecyclerViewHolder mRecyclerViewHolder;
    protected RecyclerView mRecyclerView;

    protected ViewGroup mAdapterView;
    /**
     * �����Ժ���Ϊ�������
     */
    protected Object mObj;

    public BGAViewHolderHelper(ViewGroup adapterView, View convertView) {
        mViews = new SparseArray<>();
        mAdapterView = adapterView;
        mConvertView = convertView;
        mContext = convertView.getContext();
    }

    public BGAViewHolderHelper(RecyclerView recyclerView, View convertView) {
        mViews = new SparseArray<>();
        mRecyclerView = recyclerView;
        mConvertView = convertView;
        mContext = convertView.getContext();
    }

    public void setRecyclerViewHolder(BGARecyclerViewHolder recyclerViewHolder) {
        mRecyclerViewHolder = recyclerViewHolder;
    }

    public BGARecyclerViewHolder getRecyclerViewHolder() {
        return mRecyclerViewHolder;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        if (mRecyclerViewHolder != null) {
            return mRecyclerViewHolder.getAdapterPosition();
        }
        return mPosition;
    }

    /**
     * ����item�ӿؼ�����¼�������
     *
     * @param onItemChildClickListener
     */
    public void setOnItemChildClickListener(BGAOnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    /**
     * ΪidΪviewId��item�ӿؼ����õ���¼�������
     *
     * @param viewId
     */
    public void setItemChildClickListener(@IdRes int viewId) {
        getView(viewId).setOnClickListener(this);
    }

    /**
     * ����item�ӿؼ������¼�������
     *
     * @param onItemChildLongClickListener
     */
    public void setOnItemChildLongClickListener(BGAOnItemChildLongClickListener onItemChildLongClickListener) {
        mOnItemChildLongClickListener = onItemChildLongClickListener;
    }

    /**
     * ΪidΪviewId��item�ӿؼ����ó����¼�������
     *
     * @param viewId
     */
    public void setItemChildLongClickListener(@IdRes int viewId) {
        getView(viewId).setOnLongClickListener(this);
    }

    /**
     * ����item�ӿؼ�ѡ��״̬�仯�¼�������
     *
     * @param onItemChildCheckedChangeListener
     */
    public void setOnItemChildCheckedChangeListener(BGAOnItemChildCheckedChangeListener onItemChildCheckedChangeListener) {
        mOnItemChildCheckedChangeListener = onItemChildCheckedChangeListener;
    }

    /**
     * ΪidΪviewId��item�ӿؼ�����ѡ��״̬�仯�¼�������
     *
     * @param viewId
     */
    public void setItemChildCheckedChangeListener(@IdRes int viewId) {
        if (getView(viewId) instanceof CompoundButton) {
            ((CompoundButton) getView(viewId)).setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemChildClickListener != null) {
            if (mRecyclerView != null) {
                mOnItemChildClickListener.onItemChildClick(mRecyclerView, v, getPosition());
            } else if (mAdapterView != null) {
                mOnItemChildClickListener.onItemChildClick(mAdapterView, v, getPosition());
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemChildLongClickListener != null) {
            if (mRecyclerView != null) {
                return mOnItemChildLongClickListener.onItemChildLongClick(mRecyclerView, v, getPosition());
            } else if (mAdapterView != null) {
                return mOnItemChildLongClickListener.onItemChildLongClick(mAdapterView, v, getPosition());
            }
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mOnItemChildCheckedChangeListener != null) {
            if (mRecyclerView != null) {
                mOnItemChildCheckedChangeListener.onItemChildCheckedChanged(mRecyclerView, buttonView, getPosition(), isChecked);
            } else if (mAdapterView != null) {
                mOnItemChildCheckedChangeListener.onItemChildCheckedChanged(mAdapterView, buttonView, getPosition(), isChecked);
            }
        }
    }

    /**
     * ͨ���ؼ���Id��ȡ��Ӧ�Ŀؼ������û�������mViews�����item���ؼ��в��Ҳ����浽mViews��
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * ͨ��ImageView��Id��ȡImageView
     *
     * @param viewId
     * @return
     */
    public ImageView getImageView(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * ͨ��TextView��Id��ȡTextView
     *
     * @param viewId
     * @return
     */
    public TextView getTextView(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * ��ȡitem�ĸ��ؼ�
     *
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }

    public void setObj(Object obj) {
        mObj = obj;
    }

    public Object getObj() {
        return mObj;
    }

    /**
     * ���ö�Ӧid�Ŀؼ����ı�����
     *
     * @param viewId
     * @param text
     * @return
     */
    public BGAViewHolderHelper setText(@IdRes int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * ���ö�Ӧid�Ŀؼ����ı�����
     *
     * @param viewId
     * @param stringResId �ַ�����Դid
     * @return
     */
    public BGAViewHolderHelper setText(@IdRes int viewId, @StringRes int stringResId) {
        TextView view = getView(viewId);
        view.setText(stringResId);
        return this;
    }

    /**
     * ���ö�Ӧid�Ŀؼ���html�ı�����
     *
     * @param viewId
     * @param source html�ı�
     * @return
     */
    public BGAViewHolderHelper setHtml(@IdRes int viewId, String source) {
        TextView view = getView(viewId);
        view.setText(Html.fromHtml(source));
        return this;
    }

    /**
     * ���ö�Ӧid�Ŀؼ��Ƿ�ѡ��
     *
     * @param viewId
     * @param checked
     * @return
     */
    public BGAViewHolderHelper setChecked(@IdRes int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public BGAViewHolderHelper setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public BGAViewHolderHelper setTag(@IdRes int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public BGAViewHolderHelper setVisibility(@IdRes int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public BGAViewHolderHelper setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public BGAViewHolderHelper setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * @param viewId
     * @param textColorResId ��ɫ��Դid
     * @return
     */
    public BGAViewHolderHelper setTextColorRes(@IdRes int viewId, @ColorRes int textColorResId) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorResId));
        return this;
    }

    /**
     * @param viewId
     * @param textColor ��ɫֵ
     * @return
     */
    public BGAViewHolderHelper setTextColor(@IdRes int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * @param viewId
     * @param backgroundResId ������Դid
     * @return
     */
    public BGAViewHolderHelper setBackgroundRes(@IdRes int viewId, int backgroundResId) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundResId);
        return this;
    }

    /**
     * @param viewId
     * @param color  ��ɫֵ
     * @return
     */
    public BGAViewHolderHelper setBackgroundColor(@IdRes int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * @param viewId
     * @param colorResId ��ɫֵ��Դid
     * @return
     */
    public BGAViewHolderHelper setBackgroundColorRes(@IdRes int viewId, @ColorRes int colorResId) {
        View view = getView(viewId);
        view.setBackgroundColor(mContext.getResources().getColor(colorResId));
        return this;
    }

    /**
     * @param viewId
     * @param imageResId ͼ����Դid
     * @return
     */
    public BGAViewHolderHelper setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

}