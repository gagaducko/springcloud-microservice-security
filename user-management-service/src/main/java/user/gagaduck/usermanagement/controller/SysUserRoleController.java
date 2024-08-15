package user.gagaduck.usermanagement.controller;


import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.gagaduck.usermanagement.entity.SysUserRoleEntity;
import user.gagaduck.usermanagement.service.SysUserRoleService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("sysUserRole")
public class SysUserRoleController {

    @Resource
    private SysUserRoleService sysUserRoleService;

    @PostMapping
    public ResponseEntity<SysUserRoleEntity> createSysUserRole(@RequestBody SysUserRoleEntity sysUserRole) {
        SysUserRoleEntity createdSysUserRole = sysUserRoleService.save(sysUserRole);
        return ResponseEntity.ok(createdSysUserRole);
    }

    // 由ID获得
    @GetMapping("/find/{id}")
    public ResponseEntity<SysUserRoleEntity> getSysUserRoleById(@PathVariable Long id) {
        Optional<SysUserRoleEntity> sysUserRole = sysUserRoleService.findById(id);
        return sysUserRole.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SysUserRoleEntity>> getSysUserRolesByUserId(@PathVariable Long userId) {
        List<SysUserRoleEntity> sysUserRoles = sysUserRoleService.findByUserId(userId);
        return ResponseEntity.ok(sysUserRoles);
    }

    //更新
    @PutMapping("/add/{id}")
    public ResponseEntity<SysUserRoleEntity> updateSysUserRole(@PathVariable Long id, @RequestBody SysUserRoleEntity sysUserRole) {
        if (sysUserRoleService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        sysUserRole.setId(id);
        SysUserRoleEntity updatedSysUserRole = sysUserRoleService.save(sysUserRole);
        return ResponseEntity.ok(updatedSysUserRole);
    }

    // 删除
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteSysUserRole(@PathVariable Long id) {
        if (sysUserRoleService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        sysUserRoleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/removeByUser/{id}")
    public ResponseEntity<Void> deleteSysUserRoleByUserId(@PathVariable Long id) {
        if (sysUserRoleService.findByUserId(id).isEmpty()) {
            System.out.println("here is find by user id");
            return ResponseEntity.notFound().build();
        }
        sysUserRoleService.deleteByUserId(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/removeByRole/{id}")
    public ResponseEntity<Void> deleteSysUserRoleByRoleId(@PathVariable Long id) {
        if (sysUserRoleService.findByRoleId(id).isEmpty()) {
            System.out.println("here is find by user id");
            return ResponseEntity.noContent().build();
        }
        sysUserRoleService.deleteByRoleId(id);
        return ResponseEntity.noContent().build();
    }

}
