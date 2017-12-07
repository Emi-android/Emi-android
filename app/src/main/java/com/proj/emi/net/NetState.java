package com.proj.emi.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.proj.emi.biz.EventCenter;

public class NetState implements INetState, INetChangeListener {

	private int connectState = NETWORK_ERROR; // 当前网络状态

	private Context context;
	private EventCenter eventCenter;

	public NetState(Context ctx, EventCenter eventCenter) {
		this.context = ctx;
		this.eventCenter = eventCenter;
		resetNetType();
		NetChangeReceiver.setNetChangeListener(this);
	}

	@Override
	public int getNetState() {
		return connectState;
	}

	@Override
	public boolean isUnavailable() {
		return (NETWORK_ERROR == connectState);
	}

	@Override
	public boolean isWifi() {
		return NETWORK_WIFI == connectState;
	}

	@Override
	public void resetNetType() {
		onNetChange();
	}

	@Override
	public void onNetChange() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
		        .getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null == connectivityManager) {
			return;
		}
		connectState = NETWORK_ERROR;
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
			try {
				String nettypename = networkInfo.getTypeName();
				if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
				        || "wifi".equalsIgnoreCase(nettypename)) {
					connectState = NETWORK_WIFI;
				} else {
					connectState = NETWORK_GPRS;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		eventCenter.send(new EventCenter.HcbEvent(EventCenter.EventType.EVT_NET_STATE));

	}

}
