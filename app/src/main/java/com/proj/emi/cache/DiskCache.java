package com.proj.emi.cache;

import java.io.InputStream;
import java.io.OutputStream;

interface DiskCache {

	public interface CacheSaver {
		public void saveIn(OutputStream os) throws Exception;
	}

	public interface TimeChecker {
		public boolean isAlive(long createTime);
	}

	public InputStream get(String key);

	public InputStream get(String key, TimeChecker checker);

	public void put(String key, CacheSaver obj);

	public void remove(String key);

}
