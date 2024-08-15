package msplatform.gagaduck.gateway.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import msplatform.gagaduck.gateway.constant.RedisConstant;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
*
* 过滤链
* 用于过滤菜单请求
* 用于校验该用户是否有该菜单的访问权限
*
* */
@Slf4j
@Component
@RefreshScope
public class MenuFilter implements WebFilter {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    // Helper method to create a response with boolean value
    private Mono<Void> createResponse(ServerHttpResponse response, boolean result, String message) {
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.getHeaders().add("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.getHeaders().add("Access-Control-Max-Age", "3600");
        response.setStatusCode(HttpStatus.OK);
        String responseBody = "{\"success\":" + result + ", \"message\":\"" + message + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("==================进入到了菜单访问的过滤器中==============");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        PathMatcher pathMatcher = new AntPathMatcher();
        // 检查path的前五个字符是否为"/menu"
        if (path.startsWith("/menu")) {
            // 考虑跨域预检
            String method = request.getMethod().name();
            if ("OPTIONS".equals(method)) {
                System.out.println("预检options，直接放行……");
                // 设置CORS响应头
                response.getHeaders().add("Access-Control-Allow-Origin", "*");
                response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.getHeaders().add("Access-Control-Allow-Headers", "Authorization, Content-Type");
                response.getHeaders().add("Access-Control-Max-Age", "3600");
                // 终止过滤链条并返回成功状态
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            // 去掉前五个字符得到真正的path
            String actualPath = path.substring(5);
            // 可以在这里根据actualPath做进一步的处理
            // token为空拒绝访问
            String token = request.getHeaders().getFirst("Authorization");
            if (StrUtil.isBlank(token)) {
                response.setStatusCode(HttpStatus.ACCEPTED);
                response.getHeaders().add("Access-Control-Allow-Origin", "*");
                response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.getHeaders().add("Access-Control-Allow-Headers", "Authorization, Content-Type");
                response.getHeaders().add("Access-Control-Max-Age", "3600");
                String data = "Menu Not OK, No token！";
                System.out.println(data + path);
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            // 缓存取资源权限角色关系列表
            // 相似与鉴权管理器对业务功能权限的校验
            Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(RedisConstant.RESOURCE_ROLES_MAP);
            Iterator<Object> iterator = resourceRolesMap.keySet().iterator();
            // 开始找是否有该菜单了
            List<String> authorities = new ArrayList<>();
            while (iterator.hasNext()) {
                String pattern = (String) iterator.next();
                if (pathMatcher.match(pattern, path)) {
                    authorities.addAll(Convert.toList(String.class, resourceRolesMap.get(pattern)));
                }
            }
            //从token中解析用户信息并设置到Header中去
            String realToken = token.replace("GaGaDuck: ", "");
            try {
                // 进行解析
                JWSObject jwsObject = JWSObject.parse(realToken);
                String payLoadStr = jwsObject.getPayload().toString();
                // 使用 Jackson 解析 JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode userJson = objectMapper.readTree(payLoadStr);
                // 提取 authorities
                System.out.println("menu authorities！");
                List<String> authoritiesJwt = new ArrayList<>();
                JsonNode authoritiesNode = userJson.get("authorities");
                if (authoritiesNode != null && authoritiesNode.isArray()) {
                    for (JsonNode authorityNode : authoritiesNode) {
                        // 从JWT中解析出来有哪些角色
                        String authority = authorityNode.asText();
                        authoritiesJwt.add(authority);
                    }
                }
                // 将该角色列表的每个角色与该路由所拥有权限的角色相对比
                for(String authority : authoritiesJwt) {
                    if(authorities.contains(authority)) {
                        return createResponse(response, true, "Menu OK！");
                    }
                }
                return createResponse(response, false, "Menu Not OK！As there's no Menu!");
            } catch (ParseException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            // 如果不是以"/menu"开头，直接继续过滤链
            return chain.filter(exchange);
        }
    }
}
