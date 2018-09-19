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
package com.alibaba.dubbo.common.serialize;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Object input.
 *
 * @author qian.lei
 */
public interface ObjectInput extends DataInput {// read finish

    //对象输入流，dubbo自定义
    /**
     * read object.
     *
     * @return object.
     */
    Object readObject() throws IOException, ClassNotFoundException;

    /**
     * read object.
     *
     * @param cls object type.
     * @return object.
     */
    <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException;

    /**
     * read object.
     *
     * @param cls object type.
     * @return object.
     */
    <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException;

}