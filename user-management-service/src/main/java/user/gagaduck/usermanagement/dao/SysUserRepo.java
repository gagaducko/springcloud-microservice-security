package user.gagaduck.usermanagement.dao;

import user.gagaduck.usermanagement.entity.SysUserEntity;
import org.springframework.stereotype.Repository;


@Repository
public interface SysUserRepo extends BaseRepository<SysUserEntity, Long> {
}