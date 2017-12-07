package com.proj.emi.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
			notifyChanged();
		}
	}

	protected static INetChangeListener netChangeListener = null;

	public static void setNetChangeListener(final INetChangeListener listener) {
		netChangeListener = listener;
	}

	protected void notifyChanged() {
		if (netChangeListener != null) {
			netChangeListener.onNetChange();
		}
	}
}
