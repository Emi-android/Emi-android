package com.proj.emi;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import com.proj.emi.bean.AppInfo;
import com.proj.emi.bean.DeviceInfo;
import com.proj.emi.biz.CurrentUser;
import com.proj.emi.biz.EventCenter;
import com.proj.emi.cache.CacheCenter;
import com.proj.emi.cache.SignalCache;
import com.proj.emi.net.INetState;
import com.proj.emi.net.NetChangeReceiver;
import com.proj.emi.net.NetState;
import com.hcb.http.HttpProvider;
import com.hcb.util.DeviceHelper;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class GlobalBeans {

    private GlobalBeans(final Context ctx) {
        this.ctx = ctx;
    }

    public static void initForMainUI(final Context ctx) {
        self = new GlobalBeans(ctx);
        self.uiHandler = new Handler(ctx.getMainLooper());

        self.initBizObjects();
        self.initImageLoader();
        self.watchNetState();

//        MobclickAgent.openActivityDurationTrack(false);
//
//        PushManager.getInstance().initialize(ctx);
    }

    public static GlobalBeans getSelf() {
        return self;
    }

    public void onTerminate() {
        try {
            httpProvider.terminate();
            ImageLoader.getInstance().destroy();
            exeService.shutdown();
//            locator.destory();
            MobclickAgent.onKillProcess(ctx);
        } catch (Exception e) {
        }
        self = null;
    }

    private void initBizObjects() {
        device = DeviceHelper.readDeviceInfo(ctx);
        appInfo = DeviceHelper.readAppInfo(ctx);
        exeService = new ScheduledThreadPoolExecutor(3);
        cacheCenter = new CacheCenter(ctx);
        httpProvider = new HttpProvider(ctx);
        eventCenter = new EventCenter(uiHandler);
        netState = new NetState(ctx, eventCenter);
        curUser = new CurrentUser(ctx);
//        locator = new Locator(ctx);
//        locator.start();
    }

    private void watchNetState() {
        ctx.registerReceiver(new NetChangeReceiver(), new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private int checkPermission(final String permission) {
        return ContextCompat.checkSelfPermission(ctx, permission);
    }

    private void initImageLoader() {


//        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
//                PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.re
//            ActivityCompat.requestPermissions(activity,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    1);
//        }

        LruDiskCache imgCache = null;
        try {
            final String imgCacheDir = cacheCenter.imgCacheDir;
            imgCache = new LruDiskCache(new File(imgCacheDir),
                    new Md5FileNameGenerator(), CacheCenter.MAX_DISK_IMAGE);
        } catch (Exception e) {
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub_image) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.stub_image)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.stub_image)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                //.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();//构建完成
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(ctx)
                .memoryCache(new UsingFreqLimitedMemoryCache(CacheCenter.MAX_MEM_IMAGE))
                .diskCache(imgCache)
                .defaultDisplayImageOptions(options);
        ImageLoader.getInstance().init(builder.build());
    }

    public final static DisplayImageOptions avatarOpts = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.stub_avatar)
            .showImageForEmptyUri(R.mipmap.stub_avatar)
            .showImageOnFail(R.mipmap.stub_avatar)
            .cacheInMemory(true)//设置下载的图片是否缓存在内存中
            .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
            .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.ARGB_8888)//设置图片的解码类型//
            .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
            .build();//构建完成

    private static GlobalBeans self;

    private Context ctx;
    private HttpProvider httpProvider;
    private ScheduledExecutorService exeService;

    private Handler uiHandler;
    private EventCenter eventCenter;
    private INetState netState;

    private CacheCenter cacheCenter;
    private DeviceInfo device;// device(phone,pad,or other)'s information
    private AppInfo appInfo;// this beans's information
    private CurrentUser curUser;
//    private Locator locator;

    public Context getApp() {
        return ctx;
    }

    public DeviceInfo getDeviceInfo() {
        return device;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public CurrentUser getCurUser() {
        return curUser;
    }

    public HttpProvider getHttpProvider() {
        return httpProvider;
    }

    public Handler getHandler() {
        return uiHandler;
    }

    public EventCenter getEventCenter() {
        return eventCenter;
    }

    public INetState getNetState() {
        return netState;
    }

    public CacheCenter getCacheCenter() {
        return cacheCenter;
    }

    public SignalCache getSignalCache() {
        return cacheCenter.getSignalCache();
    }

    public ScheduledExecutorService getExecutorService() {
        return exeService;
    }

//    public Locator getLocator() {
//        return locator;
//    }
}
