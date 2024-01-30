package com.hotlcc.scim.server.spring.boot.config.properties;

import lombok.Data;

import java.util.List;

/**
 * 服务提供方配置
 *
 * @author Allen
 */
@Data
public class ServiceProviderProperties {
    /**
     * 过滤配置
     *
     * @see de.captaingoldfish.scim.sdk.common.resources.complex.FilterConfig
     */
    private FilterConfigProperties filterConfig = new FilterConfigProperties();
    /**
     * 排序配置
     *
     * @see de.captaingoldfish.scim.sdk.common.resources.complex.SortConfig
     */
    private SortConfigProperties sortConfig = new SortConfigProperties();
    /**
     * 修改密码配置
     *
     * @see de.captaingoldfish.scim.sdk.common.resources.complex.ChangePasswordConfig
     */
    private ChangePasswordConfigProperties changePassword = new ChangePasswordConfigProperties();
    /**
     * 批处理配置
     *
     * @see de.captaingoldfish.scim.sdk.common.resources.complex.BulkConfig
     */
    private BulkConfigProperties bulkConfig = new BulkConfigProperties();
    /**
     * Patch 配置
     *
     * @see de.captaingoldfish.scim.sdk.common.resources.complex.PatchConfig
     */
    private PatchConfigProperties patchConfig = new PatchConfigProperties();
    /**
     * ETag 配置
     *
     * @see de.captaingoldfish.scim.sdk.common.resources.complex.ETagConfig
     */
    private ETagConfigProperties eTagConfig = new ETagConfigProperties();
    /**
     * 认证 Scheme 配置
     *
     * @see de.captaingoldfish.scim.sdk.common.resources.multicomplex.AuthenticationScheme
     */
    private List<AuthenticationSchemeProperties> authenticationSchemes;
}
