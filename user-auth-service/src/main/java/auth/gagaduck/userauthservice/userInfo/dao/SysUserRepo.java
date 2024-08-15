package auth.gagaduck.userauthservice.userInfo.dao;

import auth.gagaduck.userauthservice.userInfo.entity.SysUserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepo extends BaseRepository<SysUserEntity, Long> {
    SysUserEntity findByUsername(String username);

    SysUserEntity findByMobile(String mobile);
}
