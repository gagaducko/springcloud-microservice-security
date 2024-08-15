package msplatform.gagaduck.gateway.repository;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

/*
*
* 仓库
* Nacos配置的限流规则仓库
*
* */
@Slf4j
public class NacosFlowDefinitionRepository {

    private final NacosConfigManager nacosConfigManager;
    private final ApplicationEventPublisher publisher;
    private final String dataId;
    private final String groupId;

    public NacosFlowDefinitionRepository(NacosConfigProperties nacosConfigProperties, ApplicationEventPublisher publisher, String dataId, String groupId) {
        this.nacosConfigManager = new NacosConfigManager(nacosConfigProperties);
        this.publisher = publisher;
        this.dataId = dataId;
        this.groupId = groupId;
        initializeRules();
        addListener();
    }

    private void initializeRules() {
        try {
            String content = nacosConfigManager.getConfigService().getConfig(dataId, groupId, 5000);
            Set<GatewayFlowRule> rules = parseRules(content);
            GatewayRuleManager.loadRules(rules);
        } catch (NacosException e) {
            log.error("Failed to initialize rules from Nacos config", e);
        }
    }

    private void addListener() {
        try {
            nacosConfigManager.getConfigService().addListener(dataId, groupId, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    try {
                        Set<GatewayFlowRule> rules = parseRules(configInfo);
                        GatewayRuleManager.loadRules(rules);
                        publisher.publishEvent(new RefreshRoutesEvent(this));
                    } catch (Exception e) {
                        log.error("Failed to parse and load rules from Nacos config", e);
                    }
                }
            });
        } catch (NacosException e) {
            log.error("nacos-addListener-error", e);
        }
    }

    private Set<GatewayFlowRule> parseRules(String configInfo) {
        JSONArray array = JSON.parseArray(configInfo);
        Set<GatewayFlowRule> rules = new HashSet<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            GatewayFlowRule rule = new GatewayFlowRule();
            rule.setResource(jsonObject.getString("resource"));
            rule.setCount(jsonObject.getLong("count"));
            rule.setIntervalSec(jsonObject.getInteger("intervalSec"));
            rules.add(rule);
            System.out.println("rule:"+rule);
        }
        return rules;
    }
}
