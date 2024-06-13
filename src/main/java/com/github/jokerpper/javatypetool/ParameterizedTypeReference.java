/*
 * Copyright 2002-2023 the original author or authors.
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * Copied from spring-projects spring-framework/spring-core ParameterizedTypeReference.java (commit id: 18966d0) And modify by joker-pper.
 *
 * <p>Modify Content （current modify mark is //modify to current project use）:
 *
 * <p>Replace verification : Assert.isInstanceOf / Assert.isTrue
 *
 * <p>public boolean equals(@Nullable Object other) -&gt; public boolean equals(Object other)
 *
 * <p>Modify Syntactic Sugar for Java8
 *
 * <p>Remove @since
 *
 * <p>-----------------------------------------------------
 *
 * <p>The purpose of this class is to enable capturing and passing a generic
 * {@link Type}. In order to capture the generic type and retain it at runtime,
 * you need to create a subclass (ideally as anonymous inline class) as follows:
 *
 * <pre class="code">
 * ParameterizedTypeReference&lt;List&lt;String&gt;&gt; typeRef = new ParameterizedTypeReference&lt;List&lt;String&gt;&gt;() {};
 * </pre>
 *
 * <p>The resulting {@code typeRef} instance can then be used to obtain a {@link Type}
 * instance that carries the captured parameterized type information at runtime.
 * For more information on "super type tokens" see the link to Neal Gafter's blog post.
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @author joker-pper
 * @param <T> the referenced type
 * @see <a href="https://gafter.blogspot.nl/2006/12/super-type-tokens.html">Neal Gafter on Super Type Tokens</a>
 * @see <a href="https://github.com/spring-projects/spring-framework/blob/main/spring-core/src/main/java/org/springframework/core/ParameterizedTypeReference.java">(Copied Link) ParameterizedTypeReference.java</a>
 */
public abstract class ParameterizedTypeReference<T> {

	private final Type type;


	protected ParameterizedTypeReference() {
		Class<?> parameterizedTypeReferenceSubclass = findParameterizedTypeReferenceSubclass(getClass());
		Type type = parameterizedTypeReferenceSubclass.getGenericSuperclass();
		//modify to current project use
		//Assert.isInstanceOf(ParameterizedType.class, type, "Type must be a parameterized type");
		if (!(type instanceof ParameterizedType)) {
			throw new IllegalArgumentException(String.format("Type must be a parameterized type: %s", type == null ? null : type.getTypeName()));
		}
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		//modify to current project use
		//Assert.isTrue(actualTypeArguments.length == 1, "Number of type arguments must be 1");
		if (actualTypeArguments.length != 1) {
			throw new IllegalArgumentException("Number of type arguments must be 1");
		}
		this.type = actualTypeArguments[0];
	}

	private ParameterizedTypeReference(Type type) {
		this.type = type;
	}


	public Type getType() {
		return this.type;
	}

	@Override
	//modify to current project use
	//public boolean equals(@Nullable Object other) {
	public boolean equals(Object other) {
		//modify to current project use
		//return (this == other || (other instanceof ParameterizedTypeReference<?> that && this.type.equals(that.type)));
		return (this == other || (other instanceof ParameterizedTypeReference &&
				this.type.equals(((ParameterizedTypeReference<?>) other).type)));
	}

	@Override
	public int hashCode() {
		return this.type.hashCode();
	}

	@Override
	public String toString() {
		return "ParameterizedTypeReference<" + this.type + ">";
	}


	/**
	 * Build a {@code ParameterizedTypeReference} wrapping the given type.
	 * @param type a generic type (possibly obtained via reflection,
	 * e.g. from {@link java.lang.reflect.Method#getGenericReturnType()})
	 * @param <T> the referenced type
	 * @return a corresponding reference which may be passed into
	 * {@code ParameterizedTypeReference}-accepting methods
	 */
	public static <T> ParameterizedTypeReference<T> forType(Type type) {
		//modify to current project use
		//return new ParameterizedTypeReference<>(type) {};
		return new ParameterizedTypeReference<T>(type) {};
	}

	private static Class<?> findParameterizedTypeReferenceSubclass(Class<?> child) {
		Class<?> parent = child.getSuperclass();
		if (Object.class == parent) {
			throw new IllegalStateException("Expected ParameterizedTypeReference superclass");
		}
		else if (ParameterizedTypeReference.class == parent) {
			return child;
		}
		else {
			return findParameterizedTypeReferenceSubclass(parent);
		}
	}

}