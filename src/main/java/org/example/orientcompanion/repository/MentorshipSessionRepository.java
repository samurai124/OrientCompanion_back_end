package org.example.orientcompanion.repository;

import org.example.orientcompanion.entity.MentorshipSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorshipSessionRepository extends JpaRepository<MentorshipSession, Long> {

    List<MentorshipSession> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    List<MentorshipSession> findByCounselorIdOrderByCreatedAtDesc(Long counselorId);
}