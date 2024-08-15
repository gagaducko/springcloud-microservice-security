package msplatform.gagaduck.gateway.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


/*
*
* configuration
* 忽略的网关白名单配置
* 此处将/auth/oauth/token这些内容变为白名单,以确保可以直接对token进行校验
*
* */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix="secure.ignore")
public class IgnoreUrlsConfig {
    private List<String> urls;
}