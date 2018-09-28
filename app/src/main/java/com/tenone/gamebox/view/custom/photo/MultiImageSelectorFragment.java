package com.tenone.gamebox.view.custom.photo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.custom.photo.adapter.FolderAdapter;
import com.tenone.gamebox.view.custom.photo.adapter.ImageGridAdapter;
import com.tenone.gamebox.view.custom.photo.enty.Folder;
import com.tenone.gamebox.view.custom.photo.enty.Image;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.PermissionUtils;
import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ͼƬѡ��Fragment
 * 
 * @author John Li
 * @data: 2017��2��23�� ����5:18:36
 * @version: V1.0
 */
public class MultiImageSelectorFragment extends Fragment {

	private static final String TAG = "MultiImageSelector";

	/** ���ͼƬѡ�������int���� */
	public static final String EXTRA_SELECT_COUNT = "max_select_count";
	/** ͼƬѡ��ģʽ��int���� */
	public static final String EXTRA_SELECT_MODE = "select_count_mode";
	/** �Ƿ���ʾ�����boolean���� */
	public static final String EXTRA_SHOW_CAMERA = "show_camera";
	/** Ĭ��ѡ������ݼ� */
	public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
	/** ��ѡ */
	public static final int MODE_SINGLE = 0;
	/** ��ѡ */
	public static final int MODE_MULTI = 1;
	// ��ͬloader����
	private static final int LOADER_ALL = 0;
	private static final int LOADER_CATEGORY = 1;
	// �������ϵͳ�����
	private static final int REQUEST_CAMERA = 100;

	// �������
	private ArrayList<String> resultList = new ArrayList<String>();
	// �ļ�������
	private ArrayList<Folder> mResultFolder = new ArrayList<Folder>();

	// ͼƬGrid
	private GridView mGridView;
	private Callback mCallback;

	private ImageGridAdapter mImageAdapter;
	private FolderAdapter mFolderAdapter;

	private ListPopupWindow mFolderPopupWindow;

	// ʱ����
	private TextView mTimeLineText;
	// ���
	private TextView mCategoryText;
	// Ԥ����ť
	private Button mPreviewBtn;
	// �ײ�View
	private View mPopupAnchorView;

	private int mDesireImageCount;

	private boolean hasFolderGened = false;
	private boolean mIsShowCamera = false;

	private int mGridWidth, mGridHeight;

	private File mTmpFile;

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (Callback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"The Activity must implement MultiImageSelectorFragment.Callback interface...");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_multi_image, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// ѡ��ͼƬ����
		mDesireImageCount = getArguments().getInt(EXTRA_SELECT_COUNT);

		// ͼƬѡ��ģʽ
		final int mode = getArguments().getInt(EXTRA_SELECT_MODE);

		// Ĭ��ѡ��
		if (mode == MODE_MULTI) {
			ArrayList<String> tmp = getArguments().getStringArrayList(
					EXTRA_DEFAULT_SELECTED_LIST);
			if (tmp != null && tmp.size() > 0) {
				resultList = tmp;
			}
		}

		// �Ƿ���ʾ�����
		mIsShowCamera = getArguments().getBoolean(EXTRA_SHOW_CAMERA, true);
		mImageAdapter = new ImageGridAdapter(getActivity(), mIsShowCamera);
		// �Ƿ���ʾѡ��ָʾ��
		mImageAdapter.showSelectIndicator(mode == MODE_MULTI);

		mPopupAnchorView = view.findViewById(R.id.footer);

		mTimeLineText = view.findViewById(R.id.timeline_area);
		// ��ʼ���������ص�ǰtimeline
		mTimeLineText.setVisibility(View.GONE);

		mCategoryText = view.findViewById(R.id.category_btn);
		// ��ʼ������������ͼƬ
		mCategoryText.setText(R.string.folder_all);
		mCategoryText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (mFolderPopupWindow == null) {
					createPopupFolderList(mGridWidth, mGridHeight);
				}

				if (mFolderPopupWindow.isShowing()) {
					mFolderPopupWindow.dismiss();
				} else {
					mFolderPopupWindow.show();
					int index = mFolderAdapter.getSelectIndex();
					index = index == 0 ? index : index - 1;
					mFolderPopupWindow.getListView().setSelection(index);
				}
			}
		});

		mPreviewBtn = view.findViewById(R.id.preview);
		// ��ʼ������ť״̬��ʼ��
		if (resultList == null || resultList.size() <= 0) {
			mPreviewBtn.setText(R.string.preview);
			mPreviewBtn.setEnabled(false);
		}
		mPreviewBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
		mGridView = view.findViewById(R.id.grid);
		mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int state) {

				final Picasso picasso = Picasso.with(getActivity());
				if (state == SCROLL_STATE_IDLE
						|| state == SCROLL_STATE_TOUCH_SCROLL) {
					picasso.resumeTag(getActivity());
				} else {
					picasso.pauseTag(getActivity());
				}

				if (state == SCROLL_STATE_IDLE) {
					// ֹͣ����������ָʾ����ʧ
					mTimeLineText.setVisibility(View.GONE);
				} else if (state == SCROLL_STATE_FLING) {
					mTimeLineText.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mTimeLineText.getVisibility() == View.VISIBLE) {
					int index = firstVisibleItem + 1 == view.getAdapter()
							.getCount() ? view.getAdapter().getCount() - 1
							: firstVisibleItem + 1;
					Image image = (Image) view.getAdapter().getItem(index);
					if (image != null) {
						mTimeLineText.setText(TimeUtils
								.formatPhotoDate(image.path));
					}
				}
			}
		});
		mGridView.setAdapter(mImageAdapter);
		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
					public void onGlobalLayout() {

						final int width = mGridView.getWidth();
						final int height = mGridView.getHeight();

						mGridWidth = width;
						mGridHeight = height;

						final int desireSize = getResources()
								.getDimensionPixelOffset(R.dimen.image_size);
						final int numCount = width / desireSize;
						final int columnSpace = getResources()
								.getDimensionPixelOffset(R.dimen.space_size);
						int columnWidth = (width - columnSpace * (numCount - 1))
								/ numCount;
						mImageAdapter.setItemSize(columnWidth);

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							mGridView.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} else {
							mGridView.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
					}
				});
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				if (mImageAdapter.isShowCamera()) {
					// �����ʾ����������һ��Grid��ʾΪ����������������߼�
					if (i == 0) {
						showCameraAction();
					} else {
						// ��������
						Image image = (Image) adapterView.getAdapter().getItem(
								i);
						selectImageFromGrid(image, mode);
					}
				} else {
					// ��������
					Image image = (Image) adapterView.getAdapter().getItem(i);
					selectImageFromGrid(image, mode);
				}
			}
		});
		mFolderAdapter = new FolderAdapter(getActivity());
	}

	/**
	 * ����������ListView
	 */
	private void createPopupFolderList(int width, int height) {
		mFolderPopupWindow = new ListPopupWindow(getActivity());
		mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(
				Color.TRANSPARENT));
		mFolderPopupWindow.setAdapter(mFolderAdapter);
		mFolderPopupWindow.setContentWidth(width);
		mFolderPopupWindow.setWidth(width);
		mFolderPopupWindow.setHeight(height * 5 / 8);
		mFolderPopupWindow.setAnchorView(mPopupAnchorView);
		mFolderPopupWindow.setModal(true);
		mFolderPopupWindow
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int i, long l) {
						mFolderAdapter.setSelectIndex(i);
						index = i;
						v = adapterView;
						handler.sendEmptyMessage(0);
					}
				});
	}

	int index;
	AdapterView<?> v;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mFolderPopupWindow.dismiss();
					if (index == 0) {
						getActivity().getSupportLoaderManager().restartLoader(
								LOADER_ALL, null, mLoaderCallback);
						mCategoryText.setText(R.string.folder_all);
						if (mIsShowCamera) {
							mImageAdapter.setShowCamera(true);
						} else {
							mImageAdapter.setShowCamera(false);
						}
					} else {
						Folder folder = (Folder) v.getAdapter().getItem(index);
						if (null != folder) {
							mImageAdapter.setData(folder.images);
							mCategoryText.setText(folder.name);
							// �趨Ĭ��ѡ��
							if (resultList != null && resultList.size() > 0) {
								mImageAdapter.setDefaultSelected(resultList);
							}
						}
						mImageAdapter.setShowCamera(false);
					}
					// ���������ʼλ��
					mGridView.smoothScrollToPosition(0);
				}
			}, 100);
		}
	};

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// �״μ�������ͼƬ
		// new LoadImageTask().execute();
		getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null,
				mLoaderCallback);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// ���������ɺ󣬷���ͼƬ·��
		if (requestCode == REQUEST_CAMERA) {
			if (resultCode == Activity.RESULT_OK) {
				if (mTmpFile != null) {
					if (mCallback != null) {
						mCallback.onCameraShot(mTmpFile);
					}
				}
			} else {
				if (mTmpFile != null && mTmpFile.exists()) {
					mTmpFile.delete();
				}
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(TAG, "on change");

		if (mFolderPopupWindow != null) {
			if (mFolderPopupWindow.isShowing()) {
				mFolderPopupWindow.dismiss();
			}
		}

		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
					public void onGlobalLayout() {

						final int height = mGridView.getHeight();

						final int desireSize = getResources()
								.getDimensionPixelOffset(R.dimen.image_size);
						Log.d(TAG, "Desire Size = " + desireSize);
						final int numCount = mGridView.getWidth() / desireSize;
						Log.d(TAG, "Grid Size = " + mGridView.getWidth());
						Log.d(TAG, "num count = " + numCount);
						final int columnSpace = getResources()
								.getDimensionPixelOffset(R.dimen.space_size);
						int columnWidth = (mGridView.getWidth() - columnSpace
								* (numCount - 1))
								/ numCount;
						mImageAdapter.setItemSize(columnWidth);

						if (mFolderPopupWindow != null) {
							mFolderPopupWindow.setHeight(height * 5 / 8);
						}

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							mGridView.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} else {
							mGridView.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
					}
				});

		super.onConfigurationChanged(newConfig);

	}

	/**
	 * ѡ�����
	 */
	private void showCameraAction() {
		if (PermissionUtils.checkSelfPermission(getActivity(),
				PermissionUtils.PERMISSION_CAMERA)) {
			PermissionUtils.requestPermission(getActivity(),
					PermissionUtils.PERMISSION_CAMERA, 1);
		}
		// ��ת��ϵͳ�����
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			// ����ϵͳ������պ�����·��
			// ������ʱ�ļ�
			try {
				mTmpFile = FileUtils.createTmpFile(getActivity());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (Build.VERSION.SDK_INT >= 24) {
				Uri url = FileProvider.getUriForFile(getActivity(),
						"com.tenone.gamebox.fileprovider", mTmpFile);
				cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, url);
			} else {
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(mTmpFile));
			}
			startActivityForResult(cameraIntent, REQUEST_CAMERA);
		} else {
			Toast.makeText(getActivity(), R.string.msg_no_camera,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * ѡ��ͼƬ����
	 * 
	 * @param image
	 */
	private void selectImageFromGrid(Image image, int mode) {
		if (image != null) {
			// ��ѡģʽ
			if (mode == MODE_MULTI) {
				if (resultList.contains(image.path)) {
					resultList.remove(image.path);
					if (resultList.size() != 0) {
						mPreviewBtn.setEnabled(true);
						mPreviewBtn.setText(getResources().getString(
								R.string.preview)
								+ "(" + resultList.size() + ")");
					} else {
						mPreviewBtn.setEnabled(false);
						mPreviewBtn.setText(R.string.preview);
					}
					if (mCallback != null) {
						mCallback.onImageUnselected(image.path);
					}
				} else {
					// �ж�ѡ���������⢘
					if (mDesireImageCount == resultList.size()) {
						Toast.makeText(getActivity(),
								R.string.msg_amount_limit, Toast.LENGTH_SHORT)
								.show();
						return;
					}

					resultList.add(image.path);
					mPreviewBtn.setEnabled(true);
					mPreviewBtn.setText(getResources().getString(
							R.string.preview)
							+ "(" + resultList.size() + ")");
					if (mCallback != null) {
						mCallback.onImageSelected(image.path);
					}
				}
				mImageAdapter.select(image);
			} else if (mode == MODE_SINGLE) {
				// ��ѡģʽ
				if (mCallback != null) {
					mCallback.onSingleImageSelected(image.path);
				}
			}
		}
	}

	private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

		private final String[] IMAGE_PROJECTION = {
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.DISPLAY_NAME,
				MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media._ID };

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			if (id == LOADER_ALL) {
				CursorLoader cursorLoader = new CursorLoader(getActivity(),
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2]
								+ " DESC");
				return cursorLoader;
			} else if (id == LOADER_CATEGORY) {
				CursorLoader cursorLoader = new CursorLoader(getActivity(),
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_PROJECTION, IMAGE_PROJECTION[0] + " like '%"
								+ args.getString("path") + "%'", null,
						IMAGE_PROJECTION[2] + " DESC");
				return cursorLoader;
			}

			return null;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			if (data != null) {
				List<Image> images = new ArrayList<Image>();
				int count = data.getCount();
				if (count > 0) {
					data.moveToFirst();
					do {
						String path = data.getString(data
								.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
						String name = data.getString(data
								.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
						long dateTime = data.getLong(data
								.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
						Image image = new Image(path, name, dateTime);
						images.add(image);
						if (!hasFolderGened) {
							// ��ȡ�ļ�������
							File imageFile = new File(path);
							File folderFile = imageFile.getParentFile();
							Folder folder = new Folder();
							folder.name = folderFile.getName();
							folder.path = folderFile.getAbsolutePath();
							folder.cover = image;
							if (!mResultFolder.contains(folder)) {
								List<Image> imageList = new ArrayList<Image>();
								imageList.add(image);
								folder.images = imageList;
								mResultFolder.add(folder);
							} else {
								// ����
								Folder f = mResultFolder.get(mResultFolder
										.indexOf(folder));
								f.images.add(image);
							}
						}

					} while (data.moveToNext());

					mImageAdapter.setData(images);

					// �趨Ĭ��ѡ��
					if (resultList != null && resultList.size() > 0) {
						mImageAdapter.setDefaultSelected(resultList);
					}

					mFolderAdapter.setData(mResultFolder);
					hasFolderGened = true;

				}
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {

		}
	};

	/**
	 * �ص��ӿ�
	 */
	public interface Callback {
		void onSingleImageSelected(String path);

		void onImageSelected(String path);

		void onImageUnselected(String path);

		void onCameraShot(File imageFile);
	}
}
