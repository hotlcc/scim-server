package com.hotlcc.scim.server.spring.boot.config.properties;

import lombok.Data;

/**
 * 过滤配置
 *
 * @author Allen
 * @see de.captaingoldfish.scim.sdk.common.resources.complex.FilterConfig
 */
@Data
public class FilterConfigProperties {
    /**
     * 是否支持
     */
    private Boolean supported = false;
    /**
     * 最大结果数
     */
    private Integer maxResults;
}
