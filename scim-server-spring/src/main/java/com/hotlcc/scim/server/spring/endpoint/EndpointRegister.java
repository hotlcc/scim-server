package com.hotlcc.scim.server.spring.endpoint;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotlcc.scim.server.spring.base.BaseResourceHandler;
import de.captaingoldfish.scim.sdk.server.endpoints.EndpointDefinition;
import de.captaingoldfish.scim.sdk.server.endpoints.ResourceEndpoint;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 端点注册器
 *
 * @author Allen
 */
@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class EndpointRegister implements InitializingBean {
    @NonNull
    private final ResourceEndpoint resourceEndpoint;
    @Setter
    private List<BaseResourceHandler<?>> resourceHandlers;
    @NonNull
    private final ResourceLoader resourceLoader;

    @Override
    public void afterPropertiesSet() {
        registerEndpoint();
    }

    /**
     * 注册端点
     */
    public void registerEndpoint() {
        if (CollUtil.isEmpty(resourceHandlers)) {
            log.warn("Not exists any BaseResourceHandler.");
            return;
        }

        resourceHandlers.stream()
            .filter(Objects::nonNull)
            .forEach(handler -> {
                String handlerClassName = handler.getClass().getName();
                try {
                    registerEndpoint(resourceEndpoint, handler, resourceLoader);
                    log.info("Register endpoint success for {}", handlerClassName);
                } catch (IOException e) {
                    log.error(StrUtil.format("Register endpoint fail for {}", handlerClassName), e);
                }
            });
    }

    private void registerEndpoint(@NonNull ResourceEndpoint resourceEndpoint, @NonNull BaseResourceHandler<?> handler, @NonNull ResourceLoader resourceLoader) throws IOException {
        Class<?> handlerClass = handler.getClass();

        com.hotlcc.scim.server.spring.endpoint.EndpointDefinition endpointDefinition = AnnotationUtils.findAnnotation(handlerClass, com.hotlcc.scim.server.spring.endpoint.EndpointDefinition.class);
        if (endpointDefinition == null) {
            return;
        }

        JsonNode resourceType = loadJsonDocument(resourceLoader, endpointDefinition.resourceTypePath());
        if (resourceType == null) {
            return;
        }
        JsonNode resourceSchema = loadJsonDocument(resourceLoader, endpointDefinition.resourceSchemaPath());
        if (resourceSchema == null) {
            return;
        }
        String[] resourceSchemaExtensionPaths = endpointDefinition.resourceSchemaExtensionPaths();
        List<JsonNode> resourceSchemaExtensions = null;
        if (ArrayUtil.isNotEmpty(resourceSchemaExtensionPaths)) {
            resourceSchemaExtensions = new ArrayList<>(resourceSchemaExtensionPaths.length);
            for (String resourceSchemaExtensionPath : resourceSchemaExtensionPaths) {
                if (StrUtil.isBlank(resourceSchemaExtensionPath)) {
                    continue;
                }
                resourceSchemaExtensions.add(loadJsonDocument(resourceLoader, resourceSchemaExtensionPath));
            }
        }

        resourceEndpoint.registerEndpoint(new EndpointDefinition(resourceType, resourceSchema, resourceSchemaExtensions, handler));
    }

    private JsonNode loadJsonDocument(@NonNull ResourceLoader resourceLoader, String path) throws IOException {
        if (StrUtil.isBlank(path)) {
            return null;
        }

        Resource resource = resourceLoader.getResource(path);
        try (InputStream is = resource.getInputStream()) {
            return new ObjectMapper().readTree(is);
        }
    }
}
