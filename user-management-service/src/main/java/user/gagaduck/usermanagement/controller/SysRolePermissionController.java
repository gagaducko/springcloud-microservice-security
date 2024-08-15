package user.gagaduck.usermanagement.controller;

import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.gagaduck.usermanagement.entity.SysRolePermissionEntity;
import user.gagaduck.usermanagement.service.SysRolePermissionService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/sysRolePermission")
public class SysRolePermissionController {

    @Resource
    private SysRolePermissionService sysRolePermissionService;

    // 创建角色权限关联
    @PostMapping("/add")
    public ResponseEntity<SysRolePermissionEntity> createSysRolePermission(@RequestBody SysRolePermissionEntity sysRolePermission) {
        SysRolePermissionEntity createdSysRolePermission = sysRolePermissionService.save(sysRolePermission);
        return ResponseEntity.ok(createdSysRolePermission);
    }

    // 根据ID获取对应的角色权限关联
    @GetMapping("/find/{id}")
    public ResponseEntity<SysRolePermissionEntity> getSysRolePermissionById(@PathVariable Long id) {
        Optional<SysRolePermissionEntity> sysRolePermission = sysRolePermissionService.findById(id);
        return sysRolePermission.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 根据roleId获得对应的列表
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<SysRolePermissionEntity>> getSysRolePermissionsByRoleId(@PathVariable Long roleId) {
        List<SysRolePermissionEntity> sysRolePermissions = sysRolePermissionService.findByRoleId(roleId);
        return ResponseEntity.ok(sysRolePermissions);
    }

    // 更新
    @PutMapping("/update/{id}")
    public ResponseEntity<SysRolePermissionEntity> updateSysRolePermission(@PathVariable Long id, @RequestBody SysRolePermissionEntity sysRolePermission) {
        if (sysRolePermissionService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        sysRolePermission.setId(id);
        SysRolePermissionEntity updatedSysRolePermission = sysRolePermissionService.save(sysRolePermission);
        return ResponseEntity.ok(updatedSysRolePermission);
    }

    // 删除对应权限
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteSysRolePermission(@PathVariable Long id) {
        if (!sysRolePermissionService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        sysRolePermissionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 删除对应权限
    @DeleteMapping("/removeByRole/{id}")
    public ResponseEntity<Void> deleteSysRolePermissionByRole(@PathVariable Long id) {
        System.out.println("role id is: " + id);
        if (sysRolePermissionService.findByRoleId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        sysRolePermissionService.deleteByRoleId(id);
        return ResponseEntity.noContent().build();
    }

    // 删除权限ID对应role关系
    @DeleteMapping("/removeByPermission/{id}")
    public ResponseEntity<Void> deleteSysRolePermissionByPermission(@PathVariable Long id) {
        System.out.println("Permission id is: " + id);
        if (sysRolePermissionService.findByPermissionId(id).isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        sysRolePermissionService.deleteByPermissionId(id);
        return ResponseEntity.noContent().build();
    }


}
