package com.proj.emi.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.proj.emi.R;
import com.hcb.util.KeyboardUtil;
import com.hcb.widget.AnimEndListener;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

public class BaseDialog extends DialogFragment {

    private boolean fullScreen = true;
    protected Activity act;
    @Nullable @BindView(R.id.dlg_whole) View dlgView;
    @Nullable @BindView(R.id.dlg_frame) View frmView;

    public BaseDialog() {
        this.setStyle(STYLE_NO_FRAME, R.style.BaseDialog);
    }

    public void notFullScreen() {
        this.fullScreen = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
    }

    @Override
    public void onResume() {
        if (fullScreen && null != dlgView) {
            dlgView.setFitsSystemWindows(true);
        }
        super.onResume();
        MobclickAgent.onPageStart(simpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(simpleName());
    }

    private String simpleName() {
        return getClass().getSimpleName();
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

    protected void hideKeyboard(View v) {
        KeyboardUtil.hideKeyboard(v);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animAppear();
    }

    @Override
    public void dismiss() {
        animDisAppear();
    }

    protected void animAppear() {
        animFadeIn();
    }

    protected void animDisAppear() {
        animFadeOut();
    }

    protected void animPopup() {
        animFadeIn();
        if (null != frmView) {
            frmView.startAnimation(AnimationUtils.loadAnimation(act, R.anim.in_from_bottom));
        }
    }

    protected void animPopDown() {
        if (null != frmView) {
            frmView.startAnimation(AnimationUtils.loadAnimation(act, R.anim.out_to_bottom));
        }
        animFadeOut();
    }

    private void superDismiss() {
        willDismiss();
        try {
            super.dismiss();
 	    } catch (Exception e) {
 	    }
    }

    protected void willDismiss() {
    }

    private void animFadeIn() {
        if (null == dlgView) {
            return;
        }
        dlgView.startAnimation(AnimationUtils.loadAnimation(act, R.anim.alpha_in));
    }

    private void animFadeOut() {
        if (null == dlgView) {
            superDismiss();
            return;
        }
        Animation anim = AnimationUtils.loadAnimation(act, R.anim.alpha_out);
        anim.setAnimationListener(new AnimEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                superDismiss();
            }
        });
        dlgView.startAnimation(anim);
    }

}
