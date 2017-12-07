package com.hcb.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

public final class StorageUtils {

    /**
     * 获取手机/data/data/files/[path]目录
     */
    public static File getPhoneDir(Context context, String path) {
        File file = context.getFileStreamPath(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取SDCARD目录
     */
    public static File getSdcardDir() {
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return Environment.getExternalStorageDirectory();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static File getCacheDir(Context ctx) {
        try {
            return ctx.getCacheDir();
        } catch (Exception e) {
        }
        return null;
    }

    public static File getFilesDir(Context ctx) {
        try {
            return ctx.getFilesDir();
        } catch (Exception e) {
        }

        return null;
    }

    public interface SaveListener {
        void saved(String path);
    }

    public static void saveBitmapToMedia(final Context ctx, final Bitmap bitmap, final SaveListener listener) {
        new AsyncTask<Object, Object, String>() {

            @Override
            protected String doInBackground(Object... params) {
                final String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bitmap, "", "");
                if (null != path) {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(new File(path)));
                    ctx.sendBroadcast(intent);
                }
                return path;
            }

            @Override
            protected void onPostExecute(String s) {
                if (null != listener) {
                    try {
                        listener.saved(s);
                    } catch (Exception e) {
                    }
                }
            }
        }.execute();
    }

}
