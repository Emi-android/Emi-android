package com.proj.emi.loader.base;

import com.proj.emi.cache.SignalCache;
import com.hcb.http.HttpProvider;
import com.proj.emi.model.base.BaseResp;
import com.proj.emi.model.base.InBody;
import com.hcb.util.LoggerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CachedGetLoader<INBODY extends InBody> extends BaseGetLoader {

    private final static Logger LOG = LoggerFactory.getLogger(CachedGetLoader.class);

    @Override
    protected Logger logger() {
        return LOG;
    }

    protected final SignalCache signalCache;

    protected CachedGetLoader() {
        signalCache = beans.getSignalCache();
    }

    protected void loadCacheAccept(final String uri, final RespReactor reactor) {
        load(uri, true, true, reactor);
    }

    protected void loadCacheFirst(final String uri, final RespReactor reactor) {

        load(uri, true, false, reactor);
    }

    private void load(final String uri,
                      final boolean seeCache,
                      final boolean cacheOk,
                      final RespReactor reactor) {
        if (null == uri) {
            logger().warn("error! NULL params in calling load()");
            return;
        }
        final String cacheKey = genCacheKey(uri);
        if (seeCache) {
            String jsonStr = loadFromCache(cacheKey);
            if (null != jsonStr) {
                LoggerUtil.t(logger(), "Found cache: {}", uri);
                BaseResp resp = parseObj(jsonStr);
                dataBack(reactor, resp);
                if (cacheOk) {
                    return;
                }
            }
        }
        httpProvider.get(uri, new HttpProvider.RespStringListener() {
            @Override
            public void onResp(String str) {
                BaseResp resp = null;
                if (null != str) {
                    resp = parseObj(str);
                    if (isRespNoError(resp)) {
                        saveToCache(cacheKey, str);
                    }
                }
                dataBack(reactor, resp);
            }
        });
    }

    /**
     * 缓存有效期，缺省为永久有效
     */
    protected long getCacheTime() {
        return Long.MAX_VALUE;
    }

    /**
     * 缓存的key，子类如果需要缓存，必须重载此方法，不要返回null
     *
     * @param url : 是从load()中回调回来的参数;
     */
    protected abstract String genCacheKey(final String url);

    private void saveToCache(final String key, final String jsonStr) {
        if (null != key && null != signalCache) {
            LoggerUtil.t(logger(), "Saving response to cache.");
            signalCache.put(key, jsonStr);
        }
    }

    private String loadFromCache(final String key) {
        if (null != key && null != signalCache) {
            return signalCache.get(key, getCacheTime());
        }
        return null;
    }

    protected final static long HOUR = 3600 * 1000;// ms in one hour
}
