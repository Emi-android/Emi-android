package com.proj.emi.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.proj.emi.R;
import com.proj.emi.GlobalBeans;
import com.proj.emi.biz.ActivitySwitcher;
import com.hcb.util.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {

    private final static int SECOND = 1000;
    private int downCount = 3;

    private GlobalBeans beans;
    private Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome);

        GlobalBeans.initForMainUI(getApplication());
        beans = GlobalBeans.getSelf();
        uiHandler = beans.getHandler();

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Timer().scheduleAtFixedRate(timerTask, 0, SECOND);
            }
        }, 400);
        runInitTask();
    }

    private void runInitTask() {
        beans.getCurUser().fetchBasicInfo(null);
    }

    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (downCount > 0) {
                downCount--;
            } else {
                enterApp();
                this.cancel();
            }
        }
    };

    private void enterApp() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                enter();
            }
        });
    }

    private void enter() {
//        ActivitySwitcher.startFragment(this, BleTestFrg.class);
        ActivitySwitcher.start(this, MainActivity.class);
        finish();
    }

    private long backPressTime = 0;

    @Override
    public void onBackPressed() {
        final long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - backPressTime > 2 * SECOND) {
            backPressTime = uptimeMillis;
            ToastUtil.show(getString(R.string.press_again_to_leave));
        } else {
            onExit();
        }
    }

    private void onExit() {
        timerTask.cancel();
        finish();
        if (null != beans) {
            beans.onTerminate();
        }
    }

}
