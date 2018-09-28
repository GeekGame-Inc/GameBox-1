package com.tenone.gamebox.view.custom.photo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.tenone.gamebox.R;

import java.io.File;
import java.util.ArrayList;

/**
 * ��ͼѡ��
 * 
 * @author John Li
 * @data: 2017��2��23�� ����5:32:27
 * @version: V1.0
 */
public class MultiImageSelectorActivity extends FragmentActivity implements
		MultiImageSelectorFragment.Callback {

	/** ���ͼƬѡ�������int���ͣ�Ĭ��9 */
	public static final String EXTRA_SELECT_COUNT = "max_select_count";
	/** ͼƬѡ��ģʽ��Ĭ�϶�ѡ */
	public static final String EXTRA_SELECT_MODE = "select_count_mode";
	/** �Ƿ���ʾ�����Ĭ����ʾ */
	public static final String EXTRA_SHOW_CAMERA = "show_camera";
	/** ѡ����������Ϊ ArrayList&lt;String&gt; ͼƬ·������ */
	public static final String EXTRA_RESULT = "select_result";
	/** Ĭ��ѡ�� */
	public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";
	/** ��ѡ */
	public static final int MODE_SINGLE = 0;
	/** ��ѡ */
	public static final int MODE_MULTI = 1;

	private ArrayList<String> resultList = new ArrayList<String>();
	private Button mSubmitButton;
	private int mDefaultCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cmp_customer_actionbar);
		Intent intent = getIntent();
		mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
		int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
		boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
		if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
			resultList = intent
					.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
		}
		Bundle bundle = new Bundle();
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT,
				mDefaultCount);
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
		bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
		bundle.putStringArrayList(
				MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST,
				resultList);
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.image_grid,
						Fragment.instantiate(this,
								MultiImageSelectorFragment.class.getName(),
								bundle)).commit();
		// ���ذ�ť
		findViewById(R.id.btn_back).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});
		// ��ɰ�ť
		mSubmitButton = findViewById(R.id.commit);
		if (resultList == null || resultList.size() <= 0) {
			mSubmitButton.setText("���");
			mSubmitButton.setEnabled(false);
		} else {
			mSubmitButton.setText("���(" + resultList.size() + "/"
					+ mDefaultCount + ")");
			mSubmitButton.setEnabled(true);
		}
		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (resultList != null && resultList.size() > 0) {
					// ������ѡ���ͼƬ����
					Intent data = new Intent();
					data.putStringArrayListExtra(EXTRA_RESULT, resultList);
					setResult(RESULT_OK, data);
					finish();
				}
			}
		});
	}

	@Override
	public void onSingleImageSelected(String path) {
		Intent data = new Intent();
		resultList.add(path);
		data.putStringArrayListExtra(EXTRA_RESULT, resultList);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onImageSelected(String path) {
		if (!resultList.contains(path)) {
			resultList.add(path);
		}
		// ��ͼƬ֮�󣬸ı䰴ť״̬
		if (resultList.size() > 0) {
			mSubmitButton.setText("���(" + resultList.size() + "/"
					+ mDefaultCount + ")");
			if (!mSubmitButton.isEnabled()) {
				mSubmitButton.setEnabled(true);
			}
		}
	}

	@Override
	public void onImageUnselected(String path) {
		if (resultList.contains(path)) {
			resultList.remove(path);
			mSubmitButton.setText("���(" + resultList.size() + "/"
					+ mDefaultCount + ")");
		} else {
			mSubmitButton.setText("���(" + resultList.size() + "/"
					+ mDefaultCount + ")");
		}
		// ��Ϊѡ��ͼƬʱ���״̬
		if (resultList.size() == 0) {
			mSubmitButton.setText("���");
			mSubmitButton.setEnabled(false);
		}
	}

	@Override
	public void onCameraShot(File imageFile) {
		if (imageFile != null) {
			Intent data = new Intent();
			resultList.add(imageFile.getAbsolutePath());
			data.putStringArrayListExtra(EXTRA_RESULT, resultList);
			setResult(RESULT_OK, data);
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.fromFile(imageFile)));// ˢ��ϵͳ���
			finish();
		}
	}
}
