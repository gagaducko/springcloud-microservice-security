package user.gagaduck.usermanagement.component;

import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import user.gagaduck.usermanagement.service.RedisUpdateService;

@Component
@EnableScheduling
public class ScheduledTasks {

    @Resource
    private RedisUpdateService redisUpdateService;

    // 使用cron表达式来配置定时任务的触发时间
    // 每60s更新一次在redis中的角色与权限
    @Scheduled(fixedDelay = 60000) // 上一次任务执行完后每分钟执行一次
    public void performTask() {
        // 这里写你需要定时执行的任务
        System.out.println("执行redis的定时任务");
        redisUpdateService.loadRolePermissionToRedis();
        System.out.println("更新成功");
    }
}
