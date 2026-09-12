package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.FieldRequest;
import org.example.orientcompanion.dto.FieldResponse;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.mapper.FieldMapper;
import org.example.orientcompanion.repository.FieldRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "fields")
public class FieldService {

    private final FieldRepository fieldRepository;
    private final FieldMapper fieldMapper;

    @Cacheable(key = "'all'")
    public List<FieldResponse> findAll() {
        return fieldRepository.findAll()
                .stream()
                .map(fieldMapper::toResponse)
                .toList();
    }

    @Cacheable(key = "#id")
    public FieldResponse findById(Long id) {
        return fieldMapper.toResponse(findEntityById(id));
    }

    public Field findEntityById(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filière introuvable avec l'id : " + id));
    }

    @Cacheable(key = "'category:' + #category")
    public List<FieldResponse> findByCategory(String category) {
        return fieldRepository.findByCategory(category)
                .stream()
                .map(fieldMapper::toResponse)
                .toList();
    }

    public List<FieldResponse> search(String keyword) {
        return fieldRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(fieldMapper::toResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public FieldResponse create(FieldRequest request) {
        Field field = fieldMapper.toEntity(request);
        return fieldMapper.toResponse(fieldRepository.save(field));
    }

    @Transactional
    @CachePut(key = "#id")
    @CacheEvict(allEntries = true)
    public FieldResponse update(Long id, FieldRequest request) {
        Field field = findEntityById(id);
        fieldMapper.updateFromRequest(request, field);
        return fieldMapper.toResponse(fieldRepository.save(field));
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        if (!fieldRepository.existsById(id)) {
            throw new ResourceNotFoundException("Filière introuvable avec l'id : " + id);
        }
        fieldRepository.deleteById(id);
    }
}

