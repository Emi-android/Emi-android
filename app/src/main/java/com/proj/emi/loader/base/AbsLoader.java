package com.proj.emi.loader.base;

import com.alibaba.fastjson.JSONObject;
import com.proj.emi.AppConsts;
import com.proj.emi.GlobalBeans;
import com.proj.emi.biz.CurrentUser;
import com.hcb.http.HttpProvider;
import com.proj.emi.model.base.BaseResp;
import com.proj.emi.model.base.InBody;
import com.proj.emi.model.base.InHead;
import com.proj.emi.net.INetState;

import org.slf4j.Logger;

import java.util.Locale;

public abstract class AbsLoader {
    protected final static String KEY_HEAD = "head";
    protected final static String KEY_DODY = "body";

    protected final static String JSON_PACKAGE = "json_package";
    protected final static String REQUEST_TIME = "request_time";
    protected final static String USER_UUID = "user_uuid";
    protected final static String ACCESS_TOKEN = "access_token";
    protected final static String APP_VERSION = "app_version";
    protected final static String OS_VERSIGN = "os_version";
    protected final static String SIGN = "sign";

    protected final static String CODE = "code";
    protected final static String MESSAGE = "message";
    protected final static String PRELOAD = "preload";

    protected final GlobalBeans beans;
    protected final HttpProvider httpProvider;
    protected final CurrentUser curUser;
    protected final INetState netState;

    protected AbsLoader() {
        this.beans = GlobalBeans.getSelf();
        this.httpProvider = beans.getHttpProvider();
        this.curUser = beans.getCurUser();
        this.netState = beans.getNetState();
    }

    protected String genUrl(final String path, final Object... params) {
        return AppConsts.HOST + String.format(Locale.getDefault(), path, params);
    }

    protected BaseResp parseObj(final String jsonStr) {
        if (null != jsonStr) {
            try {
                BaseResp resp = new BaseResp();
                JSONObject jo = JSONObject.parseObject(jsonStr);
                InHead inHead = new InHead();
                inHead.setReturnCode(jo.getString(CODE));
                inHead.setReturnDescription(jo.getString(MESSAGE));
                resp.setHead(inHead);
                resp.setBody(JSONObject.parseObject(jo.getString(PRELOAD), inBodyClass()).setResult(jo.getString(CODE)).setDescription(jo.getString(MESSAGE)));
                return resp;
            } catch (Exception e) {
                logger().error("json parse error:{}", e.getMessage());
                logger().error(jsonStr);
            }
        } else if (null != netState) {
            logger().error("Http Error. {}", netState.isUnavailable() ?
                    "Cause net broken!" : "But net Ok.");
        }
        return null;
    }

    protected void dataBack(final RespReactor reactor, final BaseResp resp) {
        if (null == reactor) {
            logger().warn("Null reactor!");
            return;
        }
        try {
            if (checkResp(reactor, resp)) {
                reactor.succeed(resp.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger().warn("Exception:{}", e.getLocalizedMessage());
        }
    }

    protected abstract Class<? extends InBody> inBodyClass();

    protected abstract Logger logger();

    public interface RespReactor<BODY extends InBody> {
        void succeed(BODY body);

        void failed(String code, String reason);
    }

    protected boolean isRespNoError(final BaseResp resp) {
        return null != resp
                && isReturnOk(resp.getHead().getReturnCode())
                && isBizOk(resp.getBody().getResult());
    }

    private final static String RETURN_OK = "0";
    private final static String BIZ_OK = "0";

    private static boolean isReturnOk(final String code) {
        return RETURN_OK.equals(code);
    }

    protected boolean isBizOk(final String code) {
        return null != code && code.startsWith(BIZ_OK);
    }

    protected boolean checkResp(final RespReactor reactor, final BaseResp resp) {
        if (null == resp || null == resp.getHead() || null == resp.getBody()) {
            reactor.failed(null, "网络错误");
            return false;
        }
        String code = resp.getHead().getReturnCode();
        String desc = resp.getHead().getReturnDescription();
        if (!isReturnOk(code)) {
            logger().warn("err--code:{},desc:{}", code, desc);
            reactor.failed(code, desc);
            return false;
        }
        code = resp.getBody().getResult();
        desc = resp.getBody().getDescription();
        if (!isBizOk(code)) {
            logger().warn("err--code:{},desc:{}", code, desc);
            reactor.failed(code, desc);
            return false;
        }
        return true;
    }

}
