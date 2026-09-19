package org.example.orientcompanion;

import org.example.orientcompanion.dto.RecommendationResponse;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.entity.Recommendation;
import org.example.orientcompanion.entity.Student;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.mapper.RecommendationMapper;
import org.example.orientcompanion.repository.FieldRepository;
import org.example.orientcompanion.repository.RecommendationRepository;
import org.example.orientcompanion.repository.StudentRepository;
import org.example.orientcompanion.service.LlmExplanationService;
import org.example.orientcompanion.service.RecommendationService;
import org.example.orientcompanion.service.SchoolService;
import org.example.orientcompanion.service.ScoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ScoringService scoringService;

    @Mock
    private LlmExplanationService llmExplanationService;

    @Mock
    private RecommendationMapper recommendationMapper;

    @Mock
    private SchoolService schoolService;

    @InjectMocks
    private RecommendationService recommendationService;

    private Student student;
    private Field fieldInfo;
    private Field fieldGtr;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(recommendationService, "structuredWeight", 0.60);
        ReflectionTestUtils.setField(recommendationService, "vectorWeight", 0.40);

        student = new Student();
        student.setId(1L);
        student.setProfileEmbedding("[0.1,0.2,0.3]");

        fieldInfo = new Field();
        fieldInfo.setId(10L);
        fieldInfo.setName("Génie Informatique");
        fieldInfo.setFieldEmbedding("[0.1,0.2,0.3]");

        fieldGtr = new Field();
        fieldGtr.setId(20L);
        fieldGtr.setName("Génie Réseaux et Telecoms");
        fieldGtr.setFieldEmbedding("[0.4,0.5,0.6]");
    }

    @Test
    @DisplayName("Should return existing recommendations for student")
    void getRecommendations_WhenRecommendationsExist_ShouldReturnResponseList() {
        Recommendation recommendation = Recommendation.builder()
                .id(100L)
                .student(student)
                .field(fieldInfo)
                .score(88.5)
                .explanation("Très bon profil")
                .build();

        RecommendationResponse response = new RecommendationResponse();
        response.setId(100L);
        response.setScore(88.5);

        when(recommendationRepository.findByStudentIdOrderByScoreDesc(1L))
                .thenReturn(List.of(recommendation));
        when(recommendationMapper.toResponse(recommendation)).thenReturn(response);
        when(schoolService.findByFieldId(10L)).thenReturn(Collections.emptyList());

        List<RecommendationResponse> result = recommendationService.getRecommendations(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getScore()).isEqualTo(88.5);

        verify(recommendationRepository).findByStudentIdOrderByScoreDesc(1L);
        verify(schoolService).findByFieldId(10L);
    }

    @Test
    @DisplayName("Should generate recommendations successfully when student and fields exist")
    void generateRecommendations_WhenStudentAndFieldsExist_ShouldReturnSavedRecommendations() {
        List<Field> fields = List.of(fieldInfo, fieldGtr);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(fieldRepository.findAll()).thenReturn(fields);
        when(scoringService.calculateStructuredScore(eq(student), any(Field.class))).thenReturn(80.0);
        when(llmExplanationService.generateExplanation(eq(student), any(Field.class), anyDouble()))
                .thenReturn("Explication générée par IA");

        when(recommendationRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        RecommendationResponse responseInfo = new RecommendationResponse();
        responseInfo.setScore(80.0);
        when(recommendationMapper.toResponse(any(Recommendation.class))).thenReturn(responseInfo);

        List<RecommendationResponse> responses = recommendationService.generateRecommendations(1L);

        assertThat(responses).hasSize(2);
        verify(recommendationRepository).deleteByStudentId(1L);
        verify(recommendationRepository).saveAll(anyList());
        verify(llmExplanationService, times(2)).generateExplanation(eq(student), any(Field.class), anyDouble());
    }

    @Test
    @DisplayName("Should fall back to structured score when embeddings are missing")
    void generateRecommendations_WhenEmbeddingsAreNull_ShouldUseOnlyStructuredScore() {
        student.setProfileEmbedding(null);
        List<Field> fields = List.of(fieldInfo);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(fieldRepository.findAll()).thenReturn(fields);
        when(scoringService.calculateStructuredScore(student, fieldInfo)).thenReturn(75.0);

        when(llmExplanationService.generateExplanation(eq(student), eq(fieldInfo), anyDouble()))
                .thenReturn("Score structuré uniquement");

        when(recommendationRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(recommendationMapper.toResponse(any())).thenReturn(new RecommendationResponse());

        recommendationService.generateRecommendations(1L);

        verify(llmExplanationService).generateExplanation(eq(student), eq(fieldInfo), anyDouble());
    }

    @Test
    @DisplayName("Should return empty list when no fields are found in database")
    void generateRecommendations_WhenNoFieldsExist_ShouldReturnEmptyList() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(fieldRepository.findAll()).thenReturn(Collections.emptyList());

        List<RecommendationResponse> responses = recommendationService.generateRecommendations(1L);

        assertThat(responses).isEmpty();
        verify(recommendationRepository, never()).deleteByStudentId(anyLong());
        verify(recommendationRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student ID does not exist")
    void generateRecommendations_WhenStudentNotFound_ShouldThrowException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recommendationService.generateRecommendations(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Profil étudiant introuvable");

        verifyNoInteractions(fieldRepository, recommendationRepository, scoringService);
    }
}