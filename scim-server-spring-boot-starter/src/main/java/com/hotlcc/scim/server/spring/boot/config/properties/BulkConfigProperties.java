package com.hotlcc.scim.server.spring.boot.config.properties;

import lombok.Data;

/**
 * 批处理配置
 *
 * @author Allen
 * @see de.captaingoldfish.scim.sdk.common.resources.complex.BulkConfig
 */
@Data
public class BulkConfigProperties {
    /**
     * 是否支持
     */
    private Boolean supported = false;
    /**
     * 最大操作数
     */
    private Integer maxOperations;
    /**
     * 最大载荷数
     */
    private Long maxPayloadSize;
    /**
     * 返回资源开关
     */
    private Boolean returnResourcesEnabled;
    /**
     * 返回默认资源开关
     */
    private Boolean returnResourcesByDefault;
    /**
     * 支持批量获取
     */
    private Boolean supportBulkGet;
}
