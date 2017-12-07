package com.proj.emi.loader.base;

import com.hcb.http.HttpProvider;
import com.proj.emi.model.base.InBody;
import com.hcb.util.ReflectUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseGetLoader<INBODY extends InBody> extends AbsLoader {

    private final static Logger LOG = LoggerFactory.getLogger(BaseGetLoader.class);

    @Override
    protected Logger logger() {
        return LOG;
    }

    protected void load(final String uri, final RespReactor reactor) {
        httpProvider.get(uri, new HttpProvider.RespStringListener() {
            @Override
            public void onResp(String str) {
                dataBack(reactor, parseObj(str));
            }
        });
    }

    @Override
    protected Class<? extends InBody> inBodyClass() {
        return ReflectUtil.getClassGenricType(getClass(), 0);
    }

}
