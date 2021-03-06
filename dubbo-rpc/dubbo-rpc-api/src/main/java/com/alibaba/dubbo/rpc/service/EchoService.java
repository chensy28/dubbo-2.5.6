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
package com.alibaba.dubbo.rpc.service;

/**
 * Echo service.
 * 回声测试用于检测服务是否可用，回声测试按照正常请求流程执行，能够测试整个调用是否通畅，可用于监控。
 * 所有服务自动实现 EchoService 接口，只需将任意服务引用强制转型为 EchoService，即可使用。
 *
 * @author qian.lei
 * @export
 */
public interface EchoService {// 为啥没有实现类？用途是啥？解：实现类是通过getProxy获取的

    /**
     * echo test.
     * 回声检查
     *
     * @param message message.
     * @return message.
     */
    Object $echo(Object message);

    /**
     * 10/24 是怎么与EchoFilter关联起来的？所有服务自动实现 EchoService 接口是在哪里实现的？ 11/03-done
     * 解：1）AbstractProxyFactory中getProxy获取代理实例时有用到
     *    2）EchoService没有实现类，是通过动态代理getProxy获取实例的
     */

}