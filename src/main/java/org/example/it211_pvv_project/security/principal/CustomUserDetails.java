package org.example.it211_pvv_project.security.principal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.it211_pvv_project.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//Thông tin người dùng cho Spring Security
@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        return user.getStatus().name().equals("ACTIVE");
    }
}


//implements UserDetails
//=> Spring Security bắt buộc phải đọc user thông qua interface này.
// getAuthorities()
//        trả về role:
//        ROLE_ADMIN
//        ROLE_LECTURER
//        ROLE_STUDENT

//        isEnabled() nếu user:ACTIVE => cho login
//        nếu INACTIVE => từ chối login