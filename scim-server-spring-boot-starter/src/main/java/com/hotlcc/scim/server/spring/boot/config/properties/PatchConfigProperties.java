package com.hotlcc.scim.server.spring.boot.config.properties;

import lombok.Data;

/**
 * Patch 配置
 *
 * @author Allen
 * @see de.captaingoldfish.scim.sdk.common.resources.complex.PatchConfig
 */
@Data
public class PatchConfigProperties {
    /**
     * 是否支持
     */
    private Boolean supported = false;
    /**
     * 忽略未知属性
     */
    private Boolean ignoreUnknownAttributes;
    private Boolean activateSailsPointWorkaround;
    private Boolean activateMsAzureWorkaround;
    private Boolean activateMsAzureValueSubAttributeWorkaround;
    private Boolean msAzureComplexSimpleValueWorkaroundActive;
}
