package org.example.orientcompanion.repository;

import org.example.orientcompanion.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByStudentIdOrderByScoreDesc(Long studentId);

    List<Recommendation> findByFieldId(Long fieldId);

    void deleteByStudentId(Long studentId);
}
