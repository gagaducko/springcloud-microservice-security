package auth.gagaduck.userauthservice.userInfo.service;

import auth.gagaduck.userauthservice.userInfo.dao.SysUserRepo;
import auth.gagaduck.userauthservice.userInfo.entity.SysUserEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SysUserService {

    @Resource
    private SysUserRepo sysUserRepo;

    private Map<String, List<String>> resourceRolesMap;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    public SysUserEntity findByUsername(String username) {
        SysUserEntity sysUserEntity = sysUserRepo.findByUsername(username);
        return sysUserEntity;
    }

    public SysUserEntity findByMobile(String mobile) {
        SysUserEntity sysUserEntity = sysUserRepo.findByMobile(mobile);
        return sysUserEntity;
    }

}
