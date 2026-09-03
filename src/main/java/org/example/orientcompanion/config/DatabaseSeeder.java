//package org.example.orientcompanion.config;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.orientcompanion.entity.Field;
//import org.example.orientcompanion.repository.FieldRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class DatabaseSeeder implements CommandLineRunner {
//
//    private final FieldRepository fieldRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (fieldRepository.count() == 0) {
//            log.info("Le catalogue de filières est vide. Initialisation avec des données de test...");
//
//            Field genieLogiciel = Field.builder()
//                    .name("Génie Logiciel")
//                    .description("Étude de la conception, du développement et de la maintenance des systèmes logiciels.")
//                    .requiredTraitsJson("{\"R\": 40, \"I\": 90, \"A\": 30, \"S\": 20, \"E\": 40, \"C\": 70}")
//                    .category("Sciences et Technologies")
//                    .relatedSubjects("Mathématiques,Informatique,Physique")
//                    .build();
//
//            Field medecine = Field.builder()
//                    .name("Médecine")
//                    .description("Diagnostic, traitement et prévention des maladies humaines et soins de santé.")
//                    .requiredTraitsJson("{\"R\": 20, \"I\": 95, \"A\": 20, \"S\": 80, \"E\": 30, \"C\": 50}")
//                    .category("Santé")
//                    .relatedSubjects("Biologie,Chimie,Mathématiques")
//                    .build();
//
//            Field marketing = Field.builder()
//                    .name("Marketing et Commerce")
//                    .description("Analyse des marchés, stratégies de vente, publicité et relations clients.")
//                    .requiredTraitsJson("{\"R\": 10, \"I\": 40, \"A\": 60, \"S\": 50, \"E\": 90, \"C\": 40}")
//                    .category("Commerce et Gestion")
//                    .relatedSubjects("Économie,Français,Anglais")
//                    .build();
//
//            Field artDesign = Field.builder()
//                    .name("Art et Design")
//                    .description("Création visuelle, design graphique, modélisation et expression artistique.")
//                    .requiredTraitsJson("{\"R\": 30, \"I\": 30, \"A\": 95, \"S\": 40, \"E\": 50, \"C\": 20}")
//                    .category("Arts et Lettres")
//                    .relatedSubjects("Arts Plastiques,Français,Histoire")
//                    .build();
//
//            fieldRepository.saveAll(List.of(genieLogiciel, medecine, marketing, artDesign));
//            log.info("Initialisation du catalogue de filières terminée avec {} éléments.", fieldRepository.count());
//        } else {
//            log.info("Le catalogue de filières contient déjà {} éléments.", fieldRepository.count());
//        }
//    }
//}
