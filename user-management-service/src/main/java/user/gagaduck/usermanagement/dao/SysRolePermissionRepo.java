package user.gagaduck.usermanagement.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import user.gagaduck.usermanagement.entity.SysRolePermissionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SysRolePermissionRepo extends BaseRepository<SysRolePermissionEntity, Long> {

    List<SysRolePermissionEntity> findByRoleId(Long roleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM SysRolePermissionEntity e WHERE e.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);

    List<SysRolePermissionEntity> findByPermissionId(Long permissionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM SysRolePermissionEntity e WHERE e.permissionId = :permissionId")
    void deleteByPermissionId(@Param("permissionId") Long permissionId);
}