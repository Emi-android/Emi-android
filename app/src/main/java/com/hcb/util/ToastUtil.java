package com.hcb.util;

import android.widget.Toast;

import com.proj.emi.GlobalBeans;

public final class ToastUtil {

    private ToastUtil() {

    }

    private static Toast mToast;

    public static void show(final String title) {
        showToast(title, Toast.LENGTH_SHORT);
    }

    public static void showLong(final String title) {
        showToast(title, Toast.LENGTH_LONG);
    }

    private static void showToast(final String title, final int dur) {
        if (mToast == null) {
            mToast = Toast.makeText(GlobalBeans.getSelf().getApp(), title, dur);
        } else {
            mToast.setText(title);
        }
        mToast.show();
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
