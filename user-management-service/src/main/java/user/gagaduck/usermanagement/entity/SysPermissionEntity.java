package user.gagaduck.usermanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Data
@Entity
@Table(name = "sys_permission", schema = "auth_user", catalog = "")
public class SysPermissionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "parent_id")
    private Long parentId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "code")
    private String code;
    @Basic
    @Column(name = "url")
    private String url;
    @Basic
    @Column(name = "type")
    private Integer type;
    @Basic
    @Column(name = "icon")
    private String icon;
    @Basic
    @Column(name = "remark")
    private String remark;
    @Basic
    @Column(name = "create_date")
    private Timestamp createDate;
    @Basic
    @Column(name = "update_date")
    private Timestamp updateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysPermissionEntity that = (SysPermissionEntity) o;
        return id == that.id && type == that.type && Objects.equals(parentId, that.parentId) && Objects.equals(name, that.name) && Objects.equals(code, that.code) && Objects.equals(url, that.url) && Objects.equals(icon, that.icon) && Objects.equals(remark, that.remark) && Objects.equals(createDate, that.createDate) && Objects.equals(updateDate, that.updateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, name, code, url, type, icon, remark, createDate, updateDate);
    }
}
