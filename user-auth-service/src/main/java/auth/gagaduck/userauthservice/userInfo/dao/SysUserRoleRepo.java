package auth.gagaduck.userauthservice.userInfo.dao;

import auth.gagaduck.userauthservice.userInfo.entity.SysUserRoleEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRoleRepo extends BaseRepository<SysUserRoleEntity, Long> {
}
