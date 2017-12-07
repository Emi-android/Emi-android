package com.proj.emi.cache;

import com.proj.emi.AppConsts;
import com.hcb.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class SignalCacheImpl implements SignalCache {

    private DiskCache signalCache;// Disk LRU Cache for signal responses

    SignalCacheImpl(final File file, final int cacheVer, final int max) {
        signalCache = new DiskCacheImpl(file, cacheVer, max);
    }

    @Override
    public String get(final String key) {
        return get(key, Long.MAX_VALUE);
    }

    @Override
    public String get(final String key, final long time) {
        return parseCache(signalCache.get(key, new DiskCache.TimeChecker() {
            @Override
            public boolean isAlive(long createTime) {
                return System.currentTimeMillis() - createTime < time;
            }
        }));
    }

    private String parseCache(final InputStream is) {
        String value = null;
        if (null != is) {
            try {
                value = StringUtil.parseInStream(is);
            } catch (Exception e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }

    @Override
    public void put(final String key, final String value) {
        signalCache.put(key, new DiskCache.CacheSaver() {
            @Override
            public void saveIn(OutputStream os) throws Exception {
                os.write(value.getBytes(AppConsts.CHAR_SET));
            }
        });
    }

    @Override
    public void remove(String key) {
        signalCache.remove(key);
    }

}
