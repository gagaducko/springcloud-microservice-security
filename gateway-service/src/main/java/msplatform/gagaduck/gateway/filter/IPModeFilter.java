package msplatform.gagaduck.gateway.filter;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

/*
*
* 过滤器
* 用于过滤终端IP
* 全局过滤
* 分为两种模式，一种是黑名单模式
* 一种是白名单模式
* 从nacos配置中心获取
*
* */
@Slf4j
@Component
@RefreshScope
public class IPModeFilter implements GlobalFilter, Ordered {

    @Resource
    private NacosConfigManager nacosConfigManager;

    @Resource
    private NacosConfigProperties nacosConfigProperties;

    @Value("${spring.cloud.nacos.config.blackList-data-id:IPBlackList}")
    private String blackIpList;

    @Value("${spring.cloud.nacos.config.whiteList-data-id:IPWhiteList}")
    private String whiteIpList;

    @Value("${spring.cloud.nacos.config.ipManagement-data-id:IPManagement}")
    private String IPDataId;

    @Value("${spring.cloud.nacos.config.group}")
    private String groupConfig;

    private List<String> blackList = new ArrayList<>();
    private List<String> whiteList = new ArrayList<>();

    private Boolean isBlackList;

    @PostConstruct
    public void initConfig() throws NacosException {
        loadConfig();
        addConfigListener();
    }

    private void loadConfig() throws NacosException {
        // 首先是载入配置
        String blackIpDataId = blackIpList;
        String whiteIpDataId = whiteIpList;
        String ipDataId = IPDataId;
        String group = groupConfig;
        String configBlack = nacosConfigManager.getConfigService().getConfig(blackIpDataId, group, 5000);
        String configWhite = nacosConfigManager.getConfigService().getConfig(whiteIpDataId, group, 5000);
        String configIpManagement = nacosConfigManager.getConfigService().getConfig(ipDataId, group, 5000);

        // 然后判断采用黑名单还是白名单模式
        if (Objects.equals(configIpManagement, "true")) {
            System.out.println("采用黑名单模式限制终端IP");
            isBlackList = true;
        } else {
            System.out.println("采用白名单模式限制终端IP");
            isBlackList = false;
        }

        // 加入nacos中黑名单的IP
        if (configBlack != null) {
            System.out.println("now is in blackText");
            System.out.println(configBlack);
            String[] ips = configBlack.split(",");
            blackList.clear();
            for (String ip : ips) {
                blackList.add(ip.trim());
            }
        }

        // 加入nacos中白名单的ip
        if (configWhite != null) {
            System.out.println("now is in whiteText");
            System.out.println(configWhite);
            String[] ips = configWhite.split(",");
            whiteList.clear();
            for (String ip : ips) {
                whiteList.add(ip.trim());
            }
        }

    }

    // 添加一个listener用于监听配置文件的变化
    // 实时更新配置
    private void addConfigListener() throws NacosException {
        String blackIpDataId = blackIpList;
        String whiteIpDataId = whiteIpList;
        String isBlackOrWhiteMode = IPDataId;
        String group = groupConfig;
        nacosListener(blackIpDataId, group);
        nacosListener(whiteIpDataId, group);
        nacosListener(isBlackOrWhiteMode, group);
    }

    // nacos配置监听
    private void nacosListener(String dataId, String group) throws NacosException {
        nacosConfigManager.getConfigService().addListener(dataId, group, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                try {
                    // 接收到nacos变更后重新加载配置
                    loadConfig();
                } catch (NacosException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // IP黑白名单过滤链
        System.out.println("==================进入到了IP黑白名单管理访问的过滤器中==============");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 从request对象中获取客户端ip
        String clientIp = request.getRemoteAddress().getHostString();
        // 如果在黑名单模式同时IP在黑名单的时候
        if (isBlackList && blackList.contains(clientIp)) {
            // 拒绝黑名单上IP的访问,返回信息
            System.out.println("blackList1, clientIp = " + clientIp);
            response.setStatusCode(HttpStatus.UNAUTHORIZED); // 状态码
            String data = "Request be denied! IP in blacklist and mode is blackList now!";
            DataBuffer wrap = response.bufferFactory().wrap(data.getBytes());
            return response.writeWith(Mono.just(wrap));
        } else if (!isBlackList && whiteList.contains(clientIp)) {
            // 如果说是白名单模式并且白名单中有这个IP的时候
            // 可以访问
            // 合法请求，放⾏，执⾏后续的过滤器
            System.out.println("whiteList1, clientIp = " + clientIp);
            return chain.filter(exchange);
        } else if (!isBlackList && !whiteList.contains(clientIp)) {
            // 最后的情况是白名单模式但是不在白名单中
            // 不可以访问
            System.out.println("whiteList2, clientIp = " + clientIp);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            String data = "Request be denied! IP not in whitelist and mode is whiteList now!";
            DataBuffer wrap = response.bufferFactory().wrap(data.getBytes());
            return response.writeWith(Mono.just(wrap));
        } else {
            // 剩下的情况,一个是黑名单模式,但是这个IP不在黑名单中
            System.out.println("blackList2, clientIp = " + clientIp);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -2;
    }

}