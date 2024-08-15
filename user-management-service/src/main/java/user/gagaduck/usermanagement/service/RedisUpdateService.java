package user.gagaduck.usermanagement.service;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import user.gagaduck.usermanagement.constant.RedisConstant;
import user.gagaduck.usermanagement.dao.SysPermissionRepo;
import user.gagaduck.usermanagement.dao.SysRolePermissionRepo;
import user.gagaduck.usermanagement.dao.SysRoleRepo;
import user.gagaduck.usermanagement.entity.SysPermissionEntity;
import user.gagaduck.usermanagement.entity.SysRoleEntity;
import user.gagaduck.usermanagement.entity.SysRolePermissionEntity;

import java.util.*;

@Service
public class RedisUpdateService {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private SysRoleRepo sysRoleRepo;

    @Resource
    private SysPermissionRepo sysPermissionRepo;

    @Resource
    private SysRolePermissionRepo sysRolePermissionRepo;

    private Map<String, List<String>> resourceRolesMap;

    public void loadRolePermissionToRedis() {
        redisTemplate.delete(RedisConstant.RESOURCE_ROLES_MAP);
        resourceRolesMap = new TreeMap<>();
        List<SysRoleEntity> sysRoleEntities = sysRoleRepo.findAll();
        for(SysRoleEntity sr: sysRoleEntities) {
            //权限标识
            List<SysRolePermissionEntity> sysRolePermissionEntities = sysRolePermissionRepo.findByRoleId(sr.getId());
            for(SysRolePermissionEntity srp: sysRolePermissionEntities) {
                SysPermissionEntity sysPermissionEntity = sysPermissionRepo.findById(srp.getPermissionId()).get();
//                resourceRolesMap.put(sysPermissionEntity.getUrl(), CollUtil.toList(sr.getName()));
                String url = sysPermissionEntity.getUrl();
                // 如果resourceRolesMap中已经包含此url，则将新的角色名添加到列表中
                if(resourceRolesMap.containsKey(url)) {
                    resourceRolesMap.get(url).add(sr.getName());
                } else {
                    // 否则创建一个新的列表
                    resourceRolesMap.put(url, new ArrayList<>(Collections.singletonList(sr.getName())));
                }
            }
        }
        redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
    }


}
