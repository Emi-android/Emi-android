package com.proj.emi.loader;

import com.proj.emi.bean.UserBasicVO;
import com.proj.emi.loader.base.BaseGetLoader;

public class UserBasicLoader extends BaseGetLoader<UserBasicVO> {

    private final static String PATH = "user/homepage/%s";

    public void load(final RespReactor<UserBasicVO> reactor) {
        String url = genUrl(PATH, curUser.getUid());
        super.load(url, reactor);
    }

}
