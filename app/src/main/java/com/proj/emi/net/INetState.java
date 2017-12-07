package com.proj.emi.net;


public interface INetState {

	public static final int NETWORK_ERROR = 0; // 网络错误标识位

	public static final int NETWORK_GPRS = 1;// 2G网络标识位

	public static final int NETWORK_WIFI = 2;// wifi网络标识位

	public void resetNetType();

	public int getNetState();

	public boolean isUnavailable();

	public boolean isWifi();

}
