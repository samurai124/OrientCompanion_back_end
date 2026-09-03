package org.example.orientcompanion.repository;

import org.example.orientcompanion.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field, Long> {

    List<Field> findByCategory(String category);

    List<Field> findByNameContainingIgnoreCase(String keyword);
}