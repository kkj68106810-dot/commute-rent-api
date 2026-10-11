package com.commute.api.domain.user.dto;

import com.commute.api.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Spring security
 *
 * @author Administrator
 * @version 1.0.0
 * @since 2026/10/08
 */
@Slf4j
@Getter
@AllArgsConstructor
public class UserDetailsDto implements UserDetails {
    // @Delegate 어노테이션을 사용하여 UserDto 객체의 메서드를 이 클래스에서 직접 사용할 수 있게 합니다.
    @Delegate
    private User user;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 사용자의 권한 목록을 반환합니다.
     *
     * @return Collection<? extends GrantedAuthority>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 사용자의 비밀번호를 반환합니다.
     *
     * @return String
     */
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }


    /**
     * 사용자의 이름을 반환합니다.
     *
     * @return String
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * 계정이 만료되지 않았는지 여부를 반환합니다.
     * 현재 항상 false를 반환하므로, 모든 계정이 만료된 것으로 처리됩니다.
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * 계정이 잠기지 않았는지 여부를 반환합니다.
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }


    /**
     * 자격 증명(비밀번호)이 만료되지 않았는지 여부를 반환합니다.
     *
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * 계정이 활성화되어 있는지 여부를 반환합니다.
     *
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        return false;
    }
}
