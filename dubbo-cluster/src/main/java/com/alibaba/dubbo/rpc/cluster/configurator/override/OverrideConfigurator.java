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
package com.alibaba.dubbo.rpc.cluster.configurator.override;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.cluster.configurator.AbstractConfigurator;

/**
 * AbsentConfigurator
 *
 * @author william.liangf
 */
public class OverrideConfigurator extends AbstractConfigurator {/**@c 覆盖配置 */

    /**
     * 构建OverrideConfigurator实例
     * 1）调用父类AbstractConfigurator构造函数，初始化父类的URL configuratorUrl
     * 2）构建OverrideConfigurator实例
     */
    public OverrideConfigurator(URL url) {
        super(url);
    }

    /**
     * 配置处理
     * 1）获取到配置url中的参数集合
     * 2）将参数集合加到当前url中的参数集合
     */
    public URL doConfigure(URL currentUrl, URL configUrl) {
        return currentUrl.addParameters(configUrl.getParameters());
    }

}
