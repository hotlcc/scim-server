package com.hotlcc.scim.server.spring.authorization.basic;

/**
 * Basic 校验器
 *
 * @author Allen
 */
public interface BasicValidator {
    /**
     * 校验用户名和密码
     *
     * @param username 用户名
     * @param password 密码
     * @return 校验结果
     */
    boolean validate(String username, String password);
}
