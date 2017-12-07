package com.hcb.util;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.content.ClipboardManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UiTool {

    public static Bitmap createBitmap(ListView listView) {
        int height, rootHeight = 0;
        final int width = listView.getWidth();
        final ListAdapter listAdapter = listView.getAdapter();
        final int listItemNum = listAdapter.getCount();
        final List<View> childViews = new ArrayList<View>(listItemNum);
        View itemView;
        for (int pos = 0; pos < listItemNum; ++pos) {
            itemView = listAdapter.getView(pos, null, listView);
            itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            childViews.add(itemView);
            rootHeight += itemView.getMeasuredHeight();
        }

        height = rootHeight;
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(getBgColor(listView));

        Bitmap itemBitmap;
        int childHeight;
        int yPos = 0;
        for (int pos = 0; pos < childViews.size(); ++pos) {
            itemView = childViews.get(pos);
            childHeight = itemView.getMeasuredHeight();
            itemBitmap = viewToBitmap(itemView, width, childHeight);
            if (itemBitmap != null) {
                canvas.drawBitmap(itemBitmap, 0, yPos, null);
            }
            yPos += childHeight;
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }

    private static int getBgColor(View view) {
        Drawable dr = view.getBackground();
        if (dr instanceof ColorDrawable) {
            return ((ColorDrawable) dr).getColor();
        }
        return 0;
    }

    private static Bitmap viewToBitmap(View view, int viewWidth, int viewHeight) {
        view.layout(0, 0, viewWidth, viewHeight);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = null;
        try {
            int width = view.getWidth();
            int height = view.getHeight();
            if (width != 0 && height != 0) {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                view.layout(0, 0, width, height);
                view.draw(canvas);
            }
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

    public static void strikeTextView(final TextView tv) {
        final Paint p = tv.getPaint();
        p.setStrikeThruText(true);
        p.setAntiAlias(true);
    }

    public static void recycleBitmap(Bitmap... bmps) {
        if (null == bmps || bmps.length == 0) {
            return;
        }
        for (Bitmap bmp : bmps) {
            if (null != bmp && !bmp.isRecycled()) {
                try {
                    bmp.recycle();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void copyToClipboard(final Context ctx, final CharSequence text) {
        if (null == ctx || null == text) {
            return;
        }
        ((ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE))
                .setPrimaryClip(ClipData.newPlainText(null, text));
    }

    public static CharSequence readClipboard(final Context ctx) {
        if (null == ctx) {
            return null;
        }
        ClipData clip = ((ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip();
        if (null == clip || clip.getItemCount() == 0) {
            return null;
        }
        return clip.getItemAt(0).getText();
    }

    public static boolean isReachTop(AbsListView listView) {
        return null != listView
                && listView.getFirstVisiblePosition() == 0
                && listView.getChildCount() > 0
                && listView.getChildAt(0).getTop() >= listView.getPaddingTop();
    }

    public static void listenClick(final View.OnClickListener listener,
                                   final View... views) {
        if (null != views) {
            for (View v : views) {
                if (null != v) {
                    v.setOnClickListener(listener);
                }
            }
        }
    }

}
