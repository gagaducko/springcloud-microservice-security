package msplatform.gagaduck.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import msplatform.gagaduck.gateway.authorization.AuthorizationManager;
import msplatform.gagaduck.gateway.component.RestAuthenticationEntryPoint;
import msplatform.gagaduck.gateway.component.RestfulAccessDeniedHandler;
import msplatform.gagaduck.gateway.constant.AuthConstant;
import lombok.AllArgsConstructor;
import msplatform.gagaduck.gateway.filter.IgnoreUrlsRemoveJwtFilter;
import msplatform.gagaduck.gateway.filter.MenuFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/*
*
* configuration
* 用于配置资源服务器
* */
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    private final AuthorizationManager authorizationManager;
    private final IgnoreUrlsConfig ignoreUrlsConfig;
    private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;
    private final MenuFilter menuFilter;

    // security的过滤链
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                );
        // 增加过滤器过滤忽略的白名单Url
        http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                // 增加过滤器过滤菜单校验的请求
                .addFilterBefore(menuFilter, SecurityWebFiltersOrder.AUTHORIZATION)
                .authorizeExchange(exchange -> exchange
                        // 直接在properties中配置的白名单
                        .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(),String.class)).permitAll()
                        // 通过鉴权管理器配置的放行权限名单
                        .anyExchange().access(authorizationManager))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // 返回未授权处理的信息
                        .accessDeniedHandler(restfulAccessDeniedHandler)
                        // 返回未未认证处理
                        .authenticationEntryPoint(restAuthenticationEntryPoint))
                // 禁用 CSRF
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}