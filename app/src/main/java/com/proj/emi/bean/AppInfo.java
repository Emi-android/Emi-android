package com.proj.emi.bean;

public class AppInfo {

    private String packageName;
    private String versionName;
    private String channel;
    private int versionCode;

    public String getPackageName() {
        return packageName;
    }

    public AppInfo setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getVersionName() {
        return versionName;
    }

    public AppInfo setVersionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public AppInfo setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public AppInfo setVersionCode(int versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    @Override
    public String toString() {
        return "AppInfo [packageName=" + packageName + ", versionName="
                + versionName + ", channel=" + channel + ", versionCode="
                + versionCode + "]";
    }

}
