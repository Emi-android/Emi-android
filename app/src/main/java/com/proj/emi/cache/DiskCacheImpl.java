package com.proj.emi.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.jakewharton.disklrucache.DiskLruCache;
import com.jakewharton.disklrucache.DiskLruCache.Snapshot;
import com.hcb.util.Md5;

class DiskCacheImpl implements DiskCache {

	private DiskLruCache cache;

	public DiskCacheImpl(final File path, final int ver, final int max) {
		try {
			cache = DiskLruCache.open(path, ver, 1, max);
		} catch (IOException e) {
		}
	}

	@Override
	public void put(final String key, final CacheSaver saver) {
		if (null == cache || null == saver) {
			return;
		}
		final String diskCacheKey = Md5.encode(key);
		DiskLruCache.Editor editor = null;
		OutputStream os = null;
		try {
			if (null != cache.get(diskCacheKey)) {
				cache.remove(diskCacheKey);
			}
			editor = cache.edit(diskCacheKey);
			if (null != editor) {
				os = editor.newOutputStream(0);
				saver.saveIn(os);
				editor.commit();
				cache.flush();
			}
		} catch (Exception e) {
			try {
				editor.abort();
			} catch (Exception e1) {
			}
		} finally {
			try {
				os.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public InputStream get(final String key) {
		return get(key, null);
	}

	@Override
	public InputStream get(final String key, final TimeChecker checker) {
		if (null == cache) {
			return null;
		}
		final String diskCacheKey = Md5.encode(key);
		if (null != checker &&
		        !checker.isAlive(getCreateTime(diskCacheKey))) {
			return null;
		}
		Snapshot snapshot = null;
		try {
			snapshot = cache.get(diskCacheKey);
		} catch (IOException e) {
		}
		if (null != snapshot) {
			return snapshot.getInputStream(0);
		}
		return null;
	}

	private long getCreateTime(final String diskCacheKey) {
		final File file = new File(genCacheFilename(diskCacheKey));
		if (null != file && file.exists()) {
			return file.lastModified();
		}
		return 0;
	}

	private String genCacheFilename(final String diskCacheKey) {
		return cache.getDirectory().getAbsolutePath()
		        + "/" + diskCacheKey + ".0";
	}

	@Override
	public void remove(String key) {
		if (null == cache || null == key) {
			return;
		}
		try {
			cache.remove(Md5.encode(key));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
