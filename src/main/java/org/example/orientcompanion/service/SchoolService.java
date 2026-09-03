package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.SchoolRequest;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.entity.School;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.repository.FieldRepository;
import org.example.orientcompanion.repository.SchoolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final FieldRepository fieldRepository;

    public List<School> findAll() {
        return schoolRepository.findAll();
    }

    public List<School> findByFieldId(Long fieldId) {
        return schoolRepository.findByFieldId(fieldId);
    }

    public School findById(Long id) {
        return schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("École introuvable avec l'id : " + id));
    }

    @Transactional
    public School create(SchoolRequest request) {
        Field field = fieldRepository.findById(request.getFieldId())
                .orElseThrow(() -> new ResourceNotFoundException("Filière introuvable avec l'id : " + request.getFieldId()));

        School school = School.builder()
                .name(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .type(request.getType())
                .website(request.getWebsite())
                .description(request.getDescription())
                .field(field)
                .build();

        return schoolRepository.save(school);
    }

    @Transactional
    public School update(Long id, SchoolRequest request) {
        School school = findById(id);
        Field field = fieldRepository.findById(request.getFieldId())
                .orElseThrow(() -> new ResourceNotFoundException("Filière introuvable avec l'id : " + request.getFieldId()));

        school.setName(request.getName());
        school.setCity(request.getCity());
        school.setCountry(request.getCountry());
        school.setType(request.getType());
        school.setWebsite(request.getWebsite());
        school.setDescription(request.getDescription());
        school.setField(field);

        return schoolRepository.save(school);
    }

    @Transactional
    public void delete(Long id) {
        School school = findById(id);
        schoolRepository.delete(school);
    }
}
