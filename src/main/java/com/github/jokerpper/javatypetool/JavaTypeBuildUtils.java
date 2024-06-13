/*
 *
 * Copyright (c) 2024-2xxx, joker-pper (https://github.com/joker-pper).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.github.jokerpper.javatypetool;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * 构建java type工具类
 *
 * @author joker-pper 2024年05月15日 下午16:20:27
 */
public class JavaTypeBuildUtils {

    private JavaTypeBuildUtils() {

    }

    /**
     * 构建常见List&lt;T&gt;类型
     *
     * @param actualClass 泛型所对应的实际class
     * @return type
     */
    public static Type getParameterizedTypeList(Class<?> actualClass) {
        return getParameterizedType(List.class, actualClass);
    }

    /**
     * 构建复杂List&lt;T&gt;类型
     *
     * @param actualType 泛型所对应的实际type
     * @return type
     */
    public static Type getParameterizedTypeList(Type actualType) {
        return getParameterizedType(List.class, new Type[]{actualType});
    }

    /**
     * 构建常见Set&lt;T&gt;类型
     *
     * @param actualClass 泛型所对应的实际class
     * @return type
     */
    public static Type getParameterizedTypeSet(Class<?> actualClass) {
        return getParameterizedType(Set.class, actualClass);
    }

    /**
     * 构建复杂Set&lt;T&gt;类型
     *
     * @param actualType 泛型所对应的实际type
     * @return type
     */
    public static Type getParameterizedTypeSet(Type actualType) {
        return getParameterizedType(Set.class, new Type[]{actualType});
    }

    /**
     * 构建类型
     *
     * @param rawType     对象class类型
     * @param actualClass 泛型所对应的实际class
     * @return type
     */
    public static Type getParameterizedType(Class<?> rawType, Class<?> actualClass) {
        return getParameterizedType(rawType, new Class[]{actualClass});
    }

    /**
     * 构建类型
     *
     * @param rawType       对象class类型
     * @param actualClasses 多个泛型所对应的实际class数组
     * @return type
     */
    public static Type getParameterizedType(Class<?> rawType, Class<?>[] actualClasses) {
        return getParameterizedType(rawType, (Type[]) actualClasses);
    }

    /**
     * 构建类型
     *
     * @param rawType             对象class类型
     * @param actualTypeArguments 多个泛型所对应的实际type数组
     * @return type
     */
    public static Type getParameterizedType(Class<?> rawType, Type[] actualTypeArguments) {
        return getParameterizedType(rawType, actualTypeArguments, null);
    }

    /**
     * 构建类型
     *
     * @param rawType             对象class类型
     * @param actualTypeArguments 多个泛型所对应的实际type数组
     * @param ownerType           ownerType
     * @return type
     */
    public static Type getParameterizedType(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
        return new JavaParameterizedTypeImpl(rawType, actualTypeArguments, ownerType);
    }

}
