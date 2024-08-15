package user.gagaduck.usermanagement.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import user.gagaduck.usermanagement.dao.SysUserRoleRepo;
import user.gagaduck.usermanagement.entity.SysUserRoleEntity;

import java.util.List;
import java.util.Optional;

@Service
public class SysUserRoleService {

    @Resource
    private SysUserRoleRepo sysUserRoleRepo;

    // 保存/更新
    public SysUserRoleEntity save(SysUserRoleEntity sysUserRole) {
        return sysUserRoleRepo.save(sysUserRole);
    }

    // 根据Id获取用户角色关系
    public Optional<SysUserRoleEntity> findById(Long id) {
        return sysUserRoleRepo.findById(id);
    }

    // 通过UserId获得对应的角色列表
    public List<SysUserRoleEntity> findByUserId(Long userId) {
        return sysUserRoleRepo.findByUserId(userId);
    }

    // 根据Id删除某条用户角色关联
    public void deleteById(Long id) {
        sysUserRoleRepo.deleteById(id);
    }

    // 根据userid删除该关联
    public void deleteByUserId(Long id) {
        sysUserRoleRepo.deleteByUserId(id);
    }

    public List<SysUserRoleEntity> findByRoleId(Long roleId) {
        return sysUserRoleRepo.findByRoleId(roleId);
    }

    public void deleteByRoleId(Long roleId) {
        sysUserRoleRepo.deleteByRoleId(roleId);
    }
}
