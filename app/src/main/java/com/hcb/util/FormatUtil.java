package com.hcb.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * @author dylan.zhao
 * @ClassName: FormatUtil
 * @Description: 单位转换和格式化工具
 */
public class FormatUtil {

    public static int pixOfDip(final float dip) {
        return Math.round(dip * getDisplayMetrics().density);
    }

    public static int pixOfSp(final float sp) {
        return Math.round(sp * getDisplayMetrics().scaledDensity);
    }

    private static DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public static String formatMoney(final float money) {
        return new DecimalFormat("#,###.##").format(money);
    }

    public static String formatMoney(final int money) {
        return new DecimalFormat("#,###").format(money);
    }

    public static String formatDistance(final int meters) {
        if (meters <= 0) {
            return "0m";
        } else if (meters < 500) {
            return meters + "m";
        } else {
            return String.format(Locale.getDefault(), "%.1fkm", meters / 1000.0f);
        }
    }
}
