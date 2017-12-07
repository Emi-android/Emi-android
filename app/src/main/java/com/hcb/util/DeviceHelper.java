package com.hcb.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.proj.emi.bean.AppInfo;
import com.proj.emi.bean.DeviceInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class DeviceHelper {

    private final static int MAX_LEN = 32;

    // imsi 和 imei读不到时，此为默认值
    private final static String EMPTY = "123456789ABCDEF";

    private static final String KEY_CHANNEL = "UMENG_CHANNEL";
    private static final String DEFAULT_CHANNEL = "self";

    public static AppInfo readAppInfo(final Context context) {
        final String pkg = context.getPackageName();
        final AppInfo info = new AppInfo().setPackageName(pkg);
        try {
            final PackageInfo pkgInfo = context.getPackageManager()
                    .getPackageInfo(pkg, 0);
            info.setVersionCode(pkgInfo.versionCode)
                    .setVersionName(pkgInfo.versionName);
        } catch (Exception e) {
        }
        try {
            final ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(pkg, PackageManager.GET_META_DATA);
            final Object obj = appInfo.metaData.get(KEY_CHANNEL);
            info.setChannel((null != obj) ? String.valueOf(obj)
                    : DEFAULT_CHANNEL);
        } catch (Exception e) {
        }
        return info;
    }

    public static DeviceInfo readDeviceInfo(final Context context) {
        final DeviceInfo device = new DeviceInfo();
        final DisplayMetrics display = ScreenUtil.getDisplayMetrics(context);
        device.setOsVer(Build.VERSION.SDK_INT)
                .setScreenWidth(display.widthPixels)
                .setScreenHeight(display.heightPixels)
                .setBrand(getBrand())
                .setModel(getModel())
                .setImei(getIMEI(context))
                .setImsi(getIMSI(context))
                .setRamSize(getRamSize(context))
                .setRomSize(getRomSize(context))
                .setLocale(getLocale());

        return device;
    }

    private static String getBrand() {
        try {
            return (Build.BRAND.length() > MAX_LEN
                    ? Build.BRAND.substring(0, MAX_LEN)
                    : Build.BRAND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    private static String getModel() {
        try {
            return (Build.MODEL.length() > MAX_LEN
                    ? Build.MODEL.substring(0, MAX_LEN)
                    : Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getIMSI(final Context context) {
        String imsi = null;
        try {
            imsi = getTelephonyManager(context).getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (null != imsi && !imsi.isEmpty()) ? imsi : EMPTY;
    }

    private static String getIMEI(final Context context) {
        String imei = null;
        try {
            imei = getTelephonyManager(context).getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (null != imei && !imei.isEmpty()) ? imei : EMPTY;
    }

    private static int getRamSize(final Context context) {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader("/proc/meminfo");
            br = new BufferedReader(fr);
            String line = br.readLine();
            String[] arrayOfString = line.split("\\s+");
            return Integer.valueOf(arrayOfString[1]).intValue() / 1024;
        } catch (IOException e) {

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
        }
        return 0;
    }

    @SuppressWarnings("deprecation")
    private static int getRomSize(Context context) {
        try {
            final File root = Environment.getDataDirectory();
            final StatFs sf = new StatFs(root.getPath());
            final long total = sf.getBlockCount() * sf.getBlockSize();
            return (int) (total / 1024 / 1024);
        } catch (Exception e) {
        }
        return 0;
    }

    private static TelephonyManager getTelephonyManager(final Context context) {
        return (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    private static String getLocale() {
        return Locale.getDefault().toString();
    }
}
