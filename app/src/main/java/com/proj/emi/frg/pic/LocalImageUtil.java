package com.proj.emi.frg.pic;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 专辑帮助类
 * 
 * @author Administrator
 * 
 */
public class LocalImageUtil {

	// public interface BucketFilter {
	// public boolean accept(ImageBucket bucket);
	// }

	private ContentResolver cr;
	private static final int MIN_SIZE = 20 * 1024;

	// 缩略图列表
	private HashMap<String, String> thumbnailList = new HashMap<String, String>();
	private List<ImageItem> imageList = new ArrayList<ImageItem>();
	boolean hasBuildImageList = false;

	// boolean hasBuildImagesBucketList = false;
	// 专辑列表
	// List<HashMap<String, String>> albumList = new ArrayList<HashMap<String,
	// String>>();
	// HashMap<String, ImageBucket> bucketList = new HashMap<String,
	// ImageBucket>();

	public LocalImageUtil(Context context) {
		cr = context.getContentResolver();
	}

	/**
	 * 得到图片集
	 */
	public List<ImageItem> getImageList(boolean refresh) {
		if (refresh || !hasBuildImageList) {
			buildImageItemList();
		}
		return imageList;
	}

	/**
	 * 得到缩略图
	 */
	private void getThumbnail() {
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
		        Thumbnails.DATA };
		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
		        null, null, null);
		getThumbnailColumnData(cursor);
	}

	/**
	 * 从数据库中得到缩略图
	 * 
	 * @param cur
	 */
	private void getThumbnailColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			// int _id;
			int image_id;
			String image_path;
			// int _idColumn = cur.getColumnIndex(Thumbnails._ID);
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

			do {
				// _id = cur.getInt(_idColumn);
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);
				thumbnailList.put("" + image_id, image_path);
			} while (cur.moveToNext());
		}
	}

	/**
	 * 得到图片集
	 */
	private void buildImageItemList() {
		// long startTime = System.currentTimeMillis();

		// 构造缩略图索引
		getThumbnail();

		String columns[] = new String[] {
		        Media._ID, Media.DATA,
		        Media.SIZE, Media.DATE_ADDED,
		        Media.BUCKET_ID, Media.BUCKET_DISPLAY_NAME
		};
		String selector = Media.SIZE + ">" + MIN_SIZE + " AND "
		        + Media.BUCKET_DISPLAY_NAME + " NOT LIKE 'drawable%'";
		String order = Media.DATE_ADDED + " DESC LIMIT 100";

		Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns,
		        selector, null, order);

		if (cur.moveToFirst()) {
			int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
			int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
			int dateIndex = cur.getColumnIndexOrThrow(Media.DATE_ADDED);
			// int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
			// int bucketNameIndex = cur
			// .getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);

			// Log.d(TAG, ">_id>path>size>date>bucketId>bucketName>");
			do {
				String _id = cur.getString(photoIDIndex);
				String path = cur.getString(photoPathIndex);
				int size = cur.getInt(photoSizeIndex);
				long date = cur.getLong(dateIndex);
				// String bucketId = cur.getString(bucketIdIndex);
				// String bucketName = cur.getString(bucketNameIndex);

				// Log.d(TAG, ">" + _id + ">" + path + ">" + size + ">"
				// + date + ">" + bucketId + ">" + bucketName);

				ImageItem imageItem = new ImageItem();
				imageItem.imageId = _id;
				imageItem.imagePath = path;
				imageItem.thumbnailPath = thumbnailList.get(_id);
				imageItem.imageSize = size;
				imageItem.time = date;
				imageList.add(imageItem);

			} while (cur.moveToNext());
		}

		hasBuildImageList = true;
		// long endTime = System.currentTimeMillis();
		// Log.d(TAG, "use time: " + (endTime - startTime) + " ms");
	}

}
