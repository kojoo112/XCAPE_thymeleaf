package com.samsan.xcape.vo;

import com.samsan.xcape.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XcapeUser implements UserDetails {
    private int seq;
    private String id;
    private String telephoneNumber;
    private String storeName;
    private Role role;
    private String refreshToken;
    private String nickname;
    private String userName;
    private String email;
    private String creDate;
    private String modDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        // Kakao Login을 사용할 경우 패스워드가 필요 없음
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public XcapeUser update(String id, String name) {
        this.id = id;
        this.userName = name;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
