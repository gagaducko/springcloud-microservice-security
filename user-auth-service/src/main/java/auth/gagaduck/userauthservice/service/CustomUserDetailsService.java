package auth.gagaduck.userauthservice.service;

import auth.gagaduck.userauthservice.userInfo.entity.SysUserEntity;
import auth.gagaduck.userauthservice.userInfo.service.SysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("customUserDetailsService")
public class CustomUserDetailsService extends AbstractUserDetailsService {

    // 注入用户信息
    @Resource
    private SysUserService sysUserService;

    @Override
    SysUserEntity findSysUser(String username){
        return sysUserService.findByUsername(username);
    }
}
