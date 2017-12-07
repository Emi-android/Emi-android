package com.proj.emi.frg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.proj.emi.GlobalBeans;
import com.proj.emi.R;
import com.proj.emi.act.NaviActivity;
import com.proj.emi.biz.CurrentUser;
import com.hcb.util.KeyboardUtil;

public abstract class TitleFragment extends Fragment {

    protected final GlobalBeans beans;
    protected final CurrentUser curUser;
    protected NaviActivity act;

    protected View rootView;

    public TitleFragment() {
        beans = GlobalBeans.getSelf();
        curUser = beans.getCurUser();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (null == act) {
            act = (NaviActivity) activity;
        }
    }

    @Override
    public void onDetach() {
        act = null;
        super.onDetach();
    }

    public void setActivity(NaviActivity act) {
        this.act = act;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public int getTitleId() {
        return 0;
    }

    public String getTitleName() {
        return null;
    }

    public boolean hideLeftArrow() {
        return false;
    }

    protected boolean isAlive() {
        return null != act && !this.isDetached() && !act.isFinishing();
    }

    protected void hideKeyboard() {
        KeyboardUtil.hideKeyboard(act);
    }

    ProgressDialog dialog;

    protected void showProgressDialog(final String title, final String msg) {
        if (null == dialog) {
            dialog = ProgressDialog.show(act, title, msg);
        } else {
            dialog.setTitle(title);
            dialog.setMessage(msg);
            dialog.show();
        }
    }

    protected void showProgressDialog(final int title, final int msg) {
        showProgressDialog(getString(title), getString(msg));
    }

    protected void dismissDialog() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
        }
    }

    private int _loadState = 0;
    protected final static int LOAD_IDLE = 0;
    protected final static int LOAD_LOADING = 1;
    protected final static int LOAD_ERROR = 2;
    protected final static int LOAD_EMPTY = 3;
    protected View footer;

    protected void showLoading() {
        showState(LOAD_LOADING);
    }

    protected void showEmpty() {
        showState(LOAD_EMPTY);
    }

    protected void showError() {
        showState(LOAD_EMPTY);
    }

    private void showState(int state) {
        if (footer == null) {
            footer = View.inflate(act, R.layout.foot_loading, null);
            ((ViewGroup) rootView.getParent()).addView(footer);
        }
        _loadState = state;
        refreshFoot();
    }

    protected void refreshFoot() {
        if (null == footer) {
            return;
        }
        final ImageView iv = (ImageView) footer.findViewById(R.id.foot_image);
        final TextView tv = (TextView) footer.findViewById(R.id.foot_text);
        switch (this._loadState) {
            case LOAD_IDLE:
                footer.setVisibility(View.GONE);
                break;
            case LOAD_LOADING:
                footer.setVisibility(View.VISIBLE);
                iv.setImageResource(R.mipmap.loading);
                iv.startAnimation(AnimationUtils.loadAnimation(act,
                        R.anim.rotate_self));
                tv.setText(R.string.loading);
                tv.setOnClickListener(null);
                break;
            case LOAD_ERROR:
                footer.setVisibility(View.VISIBLE);
                iv.clearAnimation();
                iv.setImageResource(R.mipmap.warning);
                tv.setText(R.string.load_err_retry);
                tv.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        reloadData();
                    }
                });
                break;
            case LOAD_EMPTY:
                footer.setVisibility(View.VISIBLE);
                iv.clearAnimation();
                iv.setVisibility(View.GONE);
                tv.setText(R.string.empty_fornow);
                break;
        }
    }

    protected void reloadData() {
    }
}
