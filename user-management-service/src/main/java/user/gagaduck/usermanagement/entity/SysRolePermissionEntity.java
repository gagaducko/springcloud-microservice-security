package user.gagaduck.usermanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
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
