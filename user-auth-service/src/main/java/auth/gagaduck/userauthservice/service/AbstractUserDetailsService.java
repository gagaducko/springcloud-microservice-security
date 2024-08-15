package auth.gagaduck.userauthservice.service;

import auth.gagaduck.userauthservice.constant.RedisConstant;
import auth.gagaduck.userauthservice.userInfo.entity.SysPermissionEntity;
import auth.gagaduck.userauthservice.userInfo.entity.SysRoleEntity;
import auth.gagaduck.userauthservice.userInfo.entity.SysUserEntity;
import auth.gagaduck.userauthservice.userInfo.service.SysPermissionService;
import cn.hutool.core.collection.CollUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
*
* 抽象类 UserDetailService
*
* */
public abstract class AbstractUserDetailsService implements UserDetailsService {

    @Resource
    private SysPermissionService sysPermissionService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    private Map<String, List<String>> resourceRolesMap;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 直接提供给User permission
        SysUserEntity sysUserEntity = findSysUser(username);
        // 一种是直接提供给User permission
//        findSysPermission(sysUserEntity);
        // 但这里选择提供roles并将permissions store进redis中
        findSysRoles(sysUserEntity);
        return sysUserEntity;
    }

    abstract SysUserEntity findSysUser(String username);

    // 在RBAC模型下直接用权限信息作为权限列表
    public void findSysPermission(SysUserEntity sysUserEntity) throws UsernameNotFoundException {
        if(sysUserEntity == null) {
            throw new UsernameNotFoundException("抱歉,未查询到有效用户信息");
        }
        List<SysPermissionEntity> sysPermissions = sysPermissionService.findByUserId(sysUserEntity.getId());
        // 用户没有权限
        if(CollectionUtils.isEmpty(sysPermissions)) {
            return;
        }
        // 给用户存入权限,认证通过后用于渲染左侧菜单
        sysUserEntity.setPermissions(sysPermissions);
        // 封装用户信息和权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(SysPermissionEntity sp: sysPermissions) {
            //权限标识
            authorities.add(new SimpleGrantedAuthority(sp.getCode()));
        }
        sysUserEntity.setAuthorities(authorities);
    }

    // 以用户的角色信息作为authorities放入JWT，并将权限信息store进redis中方便查询
    public void findSysRoles(SysUserEntity sysUserEntity) throws UsernameNotFoundException {
        if(sysUserEntity == null) {
            throw new UsernameNotFoundException("抱歉,未查询到有效用户信息");
        }
        List<SysRoleEntity> sysRoleEntities = sysUserEntity.getRoleList();
        // 用户没有角色
        if(CollectionUtils.isEmpty(sysRoleEntities)) {
            return;
        }
        // 封装用户信息和权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        resourceRolesMap = new TreeMap<>();
        for(SysRoleEntity sr: sysRoleEntities) {
            //权限标识
            authorities.add(new SimpleGrantedAuthority(sr.getName()));
            List<SysPermissionEntity> sysPermissions = sysPermissionService.findByRoleId(sr.getId());
            for (SysPermissionEntity sysPermissionEntity: sysPermissions) {
                resourceRolesMap.put(sysPermissionEntity.getUrl(), CollUtil.toList(sr.getName()));
            }
        }
        redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
        sysUserEntity.setAuthorities(authorities);
    }
}
