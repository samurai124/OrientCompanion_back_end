package org.example.orientcompanion.repository;

import org.example.orientcompanion.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    List<School> findByFieldId(Long fieldId);

    List<School> findByNameContainingIgnoreCase(String name);

    long countAllByType(String type);
}
