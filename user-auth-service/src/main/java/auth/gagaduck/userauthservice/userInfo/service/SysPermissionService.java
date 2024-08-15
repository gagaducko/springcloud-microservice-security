package auth.gagaduck.userauthservice.userInfo.service;

import auth.gagaduck.userauthservice.userInfo.dao.SysPermissionRepo;
import auth.gagaduck.userauthservice.userInfo.entity.SysPermissionEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysPermissionService {

    @Resource
    private SysPermissionRepo sysPermissionRepo;

    public List<SysPermissionEntity> findByUserId(Long id) {
        // 根据用户ID查询权限列表
        return sysPermissionRepo.findByUserId(id);
    }

    public List<SysPermissionEntity> findByRoleId(Long id) {
        return sysPermissionRepo.findByRoleId(id);
    }
}
