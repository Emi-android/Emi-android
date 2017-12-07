package com.proj.emi.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class BannerAdapter extends PagerAdapter {

    public interface BannerListener {
        void OnItemClick(int pos);
    }

    private final List<String> data;
    private boolean needChange;
    private BannerListener listener;

    public BannerAdapter(List<String> data) {
        this.data = data;
    }

    public BannerAdapter setListener(BannerListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void notifyDataSetChanged() {
        needChange = true;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return null == data ? 0 : data.size();
    }

    @Override
    public int getItemPosition(Object object) {
        if (needChange) {
            needChange = false;
            return POSITION_NONE;
        } else {
            return super.getItemPosition(object);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int size = data.size();
        int relPos = position % size;
        while (relPos < 0) {
            relPos = size + relPos;
        }
        imageView.setTag(relPos);

        if (null != listener) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick((Integer) v.getTag());
                }
            });
        }
        fillImageBitmap(imageView, data.get(relPos));
        container.addView(imageView);
        return imageView;
    }

    protected void fillImageBitmap(final ImageView iv, final String uri) {
        try {
            ImageLoader.getInstance().displayImage(uri, iv);
        } catch (Exception e) {
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
