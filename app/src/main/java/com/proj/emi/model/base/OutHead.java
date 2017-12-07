package com.proj.emi.model.base;

import com.alibaba.fastjson.annotation.JSONField;

public class OutHead {

    @JSONField(name = "user_uuid") private String uid;// 	user_uuid	当前登录用户id	字符串	是
    @JSONField(name = "request_time") private String requestTime;//13位时间戳
    @JSONField(name = "access_token") private String accessToken;//用户token
    @JSONField(name = "app_version") private String appVersion;// app_version  字符串 app版本号
    @JSONField(name = "os_version") private String appOs = "ANDROID";// os_version 当前所使用的操作系统	字符串	是	IOS或ANDROID
    private String sign;// 签名

    private String cid;// 当前设备的client_id    字符串 是
    private String password;// 密码校验    字符串 是
    private String longitude;//当前登录用户的经度    字符串 否
    private String latitude;//当前登录用户的纬度    字符串 否

    public String getRequestTime() {
        return requestTime;
    }

    public OutHead setRequestTime(String requestTime) {
        this.requestTime = requestTime;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public OutHead setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPassword() {
        return password;
    }

    public OutHead setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public OutHead setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getAppOs() {
        return appOs;
    }

    public OutHead setAppOs(String appOs) {
        this.appOs = appOs;
        return this;
    }

    public String getCid() {
        return cid;
    }

    public OutHead setCid(String cid) {
        this.cid = cid;
        return this;
    }

    public String getLongitude() {
        return longitude;
    }

    public OutHead setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getLatitude() {
        return latitude;
    }

    public OutHead setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public OutHead setUid(String uid) {
        this.uid = uid;
        return this;
    }

    @Override
    public String toString() {
        return "OutHead{" +
                "uid='" + uid + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appOs='" + appOs + '\'' +
                ", sign='" + sign + '\'' +
                ", cid='" + cid + '\'' +
                ", password='" + password + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
