package com.hotlcc.scim.server.spring.boot.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hotlcc.scim.server.spring.authorization.BasicAuthorization;
import com.hotlcc.scim.server.spring.authorization.basic.BasicValidator;
import com.hotlcc.scim.server.spring.base.BaseResourceHandler;
import com.hotlcc.scim.server.spring.boot.config.properties.AuthenticationSchemeProperties;
import com.hotlcc.scim.server.spring.boot.config.properties.ScimServerProperties;
import com.hotlcc.scim.server.spring.boot.config.properties.ServiceProviderProperties;
import com.hotlcc.scim.server.spring.endpoint.EndpointFilter;
import com.hotlcc.scim.server.spring.endpoint.EndpointRegister;
import de.captaingoldfish.scim.sdk.common.resources.ServiceProvider;
import de.captaingoldfish.scim.sdk.common.resources.complex.BulkConfig;
import de.captaingoldfish.scim.sdk.common.resources.complex.ChangePasswordConfig;
import de.captaingoldfish.scim.sdk.common.resources.complex.ETagConfig;
import de.captaingoldfish.scim.sdk.common.resources.complex.FilterConfig;
import de.captaingoldfish.scim.sdk.common.resources.complex.PatchConfig;
import de.captaingoldfish.scim.sdk.common.resources.complex.SortConfig;
import de.captaingoldfish.scim.sdk.common.resources.multicomplex.AuthenticationScheme;
import de.captaingoldfish.scim.sdk.server.endpoints.ResourceEndpoint;
import de.captaingoldfish.scim.sdk.server.endpoints.authorize.Authorization;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SCIM Server 自动配置
 *
 * @author Allen
 */
@Configuration
public class ScimServerAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    public ScimServerProperties scimServerProperties() {
        return new ScimServerProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceProvider serviceProvider(ScimServerProperties properties) {
        return buildServiceProvider(properties.getServiceProvider());
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceEndpoint resourceEndpoint(ServiceProvider serviceProvider) {
        return new ResourceEndpoint(serviceProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public EndpointRegister endpointRegister(ResourceEndpoint resourceEndpoint,
                                             @Autowired(required = false) List<BaseResourceHandler<?>> resourceHandlers,
                                             ResourceLoader resourceLoader) {
        return new EndpointRegister(resourceEndpoint, resourceHandlers, resourceLoader);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(BasicValidator.class)
    public Authorization authorization(BasicValidator basicValidator) {
        return new BasicAuthorization(basicValidator);
    }

    @Bean
    public FilterRegistrationBean<EndpointFilter> endpointFilter(ResourceEndpoint resourceEndpoint,
                                                                 @Autowired(required = false) Authorization authorization,
                                                                 ScimServerProperties properties) {
        FilterRegistrationBean<EndpointFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new EndpointFilter(resourceEndpoint, authorization));
        bean.setName(EndpointFilter.class.getSimpleName());
        bean.setOrder(Integer.MAX_VALUE);
        bean.addUrlPatterns(StrUtil.format("{}/*", properties.getBasePath()));
        return bean;
    }

    private ServiceProvider buildServiceProvider(@NonNull ServiceProviderProperties serviceProvider) {
        return ServiceProvider.builder()
            .filterConfig(
                FilterConfig.builder()
                    .supported(serviceProvider.getFilterConfig().getSupported())
                    .maxResults(serviceProvider.getFilterConfig().getMaxResults())
                    .build()
            )
            .sortConfig(
                SortConfig.builder()
                    .supported(serviceProvider.getSortConfig().getSupported())
                    .build()
            )
            .changePasswordConfig(
                ChangePasswordConfig.builder()
                    .supported(serviceProvider.getChangePassword().getSupported())
                    .build()
            )
            .bulkConfig(
                BulkConfig.builder()
                    .supported(serviceProvider.getBulkConfig().getSupported())
                    .maxOperations(serviceProvider.getBulkConfig().getMaxOperations())
                    .maxPayloadSize(serviceProvider.getBulkConfig().getMaxPayloadSize())
                    .returnResourcesEnabled(serviceProvider.getBulkConfig().getReturnResourcesEnabled())
                    .returnResourcesByDefault(serviceProvider.getBulkConfig().getReturnResourcesByDefault())
                    .supportBulkGet(serviceProvider.getBulkConfig().getSupportBulkGet())
                    .build()
            )
            .patchConfig(
                PatchConfig.builder()
                    .supported(serviceProvider.getPatchConfig().getSupported())
                    .ignoreUnknownAttributes(serviceProvider.getPatchConfig().getIgnoreUnknownAttributes())
                    .activateSailsPointWorkaround(serviceProvider.getPatchConfig().getActivateSailsPointWorkaround())
                    .activateMsAzureWorkaround(serviceProvider.getPatchConfig().getActivateMsAzureWorkaround())
                    .activateMsAzureValueSubAttributeWorkaround(serviceProvider.getPatchConfig().getActivateMsAzureValueSubAttributeWorkaround())
                    .msAzureComplexSimpleValueWorkaroundActive(serviceProvider.getPatchConfig().getMsAzureComplexSimpleValueWorkaroundActive())
                    .build()
            )
            .eTagConfig(
                ETagConfig.builder()
                    .supported(serviceProvider.getETagConfig().getSupported())
                    .build()
            )
            .authenticationSchemes(buildAuthenticationSchemes(serviceProvider.getAuthenticationSchemes()))
            .build();
    }

    private List<AuthenticationScheme> buildAuthenticationSchemes(List<AuthenticationSchemeProperties> propertiesList) {
        if (CollUtil.isEmpty(propertiesList)) {
            return Collections.singletonList(buildDefaultAuthenticationScheme());
        }
        return propertiesList.stream()
            .filter(Objects::nonNull)
            .map(properties -> AuthenticationScheme.builder()
                .name(properties.getName())
                .description(properties.getDescription())
                .type(properties.getType())
                .specUri(properties.getSpecUri())
                .documentationUri(properties.getDocumentationUri())
                .primary(properties.getPrimary())
                .display(properties.getDisplay())
                .value(properties.getValue())
                .ref(properties.getRef())
                .build())
            .collect(Collectors.toList());
    }

    private AuthenticationScheme buildDefaultAuthenticationScheme() {
        return AuthenticationScheme.builder()
            .name("Basic")
            .description("Basic Authentication")
            .type("basic")
            .build();
    }
}
