package org.example.it211_pvv_project.security.principal;

import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.model.entity.User;
import org.example.it211_pvv_project.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

//Tải thông tin người dùng từ database
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}


//        Khi login: (admin;123456)
//        Spring sẽ tự gọi: loadUserByUsername("admin")
//              ↓
//        userRepository.findByUsername(...)
//              ↓
//        lấy user từ DB
//              ↓
//        đóng gói thành:new CustomUserDetails(user)
//              ↓
//        Spring Security sử dụng.