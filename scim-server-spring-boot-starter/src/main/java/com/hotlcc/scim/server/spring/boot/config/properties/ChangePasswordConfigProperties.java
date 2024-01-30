package com.hotlcc.scim.server.spring.boot.config.properties;

import lombok.Data;

/**
 * 修改密码配置
 *
 * @author Allen
 * @see de.captaingoldfish.scim.sdk.common.resources.complex.ChangePasswordConfig
 */
@Data
public class ChangePasswordConfigProperties {
    /**
     * 是否支持
     */
    private Boolean supported = false;
}
