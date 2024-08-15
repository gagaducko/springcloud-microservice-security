package user.gagaduck.usermanagement.dao;

import user.gagaduck.usermanagement.entity.SysPermissionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysPermissionRepo extends BaseRepository<SysPermissionEntity, Long> {

    List<SysPermissionEntity> getByName(String name);
}
