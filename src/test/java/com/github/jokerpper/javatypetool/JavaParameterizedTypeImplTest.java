package com.github.jokerpper.javatypetool;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * @author joker-pper 2024年05月20日 下午22:00:11
 */
public class JavaParameterizedTypeImplTest {

    @Test
    public void testListStringTypeWithReference() {
        Type actual = new JavaParameterizedTypeImpl(List.class, new Type[]{String.class}, null);

        Type expected = new ParameterizedTypeReference<List<String>>() {
        }.getType();

        validateSameType(actual, expected);

        //验证一下equals other

        Assert.assertNotEquals(actual, List.class);
        Assert.assertNotEquals(expected, List.class);
    }


    @Test
    public void testMyGenericClassWithReference() {
        Type actual = new JavaParameterizedTypeImpl(MyGenericClass.class, new Type[]{String.class}, null);

        Type expected = new ParameterizedTypeReference<MyGenericClass<String>>() {
        }.getType();

        validateSameType(actual, expected);

        actual = new JavaParameterizedTypeImpl(MyGenericClass.class, new Type[]{String.class}, this.getClass());
        expected = new ParameterizedTypeReference<MyGenericClass<String>>() {
        }.getType();

        validateSameType(actual, expected);
    }

    @Test
    public void testMockWithEquals() {

        Type a = new JavaParameterizedTypeImpl(Set.class, new Type[]{String.class}, null);

        Type b = new JavaParameterizedTypeImpl(List.class, new Type[]{String.class}, null);

        Assert.assertNotEquals(a, b);

        a = new JavaParameterizedTypeImpl(Set.class, new Type[]{String.class}, null);

        b = new JavaParameterizedTypeImpl(List.class, new Type[]{Long.class}, null);

        Assert.assertNotEquals(a, b);


        a = new JavaParameterizedTypeImpl(Set.class, new Type[]{String.class}, null);

        b = new JavaParameterizedTypeImpl(Set.class, new Type[]{Long.class}, null);

        Assert.assertNotEquals(a, b);

        a = new JavaParameterizedTypeImpl(Set.class, new Type[]{String.class}, null);

        b = new JavaParameterizedTypeImpl(Map.class, new Type[]{String.class, Long.class}, null);

        Assert.assertNotEquals(a, b);

        a = new JavaParameterizedTypeImpl(Set.class, new Type[]{String.class}, Set.class);

        b = new JavaParameterizedTypeImpl(Map.class, new Type[]{String.class, Long.class}, Map.class);

        Assert.assertNotEquals(a, b);
    }

    @Test
    public void testMockEmptyTypeArguments() {
        Type type = new JavaParameterizedTypeImpl(String.class, new Type[]{}, null);
        Assert.assertEquals(String.class.getName(), type.getTypeName());
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