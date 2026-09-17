package org.example.orientcompanion.repository;

import org.example.orientcompanion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE TYPE(u) = org.example.orientcompanion.entity.Student")
    long countStudents();

    @Query("SELECT COUNT(u) FROM User u WHERE TYPE(u) = org.example.orientcompanion.entity.Counselor")
    long countCounselors();
}