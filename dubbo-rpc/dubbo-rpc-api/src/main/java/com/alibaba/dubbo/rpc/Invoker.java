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
package com.alibaba.dubbo.rpc;

import com.alibaba.dubbo.common.Node;

/**
 * Invoker. (API/SPI, Prototype, ThreadSafe)  领域模型：实体域
 *
 * @author william.liangf
 * @see com.alibaba.dubbo.rpc.Protocol#refer(Class, com.alibaba.dubbo.common.URL)
 * @see com.alibaba.dubbo.rpc.InvokerListener
 * @see com.alibaba.dubbo.rpc.protocol.AbstractInvoker
 */
/**
 * Invoker是提供者还是消费者调用的？ 解：服务的执行体
 * @csy-v2 一次接口的调用就是一个invoker吗？
 * invoker里面可以有多个interfaces，不仅是一个
 */
public interface Invoker<T> extends Node { // invoker（执行者，执行具体的调用）
    //Invoker，调用者对应一个服务接口，通过invoke方法执行调用，参数为Invocation，返回值为Result
    //继承Node节点，每个调用者就是一个节点
    /**
     * get service interface.
     *
     * @return service interface.
     */
    Class<T> getInterface();

    /**
     * invoke.
     *
     * @param invocation
     * @return result
     * @throws RpcException
     */

    /**
     * 这个方法用途？ 解：执行方法调用
     */
    Result invoke(Invocation invocation) throws RpcException;

}