package auth.gagaduck.userauthservice.userInfo.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sys_role_permission", schema = "auth_user", catalog = "")
public class SysRolePermissionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "role_id")
    private Long roleId;
    @Basic
    @Column(name = "permission_id")
    private Long permissionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysRolePermissionEntity that = (SysRolePermissionEntity) o;
        return id == that.id && roleId == that.roleId && permissionId == that.permissionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleId, permissionId);
    }
}
