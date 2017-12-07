package com.proj.emi.act;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.proj.emi.GlobalBeans;
import com.proj.emi.R;
import com.proj.emi.biz.ActivityWatcher;

public class BaseFragAct extends FragmentActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        if (null == GlobalBeans.getSelf()) {
            GlobalBeans.initForMainUI(getApplication());
        }
        super.onCreate(arg0);
        changeBgIfNeed(getResources().getColor(R.color.global_bg));
    }

    protected void setStatuBarColor(int color) {
        changeBgIfNeed(color);
    }

    private void changeBgIfNeed(final int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            changeBg21(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            changeBg19(color);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void changeBg19(final int color) {
        getWindow().getDecorView().setBackgroundColor(color);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void changeBg21(final int color) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
        window.setNavigationBarColor(getResources().getColor(R.color.light_black));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityWatcher.setCurAct(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityWatcher.setCurAct(null);
    }
}
