package user.gagaduck.usermanagement.controller;

import user.gagaduck.usermanagement.entity.SysUserEntity;
import user.gagaduck.usermanagement.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    // 查看全部用户
    @GetMapping("/all")
    public List<SysUserEntity> getAllUsers() {
        return sysUserService.findAllUsers();
    }

    // 添加用户
    @PostMapping("/add")
    public SysUserEntity addUser(@RequestBody SysUserEntity user) {
        return sysUserService.addUser(user);
    }

    // 删除用户
    @DeleteMapping("delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        sysUserService.deleteUser(id);
    }

    // 修改用户信息
    @PutMapping("/update")
    public SysUserEntity updateUser(@RequestBody SysUserEntity user) {
        return sysUserService.updateUser(user);
    }
}
