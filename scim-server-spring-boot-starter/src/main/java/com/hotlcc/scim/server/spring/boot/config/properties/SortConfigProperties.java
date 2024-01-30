package com.hotlcc.scim.server.spring.boot.config.properties;

import lombok.Data;

/**
 * 排序配置
 *
 * @author Allen
 * @see de.captaingoldfish.scim.sdk.common.resources.complex.SortConfig
 */
@Data
public class SortConfigProperties {
    /**
     * 是否支持
     */
    private Boolean supported = false;
}
