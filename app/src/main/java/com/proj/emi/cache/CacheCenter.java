package com.proj.emi.cache;

import android.content.Context;

import com.hcb.util.FileUtil;
import com.hcb.util.StorageUtils;
import com.proj.emi.AppConsts;

import java.io.File;

public class CacheCenter {

    private final static int SIGNAL_VER = 2;
    private final static int M = 1024 * 1024;// 单位 M

    public final static String ROOT_DIR = AppConsts.CACHE_ROOT;
    private final static String CACHE = "cache";
    private final static String SIGNAL = "signal";
    private final static String IMAGE = "image";
//    private final static String UPLOAD = "upload";

    private final static String CAMERA = "camera";
    private static String cacheRoot;
    public static String imgCacheDir;
    public final static int MAX_MEM_IMAGE = 5 * M;
    public final static int MAX_DISK_IMAGE = 10 * M;

    private SignalCache signalCache;// Disk LRU Cache for signal responses

    public CacheCenter(final Context ctx) {
        final File sdcardDir = StorageUtils.getSdcardDir();
        if (null != sdcardDir && sdcardDir.exists()) {
            cacheOnSdcard(genSdcardCachePath(
                    sdcardDir, ctx.getPackageName()));
        } else {
            cacheInPhone(StorageUtils.getPhoneDir(ctx, CACHE));
        }
        imgCacheDir = FileUtil.linkFileName(ctx.getCacheDir().toString(), IMAGE);
//        imgCacheDir = ctx.getCacheDir().toString();
    }

    public SignalCache getSignalCache() {
        return signalCache;
    }

    private void cacheInPhone(final File file) {
        saveSignalRoot(file);
        signalCache = new SignalCacheImpl(
                new File(file, SIGNAL), SIGNAL_VER, 2 * M);
    }

    private void saveSignalRoot(File file) {
        cacheRoot = file.getAbsolutePath();
    }

    private void cacheOnSdcard(final File sdcardDir) {
        saveSignalRoot(sdcardDir);
        signalCache = new SignalCacheImpl(
                new File(sdcardDir, SIGNAL), SIGNAL_VER, 4 * M);
    }

    private static File genSdcardCachePath(File sdcardDir, String pkg) {
        return new File(sdcardDir, FileUtil.linkFileName(ROOT_DIR, pkg, CACHE));
    }

    public static void clearCameraTempFile() {
        FileUtil.deleteFile(cameraDir);
    }

    private static String cameraDir;

    public static File genTmpImgFile(final Context ctx) {
        try {
            final File cameraDir = getCameraDir(ctx);
            if (!cameraDir.exists()) {
                cameraDir.mkdirs();
            }
            final File file = new File(cameraDir, genTempCameraFile());
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    private static String genTempCameraFile() {
        return "camera_" + System.currentTimeMillis() + ".jpg";
    }

    private static File getCameraDir(final Context ctx) {
        File comeraDir = null;
        if (null == cameraDir) {
            File dir = StorageUtils.getSdcardDir();
            if (null != dir && dir.exists()) {
                comeraDir = new File(dir, FileUtil.linkFileName(ROOT_DIR,
                        ctx.getPackageName(), CAMERA));
            } else {
                comeraDir = StorageUtils.getPhoneDir(ctx,
                        FileUtil.linkFileName(CAMERA));
            }
            cameraDir = comeraDir.getAbsolutePath();
        } else {
            comeraDir = new File(cameraDir);
        }
        return comeraDir;
    }

    @SuppressWarnings(value = "unused")
    public static void clearSignalCache() {
        FileUtil.deleteFile(cacheRoot + File.separator + SIGNAL);
    }

    @SuppressWarnings(value = "unused")
    public static void clearCache() {
        FileUtil.deleteFile(cacheRoot);
    }
}
