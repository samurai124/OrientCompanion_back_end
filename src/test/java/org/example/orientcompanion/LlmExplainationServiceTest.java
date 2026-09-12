package org.example.orientcompanion;

import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.entity.Student;
import org.example.orientcompanion.service.LlmExplanationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LlmExplainationServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    private LlmExplanationService llmExplanationService;

    private Student student;
    private Field field;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.build()).thenReturn(webClient);

        llmExplanationService = new LlmExplanationService(webClientBuilder);
        ReflectionTestUtils.setField(llmExplanationService, "apiUrl", "https://api.openai.com/v1");
        ReflectionTestUtils.setField(llmExplanationService, "apiKey", "test-key");
        ReflectionTestUtils.setField(llmExplanationService, "model", "gpt-4o-mini");
        ReflectionTestUtils.setField(llmExplanationService, "timeoutMs", 5000L);

        student = new Student();
        student.setId(1L);
        student.setInterestsJson("Software, AI, Web");

        field = new Field();
        field.setId(10L);
        field.setName("Génie Informatique");
        field.setDescription("Formation centrée sur le développement logiciel.");
    }

    @Test
    @DisplayName("Should return fallback explanation when apiUrl is null or blank")
    void generateExplanation_WhenApiUrlIsBlank_ShouldReturnFallback() {
        ReflectionTestUtils.setField(llmExplanationService, "apiUrl", "");

        String explanation = llmExplanationService.generateExplanation(student, field, 85.0);

        assertThat(explanation)
                .contains("Génie Informatique")
                .contains("une très bonne correspondance")
                .contains("85")
                .contains("/100");

        verifyNoInteractions(webClient);
    }

    @Test
    @DisplayName("Should return LLM response when WebClient call succeeds")
    void generateExplanation_WhenLlmCallSucceeds_ShouldReturnGeneratedText() {
        String expectedContent = "Cette filière correspond parfaitement à vos compétences en développement.";
        WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        Object mockResponse = createMockChatResponse(expectedContent);
        when(responseSpec.bodyToMono(any(Class.class))).thenReturn(Mono.just(mockResponse));

        String explanation = llmExplanationService.generateExplanation(student, field, 85.0);

        assertThat(explanation).isEqualTo(expectedContent);
    }

    @Test
    @DisplayName("Should fallback gracefully when WebClient throws exception or times out")
    void generateExplanation_WhenLlmCallFails_ShouldReturnFallback() {
        when(webClient.post()).thenThrow(new RuntimeException("Timeout or Connection refused"));

        String explanation = llmExplanationService.generateExplanation(student, field, 60.0);

        assertThat(explanation)
                .contains("Génie Informatique")
                .contains("une correspondance modérée")
                .contains("60")
                .contains("/100");
    }

    private Object createMockChatResponse(String content) {
        try {
            Class<?> messageClass = Class.forName("org.example.orientcompanion.service.LlmExplanationService$ChatMessage");
            Class<?> choiceClass = Class.forName("org.example.orientcompanion.service.LlmExplanationService$Choice");
            Class<?> responseClass = Class.forName("org.example.orientcompanion.service.LlmExplanationService$ChatResponse");

            var msgConstructor = messageClass.getDeclaredConstructor(String.class, String.class);
            msgConstructor.setAccessible(true);
            Object message = msgConstructor.newInstance("assistant", content);

            var choiceConstructor = choiceClass.getDeclaredConstructor(messageClass);
            choiceConstructor.setAccessible(true);
            Object choice = choiceConstructor.newInstance(message);

            var responseConstructor = responseClass.getDeclaredConstructor(java.util.List.class);
            responseConstructor.setAccessible(true);
            return responseConstructor.newInstance(java.util.List.of(choice));
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate private records for test", e);
        }
    }
}