package auth.gagaduck.userauthservice.userInfo.dao;

import auth.gagaduck.userauthservice.userInfo.entity.SysPermissionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysPermissionRepo extends BaseRepository<SysPermissionEntity, Long> {

    @Query(value = "select  * from auth_user.sys_permission where id in (select permission_id from auth_user.sys_role_permission where role_id in (select role_id from auth_user.sys_user_role where user_id = :id))", nativeQuery = true)
    List<SysPermissionEntity> findByUserId(@Param("id") Long id);

    @Query(value = "select  * from auth_user.sys_permission where id in (select permission_id from auth_user.sys_role_permission where role_id = :id)", nativeQuery = true)
    List<SysPermissionEntity> findByRoleId(@Param("id")Long id);
}
