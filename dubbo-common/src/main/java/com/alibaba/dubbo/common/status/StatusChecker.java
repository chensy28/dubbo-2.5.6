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
package com.alibaba.dubbo.common.status;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * StatusChecker 检查服务依赖各种资源的状态
 *
 * @author william.liangf
 */
@SPI
public interface StatusChecker { //状态监测

    /**
     * check status
     *
     * @return status
     */
    Status check();

}