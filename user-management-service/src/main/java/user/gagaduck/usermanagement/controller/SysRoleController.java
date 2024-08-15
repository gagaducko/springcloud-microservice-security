package user.gagaduck.usermanagement.controller;

import user.gagaduck.usermanagement.entity.SysRoleEntity;
import user.gagaduck.usermanagement.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    // 查看全部角色
    @GetMapping("/all")
    public List<SysRoleEntity> getAllRoles() {
        return sysRoleService.findAllRoles();
    }

    // 通过roleId获取角色
    @GetMapping("/getRolesById/{id}")
    public SysRoleEntity getRolesById(@PathVariable Long id) {
        return sysRoleService.getRolesById(id);
    }

    // 通过name获取角色
    @GetMapping("/getRolesByName/{name}")
    public SysRoleEntity getRolesByName(@PathVariable String name) {
        return sysRoleService.getRolesByName(name);
    }

    // 添加角色
    @PostMapping("/add")
    public SysRoleEntity addRole(@RequestBody SysRoleEntity role) {
        return sysRoleService.addRole(role);
    }

    // 删除角色
    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable Long id) {
        sysRoleService.deleteRole(id);
    }

    // 修改角色信息
    @PutMapping("/update")
    public SysRoleEntity updateRole(@RequestBody SysRoleEntity role) {
        return sysRoleService.updateRole(role);
    }
}
