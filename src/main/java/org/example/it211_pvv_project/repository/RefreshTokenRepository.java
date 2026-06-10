package org.example.it211_pvv_project.repository;

import org.example.it211_pvv_project.model.entity.RefreshToken;
import org.example.it211_pvv_project.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Quản lý Refresh Token.
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}