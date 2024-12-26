# java-type-tool

[![Java support](https://img.shields.io/badge/Java-8+-green?logo=java&logoColor=white)](https://openjdk.java.net/)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.joker-pper/java-type-tool.svg?label=Maven%20Central)](https://central.sonatype.com/search?q=io.github.joker-pper:java-type-tool)
[![Last SNAPSHOT](https://img.shields.io/nexus/snapshots/https/s01.oss.sonatype.org/io.github.joker-pper/java-type-tool?label=latest%20snapshot)](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/joker-pper/java-type-tool/)
[![Upload coverage reports to Codecov](https://github.com/joker-pper/java-type-tool/actions/workflows/ci.yml/badge.svg)](https://github.com/joker-pper/java-type-tool/actions/workflows/ci.yml)
[![codecov](https://codecov.io/github/joker-pper/java-type-tool/branch/main/graph/badge.svg?token=SAY24MUY9O)](https://codecov.io/github/joker-pper/java-type-tool)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

    轻量级java type工具类库.

> 引入方式

    <dependencies>
        <dependency>
            <groupId>io.github.joker-pper</groupId>
            <artifactId>java-type-tool</artifactId>
            <version>TAG</version>
        </dependency>
    </dependencies>       


----------

> 使用说明

```

ParameterizedTypeReference 用于获取Type，适用于手动指定的场景。（copied from spring-core ParameterizedTypeReference）

示例（获取List<String>类型）： 
new ParameterizedTypeReference<List<String>>(){}.getType();


JavaTypeBuildUtils 用于获取Type，适用于动态构造的场景。

示例（获取List<String>类型）： 
JavaTypeBuildUtils.getParameterizedTypeWithList(String.class);

示例（获取Set<String>类型）： 
JavaTypeBuildUtils.getParameterizedTypeWithSet(String.class);

示例（获取Map<String, Object>类型）： 
JavaTypeBuildUtils.getParameterizedType(Map.class, new Class[]{String.class, Object.class});

示例（获取List<Map<String, Object>>类型）： 
JavaTypeBuildUtils.getParameterizedTypeWithList(JavaTypeBuildUtils.getParameterizedType(Map.class, new Class[]{String.class, Object.class}));

```