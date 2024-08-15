package user.gagaduck.usermanagement.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Data
@Entity
@Table(name = "sys_user", schema = "auth_user", catalog = "")
public class SysUserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "is_account_non_expired")
    private Integer isAccountNonExpired;
    @Basic
    @Column(name = "is_account_non_locked")
    private Integer isAccountNonLocked;
    @Basic
    @Column(name = "is_credentials_non_expired")
    private Integer isCredentialsNonExpired;
    @Basic
    @Column(name = "is_enabled")
    private Integer isEnabled;
    @Basic
    @Column(name = "nick_name")
    private String nickName;
    @Basic
    @Column(name = "mobile")
    private String mobile;
    @Basic
    @Column(name = "email")
    private String email;
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
        SysUserEntity that = (SysUserEntity) o;
        return id == that.id && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(isAccountNonExpired, that.isAccountNonExpired) && Objects.equals(isAccountNonLocked, that.isAccountNonLocked) && Objects.equals(isCredentialsNonExpired, that.isCredentialsNonExpired) && Objects.equals(isEnabled, that.isEnabled) && Objects.equals(nickName, that.nickName) && Objects.equals(mobile, that.mobile) && Objects.equals(email, that.email) && Objects.equals(createDate, that.createDate) && Objects.equals(updateDate, that.updateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, nickName, mobile, email, createDate, updateDate);
    }
}
