package auth.gagaduck.userauthservice.userInfo.dao;

import auth.gagaduck.userauthservice.userInfo.entity.SysRoleEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleRepo extends BaseRepository<SysRoleEntity, Long> {
}