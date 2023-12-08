package com.recipes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.recipes.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByUsername(String email);

    @Query(value = "SELECT * FROM users INNER JOIN user_role ON users.user_id = user_role.user_id WHERE username = :username", nativeQuery = true)
    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findAll();

    @Query(value = "SELECT ROLES FROM USER_ROLE WHERE USER_ID= ?", nativeQuery = true)
    String findRolesById(Integer id);

    Page<UserEntity> findAllByUsernameNotLike(String username, Pageable pageable);
}