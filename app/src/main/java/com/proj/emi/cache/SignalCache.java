package com.proj.emi.cache;

public interface SignalCache extends AbsCache<String> {

	/**
	 * 获取有生命期限制的缓存
	 * 
	 * @param key key
	 * @param time 缓存有效期
	 */
	public String get(String key, long time);

}
