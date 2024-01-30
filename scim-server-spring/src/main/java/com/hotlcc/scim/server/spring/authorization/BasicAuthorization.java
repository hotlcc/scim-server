package com.hotlcc.scim.server.spring.authorization;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.hotlcc.scim.server.spring.authorization.basic.BasicValidator;
import de.captaingoldfish.scim.sdk.server.endpoints.authorize.Authorization;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Basic 认证
 *
 * @author Allen
 */
@RequiredArgsConstructor
public class BasicAuthorization implements Authorization {
    @NonNull
    private final BasicValidator basicValidator;

    @Override
    public Set<String> getClientRoles() {
        return Collections.emptySet();
    }

    @Override
    public boolean authenticate(Map<String, String> httpHeaders, Map<String, String> queryParams) {
        String authorization = httpHeaders.get(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(authorization)) {
            return false;
        }
        String basicValue = authorization.replace("Basic ", StrUtil.EMPTY);
        if (StrUtil.isBlank(basicValue)) {
            return false;
        }
        String basicRawValue = Base64Decoder.decodeStr(basicValue);
        String[] basicRawValueArray = StrUtil.splitToArray(basicRawValue, ":");
        String username = ArrayUtil.get(basicRawValueArray, 0);
        if (StrUtil.isBlank(username)) {
            return false;
        }
        String password = ArrayUtil.get(basicRawValueArray, 1);
        if (StrUtil.isBlank(password)) {
            return false;
        }
        return basicValidator.validate(username, password);
    }
}
