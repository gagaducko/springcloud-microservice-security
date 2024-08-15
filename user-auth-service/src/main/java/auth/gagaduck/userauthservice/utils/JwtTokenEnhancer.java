package auth.gagaduck.userauthservice.utils;

import auth.gagaduck.userauthservice.userInfo.entity.SysUserEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class JwtTokenEnhancer implements TokenEnhancer{
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        // 给JWT添加额外信息，此处暂不添加
        // 以下为添加用户信息的内容
//        Map<String, Object> info = new HashMap<>();
//        SysUserEntity user = (SysUserEntity) authentication.getPrincipal();
//        System.out.println("user:" + user);
//        info.put("role", user.getRoleList());
//        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}
