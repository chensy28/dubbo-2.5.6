/*
 * Copyright 1999-2011 Alibaba Group.
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
package com.alibaba.dubbo.rpc.protocol;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.support.ProtocolUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * abstract ProtocolSupport.
 *
 * @author qian.lei
 * @author william.liangf
 */
public abstract class AbstractProtocol implements Protocol {
    /**
     * 数据结构 -》
     *
     * 类继承关系：
     * 1）AbstractExporter实现Exporter接口
     *
     * 包含的数据：
     * 1）Set<Invoker<?>> invokers ：调用列表
     * 2）Map<String, Exporter<?>> exporterMap ：暴露服务的本地缓存
     *
     * 包含的功能：
     * 1）getServerShutdownTimeout()：获取服务停机超时时间
     * 2）destroy()：释放协议
     */

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 将serviceKey与Exporter映射起来，这是局部变量，属于各个对象，其它线程能用吗？怎样实现缓存效果？
     * 在什么时候缓存的？ 在AbstractProxyProtocol中的export中，在从本地缓存中没找到时，就会创建暴露对象，并且缓存下来
     */
    protected final Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<String, Exporter<?>>(); //服务暴露信息Exporter的缓存

    // 11/12 ConcurrentHashMap、ConcurrentHashSet原理学习实践
    protected final Set<Invoker<?>> invokers = new ConcurrentHashSet<Invoker<?>>(); // 11/12 这里怎么会有多个Invoker

    protected static String serviceKey(URL url) {
        return ProtocolUtils.serviceKey(url);
    }

    //serviceKey用在哪里
    protected static String serviceKey(int port, String serviceName, String serviceVersion, String serviceGroup) {
        return ProtocolUtils.serviceKey(port, serviceName, serviceVersion, serviceGroup);
    }

    @SuppressWarnings("deprecation")
    //微服务架构—优雅停机方案 https://my.oschina.net/yu120/blog/1788928
    protected static int getServerShutdownTimeout() {  //获取停机时间
        //优雅停机，超时控制，超过时间还没有完成操作，则强制退出
        int timeout = Constants.DEFAULT_SERVER_SHUTDOWN_TIMEOUT;
        String value = ConfigUtils.getProperty(Constants.SHUTDOWN_WAIT_KEY);
        if (value != null && value.length() > 0) {
            try {
                timeout = Integer.parseInt(value);
            } catch (Exception e) {
            }
        } else {
            value = ConfigUtils.getProperty(Constants.SHUTDOWN_WAIT_SECONDS_KEY);
            if (value != null && value.length() > 0) {
                try {
                    timeout = Integer.parseInt(value) * 1000;
                } catch (Exception e) {
                }
            }
        }

        return timeout;
    }

    /**
     * 协议的销毁
     * 1）本地缓存中的调用信息invoker移除：invokers.remove(invoker) 并销毁：invoker.destroy()
     * 2）本地缓存中的暴露信息exporter移除：exporterMap.remove(key) 并解除暴露：exporter.unexport()
     */
    public void destroy() {
        for (Invoker<?> invoker : invokers) {
            if (invoker != null) {
                invokers.remove(invoker); //缓存中移除调用者invoker
                try {
                    if (logger.isInfoEnabled()) {
                        logger.info("Destroy reference: " + invoker.getUrl());
                    }
                    invoker.destroy();
                } catch (Throwable t) {
                    logger.warn(t.getMessage(), t);
                }
            }
        }
        for (String key : new ArrayList<String>(exporterMap.keySet())) {
            Exporter<?> exporter = exporterMap.remove(key); //缓存中移除暴露exporter
            if (exporter != null) {
                try {
                    if (logger.isInfoEnabled()) {
                        logger.info("Unexport service: " + exporter.getInvoker().getUrl());
                    }
                    exporter.unexport();
                } catch (Throwable t) {
                    logger.warn(t.getMessage(), t);
                }
            }
        }
    }
}