package msplatform.gagaduck.gateway.component;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/*
*
* 自定义返回结果
* 用于sentinel
* 用于处理Sentinel熔断时的异常情况。
*
* */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SentinelFallbackHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        ServerHttpResponse response = exchange.getResponse();
        // 设置响应状态码
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        // 设置响应类型
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 构造错误响应内容
        String errorJson = "{\"code\":429, \"msg\":\"请求超过最大数，请稍后再试\"}";
        // 返回响应
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorJson.getBytes())));

    }
}
