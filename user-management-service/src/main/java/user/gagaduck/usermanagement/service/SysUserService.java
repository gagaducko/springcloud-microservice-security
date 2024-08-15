package user.gagaduck.usermanagement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import user.gagaduck.usermanagement.dao.SysUserRepo;
import user.gagaduck.usermanagement.entity.SysUserEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class SysUserService {

    @Resource
    private SysUserRepo sysUserRepo;

    @Resource
    private PasswordEncoder passwordEncoder;

    // 查看全部用户
    public List<SysUserEntity> findAllUsers() {
        return sysUserRepo.findAll();
    }

    // 添加用户
    public SysUserEntity addUser(SysUserEntity user) {
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        Date currentData = new Date();
        Timestamp timestamp = new Timestamp(currentData.getTime());
        user.setCreateDate(timestamp);
        user.setUpdateDate(timestamp);
        return sysUserRepo.save(user);
    }

    // 删除用户
    public void deleteUser(Long id) {
        sysUserRepo.deleteById(id);
    }

    // 修改用户信息
    public SysUserEntity updateUser(SysUserEntity user) {
        Date currentData = new Date();
        Timestamp timestamp = new Timestamp(currentData.getTime());
        user.setUpdateDate(timestamp);
        return sysUserRepo.save(user);
    }

}
