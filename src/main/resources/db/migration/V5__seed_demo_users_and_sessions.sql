-- =============================================================================
-- V5: Seed demo users (1 admin, 8 counselors, 20 students)
--     + mentorship sessions between them
--
-- All passwords are BCrypt hashes of "Password123"
-- =============================================================================


-- =============================================================================
-- 1. ADMIN
-- =============================================================================
INSERT INTO users (user_type, email, password_hash, full_name, created_at, enabled) VALUES
('ADMIN', 'admin@orientcompanion.ma', '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Hamza El Adminsi', '2025-09-01 08:00:00', 1);

INSERT INTO admins (id) VALUES (LAST_INSERT_ID());


-- =============================================================================
-- 2. COUNSELORS
-- =============================================================================
INSERT INTO users (user_type, email, password_hash, full_name, created_at, enabled) VALUES
('COUNSELOR', 'youssef.amrani@orientcompanion.ma',    '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Youssef Amrani',    '2025-09-02 09:00:00', 1),
('COUNSELOR', 'fatima.bensalem@orientcompanion.ma',   '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Fatima Bensalem',   '2025-09-02 09:15:00', 1),
('COUNSELOR', 'mehdi.chaoui@orientcompanion.ma',      '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Mehdi Chaoui',      '2025-09-02 09:30:00', 1),
('COUNSELOR', 'nadia.errachidi@orientcompanion.ma',   '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Nadia Errachidi',   '2025-09-02 09:45:00', 1),
('COUNSELOR', 'karim.fassi@orientcompanion.ma',       '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Karim Fassi',       '2025-09-02 10:00:00', 1),
('COUNSELOR', 'salma.guerraoui@orientcompanion.ma',   '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Salma Guerraoui',   '2025-09-02 10:15:00', 1),
('COUNSELOR', 'omar.haddad@orientcompanion.ma',       '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Omar Haddad',       '2025-09-02 10:30:00', 1),
('COUNSELOR', 'zineb.idrissi@orientcompanion.ma',     '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Zineb Idrissi',     '2025-09-02 10:45:00', 1);

-- Retrieve counselor IDs (inserted sequentially, admin got id=1)
-- We use a helper temp table to map emails → IDs cleanly
INSERT INTO counselors (id, bio, specialty_field_id)
SELECT u.id,
       CASE u.email
           WHEN 'youssef.amrani@orientcompanion.ma'  THEN 'Ingénieur en Génie Logiciel diplômé de l''ENSIAS. 10 ans d''expérience en développement et accompagnement d''étudiants en informatique.'
           WHEN 'fatima.bensalem@orientcompanion.ma' THEN 'Ancienne praticienne en médecine générale. Conseille les étudiants souhaitant s''orienter vers les filières de la santé.'
           WHEN 'mehdi.chaoui@orientcompanion.ma'    THEN 'Expert en Finance et Comptabilité, ancien auditeur Big Four. Accompagne les profils tournés vers la gestion et les marchés financiers.'
           WHEN 'nadia.errachidi@orientcompanion.ma' THEN 'Architecte DPLG diplômée de l''ENA Rabat. Passionnée par l''urbanisme et le patrimoine architectural marocain.'
           WHEN 'karim.fassi@orientcompanion.ma'     THEN 'Data Scientist senior chez une fintech casablancaise. Expert en IA et machine learning appliqués à l''orientation professionnelle.'
           WHEN 'salma.guerraoui@orientcompanion.ma' THEN 'Docteure en Droit des affaires. Conseille les étudiants en droit, sciences politiques et relations internationales.'
           WHEN 'omar.haddad@orientcompanion.ma'     THEN 'Ingénieur Télécoms diplômé de l''INPT. Spécialiste en cybersécurité et systèmes de communication mobiles.'
           WHEN 'zineb.idrissi@orientcompanion.ma'   THEN 'Psychologue clinicienne et chercheuse en sciences cognitives. Oriente les étudiants vers les filières sciences humaines et sociales.'
       END,
       CASE u.email
           WHEN 'youssef.amrani@orientcompanion.ma'  THEN 1  -- Génie Logiciel
           WHEN 'fatima.bensalem@orientcompanion.ma' THEN 8  -- Médecine Générale
           WHEN 'mehdi.chaoui@orientcompanion.ma'    THEN 13 -- Finance & Comptabilité
           WHEN 'nadia.errachidi@orientcompanion.ma' THEN 17 -- Architecture
           WHEN 'karim.fassi@orientcompanion.ma'     THEN 5  -- IA & Data Science
           WHEN 'salma.guerraoui@orientcompanion.ma' THEN 15 -- Droit
           WHEN 'omar.haddad@orientcompanion.ma'     THEN 7  -- Télécoms & Réseaux
           WHEN 'zineb.idrissi@orientcompanion.ma'   THEN 19 -- Psychologie
       END
FROM users u
WHERE u.email IN (
    'youssef.amrani@orientcompanion.ma',
    'fatima.bensalem@orientcompanion.ma',
    'mehdi.chaoui@orientcompanion.ma',
    'nadia.errachidi@orientcompanion.ma',
    'karim.fassi@orientcompanion.ma',
    'salma.guerraoui@orientcompanion.ma',
    'omar.haddad@orientcompanion.ma',
    'zineb.idrissi@orientcompanion.ma'
);


-- =============================================================================
-- 3. STUDENTS (20 étudiants avec profils diversifiés)
-- =============================================================================
INSERT INTO users (user_type, email, password_hash, full_name, created_at, enabled) VALUES
('STUDENT', 'amine.benali@student.ma',      '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Amine Benali',      '2025-09-05 10:00:00', 1),
('STUDENT', 'sara.moussaoui@student.ma',    '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Sara Moussaoui',    '2025-09-05 10:05:00', 1),
('STUDENT', 'tariq.ouazzani@student.ma',    '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Tariq Ouazzani',    '2025-09-05 10:10:00', 1),
('STUDENT', 'hind.alaoui@student.ma',       '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Hind Alaoui',       '2025-09-05 10:15:00', 1),
('STUDENT', 'rachid.benkirane@student.ma',  '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Rachid Benkirane',  '2025-09-05 10:20:00', 1),
('STUDENT', 'layla.tahiri@student.ma',      '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Layla Tahiri',      '2025-09-05 10:25:00', 1),
('STUDENT', 'yassine.zouiten@student.ma',   '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Yassine Zouiten',   '2025-09-05 10:30:00', 1),
('STUDENT', 'nour.el.houda@student.ma',     '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Nour El Houda',     '2025-09-05 10:35:00', 1),
('STUDENT', 'imad.berrada@student.ma',      '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Imad Berrada',      '2025-09-05 10:40:00', 1),
('STUDENT', 'sofia.nadifi@student.ma',      '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Sofia Nadifi',      '2025-09-05 10:45:00', 1),
('STUDENT', 'kamal.rhazi@student.ma',       '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Kamal Rhazi',       '2025-09-06 09:00:00', 1),
('STUDENT', 'meryem.skali@student.ma',      '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Meryem Skali',      '2025-09-06 09:10:00', 1),
('STUDENT', 'hamza.tazi@student.ma',        '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Hamza Tazi',        '2025-09-06 09:20:00', 1),
('STUDENT', 'dounia.bakkali@student.ma',    '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Dounia Bakkali',    '2025-09-06 09:30:00', 1),
('STUDENT', 'bilal.kettani@student.ma',     '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Bilal Kettani',     '2025-09-06 09:40:00', 1),
('STUDENT', 'rim.el.mansouri@student.ma',   '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Rim El Mansouri',   '2025-09-06 09:50:00', 1),
('STUDENT', 'adil.sabiri@student.ma',       '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Adil Sabiri',       '2025-09-06 10:00:00', 1),
('STUDENT', 'hajar.filali@student.ma',      '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Hajar Filali',      '2025-09-06 10:10:00', 1),
('STUDENT', 'oussama.el.yazghi@student.ma', '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Oussama El Yazghi', '2025-09-06 10:20:00', 1),
('STUDENT', 'ghita.rami@student.ma',        '$2a$12$mQmMG8YqA2E.VK8eMHW2f.xJZNuPqH3G1v0z9OeQiDWPblkBtyW.2', 'Ghita Rami',        '2025-09-06 10:30:00', 0); -- compte suspendu (demo)

-- Student profiles (interests, personality scores RIASEC, academic scores, assessment dates)
INSERT INTO students (id, interests_json, personality_scores_json, academic_scores_json, assessment_date)
SELECT u.id,
       CASE u.email
           WHEN 'amine.benali@student.ma'      THEN '["Programmation","Algorithmique","Jeux vidéo","Mathématiques"]'
           WHEN 'sara.moussaoui@student.ma'    THEN '["Biologie","Médecine","Bénévolat médical","Sciences de la vie"]'
           WHEN 'tariq.ouazzani@student.ma'    THEN '["Finance","Investissement","Bourse","Économie"]'
           WHEN 'hind.alaoui@student.ma'       THEN '["Architecture","Design","Arts plastiques","Urbanisme"]'
           WHEN 'rachid.benkirane@student.ma'  THEN '["Cybersécurité","Réseaux","Hacking éthique","Linux"]'
           WHEN 'layla.tahiri@student.ma'      THEN '["Droit","Justice","Philosophie","Politique"]'
           WHEN 'yassine.zouiten@student.ma'   THEN '["Data Science","Statistiques","Python","Machine Learning"]'
           WHEN 'nour.el.houda@student.ma'     THEN '["Psychologie","Sociologie","Développement personnel","Écoute"]'
           WHEN 'imad.berrada@student.ma'      THEN '["Génie Civil","Construction","Infrastructure","Topographie"]'
           WHEN 'sofia.nadifi@student.ma'      THEN '["Marketing digital","Réseaux sociaux","Design graphique","Communication"]'
           WHEN 'kamal.rhazi@student.ma'       THEN '["Intelligence Artificielle","Robotique","Électronique","Mécatronique"]'
           WHEN 'meryem.skali@student.ma'      THEN '["Pharmacie","Biochimie","Recherche médicale","Chimie"]'
           WHEN 'hamza.tazi@student.ma'        THEN '["Télécommunications","5G","Réseaux mobiles","IoT"]'
           WHEN 'dounia.bakkali@student.ma'    THEN '["Agronomie","Agriculture durable","Environnement","Botanique"]'
           WHEN 'bilal.kettani@student.ma'     THEN '["Sciences politiques","Géopolitique","Relations internationales","Histoire"]'
           WHEN 'rim.el.mansouri@student.ma'   THEN '["Infirmerie","Soins","Santé publique","Bénévolat"]'
           WHEN 'adil.sabiri@student.ma'       THEN '["Génie Électrique","Énergie renouvelable","Automatisme","Robotique"]'
           WHEN 'hajar.filali@student.ma'      THEN '["Design graphique","Illustration","Arts numériques","Photographie"]'
           WHEN 'oussama.el.yazghi@student.ma' THEN '["Commerce","Entrepreneuriat","Management","Leadership"]'
           WHEN 'ghita.rami@student.ma'        THEN '["Médecine dentaire","Chirurgie","Sciences de la santé","Anatomie"]'
       END,
       CASE u.email
           WHEN 'amine.benali@student.ma'      THEN '{"R":40,"I":90,"A":30,"S":20,"E":40,"C":70}'
           WHEN 'sara.moussaoui@student.ma'    THEN '{"R":20,"I":90,"A":20,"S":85,"E":30,"C":50}'
           WHEN 'tariq.ouazzani@student.ma'    THEN '{"R":10,"I":65,"A":25,"S":35,"E":80,"C":85}'
           WHEN 'hind.alaoui@student.ma'       THEN '{"R":55,"I":60,"A":90,"S":45,"E":50,"C":45}'
           WHEN 'rachid.benkirane@student.ma'  THEN '{"R":50,"I":88,"A":20,"S":15,"E":35,"C":80}'
           WHEN 'layla.tahiri@student.ma'      THEN '{"R":10,"I":70,"A":35,"S":65,"E":80,"C":78}'
           WHEN 'yassine.zouiten@student.ma'   THEN '{"R":30,"I":95,"A":30,"S":25,"E":40,"C":70}'
           WHEN 'nour.el.houda@student.ma'     THEN '{"R":10,"I":80,"A":50,"S":90,"E":40,"C":45}'
           WHEN 'imad.berrada@student.ma'      THEN '{"R":82,"I":72,"A":38,"S":28,"E":45,"C":58}'
           WHEN 'sofia.nadifi@student.ma'      THEN '{"R":15,"I":40,"A":72,"S":60,"E":88,"C":40}'
           WHEN 'kamal.rhazi@student.ma'       THEN '{"R":65,"I":88,"A":22,"S":18,"E":32,"C":62}'
           WHEN 'meryem.skali@student.ma'      THEN '{"R":25,"I":88,"A":15,"S":62,"E":28,"C":72}'
           WHEN 'hamza.tazi@student.ma'        THEN '{"R":60,"I":82,"A":18,"S":22,"E":35,"C":65}'
           WHEN 'dounia.bakkali@student.ma'    THEN '{"R":72,"I":68,"A":32,"S":52,"E":48,"C":55}'
           WHEN 'bilal.kettani@student.ma'     THEN '{"R":10,"I":75,"A":42,"S":68,"E":82,"C":65}'
           WHEN 'rim.el.mansouri@student.ma'   THEN '{"R":32,"I":58,"A":18,"S":92,"E":28,"C":50}'
           WHEN 'adil.sabiri@student.ma'       THEN '{"R":78,"I":80,"A":20,"S":18,"E":32,"C":60}'
           WHEN 'hajar.filali@student.ma'      THEN '{"R":30,"I":28,"A":95,"S":42,"E":52,"C":22}'
           WHEN 'oussama.el.yazghi@student.ma' THEN '{"R":12,"I":50,"A":48,"S":58,"E":90,"C":52}'
           WHEN 'ghita.rami@student.ma'        THEN '{"R":52,"I":85,"A":52,"S":72,"E":28,"C":55}'
       END,
       CASE u.email
           WHEN 'amine.benali@student.ma'      THEN '{"math":18,"physique":16,"informatique":19,"français":13,"anglais":15}'
           WHEN 'sara.moussaoui@student.ma'    THEN '{"biologie":19,"chimie":18,"physique":17,"math":16,"français":15}'
           WHEN 'tariq.ouazzani@student.ma'    THEN '{"math":17,"économie":18,"français":16,"anglais":17,"histoire":15}'
           WHEN 'hind.alaoui@student.ma'       THEN '{"math":15,"arts":19,"physique":14,"français":16,"histoire":15}'
           WHEN 'rachid.benkirane@student.ma'  THEN '{"math":17,"informatique":18,"physique":15,"français":12,"anglais":16}'
           WHEN 'layla.tahiri@student.ma'      THEN '{"français":19,"histoire":18,"philosophie":18,"math":13,"anglais":16}'
           WHEN 'yassine.zouiten@student.ma'   THEN '{"math":19,"statistiques":18,"informatique":17,"physique":16,"français":13}'
           WHEN 'nour.el.houda@student.ma'     THEN '{"français":17,"philosophie":18,"biologie":16,"histoire":16,"math":13}'
           WHEN 'imad.berrada@student.ma'      THEN '{"math":18,"physique":17,"géologie":16,"français":13,"dessin":15}'
           WHEN 'sofia.nadifi@student.ma'      THEN '{"français":17,"anglais":18,"arts":16,"économie":15,"math":12}'
           WHEN 'kamal.rhazi@student.ma'       THEN '{"math":18,"physique":17,"informatique":18,"électronique":17,"français":12}'
           WHEN 'meryem.skali@student.ma'      THEN '{"chimie":19,"biologie":18,"math":17,"physique":16,"français":14}'
           WHEN 'hamza.tazi@student.ma'        THEN '{"math":17,"physique":16,"informatique":17,"électronique":16,"français":13}'
           WHEN 'dounia.bakkali@student.ma'    THEN '{"biologie":17,"chimie":16,"géologie":17,"math":15,"français":14}'
           WHEN 'bilal.kettani@student.ma'     THEN '{"histoire":18,"géographie":17,"français":18,"anglais":17,"philosophie":16}'
           WHEN 'rim.el.mansouri@student.ma'   THEN '{"biologie":16,"chimie":15,"français":15,"sciences sociales":17,"math":13}'
           WHEN 'adil.sabiri@student.ma'       THEN '{"math":18,"physique":18,"électronique":17,"informatique":15,"français":12}'
           WHEN 'hajar.filali@student.ma'      THEN '{"arts":19,"français":16,"histoire":15,"anglais":15,"math":11}'
           WHEN 'oussama.el.yazghi@student.ma' THEN '{"économie":17,"français":16,"math":15,"anglais":16,"histoire":14}'
           WHEN 'ghita.rami@student.ma'        THEN '{"biologie":18,"chimie":17,"physique":16,"math":16,"français":14}'
       END,
       CASE u.email
           WHEN 'amine.benali@student.ma'      THEN '2026-01-15'
           WHEN 'sara.moussaoui@student.ma'    THEN '2026-01-16'
           WHEN 'tariq.ouazzani@student.ma'    THEN '2026-01-17'
           WHEN 'hind.alaoui@student.ma'       THEN '2026-01-18'
           WHEN 'rachid.benkirane@student.ma'  THEN '2026-01-20'
           WHEN 'layla.tahiri@student.ma'      THEN '2026-01-21'
           WHEN 'yassine.zouiten@student.ma'   THEN '2026-01-22'
           WHEN 'nour.el.houda@student.ma'     THEN '2026-01-23'
           WHEN 'imad.berrada@student.ma'      THEN '2026-02-03'
           WHEN 'sofia.nadifi@student.ma'      THEN '2026-02-04'
           WHEN 'kamal.rhazi@student.ma'       THEN '2026-02-10'
           WHEN 'meryem.skali@student.ma'      THEN '2026-02-11'
           WHEN 'hamza.tazi@student.ma'        THEN '2026-02-12'
           WHEN 'dounia.bakkali@student.ma'    THEN '2026-02-14'
           WHEN 'bilal.kettani@student.ma'     THEN '2026-02-17'
           WHEN 'rim.el.mansouri@student.ma'   THEN '2026-02-18'
           WHEN 'adil.sabiri@student.ma'       THEN '2026-03-01'
           WHEN 'hajar.filali@student.ma'      THEN '2026-03-02'
           WHEN 'oussama.el.yazghi@student.ma' THEN '2026-03-05'
           WHEN 'ghita.rami@student.ma'        THEN NULL -- pas encore passé l'assessment
       END
FROM users u
WHERE u.email IN (
    'amine.benali@student.ma',
    'sara.moussaoui@student.ma',
    'tariq.ouazzani@student.ma',
    'hind.alaoui@student.ma',
    'rachid.benkirane@student.ma',
    'layla.tahiri@student.ma',
    'yassine.zouiten@student.ma',
    'nour.el.houda@student.ma',
    'imad.berrada@student.ma',
    'sofia.nadifi@student.ma',
    'kamal.rhazi@student.ma',
    'meryem.skali@student.ma',
    'hamza.tazi@student.ma',
    'dounia.bakkali@student.ma',
    'bilal.kettani@student.ma',
    'rim.el.mansouri@student.ma',
    'adil.sabiri@student.ma',
    'hajar.filali@student.ma',
    'oussama.el.yazghi@student.ma',
    'ghita.rami@student.ma'
);


-- =============================================================================
-- 4. MENTORSHIP SESSIONS
-- =============================================================================
INSERT INTO mentorship_sessions (student_id, counselor_id, status, scheduled_at, created_at)
SELECT
    s.id AS student_id,
    c.id AS counselor_id,
    ms.status,
    ms.scheduled_at,
    ms.created_at
FROM (
    VALUES
    -- (student_email, counselor_email, status, scheduled_at, created_at)
    ROW('amine.benali@student.ma',      'youssef.amrani@orientcompanion.ma',  'COMPLETED',  '2026-02-05 10:00:00', '2026-01-25 08:00:00'),
    ROW('amine.benali@student.ma',      'karim.fassi@orientcompanion.ma',     'COMPLETED',  '2026-03-10 14:00:00', '2026-03-01 09:00:00'),
    ROW('amine.benali@student.ma',      'youssef.amrani@orientcompanion.ma',  'SCHEDULED',  '2026-10-15 09:00:00', '2026-09-10 10:00:00'),
    ROW('sara.moussaoui@student.ma',    'fatima.bensalem@orientcompanion.ma', 'COMPLETED',  '2026-02-12 11:00:00', '2026-02-01 08:00:00'),
    ROW('sara.moussaoui@student.ma',    'fatima.bensalem@orientcompanion.ma', 'SCHEDULED',  '2026-10-20 10:00:00', '2026-09-12 08:00:00'),
    ROW('tariq.ouazzani@student.ma',    'mehdi.chaoui@orientcompanion.ma',    'COMPLETED',  '2026-02-20 09:00:00', '2026-02-10 07:30:00'),
    ROW('tariq.ouazzani@student.ma',    'mehdi.chaoui@orientcompanion.ma',    'SCHEDULED',  '2026-10-22 11:00:00', '2026-09-14 09:00:00'),
    ROW('hind.alaoui@student.ma',       'nadia.errachidi@orientcompanion.ma', 'COMPLETED',  '2026-03-01 14:00:00', '2026-02-18 10:00:00'),
    ROW('hind.alaoui@student.ma',       'nadia.errachidi@orientcompanion.ma', 'REQUESTED',  NULL,                  '2026-09-15 09:00:00'),
    ROW('rachid.benkirane@student.ma',  'omar.haddad@orientcompanion.ma',     'COMPLETED',  '2026-03-05 10:00:00', '2026-02-22 08:00:00'),
    ROW('rachid.benkirane@student.ma',  'youssef.amrani@orientcompanion.ma',  'SCHEDULED',  '2026-10-18 10:00:00', '2026-09-08 09:00:00'),
    ROW('layla.tahiri@student.ma',      'salma.guerraoui@orientcompanion.ma', 'COMPLETED',  '2026-03-08 15:00:00', '2026-02-25 09:00:00'),
    ROW('layla.tahiri@student.ma',      'salma.guerraoui@orientcompanion.ma', 'REQUESTED',  NULL,                  '2026-09-16 10:00:00'),
    ROW('yassine.zouiten@student.ma',   'karim.fassi@orientcompanion.ma',     'COMPLETED',  '2026-03-12 09:00:00', '2026-03-02 08:00:00'),
    ROW('yassine.zouiten@student.ma',   'karim.fassi@orientcompanion.ma',     'SCHEDULED',  '2026-10-25 14:00:00', '2026-09-17 08:30:00'),
    ROW('nour.el.houda@student.ma',     'zineb.idrissi@orientcompanion.ma',   'COMPLETED',  '2026-03-15 11:00:00', '2026-03-05 09:00:00'),
    ROW('nour.el.houda@student.ma',     'zineb.idrissi@orientcompanion.ma',   'REQUESTED',  NULL,                  '2026-09-17 11:00:00'),
    ROW('imad.berrada@student.ma',      'youssef.amrani@orientcompanion.ma',  'COMPLETED',  '2026-04-01 10:00:00', '2026-03-20 09:00:00'),
    ROW('sofia.nadifi@student.ma',      'salma.guerraoui@orientcompanion.ma', 'REQUESTED',  NULL,                  '2026-09-18 08:00:00'),
    ROW('kamal.rhazi@student.ma',       'omar.haddad@orientcompanion.ma',     'COMPLETED',  '2026-04-10 09:00:00', '2026-04-01 08:00:00'),
    ROW('kamal.rhazi@student.ma',       'karim.fassi@orientcompanion.ma',     'SCHEDULED',  '2026-10-28 10:00:00', '2026-09-15 09:00:00'),
    ROW('meryem.skali@student.ma',      'fatima.bensalem@orientcompanion.ma', 'COMPLETED',  '2026-04-15 14:00:00', '2026-04-05 08:30:00'),
    ROW('meryem.skali@student.ma',      'fatima.bensalem@orientcompanion.ma', 'REQUESTED',  NULL,                  '2026-09-18 10:00:00'),
    ROW('hamza.tazi@student.ma',        'omar.haddad@orientcompanion.ma',     'SCHEDULED',  '2026-10-30 11:00:00', '2026-09-16 10:00:00'),
    ROW('dounia.bakkali@student.ma',    'zineb.idrissi@orientcompanion.ma',   'REQUESTED',  NULL,                  '2026-09-17 12:00:00'),
    ROW('bilal.kettani@student.ma',     'salma.guerraoui@orientcompanion.ma', 'COMPLETED',  '2026-05-05 10:00:00', '2026-04-25 09:00:00'),
    ROW('bilal.kettani@student.ma',     'salma.guerraoui@orientcompanion.ma', 'SCHEDULED',  '2026-11-02 09:00:00', '2026-09-18 08:00:00'),
    ROW('rim.el.mansouri@student.ma',   'fatima.bensalem@orientcompanion.ma', 'COMPLETED',  '2026-05-10 11:00:00', '2026-04-30 10:00:00'),
    ROW('adil.sabiri@student.ma',       'youssef.amrani@orientcompanion.ma',  'REQUESTED',  NULL,                  '2026-09-18 11:00:00'),
    ROW('oussama.el.yazghi@student.ma', 'mehdi.chaoui@orientcompanion.ma',    'COMPLETED',  '2026-05-20 14:00:00', '2026-05-10 09:00:00'),
    ROW('oussama.el.yazghi@student.ma', 'mehdi.chaoui@orientcompanion.ma',    'SCHEDULED',  '2026-11-05 10:00:00', '2026-09-18 12:00:00')
) AS ms(student_email, counselor_email, status, scheduled_at, created_at)
JOIN users s ON s.email = ms.student_email
JOIN users c ON c.email = ms.counselor_email;
