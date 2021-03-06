/*
 * Copyright 1999-2012 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.cache.support.jcache;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.util.concurrent.TimeUnit;

/**
 * JCache
 *
 * @author william.liangf
 */
public class JCache implements com.alibaba.dubbo.cache.Cache {/**@c java缓存新标准 */

    private final Cache<Object, Object> store;

    public JCache(URL url) {
        String method = url.getParameter(Constants.METHOD_KEY, "");
        String key = url.getAddress() + "." + url.getServiceKey() + "." + method;
        // jcache 为SPI实现的全限定类名
        String type = url.getParameter("jcache");

        //java 缓存了解
        CachingProvider provider = type == null || type.length() == 0 ? Caching.getCachingProvider() : Caching.getCachingProvider(type);
        CacheManager cacheManager = provider.getCacheManager();
        Cache<Object, Object> cache = cacheManager.getCache(key);
        if (cache == null) {
            try {
                //configure the cache
                MutableConfiguration config =
                        new MutableConfiguration<Object, Object>()
                                .setTypes(Object.class, Object.class)
                                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MILLISECONDS, url.getMethodParameter(method, "cache.write.expire", 60 * 1000))))
                                .setStoreByValue(false)
                                .setManagementEnabled(true)
                                .setStatisticsEnabled(true);
                cache = cacheManager.createCache(key, config);
            } catch (CacheException e) {
                // 初始化cache 的并发情况
                cache = cacheManager.getCache(key);
            }
        }

        this.store = cache;
    }

    public void put(Object key, Object value) {
        store.put(key, value);
    }

    public Object get(Object key) {
        return store.get(key);
    }

    public static void main(String[] args) {
        JCache cache = new JCache(URL.valueOf("dubbo://172.16.120.89:20881/com.alibaba.dubbo.demo.CommonService?anyhost=true&application=demo-consumer&check=false&dubbo=2.0.0&dynamic=false&generic=false&interface=com.alibaba.dubbo.demo.CommonService&methods=test,sayHello,sayHello,sayHello&pid=90166&remote.timestamp=1598238030586&side=consumer&timestamp=1598239876167"));
        cache.put("test", 101);
        System.out.println(cache.get("test"));
        /**
         * @chen 此处为啥报？JCache是否能用
         * Error: A JNI error has occurred, please check your installation and try again
         * Exception in thread "main" java.lang.NoClassDefFoundError: javax/cache/CacheException
         *
         */
    }

}
