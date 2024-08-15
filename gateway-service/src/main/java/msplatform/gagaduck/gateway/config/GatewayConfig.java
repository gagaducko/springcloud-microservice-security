package msplatform.gagaduck.gateway.config;

import jakarta.annotation.Resource;
import msplatform.gagaduck.gateway.component.SentinelFallbackHandler;
import msplatform.gagaduck.gateway.repository.NacosFlowDefinitionRepository;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/*
*
* configuration
* 服务容错从nacos上的动态加载
*
* */
@Configuration
public class GatewayConfig {

    @Resource
    private ApplicationEventPublisher publisher;


    @Value("${spring.cloud.nacos.config.flow-data-id:gateway-flow-rules.json}")
    private String gatewayFlowRulesDataId;


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelFallbackHandler sentinelGatewayExceptionHandler() {
        return new SentinelFallbackHandler();
    }

    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    /**
     * Nacos实现方式
     */
    @Configuration
    public class NacosDynFlow {

        @Resource
        private NacosConfigProperties nacosConfigProperties;

        @Bean
        public NacosFlowDefinitionRepository nacosFlowDefinitionRepository() {
            // 这里填写在Nacos中配置限流规则的Data ID和Group
            return new NacosFlowDefinitionRepository(nacosConfigProperties, publisher, gatewayFlowRulesDataId, "DEFAULT_GROUP");
        }
    }

}
