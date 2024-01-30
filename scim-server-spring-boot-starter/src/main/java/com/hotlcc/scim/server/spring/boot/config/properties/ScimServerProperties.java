package com.hotlcc.scim.server.spring.boot.config.properties;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * SCIM Server 配置属性
 *
 * @author Allen
 */
@ConfigurationProperties("scim.server")
@Data
@RefreshScope
public class ScimServerProperties implements InitializingBean {
    /**
     * 基本路径
     */
    private String basePath = "/scim/v2";
    /**
     * 服务提供方配置
     */
    private ServiceProviderProperties serviceProvider = new ServiceProviderProperties();

    private void check() {
        Assert.notBlank(basePath, "The config 'scim.server.base-path' is error!");
    }

    @Override
    public void afterPropertiesSet() {
        check();
    }
}
