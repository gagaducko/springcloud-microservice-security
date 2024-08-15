package user.gagaduck.usermanagement.service;

import user.gagaduck.usermanagement.dao.SysRoleRepo;
import user.gagaduck.usermanagement.entity.SysRoleEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SysRoleService {

    @Resource
    private SysRoleRepo sysRoleRepo;

    // 查看全部角色
    public List<SysRoleEntity> findAllRoles() {
        return sysRoleRepo.findAll();
    }

    // 添加角色
    public SysRoleEntity addRole(SysRoleEntity role) {
        Date currentData = new Date();
        Timestamp timestamp = new Timestamp(currentData.getTime());
        role.setUpdateDate(timestamp);
        role.setCreateDate(timestamp);
        return sysRoleRepo.save(role);
    }

    // 删除角色
    public void deleteRole(Long id) {
        sysRoleRepo.deleteById(id);
    }

    // 修改角色信息
    public SysRoleEntity updateRole(SysRoleEntity role) {
        Date currentData = new Date();
        Timestamp timestamp = new Timestamp(currentData.getTime());
        role.setUpdateDate(timestamp);
        return sysRoleRepo.save(role);
    }

    public SysRoleEntity getRolesById(Long id) {
        return sysRoleRepo.findById(id).orElse(null);
    }

    public SysRoleEntity getRolesByName(String name) {
        return sysRoleRepo.getByName(name).get(0);
    }
}
