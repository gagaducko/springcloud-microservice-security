package user.gagaduck.usermanagement.dao;

import user.gagaduck.usermanagement.entity.SysRoleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SysRoleRepo extends BaseRepository<SysRoleEntity, Long> {
    List<SysRoleEntity> getByName(String name);
}