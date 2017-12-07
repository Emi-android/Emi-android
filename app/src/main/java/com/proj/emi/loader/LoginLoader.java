package com.proj.emi.loader;

import com.proj.emi.loader.base.AbsLoader;
import com.proj.emi.loader.base.BasePostLoader;
import com.proj.emi.model.login.LoginInBody;
import com.proj.emi.model.login.LoginOutBody;

public class LoginLoader extends BasePostLoader<LoginOutBody, LoginInBody> {

    private final static String PATH = "login/check/%s";

    public void login(final LoginOutBody body, AbsLoader.RespReactor reactor) {
     super.load(genUrl(PATH, body.getPhone()), body, reactor);
    }

}
