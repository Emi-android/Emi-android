package com.proj.emi.biz;

import android.content.Context;

import com.proj.emi.GlobalBeans;
import com.proj.emi.bean.UserBasicVO;
import com.proj.emi.loader.UserBasicLoader;
import com.proj.emi.loader.base.AbsLoader;

public class CurrentUser {

    public interface UserInfoListener {
        void succeed();

        void failed(String reason);
    }

    private final Context ctx;

    private String uid;
    private String token;
    private String cid;
    private String password;// 密码
    private String phone;
    private float weight;// 体重

    public CurrentUser(final Context ctx) {
        this.ctx = ctx;

        uid = HtPrefs.getUid(ctx);
        token = HtPrefs.getToken(ctx);
        password = HtPrefs.getPassword(ctx);
        phone = HtPrefs.getPhone(ctx);

        weight = HtPrefs.getWeight(ctx);
    }

    public boolean isCurUser(final String uid) {
        return null != uid && uid.equals(this.uid);
    }

    public void logout() {
        setUid(null);
        setPassword(null);
        setPhone(null);
        userBasic = null;
    }

    public String getToken() {
        return token;
    }

    public CurrentUser setToken(String token) {
        this.token = token;
        HtPrefs.setToken(ctx, token);
        return this;
    }

    public String getUid() {
        return uid;
    }

    public CurrentUser setUid(final String uid) {
        this.uid = uid;
        HtPrefs.setUid(ctx, uid);
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        HtPrefs.setPhone(ctx, phone);
    }

    public String getPassword() {
        return password;
    }

    public CurrentUser setPassword(String password) {
        this.password = password;
        HtPrefs.setPassword(ctx, password);
        return this;
    }

    public float getWeight() {
        return weight;
    }

    public CurrentUser setWeight(float weight) {
        this.weight = weight;
        HtPrefs.setWeight(ctx, weight);
        return this;
    }

    public String getCid() {
        return cid;
    }

    public CurrentUser setCid(String cid) {
        this.cid = cid;
        return this;
    }

    private UserBasicVO userBasic;

    public UserBasicVO getUserBasic() {
        return userBasic;
    }

    public void fetchBasicInfo(final UserInfoListener listener) {
        if (userBasic != null) {
            notifySucceed(listener);
            return;
        }
        reloadBasic(listener);
    }

    public void reloadBasic(final UserInfoListener listener) {
        if (null == uid) {
            return;
        }
        new UserBasicLoader().load(new AbsLoader.RespReactor<UserBasicVO>() {
            @Override
            public void succeed(UserBasicVO body) {
                userBasic = body;
                notifySucceed(listener);
                GlobalBeans.getSelf().getEventCenter().send(new EventCenter.HcbEvent(EventCenter.EventType.EVT_LOAD_INFO));
            }

            @Override
            public void failed(String code, String reason) {
                notifyFailed(listener, reason);
            }
        });
    }

    private void notifySucceed(final UserInfoListener listener) {
        if (null != listener) {
            listener.succeed();
        }
    }

    private void notifyFailed(final UserInfoListener listener, final String reason) {
        if (null != listener) {
            listener.failed(reason);
        }
    }

}
