package org.example.orientcompanion.repository;
import org.example.orientcompanion.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.assessmentDate IS NOT NULL AND s.personalityScoresJson IS NOT NULL")
    List<Student> findAllAssessedStudents();

    List<Student> findAllByAssessmentDateIsNotNullOrderByAssessmentDateDesc(Pageable pageable);
}