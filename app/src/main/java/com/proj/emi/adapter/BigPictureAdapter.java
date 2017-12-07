package com.proj.emi.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.proj.emi.GlobalBeans;
import com.proj.emi.R;

import com.hcb.util.StringUtil;
import com.hcb.widget.photoview.PhotoView;
import com.hcb.widget.photoview.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class BigPictureAdapter
        extends PagerAdapter implements PhotoViewAttacher.OnViewTapListener {

    final List<String> imgs;
    private OnClickListener clickListener;

    private boolean isAvatar = false;

    public BigPictureAdapter(final List<String> imgUrls) {
        this.imgs = imgUrls;
    }

    public BigPictureAdapter setClickListener(final OnClickListener listener) {
        this.clickListener = listener;
        return this;
    }

    public BigPictureAdapter setAvatar(boolean b) {
        this.isAvatar = b;
        return this;
    }

    @Override
    public int getCount() {
        return null != imgs ? imgs.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setImageResource(R.drawable.stub_image);

        photoView.setOnViewTapListener(this);
        loadImageForView(photoView, imgs.get(position));
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (null != clickListener) {
            clickListener.onClick(view);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ImageView) object).setTag(null);
        container.removeView((View) object);
    }

    private void loadImageForView(final ImageView iv, final String url) {
        if (StringUtil.isEmpty(url)) {
            iv.setImageResource(isAvatar ? R.mipmap.stub_avatar_big : R.drawable.stub_image);
            return;
        }
        final ImageLoader imgLoader = ImageLoader.getInstance();
        imgLoader.displayImage(url, iv, isAvatar ? GlobalBeans.avatarOpts : null,
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        imgLoader.displayImage(url, iv, bigImgOpts);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                    }
                }
        );
    }

    private final static DisplayImageOptions bigImgOpts = new DisplayImageOptions.Builder()
            .cacheInMemory(true)//设置下载的图片是否缓存在内存中
            .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
            .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
            .resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位
            .build();//构建完成
}
