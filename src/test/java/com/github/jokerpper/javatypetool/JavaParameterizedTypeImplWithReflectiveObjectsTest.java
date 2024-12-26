package com.github.jokerpper.javatypetool;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * @author joker-pper 2024年12月26日 下午15:20:21
 */
public class JavaParameterizedTypeImplWithReflectiveObjectsTest {

    static Class TARGET_CLASS = null;
    static Method TARGET_CLASS_MAKE_METHOD = null;

    @BeforeClass
    public static void beforeClass() {
        Assume.assumeTrue("环境条件检查不通过，跳过执行", isEnvironmentSatisfied());
    }

    private static boolean isEnvironmentSatisfied() {
        //检查环境条件，返回布尔值
        int jdkVersion = getJdkVersion();
        if (jdkVersion > 11) {
            return false;
        }
        init();
        return TARGET_CLASS != null;
    }

    private static void init() {
        Class targetClass = findClass("sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl");
        if (targetClass == null) {
            return;
        }

        TARGET_CLASS = targetClass;
        try {
            TARGET_CLASS_MAKE_METHOD = TARGET_CLASS.getMethod("make", Class.class, Type[].class, Type.class);
        } catch (NoSuchMethodException e) {
        }
    }

    static int getJdkVersion() {
        //获取标准java版本
        String javaVersion = System.getProperty("java.specification.version");
        int javaVersionInt;
        if (javaVersion.startsWith("1.")) {
            //1.7 1.8
            javaVersionInt = Integer.parseInt(javaVersion.substring(javaVersion.indexOf(".") + 1));
        } else {
            //11 17
            javaVersionInt = Integer.parseInt(javaVersion);
        }
        return javaVersionInt;
    }

    static Class findClass(String className) {
        Class targetClass = null;
        try {
            targetClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
        }
        return targetClass;
    }


    static Type makeByReflectiveObjects(Class<?> rawType,
                                        Type[] actualTypeArguments,
                                        Type ownerType) {

        try {
            return (Type) TARGET_CLASS_MAKE_METHOD.invoke(TARGET_CLASS, rawType, actualTypeArguments, ownerType);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            if (e instanceof InvocationTargetException) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
            }
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testListStringTypeWithReflectiveObjectsParameterizedTypeImpl() {
        Type actual = new JavaParameterizedTypeImpl(List.class, new Type[]{String.class}, null);

        Type expected = makeByReflectiveObjects(List.class, new Type[]{String.class}, null);

        validateSameType(actual, expected);

        //验证一下equals other
        Assert.assertNotEquals(actual, List.class);
        Assert.assertNotEquals(expected, List.class);
    }

    @Test
    public void testListEmptyTypeWithReflectiveObjectsParameterizedTypeImpl() {
        String targetClassName = "java.lang.reflect.MalformedParameterizedTypeException";
        Class targetClass = findClass(targetClassName);
        Assume.assumeTrue(String.format("%s不存在", targetClassName), targetClass != null);

        Assert.assertThrows(targetClass, () -> new JavaParameterizedTypeImpl(List.class, new Type[]{}, null));
        Assert.assertThrows(targetClass, () -> makeByReflectiveObjects(List.class, new Type[]{}, null));
    }


    @Test
    public void testMyGenericClassWithReflectiveObjectsParameterizedTypeImpl() {
        Type actual = new JavaParameterizedTypeImpl(MyGenericClass.class, new Type[]{String.class}, null);
        Type expected = makeByReflectiveObjects(MyGenericClass.class, new Type[]{String.class}, null);

        validateSameType(actual, expected);

        actual = new JavaParameterizedTypeImpl(MyGenericClass.class, new Type[]{String.class}, this.getClass());
        expected = makeByReflectiveObjects(MyGenericClass.class, new Type[]{String.class}, this.getClass());

        validateSameType(actual, expected);
    }

    @Test
    public void testMockOwnerTypeIsParameterizedTypeWithReflectiveObjectsParameterizedTypeImpl() {
        Type ownerType = new ParameterizedTypeReference<Map.Entry<String, ? extends Number>>() {
        }.getType();

        Type actual = new JavaParameterizedTypeImpl(MyGenericClass.class, new Type[]{String.class}, ownerType);

        Type expected = makeByReflectiveObjects(MyGenericClass.class, new Type[]{String.class}, ownerType);

        validateSameType(actual, expected);

        ownerType = new ParameterizedTypeReference<List<Map<? extends Number, Map<? super String, Map<?, ?>>>>>() {
        }.getType();

        actual = new JavaParameterizedTypeImpl(MyGenericClass.class, new Type[]{String.class}, ownerType);

        expected = makeByReflectiveObjects(MyGenericClass.class, new Type[]{String.class}, ownerType);

        validateSameType(actual, expected);


        ownerType = new ParameterizedTypeReference<MyGenericClass<List<Map<? extends Number, Map<? super String, Map<?, ?>>>>>>() {
        }.getType();

        actual = new JavaParameterizedTypeImpl(MyGenericClass.class, new Type[]{String.class}, ownerType);

        expected = makeByReflectiveObjects(MyGenericClass.class, new Type[]{String.class}, ownerType);

        validateSameType(actual, expected);


        ownerType = new ParameterizedTypeReference<MyGenericClass<List<Map<? extends Number, Map<? super String, Map<?, MyGenericClass<Set<String>>>>>>>>() {
        }.getType();

        actual = new JavaParameterizedTypeImpl(MyGenericClass.class, new Type[]{String.class}, ownerType);

        expected = makeByReflectiveObjects(MyGenericClass.class, new Type[]{String.class}, ownerType);

        validateSameType(actual, expected);

    }

    void validateSameType(Type actual, Type expected) {

        //验证hash code一致
        Assert.assertEquals(expected.hashCode(), actual.hashCode());

        //验证相等
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(actual, expected);

        Assert.assertEquals(actual, actual);
        Assert.assertEquals(expected, expected);

        //验证type name 和 toString一致
        Assert.assertEquals(expected.getTypeName(), actual.getTypeName());
        Assert.assertEquals(expected.toString(), actual.toString());
    }


    public static class MyGenericClass<T> {
    }
}