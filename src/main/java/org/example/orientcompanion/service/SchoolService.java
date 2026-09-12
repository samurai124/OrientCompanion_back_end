package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.SchoolRequest;
import org.example.orientcompanion.dto.SchoolResponse;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.entity.School;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.mapper.SchoolMapper;
import org.example.orientcompanion.repository.FieldRepository;
import org.example.orientcompanion.repository.SchoolRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "schools")
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final FieldRepository fieldRepository;
    private final SchoolMapper schoolMapper;

    @Cacheable(key = "'all'")
    public List<SchoolResponse> findAll() {
        return schoolRepository.findAll()
                .stream()
                .map(schoolMapper::toResponse)
                .toList();
    }

    @Cacheable(key = "'field:' + #fieldId")
    public List<SchoolResponse> findByFieldId(Long fieldId) {
        return schoolRepository.findByFieldId(fieldId)
                .stream()
                .map(schoolMapper::toResponse)
                .toList();
    }

    @Cacheable(key = "#id")
    public SchoolResponse findById(Long id) {
        return schoolMapper.toResponse(
                schoolRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("École introuvable avec l'id : " + id))
        );
    }

    @Transactional
    @CacheEvict(key = "'all'", allEntries = true)
    public SchoolResponse create(SchoolRequest request) {
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

        return schoolMapper.toResponse(schoolRepository.save(school));
    }

    @Transactional
    @CachePut(key = "#id")
    @CacheEvict(allEntries = true)
    public SchoolResponse update(Long id, SchoolRequest request) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("École introuvable avec l'id : " + id));

        Field field = fieldRepository.findById(request.getFieldId())
                .orElseThrow(() -> new ResourceNotFoundException("Filière introuvable avec l'id : " + request.getFieldId()));

        schoolMapper.updateFromRequest(request, school);
        school.setField(field);

        return schoolMapper.toResponse(schoolRepository.save(school));
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        if (!schoolRepository.existsById(id)) {
            throw new ResourceNotFoundException("École introuvable avec l'id : " + id);
        }
        schoolRepository.deleteById(id);
    }
}