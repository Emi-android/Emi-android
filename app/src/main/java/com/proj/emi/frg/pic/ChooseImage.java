package com.proj.emi.frg.pic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.proj.emi.R;
import com.proj.emi.actlink.NaviRightDecorator;
import com.proj.emi.cache.CacheCenter;
import com.proj.emi.frg.TitleFragment;
import com.hcb.util.LoggerUtil;
import com.hcb.util.ToastUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChooseImage extends TitleFragment implements NaviRightDecorator {

	private final static Logger LOG = LoggerFactory.getLogger(
			ChooseImage.class);

	public final static String EXT_TITLE = "title";
	public final static String EXT_MAX = "max";
	public final static String EXT_IMAGES = "images";
	public final static String ACTION_RESULT = "com.hcb.carclub.pick.images";

	private String title;
	int maxCount = 1;
	private File cameraFile;

	private TextView rightBtn;
	private GridView gridView;
	List<ImageItem> dataList;
	ImagePickAdapter adapter;// 自定义的适配器
	LocalImageUtil helper;

	@Override
	public void decorRightBtn(TextView tv) {
		rightBtn = tv;
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				choseDone();
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getParamFromBundle(getArguments());
		act.setNaviTitle(title);
		changeCount(0);
		helper = new LocalImageUtil(act);

	}

	private void getParamFromBundle(Bundle bundle) {
		if (null != bundle) {
			title = bundle.getString(EXT_TITLE);
			if (null == title) {
				title = "选择图片";
			}
			maxCount = bundle.getInt(EXT_MAX, maxCount);
		}
	}

	protected void changeCount(int count) {
		rightBtn.setText("完成(" + count + "/" + maxCount + ")");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.frg_pick_pictures, container,
		        false);

		gridView = (GridView) rootView.findViewById(R.id.image_grid);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		dataList = helper.getImageList(false);
		adapter = new ImagePickAdapter(act, dataList, maxCount,
		        new ImagePickAdapter.SelectListener() {
			        @Override
			        public void onCamera() {
				        takePhoto();
			        }

			        @Override
			        public void onCount(int count) {
				        changeCount(count);
			        }
		        });

		gridView.setAdapter(adapter);
	}

	private final static int PIC_CAMERA = 2;

	private void takePhoto() {
		cameraFile = CacheCenter.genTmpImgFile(act);
		if(null==cameraFile) {
			LOG.error("不能拍照，因为存储卡不存在。");
			return;
		}
		Intent intent = new Intent();
		intent.setAction("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
		act.startActivityForResult(intent, PIC_CAMERA);
	}

	private String genTempCameraFile() {
		return "camera_" + System.currentTimeMillis() + ".jpg";
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK || requestCode != PIC_CAMERA) {
			return;
		}
		String filePath = cameraFile.getAbsolutePath();
		LoggerUtil.t(LOG, "photo taken :{}", filePath);

		if (null != filePath) {
			ImageItem item = new ImageItem();
			item.setImagePath(filePath);
			ArrayList<ImageItem> images = new ArrayList<ImageItem>(1);
			images.add(item);
			backResult(images);
		}
	}

	protected void choseDone() {
		ArrayList<ImageItem> images = adapter.getSelected();

		if (!images.isEmpty()) {
			backResult(images);
		} else {
			ToastUtil.show("没有选择图片");
			act.finish();
		}
	}

	private void backResult(ArrayList<ImageItem> images) {
		// ToastUtil.show("选择" + images.size() + "张图片");
		final Intent intent = new Intent(ACTION_RESULT);
		intent.putExtra(EXT_IMAGES, JSONObject.toJSONString(images));

		act.setResult(Activity.RESULT_OK, intent);
		act.finish();
	}

}
