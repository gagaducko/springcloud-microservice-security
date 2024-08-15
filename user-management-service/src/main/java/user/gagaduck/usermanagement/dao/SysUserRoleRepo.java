package user.gagaduck.usermanagement.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import user.gagaduck.usermanagement.entity.SysUserRoleEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SysUserRoleRepo extends BaseRepository<SysUserRoleEntity, Long> {


    List<SysUserRoleEntity> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM SysUserRoleEntity e WHERE e.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    List<SysUserRoleEntity> findByRoleId(Long roleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM SysUserRoleEntity e WHERE e.roleId = :roleId")
    void deleteByRoleId(Long roleId);
}