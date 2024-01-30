package com.hotlcc.scim.server.spring.base;

import de.captaingoldfish.scim.sdk.server.endpoints.ResourceHandler;

/**
 * 基础的资源处理器：所有的自定义资源处理器需要继承该类
 *
 * @author Allen
 */
public abstract class BaseResourceHandler<T extends BaseResourceNode> extends ResourceHandler<T> {
}
