package com.hotlcc.scim.server.spring.endpoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 端点定义注解
 *
 * @author Allen
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndpointDefinition {
    /**
     * 资源类型 JSON 定义文档路径，同时支持文件系统路径和ClassPath路径
     */
    String resourceTypePath();

    /**
     * 资源 Schema JSON 定义文档路径，同时支持文件系统路径和ClassPath路径
     */
    String resourceSchemaPath();

    /**
     * 资源 Schema 扩展 JSON 定义文档路径，同时支持文件系统路径和ClassPath路径
     */
    String[] resourceSchemaExtensionPaths() default {};
}
