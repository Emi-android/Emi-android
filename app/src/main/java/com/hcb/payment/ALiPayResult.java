package com.hcb.payment;import android.text.TextUtils;import java.util.Map;/** * Created by lzh on 2017/4/1. */public class ALiPayResult {	private String resultStatus;	private String result;	private String memo;	public ALiPayResult(Map<String, String> rawResult) {		if (rawResult == null) {			return;		}		for (String key : rawResult.keySet()) {			if (TextUtils.equals(key, "resultStatus")) {				resultStatus = rawResult.get(key);			} else if (TextUtils.equals(key, "result")) {				result = rawResult.get(key);			} else if (TextUtils.equals(key, "memo")) {				memo = rawResult.get(key);			}		}	}	@Override	public String toString() {		return "{\"memo\":\"" + memo				+ "\",\"result\":" + result + ",\"resultStatus\":\"" + resultStatus + "\"}";	}	/**	 * @return the resultStatus	 */	public String getResultStatus() {		return resultStatus;	}	/**	 * @return the memo	 */	public String getMemo() {		return memo;	}	/**	 * @return the result	 */	public String getResult() {		return result;	}}