package auth.gagaduck.userauthservice.userInfo.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "sys_user", schema = "auth_user", catalog = "")
public class SysUserEntity  implements UserDetails {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

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

    /**
     * 拥有权限集合
     * @TableField(exist = false) 该属性不是数据库表字段
     */

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<SysRoleEntity> roleList = new ArrayList<>();

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Transient
    private List<SysPermissionEntity> permissions = new ArrayList<>();
}
