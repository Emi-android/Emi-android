package com.proj.emi.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.proj.emi.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PagableAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory
            .getLogger(PagableAdapter.class);

    public static interface MoreLoader {
        public int pageSize();

        public void nextPage();
    }

    protected final static int TYPE_FOOT = 0;

    private final static int LOAD_IDLE = 0;
    private final static int LOAD_LOADING = 1;
    private final static int LOAD_ERROR = 2;

    protected final Activity act;
    public int pageSize;

    protected boolean _hasMore = true;
    private int _counter = 0;
    private int _loadState = 0;

    private MoreLoader _moreLoader;
    private View footer;
    private int footerState = 0;

    public PagableAdapter(final Activity act) {
        super();
        this.act = act;
    }

    public void setMoreLoader(final MoreLoader moreLoader) {
        this._moreLoader = moreLoader;
        this.pageSize = Math.abs(moreLoader.pageSize());
    }

    public void setNoMore() {
        _hasMore = false;
        notifyDataSetChanged();
    }

    public void setMore() {
        _hasMore = true;
        notifyDataSetChanged();
    }

    public void dataCleared() {
        _counter = 0;
    }

    public void onDataChange() {
        final int oldCount = this._counter;
        this._counter = getRealCount();
        if (this._counter > oldCount) {
            this._hasMore = this._counter - oldCount >= pageSize;
        }
        if (this._loadState != LOAD_ERROR) {
            this._loadState = LOAD_IDLE;
        }
    }

    protected abstract int getRealCount();

    protected void resetFootSize(View convertView) {
    }

    protected void loadMore() {
        if (null != this._moreLoader) {
            this._loadState = LOAD_LOADING;
            this.footerState = LOAD_LOADING;
            refreshFoot();
            footer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _moreLoader.nextPage();
                }
            }, 50);
        }
    }

    public void setLoadError() {
        this._loadState = LOAD_ERROR;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this._hasMore ? (1 + getRealCount()) : getRealCount();
    }

    @Override
    public int getViewTypeCount() {
        return this._hasMore ? 2 : 1;
    }

    @Override
    public int getItemViewType(final int position) {

        return (isFoot(position)) ? TYPE_FOOT : 1;
    }

    protected boolean isFoot(int position) {
        return this._hasMore && (position == getCount() - 1);
    }

    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        if (isFoot(position)) {
            return makeFoot();
        }
        if (convertView == null || null == convertView.getTag()) {
            convertView = makeItemView(position, parent);
        }
        bandData(position, convertView);
        return convertView;
    }

    protected abstract View makeItemView(int position, ViewGroup parent);

    protected abstract void bandData(
            int position, View convertView);

    protected View makeFoot() {
        if (footer == null) {
            footer = View.inflate(this.act, R.layout.foot_loading, null);
        } else if (null != footer.getParent() && !(footer.getParent() instanceof AdapterView)) {
            ((ViewGroup) footer.getParent()).removeView(footer);
        }
        if (footerState != _loadState) {
            footerState = _loadState;
            refreshFoot();
        }
        if (this._hasMore && this._loadState == LOAD_IDLE) {
            loadMore();
        }
        return footer;
    }

    private void refreshFoot() {
        final ImageView iv = (ImageView) footer.findViewById(R.id.foot_image);
        final TextView tv = (TextView) footer.findViewById(R.id.foot_text);
        switch (this.footerState) {
            case LOAD_IDLE:
                footer.setVisibility(View.INVISIBLE);
                break;
            case LOAD_LOADING:
                footer.setVisibility(View.VISIBLE);
                iv.setImageResource(R.mipmap.loading);
                iv.startAnimation(AnimationUtils.loadAnimation(act,
                        R.anim.rotate_self));
                tv.setText(R.string.loading);
                footer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // do nothing
                    }
                });
                break;
            case LOAD_ERROR:
                footer.setVisibility(View.VISIBLE);
                iv.clearAnimation();
                iv.setImageResource(R.mipmap.warning);
                tv.setText(R.string.load_err_retry);
                footer.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        loadMore();
                        refreshFoot();
                    }
                });
                break;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
