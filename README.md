# scim-server

[![scim-server](https://img.shields.io/badge/Github-scim--server-1f883d.svg)](https://github.com/hotlcc/scim-server)
[![scim-server](https://img.shields.io/badge/Gitee-scim--server-fe7300.svg)](https://gitee.com/hotlcc/scim-server)
[![License](https://img.shields.io/badge/License-BSD--3--Clause-blue.svg)](LICENSE)

## 1、介绍

基于 [Captain-P-Goldfish/SCIM-SDK](https://github.com/Captain-P-Goldfish/SCIM-SDK) 的 Spring 版本。

## 2、Spring Boot 环境使用说明

```xml
<dependency>
  <groupId>com.hotlcc.scim.server</groupId>
  <artifactId>scim-server-spring-boot-starter</artifactId>
  <version>${scim-server.version}</version>
</dependency>
```

### 2.1、创建“资源类型”JSON定义

参考 [How to use the server implementation](https://github.com/Captain-P-Goldfish/SCIM-SDK/wiki/How-to-use-the-server-implementation) 中第1点。

### 2.2、创建“资源Schema”JSON定义

参考 [How to use the server implementation](https://github.com/Captain-P-Goldfish/SCIM-SDK/wiki/How-to-use-the-server-implementation) 中第2点。

### 2.3、创建“BaseResourceNode”实现

类似 [How to use the server implementation](https://github.com/Captain-P-Goldfish/SCIM-SDK/wiki/How-to-use-the-server-implementation) 中第4点。

示例：

```java
public class PersonResourceNode extends BaseResourceNode {
    private final static String SCHEMA = "urn:hotlcc:params:scim:schemas:demo:2.0:Person";

    public PersonResourceNode() {
        setSchemas(Collections.singleton(SCHEMA));
    }

    public String getPersonCode() {
        return getStringAttribute(FieldNames.PERSON_CODE).orElse(null);
    }

    public void setPersonCode(String personCode) {
        setAttribute(FieldNames.PERSON_CODE, personCode);
    }

    public String getPersonName() {
        return getStringAttribute(FieldNames.PERSON_NAME).orElse(null);
    }

    public void setPersonName(String personName) {
        setAttribute(FieldNames.PERSON_NAME, personName);
    }

    private interface FieldNames {
        String PERSON_CODE = "personCode";
        String PERSON_NAME = "personName";
    }
}
```

### 2.4、创建“BaseResourceHandler”实现

类似 [How to use the server implementation](https://github.com/Captain-P-Goldfish/SCIM-SDK/wiki/How-to-use-the-server-implementation) 中第5点。

示例：

```java
@EndpointDefinition(
    resourceTypePath = "classpath:scim/person-resource-type.json",
    resourceSchemaPath = "classpath:scim/person-resource-schema.json"
)
@Component
public class PersonResourceHandler extends BaseResourceHandler<PersonResourceNode> {
    /**
     * demo
     */
    private PersonResourceNode buildPersonResourceNode() {
        PersonResourceNode resourceNode = new PersonResourceNode();
        resourceNode.setPersonCode("test");
        resourceNode.setPersonName("张三");
        return resourceNode;
    }

    @Override
    public PersonResourceNode createResource(PersonResourceNode resource, Context context) {
        System.out.println("createResource");
        return buildPersonResourceNode();
    }

    @Override
    public PersonResourceNode getResource(String id, List<SchemaAttribute> attributes, List<SchemaAttribute> excludedAttributes, Context context) {
        System.out.println("getResource");
        return buildPersonResourceNode();
    }

    @Override
    public PartialListResponse<PersonResourceNode> listResources(long startIndex, int count, FilterNode filter, SchemaAttribute sortBy, SortOrder sortOrder, List<SchemaAttribute> attributes, List<SchemaAttribute> excludedAttributes, Context context) {
        System.out.println("listResources");
        List<PersonResourceNode> list = Collections.singletonList(buildPersonResourceNode());
        PartialListResponse.PartialListResponseBuilder<PersonResourceNode> builder = PartialListResponse.builder();
        return builder.resources(Collections.singletonList(buildPersonResourceNode())).totalResults(1).build();
    }

    @Override
    public PersonResourceNode updateResource(PersonResourceNode resourceToUpdate, Context context) {
        System.out.println("updateResource");
        return buildPersonResourceNode();
    }

    @Override
    public void deleteResource(String id, Context context) {
        System.out.println("deleteResource");
    }
}
```

### 2.5、创建“BasicValidator”实现

如果不需要 Basic 认证可以忽略此步骤。

示例：

```java
@Component
public class SimpleBasicValidator implements BasicValidator {
    @Override
    public boolean validate(String username, String password) {
        return StrUtil.equals(username, "test")
            && StrUtil.equals(password, "123456");
    }
}
```

### 2.6、配置“application.yml”

配置项参考：[ScimServerProperties](scim-server-spring-boot-starter/src/main/java/com/hotlcc/scim/server/spring/boot/config/properties/ScimServerProperties.java)

## 3、Spring 环境使用说明

```xml
<dependency>
  <groupId>com.hotlcc.scim.server</groupId>
  <artifactId>scim-server-spring</artifactId>
  <version>${scim-server.version}</version>
</dependency>
```

Bean 配置参考 [ScimServerAutoConfig](scim-server-spring-boot-starter/src/main/java/com/hotlcc/scim/server/spring/boot/config/ScimServerAutoConfig.java)

