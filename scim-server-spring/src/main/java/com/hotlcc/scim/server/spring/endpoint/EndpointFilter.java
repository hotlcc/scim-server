package com.hotlcc.scim.server.spring.endpoint;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import de.captaingoldfish.scim.sdk.common.constants.enums.HttpMethod;
import de.captaingoldfish.scim.sdk.common.response.ScimResponse;
import de.captaingoldfish.scim.sdk.server.endpoints.Context;
import de.captaingoldfish.scim.sdk.server.endpoints.ResourceEndpoint;
import de.captaingoldfish.scim.sdk.server.endpoints.authorize.Authorization;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 端点过滤器
 *
 * @author Allen
 */
@Slf4j
@RequiredArgsConstructor
public class EndpointFilter extends OncePerRequestFilter {
    @NonNull
    private final ResourceEndpoint resourceEndpoint;
    private final Authorization authorization;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String query = Optional.ofNullable(request.getQueryString())
            .filter(StrUtil::isNotBlank)
            .map(q -> StrUtil.format("?{}", q))
            .orElse(StrUtil.EMPTY);

        ScimResponse scimResponse = resourceEndpoint.handleRequest(
            request.getRequestURL().toString() + query,
            HttpMethod.valueOf(request.getMethod()),
            ServletUtil.getBody(request),
            ServletUtil.getHeaderMap(request),
            authorization == null ? null : new Context(authorization)
        );

        rewriteResponse(scimResponse, response);
    }

    /**
     * 重写响应
     */
    private void rewriteResponse(ScimResponse scimResponse, HttpServletResponse response) {
        if (scimResponse == null || response == null) {
            return;
        }
        // header
        Map<String, String> headers = scimResponse.getHttpHeaders();
        if (MapUtil.isNotEmpty(headers)) {
            headers.entrySet().stream()
                .filter(Objects::nonNull)
                .forEach(entry -> response.setHeader(entry.getKey(), entry.getValue()));
        }
        // 状态
        response.setStatus(scimResponse.getHttpStatus());
        // body
        try (OutputStream os = response.getOutputStream()) {
            IoUtil.writeUtf8(os, false, scimResponse.toString());
        } catch (IOException e) {
            log.error("Rewrite ScimResponse to HttpServletResponse Error.", e);
        }
    }
}
