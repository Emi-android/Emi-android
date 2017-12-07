package com.proj.emi;

import android.app.Application;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class HcbApp extends Application {

    public static Application self;
    public static IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        api = WXAPIFactory.createWXAPI(this, AppConsts.APP_ID);
    }

}
