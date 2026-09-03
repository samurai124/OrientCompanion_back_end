package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.dto.FieldRequest;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.repository.FieldRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final FieldRepository fieldRepository;

    public List<Field> findAll() {
        return fieldRepository.findAll();
    }

    public Field findById(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filière introuvable avec l'id : " + id));
    }

    public List<Field> findByCategory(String category) {
        return fieldRepository.findByCategory(category);
    }

    public List<Field> search(String keyword) {
        return fieldRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Field create(FieldRequest request) {
        Field field = Field.builder()
                .name(request.getName())
                .description(request.getDescription())
                .requiredTraitsJson(request.getRequiredTraitsJson())
                .category(request.getCategory())
                .relatedSubjects(request.getRelatedSubjects())
                .build();



        return fieldRepository.save(field);
    }

    public Field update(Long id, FieldRequest request) {
        Field field = findById(id);

        field.setName(request.getName());
        field.setDescription(request.getDescription());
        field.setRequiredTraitsJson(request.getRequiredTraitsJson());
        field.setCategory(request.getCategory());
        field.setRelatedSubjects(request.getRelatedSubjects());


        return fieldRepository.save(field);
    }

    public void delete(Long id) {
        Field field = findById(id);
        fieldRepository.delete(field);
    }
}