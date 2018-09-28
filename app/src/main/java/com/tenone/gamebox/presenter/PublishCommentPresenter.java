package com.tenone.gamebox.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.PublishCommentBiz;
import com.tenone.gamebox.mode.listener.CollectImageClickListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.PublishCommentView;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.photo.MultiImageSelectorActivity;
import com.tenone.gamebox.view.custom.popupwindow.CollectImageWindow;
import com.tenone.gamebox.view.utils.CameraUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;
public class PublishCommentPresenter extends BasePresenter implements
		OnClickListener, CollectImageClickListener{
	private PublishCommentBiz commentBiz;
	private PublishCommentView commentView;
	private Context mContext;
	public static final int REQUESTCODE = 0x002;
	private CollectImageWindow window;
	private List<String> imageUrls = new ArrayList<String>();
	private ArrayList<String> photoArray = new ArrayList<String>();
	AlertDialog alertDialog;
    String commentName;
	public PublishCommentPresenter(PublishCommentView v, Context cxt) {
		this.commentBiz = new PublishCommentBiz();
		this.commentView = v;
		this.mContext = cxt;
		photoArray.addAll(photoArray());
	}

	public void initView() {
		getTitleBarView().setLeftImg(R.drawable.icon_back_grey);
		getTitleBarView().setTitleText( "\u53d1\u8868\u8bc4\u8bba" );
		getTitleBarView().setRightText( "\u786e\u5b9a" );
		commentName = getIntent().getStringExtra( "commentName" );
		getEditText().setHint( "\u56de\u590d:@" +commentName );
	}

	public void initListener() {
		getTitleBarView().getLeftImg().setOnClickListener(this);
		getAlbumImgView().setOnClickListener(this);
		getCollectImgView().setOnClickListener(this);
		getTitleBarView().getRightText().setOnClickListener(this);
	}

	public TitleBarView getTitleBarView() {
		return commentView.getTitleBarView();
	}

	public CustomizeEditText getEditText() {
		return commentView.getEditText();
	}

	public ImageView getAlbumImgView() {
		return commentView.getAlbumImgView();
	}

	public ImageView getImgView() {
		return commentView.getImgView();
	}

	public ImageView getCollectImgView() {
		return commentView.getCollectImgView();
	}

	public ArrayList<String> collectImgUrls(List<ResultItem> resultItems) {
		return commentBiz.collectImgUrls(resultItems);
	}

	public ArrayList<String> photoArray() {
		return commentBiz.phontoArray();
	}

	public Intent getIntent() {
		return commentView.getIntent();
	}

	public String getGameId() {
		return commentBiz.getGameId(getIntent());
	}

	public long getTopicId() {
		return commentBiz.getTopicId(getIntent());
	}

	public long getReplyId() {
		return commentBiz.getReplyId(getIntent());
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null && data.hasExtra("select_result")) {
					List<String> list = data
							.getStringArrayListExtra("select_result");
					if (list != null) {
						photoArray.clear();
						photoArray.addAll(list);
						ImageLoadUtils.loadNormalImg(getImgView(), mContext,
								photoArray.get(0));
					}
				}
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_titleBar_leftImg:
			close(mContext);
			break;
		case R.id.id_publish_album:
			openAlbum();
			break;
		case R.id.id_publish_collect:
			openCollect(v);
			break;
		case R.id.id_titleBar_rightText:
			final String comment = getEditText().getText().toString();
			if (TextUtils.isEmpty(comment)) {
				showToast(mContext, "\u8bf7\u8f93\u5165\u8bc4\u8bba\u5185\u5bb9" );
				break;
			}
			alertDialog = buildProgressDialog(mContext);
			break;
		}
	}

	private void openAlbum() {
		Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
				MultiImageSelectorActivity.MODE_MULTI);
		intent.putStringArrayListExtra(
				MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
				photoArray);
		CameraUtils.startAlbum(mContext, intent, REQUESTCODE);
	}

	void openCollect(View v) {
		if (window == null) {
			window = new CollectImageWindow(imageUrls, mContext);
		}
		window.showAsDropDown(v);
		window.setListener(this);
	}

	@Override
	public void onImageClick(String imgUrl) {
		photoArray().clear();
		ImageLoadUtils.loadNormalImg(getImgView(), mContext, imgUrl);
	}
}
