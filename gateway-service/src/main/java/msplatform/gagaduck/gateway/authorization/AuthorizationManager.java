package msplatform.gagaduck.gateway.authorization;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSObject;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msplatform.gagaduck.gateway.constant.RedisConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
*
* 鉴权管理器
* 用于判断是否有访问资源的权限
*
* */
@Component
@AllArgsConstructor
@Slf4j
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        // 首先先获得请求路径对应的path
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        String path = request.getURI().getPath();
        PathMatcher pathMatcher = new AntPathMatcher();
        // 对应跨域的预检请求直接放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }
        // 当请求的token为空拒绝访问
        String token = request.getHeaders().getFirst("Authorization");
        if (StrUtil.isBlank(token)) {
            return Mono.just(new AuthorizationDecision(false));
        }
        // 随后从缓存获取user-auth-service中所缓存的资源权限角色关系列表
        Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(RedisConstant.RESOURCE_ROLES_MAP);
        Iterator<Object> iterator = resourceRolesMap.keySet().iterator();
        // 请求路径匹配到的资源需要的角色权限集合authorities
        List<String> authorities = new ArrayList<>();
        while (iterator.hasNext()) {
            String pattern = (String) iterator.next();
            if (pathMatcher.match(pattern, path)) {
                authorities.addAll(Convert.toList(String.class, resourceRolesMap.get(pattern)));
            }
        }
        // 此处暂不采用mono了，直接解析
        // 给一个正确的返回和一个错误的返回
        Mono<AuthorizationDecision> authorizationDecisionMonoTrue = Mono.just(new AuthorizationDecision(true));
        Mono<AuthorizationDecision> authorizationDecisionMonoFalse = Mono.just(new AuthorizationDecision(false));
        // 从token中解析用户信息并设置到Header中去
        // 将所携带的token头GaGaDuck先去掉
        String realToken = token.replace("GaGaDuck: ", "");
        try {
            // 解析token
            JWSObject jwsObject = JWSObject.parse(realToken);
            String payLoadStr = jwsObject.getPayload().toString();
            // 使用 Jackson 解析 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode userJson = objectMapper.readTree(payLoadStr);
            // 提取 authorities的列表
            List<String> authoritiesJwt = new ArrayList<>();
            JsonNode authoritiesNode = userJson.get("authorities");
            if (authoritiesNode != null && authoritiesNode.isArray()) {
                for (JsonNode authorityNode : authoritiesNode) {
                    // 从JWT中解析出来有哪些角色
                    // 将角色加入到authoritiesJwt
                    String authority = authorityNode.asText();
                    authoritiesJwt.add(authority);
                }
            }
            // 将该角色列表的每个角色与该路由所拥有权限的角色相对比
            for(String authority : authoritiesJwt) {
                if(authorities.contains(authority)) {
                    // 如果有的话，权限校验通过，可以访问该路径
                    return authorizationDecisionMonoTrue;
                }
            }
            // 没有的话就不能访问该路径
            return authorizationDecisionMonoFalse;
        } catch (ParseException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}