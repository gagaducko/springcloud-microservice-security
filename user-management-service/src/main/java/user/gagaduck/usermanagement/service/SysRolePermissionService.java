package user.gagaduck.usermanagement.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import user.gagaduck.usermanagement.dao.SysRolePermissionRepo;
import user.gagaduck.usermanagement.entity.SysRolePermissionEntity;

import java.util.List;
import java.util.Optional;

@Service
public class SysRolePermissionService {

    @Resource
    private SysRolePermissionRepo sysRolePermissionRepo;

    // 保存/更新
    public SysRolePermissionEntity save(SysRolePermissionEntity sysRolePermission) {
        return sysRolePermissionRepo.save(sysRolePermission);
    }

    //根据Id获取用户权限关系
    public Optional<SysRolePermissionEntity> findById(Long id) {
        return sysRolePermissionRepo.findById(id);
    }

    // 通过RoleId获得对应的权限列表
    public List<SysRolePermissionEntity> findByRoleId(Long roleId) {
        return sysRolePermissionRepo.findByRoleId(roleId);
    }

    // 根据Id删除某条角色权限关联
    public void deleteById(Long id) {
        sysRolePermissionRepo.deleteById(id);
    }

    public void deleteByRoleId(Long id) {
        sysRolePermissionRepo.deleteByRoleId(id);
    }

    public List<SysRolePermissionEntity> findByPermissionId(Long permissionId) {
        return sysRolePermissionRepo.findByPermissionId(permissionId);
    }

    public void deleteByPermissionId(Long id) {
        sysRolePermissionRepo.deleteByPermissionId(id);
    }
}
