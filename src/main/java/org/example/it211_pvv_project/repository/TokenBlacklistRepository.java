package org.example.it211_pvv_project.repository;

import org.example.it211_pvv_project.model.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

//Kiểm tra token đã logout.
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByToken(String token);
}