package com.github.jokerpper.javatypetool;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author joker-pper 2024年05月21日 上午11:33:15
 */
public class JavaTypeBuildUtilsTest {

    @Test
    public void test() {
        //验证List<String>
        Assert.assertEquals(new ParameterizedTypeReference<List<String>>() {
        }.getType(), JavaTypeBuildUtils.getParameterizedTypeWithList(String.class));


        //验证Set<String>
        Assert.assertEquals(new ParameterizedTypeReference<Set<String>>() {
        }.getType(), JavaTypeBuildUtils.getParameterizedTypeWithSet(String.class));


        //验证Map<String, Object>
        Assert.assertEquals(new ParameterizedTypeReference<Map<String, Object>>() {
        }.getType(), JavaTypeBuildUtils.getParameterizedType(Map.class, new Class[]{String.class, Object.class}));


        //验证List<Map<String, Object>>

        Assert.assertEquals(new ParameterizedTypeReference<List<Map<String, Object>>>() {
        }.getType(), JavaTypeBuildUtils.getParameterizedTypeWithList(JavaTypeBuildUtils.getParameterizedType(Map.class, new Class[]{String.class, Object.class})));


        //验证Set<Map<String, Object>>

        Assert.assertEquals(new ParameterizedTypeReference<Set<Map<String, Object>>>() {
        }.getType(), JavaTypeBuildUtils.getParameterizedTypeWithSet(JavaTypeBuildUtils.getParameterizedType(Map.class, new Class[]{String.class, Object.class})));


        //验证List<Map<String, Map<String, Object>>> -- 复杂
        Assert.assertEquals(new ParameterizedTypeReference<List<Map<String, Map<String, Object>>>>() {
                }.getType(), JavaTypeBuildUtils.getParameterizedTypeWithList(
                        JavaTypeBuildUtils.getParameterizedType(Map.class, new Type[]
                                {
                                        String.class,
                                        JavaTypeBuildUtils.getParameterizedType(Map.class, new Class[]{String.class, Object.class})
                                }
                        )
                )
        );

    }

}