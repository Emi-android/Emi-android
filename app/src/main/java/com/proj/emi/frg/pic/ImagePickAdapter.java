package com.proj.emi.frg.pic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.proj.emi.R;
import com.hcb.util.FormatUtil;
import com.hcb.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class ImagePickAdapter extends BaseAdapter implements OnClickListener {

	Activity act;
	private SelectListener listener = null;
	List<ImageItem> dataList;
	int maxCount;
	LocalBitmap localBitmap;
	Map<Integer, ImageItem> chosed;

	public static interface SelectListener {
		public void onCamera();

		public void onCount(int count);
	}

	public ImagePickAdapter(Activity act, List<ImageItem> list, int max,
							SelectListener listener) {
		this.act = act;
		this.dataList = list;
		this.maxCount = max;
		this.listener = listener;
		this.chosed = new HashMap<Integer, ImageItem>(max);
		this.localBitmap = new LocalBitmap();
		makeParam();
	}

	private void chose(int position) {
		chosed.put(position, dataList.get(position));
	}

	private void unChose(int position) {
		chosed.remove(position);
	}

	private boolean isChosen(final int position) {
		return chosed.containsKey(position);
	}

	private void changeCount() {
		if (listener != null) {
			listener.onCount(chosed.size());
		}
	}

	public ArrayList<ImageItem> getSelected() {
		return new ArrayList<ImageItem>(chosed.values());
	}

	@Override
	public int getCount() {
		int count = 1;
		if (dataList != null) {
			count += dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class Holder {
		private int position;
		private ImageView iv;
		private ImageView selected;
	}

	GridView.LayoutParams itemParams;

	private void makeParam() {
		int w = (act.getResources().getDisplayMetrics().widthPixels
		        - 4 * FormatUtil.pixOfDip(6)) / 3;
		itemParams = new GridView.LayoutParams(w, w);
	}

	@Override
	public View getView(final int position, View convertView,
	        ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(act, R.layout.cell_image_pick, null);
			convertView.setLayoutParams(itemParams);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView.findViewById(
			        R.id.isselected);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.position = position;
		if (position == 0) {
			holder.iv.setScaleType(ScaleType.CENTER);
			holder.iv.setImageResource(R.mipmap.camera);
			holder.selected.setImageResource(0);
		} else {
			holder.iv.setScaleType(ScaleType.CENTER_CROP);
			fillContent(position - 1, holder);
		}
		convertView.setOnClickListener(this);

		return convertView;
	}

	private void fillContent(final int position, final Holder holder) {
		final ImageItem item = dataList.get(position);
		loadImageForView(holder.iv, item.getThumbnailPath(),
		        item.getImagePath());
		if (isChosen(position)) {
			holder.selected.setImageResource(R.drawable.check_on);
		} else {
			holder.selected.setImageResource(0);
		}
	}

	private void loadImageForView(final ImageView iv,
	        final String thumbnailPath,
	        final String imagePath) {
		if (!TextUtils.isEmpty(thumbnailPath)) {
			iv.setTag(thumbnailPath);
			localBitmap.asyncDecodeThumb(thumbnailPath, new LocalBitmap.BitmapReactor() {
				@Override
				public void onBitmap(Bitmap bitmap) {
					if (thumbnailPath.equals(iv.getTag()))
						iv.setImageBitmap(bitmap);
				}
			});
		} else {
			iv.setTag(imagePath);
			localBitmap.asyncDecodeSmall(imagePath, new LocalBitmap.BitmapReactor() {
				@Override
				public void onBitmap(Bitmap bitmap) {
					if (imagePath.equals(iv.getTag()))
						iv.setImageBitmap(bitmap);
				}
			});
		}
	}

	@Override
	public void onClick(View v) {

		Holder holder = (Holder) v.getTag();
		int position = holder.position;
		if (position == 0) {
			// take photo
			listener.onCamera();
			return;
		}
		--position;
		if (isChosen(position)) {
			unChose(position);
			holder.selected.setImageResource(0);
			changeCount();
		} else {
			if (chosed.size() < maxCount) {
				chose(position);
				holder.selected.setImageResource(R.drawable.check_on);
				changeCount();
			} else {
				ToastUtil.show("最多选择" + maxCount + "张图片");
			}
		}
	}

}
