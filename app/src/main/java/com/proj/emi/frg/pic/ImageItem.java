package com.proj.emi.frg.pic;

public class ImageItem {
	String imageId;
	String thumbnailPath;
	String imagePath;
	int imageSize;
	long time;

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
