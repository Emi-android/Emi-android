package com.proj.emi.loader.base;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.hcb.util.Md5;
import com.proj.emi.AppConsts;
import com.proj.emi.biz.CurrentUser;
import com.hcb.http.HttpProvider;
import com.proj.emi.model.base.InBody;
import com.proj.emi.model.base.OutBody;
import com.proj.emi.model.base.OutHead;
import com.hcb.util.LoggerUtil;
import com.hcb.util.ReflectUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public abstract class BasePostLoader<OUTBODY extends OutBody, INBODY extends InBody> extends AbsLoader {

    private final static Logger LOG = LoggerFactory.getLogger(BasePostLoader.class);

    @Override
    protected Logger logger() {
        return LOG;
    }

    private OUTBODY curLoading = null;

    protected OutHead genDefaultOutHead() {
        final OutHead head = new OutHead();
        final CurrentUser user = beans.getCurUser();
        if (null != user) {
            head
                    .setCid(user.getCid())
                    .setUid(user.getUid())
                    .setAccessToken(user.getToken())
                    .setRequestTime(new Date().getTime() + "")
                    .setPassword(user.getPassword());
        }
        head.setAppVersion(beans.getAppInfo().getVersionName());
//        final Locator.Coordinate2D c2d = Locator.getCoordinate();
//        if (null != c2d) {
//            head.setLatitude(String.valueOf(c2d.latitude)).setLongitude(String.valueOf(c2d.longitude));
//        }
        return head;
    }

    protected void load(final String uri, final OUTBODY body, final RespReactor reactor) {
        if (null == uri || null == body) {
            logger().warn("error! NULL params in calling load()");
            return;
        }
        if (isDuplicateReq(curLoading, body)) {
            LoggerUtil.i(logger(), "Ignore a duplicate load(). uri:{}", uri);
            return;
        }
        OutHead outHead = genDefaultOutHead();
        String url = uri + "?access_token=" + outHead.getAccessToken()
                + "&user_uuid=" + outHead.getUid()
                + "&request_time=" + outHead.getRequestTime()
                + "&sign=" + pinjie(body, outHead)
                + "&app_version=" + outHead.getAppVersion()
                + "&os_version=" + outHead.getAppOs();
        httpProvider.post(url, buildReqJson(body), new HttpProvider.RespStringListener() {
            @Override
            public void onResp(String str) {
                dataBack(reactor, parseObj(str));
            }
        });
    }

    protected String buildReqJson(final OUTBODY body) {
//        JSONObject jo = new JSONObject();
//        jo.put(JSON_PACKAGE, body);
//        String str = jo.getString(JSON_PACKAGE);
//        return str;
        return body.toString();
    }

    private String pinjie(OUTBODY body, OutHead outHead){
        String unSign = body.toString();
        String[] strs = unSign.replace("{", "")
                .replace("}", "").replace("\"", "")
                .replace(" ", "").split(",");
        TreeMap<String, String> tm = new TreeMap<>();
        for (int i = 0; i<strs.length; i++){
            String[] strss = strs[i].split(":");
            tm.put(strss[0], strss[1]);
        }
        tm.put("access_token", outHead.getAccessToken());
        tm.put("user_uuid", outHead.getUid());
        tm.put("request_time", outHead.getRequestTime());
        String str = tm.toString().replace("{", "")
                .replace("}", "").replace(",", "&").replace(" ", "")
                + "&key=" + AppConsts.KEY;

        return Md5.sha256Encrypt(str);
    }

    @Override
    protected Class<INBODY> inBodyClass() {
        return ReflectUtil.getClassGenricType(getClass(), 1);
    }

    protected boolean isDuplicateReq(final OUTBODY origin, final OUTBODY fresh) {
        return null != origin && origin.equals(fresh);
    }

}
