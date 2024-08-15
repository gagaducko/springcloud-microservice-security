package msplatform.gagaduck.gateway.config;

import jakarta.annotation.Resource;
import msplatform.gagaduck.gateway.repository.NacosRouteDefinitionRepository;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
*
* configuration
* 用于服务网关从nacos上的的动态加载
*
* */
@Configuration
public class DynamicRouteConfig {

    @Resource
    private ApplicationEventPublisher publisher;


    @Value("${spring.cloud.nacos.config.router-data-id:gateway-router.json}")
    private String routerDataId;

    /**
     * Nacos实现方式
     */
    @Configuration
    public class NacosDynRoute {

        @Resource
        private NacosConfigProperties nacosConfigProperties;

        @Bean
        public NacosRouteDefinitionRepository nacosRouteDefinitionRepository() {
            return new NacosRouteDefinitionRepository(routerDataId,publisher, nacosConfigProperties);
        }
    }
}
