package com.hotlcc.scim.server.spring.boot.config.properties;

import lombok.Data;

/**
 * ETag 配置
 *
 * @author Allen
 * @see de.captaingoldfish.scim.sdk.common.resources.complex.ETagConfig
 */
@Data
public class ETagConfigProperties {
    /**
     * 是否支持
     */
    private Boolean supported = false;
}
