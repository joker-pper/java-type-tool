/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jokerpper.javatypetool;


import java.io.Serializable;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Copied from spring-projects spring-framework/spring-core ResolvableType.java(SyntheticParameterizedType) (commit id: 6df2764) And modify by joker-pper.
 *
 * <p>Modify Content:
 * <p>to compatible default mechanism
 *
 * @author Phillip Webb
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 * @author Yanming Zhou
 * @author joker-pper
 * @see <a href="https://github.com/spring-projects/spring-framework/blob/main/spring-core/src/main/java/org/springframework/core/ResolvableType.java">(Copied Link) ResolvableType.java(SyntheticParameterizedType)</a>
 */
final class JavaParameterizedTypeImpl implements ParameterizedType, Serializable {

    private final Type rawType;

    private final Type[] typeArguments;

    private final Type ownerType;

    public JavaParameterizedTypeImpl(Type rawType, Type[] typeArguments, Type ownerType) {
        Class<?> rawClass = (Class<?>) rawType;
        TypeVariable<?>[] typeParameters = rawClass.getTypeParameters();
        if (typeParameters.length != typeArguments.length) {
            throw new MalformedParameterizedTypeException();
        }

        this.rawType = rawType;
        this.typeArguments = typeArguments;
        this.ownerType = ownerType == null ? rawClass.getDeclaringClass() : ownerType;
    }

    @Override
    public String getTypeName() {
        String typeArgumentsName = "";
        if (this.typeArguments.length > 0) {
            StringJoiner stringJoiner = new StringJoiner(", ", "<", ">");
            for (Type argument : this.typeArguments) {
                stringJoiner.add(argument.getTypeName());
            }
            typeArgumentsName = stringJoiner.toString();
        }

        if (this.ownerType == null) {
            return this.rawType.getTypeName() + typeArgumentsName;
        } else {
            //compatible default mechanism
            StringBuilder builder = new StringBuilder(this.ownerType.getTypeName());
            builder.append("$");
            Class<?> rawClass = (Class<?>) this.rawType;
            if (this.ownerType instanceof ParameterizedType) {
                ParameterizedType ownerParameterizedType = (ParameterizedType) this.ownerType;
                String toReplaceStr = ((Class<?>) (ownerParameterizedType.getRawType())).getName() + "$";
                builder.append(rawClass.getName().replace(toReplaceStr, ""));
            } else {
                builder.append(rawClass.getSimpleName());
            }
            builder.append(typeArgumentsName);
            return builder.toString();
        }

    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public Type getRawType() {
        return this.rawType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return this.typeArguments;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ParameterizedType)) {
            return false;
        }
        ParameterizedType otherType = (ParameterizedType) other;
        return (Objects.equals(otherType.getOwnerType(), this.ownerType)
                && this.rawType.equals(otherType.getRawType())
                && Arrays.equals(this.typeArguments, otherType.getActualTypeArguments()));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.typeArguments) ^ Objects.hashCode(this.rawType) ^ Objects.hashCode(this.ownerType);
    }

    @Override
    public String toString() {
        return getTypeName();
    }
}
