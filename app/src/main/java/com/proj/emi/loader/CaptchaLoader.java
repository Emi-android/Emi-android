package com.proj.emi.loader;

import com.proj.emi.loader.base.AbsLoader;
import com.proj.emi.loader.base.BaseGetLoader;
import com.proj.emi.model.base.InBody;

public class CaptchaLoader extends BaseGetLoader<InBody> {

    private final static String PATH = "login/captcha/%s";

    public void load(final String phone, final AbsLoader.RespReactor reactor) {
        super.load(genUrl(PATH, phone), reactor);
    }
}
