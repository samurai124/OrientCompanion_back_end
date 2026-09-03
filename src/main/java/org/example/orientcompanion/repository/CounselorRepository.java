package org.example.orientcompanion.repository;

import org.example.orientcompanion.entity.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CounselorRepository extends JpaRepository<Counselor, Long> {

    List<Counselor> findBySpecialtyField_Id(Long fieldId);}