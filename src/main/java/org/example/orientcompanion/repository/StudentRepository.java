package org.example.orientcompanion.repository;
import org.example.orientcompanion.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}