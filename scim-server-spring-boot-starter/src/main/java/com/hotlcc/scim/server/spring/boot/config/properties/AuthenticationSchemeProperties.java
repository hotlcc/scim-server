package com.hotlcc.scim.server.spring.boot.config.properties;

import lombok.Data;

/**
 * 认证 Scheme 配置
 *
 * @author Allen
 * @see de.captaingoldfish.scim.sdk.common.resources.multicomplex.AuthenticationScheme
 */
@Data
public class AuthenticationSchemeProperties {
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 类型
     */
    private String type;
    private String specUri;
    private String documentationUri;
    /**
     * 主要的
     */
    private Boolean primary;
    private String display;
    private String value;
    private String ref;
}
