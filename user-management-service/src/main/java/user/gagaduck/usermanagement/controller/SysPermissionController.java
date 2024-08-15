package user.gagaduck.usermanagement.controller;

import user.gagaduck.usermanagement.entity.SysPermissionEntity;
import user.gagaduck.usermanagement.entity.SysRoleEntity;
import user.gagaduck.usermanagement.service.SysPermissionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/sysPermission")
public class SysPermissionController {

    @Resource
    private SysPermissionService sysPermissionService;

    // 查看全部权限
    @GetMapping("/all")
    public List<SysPermissionEntity> getAllPermissions() {
        return sysPermissionService.findAllPermissions();
    }

    // 添加权限
    @PostMapping("/add")
    public SysPermissionEntity addPermission(@RequestBody SysPermissionEntity permission) {
        return sysPermissionService.addPermission(permission);
    }

    // 通过permissionId获取权限
    @GetMapping("/getPermissionById/{id}")
    public SysPermissionEntity getPermissionById(@PathVariable Long id) {
        return sysPermissionService.getPermissionById(id);
    }

    // 通过permissionName获取权限
    @GetMapping("/getPermissionByName/{name}")
    public SysPermissionEntity getPermissionByName(@PathVariable String name) {
        return sysPermissionService.getPermissionByName(name);
    }

    // 删除权限
    @DeleteMapping("/delete/{id}")
    public void deletePermission(@PathVariable Long id) {
        sysPermissionService.deletePermission(id);
    }

    // 修改权限信息
    @PutMapping("/update")
    public SysPermissionEntity updatePermission(@RequestBody SysPermissionEntity permission) {
        return sysPermissionService.updatePermission(permission);
    }
}
