package user.gagaduck.usermanagement.service;

import user.gagaduck.usermanagement.dao.SysPermissionRepo;
import user.gagaduck.usermanagement.entity.SysPermissionEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class SysPermissionService {

    @Resource
    private SysPermissionRepo sysPermissionRepo;

    // 查看全部权限
    public List<SysPermissionEntity> findAllPermissions() {
        return sysPermissionRepo.findAll();
    }

    // 添加权限
    public SysPermissionEntity addPermission(SysPermissionEntity permission) {
        Date currentData = new Date();
        Timestamp timestamp = new Timestamp(currentData.getTime());
        permission.setCreateDate(timestamp);
        permission.setUpdateDate(timestamp);
        permission.setType(1);
        return sysPermissionRepo.save(permission);
    }

    // 删除权限
    public void deletePermission(Long id) {
        sysPermissionRepo.deleteById(id);
    }

    // 修改权限信息
    public SysPermissionEntity updatePermission(SysPermissionEntity permission) {
        Date currentData = new Date();
        Timestamp timestamp = new Timestamp(currentData.getTime());
        permission.setUpdateDate(timestamp);
        return sysPermissionRepo.save(permission);
    }

    public SysPermissionEntity getPermissionById(Long id) {
        return sysPermissionRepo.findById(id).orElse(null);
    }

    public SysPermissionEntity getPermissionByName(String name) {
        return sysPermissionRepo.getByName(name).get(0);
    }
}
