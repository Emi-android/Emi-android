package com.hcb.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class BitmapUtil {

    /**
     * @param pathName 文件路径
     * @param width    宽度限制
     * @param height   高度限制
     * @return 先解码为尺寸接近的Bitmap，然后inside
     */
    public static Bitmap decodeInside(final String pathName, final int width, final int height) {
        final Bitmap bmp4Crop = decodeSmall(pathName, width, height);
        Bitmap result = null;
        if (null != bmp4Crop) {
            try {
                result = scaledInside(bmp4Crop, width, height);
            } catch (Exception e) {
                UiTool.recycleBitmap(bmp4Crop);
            }
        }
        return result;
    }

    /**
     * @param pathName 文件路径
     * @param width    宽度限制
     * @param height   高度限制
     * @return 先解码为尺寸接近的Bitmap，然后crop
     */
    public static Bitmap decodeCrop(final String pathName, final int width, final int height) {
        final Bitmap bmp4Crop = decodeSmall(pathName, width, height);
        Bitmap result = null;
        if (null != bmp4Crop) {
            try {
                result = scaledCrop(bmp4Crop, width, height);
            } catch (Exception e) {
                UiTool.recycleBitmap(bmp4Crop);
            }
        }
        return result;
    }

    public static Bitmap decodeSquare(final String pathName, final int width) {
        return decodeCrop(pathName, width, width);
    }

    private static Bitmap scaledCrop(final Bitmap source, final int width, final int height) {
        float w = source.getWidth(), h = source.getHeight();
        float scaleW = width / w, scaleH = height / h;
        float scale;
        int x = 0, y = 0;
        if (scaleW - scaleH > 0.01f) {
            scale = scaleW;
            if (height > 0) {
                y = (int) ((h - height / scale) / 2);
            }
            h = height / scale;
        } else if (scaleW - scaleH < -0.01f) {
            scale = scaleH;
            if (w > 0) {
                x = (int) ((w - width / scale) / 2);
            }
            w = width / scale;
        } else {
            scale = scaleH;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(source, x, y, (int) w, (int) h, matrix, true);
        } catch (Exception e) {
            UiTool.recycleBitmap(source);
        }
        return result;
    }

    public static Bitmap scaledInside(final Bitmap source, final int width, final int height) {
        float w = source.getWidth(), h = source.getHeight();
        float scaleW = width / w, scaleH = height / h;
        float scale;
        if (scaleW - scaleH < -0.01f) {
            scale = scaleW;
        } else {
            scale = scaleH;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(source, 0, 0, (int) w, (int) h, matrix, true);
        } catch (Exception e) {
            UiTool.recycleBitmap(source);
        }
        return result;
    }

    @SuppressWarnings(value = "unused")
    @Deprecated
    public static boolean compressImgFile(final String src,
                                          final File dest,
                                          final long maxLenght,
                                          final int maxSize) {

        ByteArrayOutputStream baos = null;
        try {
            baos = compressImageToOS(src, maxLenght, maxSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == baos) {
            return false;
        }
        try {
            return writeBaosToFile(dest, baos);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    public static ByteArrayOutputStream compressImageToOS(final String src, final long maxPixels, final int maxSide) {
        final Bitmap bmp = decodeInside(src, maxSide, maxSide);
        final CompressFormat format = awareFormat(src);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int options = 100;
        bmp.compress(format, options, baos);
        while (options > 0 && baos.toByteArray().length > maxPixels) {
            baos.reset();
            options -= 5;
            bmp.compress(format, options, baos);
        }
        bmp.recycle();
        return baos;
    }

    @SuppressWarnings(value = "unused")
    @Deprecated
    private static boolean writeBaosToFile(final File dest,
                                           final ByteArrayOutputStream baos) throws Exception {
        if (null == dest) {
            return false;
        }
        if (!dest.exists()) {
            dest.createNewFile();
        }
        final FileOutputStream fos = new FileOutputStream(dest);
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
        return true;
    }

    private static Bitmap decodeSmall(final String pathName, final int width, final int height) {
        final File dst = new File(pathName);
        if (null == dst || !dst.exists()) {
            return null;
        }
        BitmapFactory.Options opts = null;
        if (width > 0 && height > 0) {
            opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, opts);
            opts.inSampleSize = computeSampleSize(opts, width, height);
            opts.inJustDecodeBounds = false;
            opts.inInputShareable = true;
            opts.inPurgeable = true;
        }
        try {
            return BitmapFactory.decodeFile(pathName, opts);
        } catch (Throwable t) {
        }
        return null;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int aimW, int aimH) {
        double w = options.outWidth;
        double h = options.outHeight;
        int initialSize = (int) Math.min(Math.floor(w / aimW), Math.floor(h / aimH));
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

//    private static int computeSampleSize(BitmapFactory.Options options,
//                                         int minSideLength, int maxNumOfPixels) {
//        int initialSize = computeInitialSampleSize(options, minSideLength,
//                maxNumOfPixels);
//
//        int roundedSize;
//        if (initialSize <= 8) {
//            roundedSize = 1;
//            while (roundedSize < initialSize) {
//                roundedSize <<= 1;
//            }
//        } else {
//            roundedSize = (initialSize + 7) / 8 * 8;
//        }
//
//        return roundedSize;
//    }
//
//    private static int computeInitialSampleSize(BitmapFactory.Options options,
//                                                int minSideLength,
//                                                int maxNumOfPixels) {
//        double w = options.outWidth;
//        double h = options.outHeight;
//
//        int lowerBound = (maxNumOfPixels == -1) ? 1
//                : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
//        int upperBound = (minSideLength == -1) ? 128
//                : (int) Math.min(Math.floor(w / minSideLength),
//                Math.floor(h / minSideLength));
//
//        if (upperBound < lowerBound) {
//            return lowerBound;
//        }
//
//        if ((maxNumOfPixels == -1) &&
//                (minSideLength == -1)) {
//            return 1;
//        } else if (minSideLength == -1) {
//            return lowerBound;
//        } else {
//            return upperBound;
//        }
//    }

    public static CompressFormat awareFormat(final String name) {
        if (!StringUtil.isEmpty(name)) {
            CompressFormat format = awareMagic(name);
            if (null == format) {
                format = awareFileName(name);
            }
            if (null != format) {
                return format;
            }
        }
        return CompressFormat.JPEG;
    }

    /**
     * 分析文件扩展名，只能得到该文件名声称的压缩格式，不准确
     */
    private static CompressFormat awareFileName(final String name) {
        int split = name.lastIndexOf('.');
        if (split > 0 && split < name.length()) {
            if (name.substring(split + 1)
                    .toLowerCase(Locale.getDefault()).contains("png")) {
                return CompressFormat.PNG;
            } else {
                return CompressFormat.JPEG;
            }
        }
        return null;
    }

    /**
     * 分析文件二进制头中的‘魔数’，得到准确的压缩格式
     */
    private static CompressFormat awareMagic(String name) {
        return awareMagic(null, name);
    }

    /**
     * 分析文件二进制头中的‘魔数’，得到准确的压缩格式
     */
    public static CompressFormat awareMagic(Context ctx, String name) {
        final char[] buffer = FileUtil.readMagic(ctx, name, 8);
        if (null != buffer) {
            if (isMagicJpg(buffer)) {
                return CompressFormat.JPEG;
            }
            if (isMagicPng(buffer)) {
                return CompressFormat.PNG;
            }
        }
        return null;
    }

    private static boolean isMagicPng(final char[] buffer) {
        return MAGIC_PNG.equals(buffer);
    }

    private static boolean isMagicJpg(final char[] buffer) {
        return buffer[0] == MAGIC_JPG[0] && buffer[1] == MAGIC_JPG[1];
    }

    private final static char[] MAGIC_JPG = {0xFF, 0xd8};
    private final static char[] MAGIC_PNG = {0x89, 0x50, 0x4E, 0x47,
            0x0D, 0x0A, 0x1A, 0x0A};

}