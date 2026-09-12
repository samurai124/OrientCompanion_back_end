

INSERT INTO fields (id, name, description, required_traits_json, category, related_subjects) VALUES

-- Sciences & Technologies
(1,  'Génie Logiciel',
     'Conception, développement et maintenance de systèmes logiciels complexes.',
     '{"R": 40, "I": 90, "A": 30, "S": 20, "E": 40, "C": 70}',
     'Sciences et Technologies', 'Mathématiques,Informatique,Physique'),

(2,  'Génie Informatique',
     'Systèmes embarqués, réseaux, architecture matérielle et logicielle.',
     '{"R": 60, "I": 85, "A": 20, "S": 20, "E": 30, "C": 65}',
     'Sciences et Technologies', 'Mathématiques,Informatique,Physique,Électronique'),

(3,  'Génie Civil',
     'Conception et réalisation d''ouvrages de bâtiment, routes et infrastructures.',
     '{"R": 80, "I": 70, "A": 40, "S": 30, "E": 50, "C": 60}',
     'Sciences et Technologies', 'Mathématiques,Physique,Géologie'),

(4,  'Génie Électrique',
     'Systèmes électriques, électronique de puissance et automatisme.',
     '{"R": 75, "I": 80, "A": 20, "S": 20, "E": 35, "C": 60}',
     'Sciences et Technologies', 'Mathématiques,Physique,Électronique'),

(5,  'Intelligence Artificielle & Data Science',
     'Apprentissage automatique, big data, modélisation statistique.',
     '{"R": 30, "I": 95, "A": 35, "S": 25, "E": 45, "C": 70}',
     'Sciences et Technologies', 'Mathématiques,Informatique,Statistiques'),

(6,  'Cybersécurité',
     'Protection des systèmes d''information, cryptographie, audit de sécurité.',
     '{"R": 50, "I": 90, "A": 20, "S": 20, "E": 40, "C": 80}',
     'Sciences et Technologies', 'Informatique,Mathématiques,Réseaux'),

(7,  'Télécommunications & Réseaux',
     'Réseaux mobiles, protocoles, fibre optique et systèmes de communication.',
     '{"R": 60, "I": 82, "A": 15, "S": 20, "E": 35, "C": 65}',
     'Sciences et Technologies', 'Physique,Informatique,Mathématiques,Électronique'),

-- Santé
(8,  'Médecine Générale',
     'Diagnostic, traitement et prévention des maladies humaines.',
     '{"R": 20, "I": 95, "A": 20, "S": 80, "E": 30, "C": 50}',
     'Santé', 'Biologie,Chimie,Mathématiques,Physique'),

(9,  'Pharmacie',
     'Sciences du médicament, biochimie et pratique officinale.',
     '{"R": 25, "I": 90, "A": 15, "S": 60, "E": 30, "C": 70}',
     'Santé', 'Chimie,Biologie,Mathématiques'),

(10, 'Médecine Dentaire',
     'Soins bucco-dentaires, chirurgie orale et orthodontie.',
     '{"R": 50, "I": 85, "A": 50, "S": 70, "E": 30, "C": 55}',
     'Santé', 'Biologie,Chimie,Physique'),

(11, 'Sciences Infirmières',
     'Soins infirmiers, santé publique et prise en charge des patients.',
     '{"R": 30, "I": 60, "A": 20, "S": 90, "E": 30, "C": 50}',
     'Santé', 'Biologie,Chimie,Sciences sociales'),

-- Commerce & Gestion
(12, 'Commerce & Gestion',
     'Analyse des marchés, stratégies d''entreprise et management.',
     '{"R": 10, "I": 50, "A": 50, "S": 55, "E": 90, "C": 50}',
     'Commerce et Gestion', 'Économie,Mathématiques,Français,Anglais'),

(13, 'Finance & Comptabilité',
     'Comptabilité, audit, marchés financiers et gestion de portefeuille.',
     '{"R": 10, "I": 60, "A": 20, "S": 30, "E": 70, "C": 85}',
     'Commerce et Gestion', 'Mathématiques,Économie,Statistiques'),

(14, 'Marketing & Communication',
     'Stratégie marketing, publicité, branding et marketing digital.',
     '{"R": 10, "I": 40, "A": 70, "S": 60, "E": 90, "C": 40}',
     'Commerce et Gestion', 'Économie,Français,Anglais,Arts Plastiques'),

-- Droit & Sciences Politiques
(15, 'Droit',
     'Droit civil, pénal, international et des affaires.',
     '{"R": 10, "I": 70, "A": 30, "S": 60, "E": 75, "C": 80}',
     'Droit et Sciences Politiques', 'Français,Histoire,Philosophie'),

(16, 'Sciences Politiques & Relations Internationales',
     'Gouvernance, diplomatie, géopolitique et institutions internationales.',
     '{"R": 10, "I": 75, "A": 40, "S": 65, "E": 80, "C": 65}',
     'Droit et Sciences Politiques', 'Histoire,Géographie,Français,Anglais'),

-- Arts & Lettres
(17, 'Architecture',
     'Conception architecturale, urbanisme et développement durable.',
     '{"R": 60, "I": 65, "A": 90, "S": 40, "E": 55, "C": 50}',
     'Arts et Lettres', 'Mathématiques,Arts Plastiques,Histoire,Physique'),

(18, 'Design Graphique & Arts Visuels',
     'Création visuelle, design graphique, illustration et arts numériques.',
     '{"R": 30, "I": 30, "A": 95, "S": 40, "E": 55, "C": 20}',
     'Arts et Lettres', 'Arts Plastiques,Français,Histoire'),

-- Sciences Humaines & Sociales
(19, 'Psychologie',
     'Comportement humain, psychologie clinique et neurosciences.',
     '{"R": 10, "I": 80, "A": 50, "S": 85, "E": 40, "C": 45}',
     'Sciences Humaines', 'Biologie,Philosophie,Français,Sciences sociales'),

(20, 'Sociologie & Sciences Humaines',
     'Analyse des sociétés, des cultures et des comportements collectifs.',
     '{"R": 10, "I": 70, "A": 50, "S": 80, "E": 45, "C": 40}',
     'Sciences Humaines', 'Histoire,Géographie,Français,Sciences sociales'),

-- Agronomie & Environnement
(21, 'Agronomie & Agroalimentaire',
     'Sciences agricoles, technologie alimentaire et développement rural.',
     '{"R": 70, "I": 65, "A": 30, "S": 50, "E": 45, "C": 55}',
     'Agronomie et Environnement', 'Biologie,Chimie,Mathématiques,Sciences de la Terre'),

(22, 'Environnement & Développement Durable',
     'Gestion des ressources naturelles, écologie et politiques environnementales.',
     '{"R": 65, "I": 70, "A": 35, "S": 55, "E": 50, "C": 50}',
     'Agronomie et Environnement', 'Biologie,Chimie,Géographie,Sciences de la Terre');




INSERT INTO schools (name, city, country, type, website, description, field_id) VALUES

('ENSIAS - École Nationale Supérieure d''Informatique et d''Analyse des Systèmes',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://ensias.um5.ac.ma',
 'Grande école d''ingénieurs en informatique rattachée à l''Université Mohammed V de Rabat. Accès sur concours national commun.',
 1),

('EMI - École Mohammadia d''Ingénieurs',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://www.emi.ac.ma',
 'École d''ingénieurs pluridisciplinaire offrant notamment une filière Génie Informatique et Logiciel.',
 1),

('ENSA Marrakech - École Nationale des Sciences Appliquées',
 'Marrakech', 'Maroc', 'Grande École Publique',
 'https://ensa-marrakech.uca.ma',
 'Forme des ingénieurs d''État en informatique, génie logiciel et systèmes embarqués.',
 1),

('ENSA Kénitra',
 'Kénitra', 'Maroc', 'Grande École Publique',
 'https://ensa.uit.ac.ma',
 'École d''ingénieurs proposant des formations en informatique et génie logiciel.',
 1),

('FST Fès - Faculté des Sciences et Techniques',
 'Fès', 'Maroc', 'Faculté Publique',
 'https://fst-usmba.ac.ma',
 'Master et Licence en Génie Logiciel, systèmes distribués et développement web.',
 1),

('INPT - Institut National des Postes et Télécommunications',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://www.inpt.ac.ma',
 'École d''ingénieurs spécialisée en systèmes d''information, réseaux et cybersécurité.',
 2),

('ENSA Casablanca',
 'Casablanca', 'Maroc', 'Grande École Publique',
 'https://ensa-casablanca.univh2c.ma',
 'Forme des ingénieurs en informatique, réseaux et systèmes embarqués.',
 2),

('ENSA Tétouan',
 'Tétouan', 'Maroc', 'Grande École Publique',
 'https://ensa-tetouan.ac.ma',
 'Filières d''ingénierie informatique et systèmes intelligents.',
 2),

('Université Privée de Fès - Pôle Ingénierie',
 'Fès', 'Maroc', 'Université Privée',
 'https://www.upf.ac.ma',
 'Cycle ingénieur en informatique et réseaux, accrédité par l''État.',
 2),


('EHTP - École Hassania des Travaux Publics',
 'Casablanca', 'Maroc', 'Grande École Publique',
 'https://www.ehtp.ac.ma',
 'Grande école de référence pour le génie civil, travaux publics et infrastructures au Maroc.',
 3),

('EMI - École Mohammadia d''Ingénieurs',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://www.emi.ac.ma',
 'Filière Génie Civil proposant des formations en BTP, structures et urbanisme.',
 3),

('ENSA Oujda',
 'Oujda', 'Maroc', 'Grande École Publique',
 'https://ensa.ump.ac.ma',
 'Ingénierie en génie civil et génie des matériaux.',
 3),

('FST Settat',
 'Settat', 'Maroc', 'Faculté Publique',
 'https://fsts.uh2c.ac.ma',
 'Licence et Master en Sciences et Techniques du Génie Civil.',
 3),


('ENIM - École Nationale de l''Industrie Minérale',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://www.enim.ac.ma',
 'Formation en génie électrique, automatisme et génie industriel.',
 4),

('ENSA Agadir',
 'Agadir', 'Maroc', 'Grande École Publique',
 'https://ensa.uiz.ac.ma',
 'Filière électrique et systèmes d''énergie renouvelable.',
 4),

('ENSEM - École Nationale Supérieure d''Électricité et de Mécanique',
 'Casablanca', 'Maroc', 'Grande École Publique',
 'https://ensem.univh2c.ma',
 'Formation d''ingénieurs en électrotechnique, automatique et génie énergétique.',
 4),

('ENSIAS - École Nationale Supérieure d''Informatique et d''Analyse des Systèmes',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://ensias.um5.ac.ma',
 'Filière Intelligence Artificielle et Data Science, une des premières au Maroc.',
 5),

('UM6P - Université Mohammed VI Polytechnique',
 'Ben Guerir', 'Maroc', 'Université Publique',
 'https://www.um6p.ma',
 'Programmes de pointe en Data Science, IA et sciences computationnelles.',
 5),

('École des Sciences de l''Information (ESI)',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://www.esi.ac.ma',
 'Spécialisation en management de l''information, data analytics et systèmes décisionnels.',
 5),


('INPT - Institut National des Postes et Télécommunications',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://www.inpt.ac.ma',
 'Filière cybersécurité et confiance numérique, en partenariat avec des acteurs industriels.',
 6),

('UIR - Université Internationale de Rabat',
 'Rabat', 'Maroc', 'Université Privée',
 'https://www.uir.ac.ma',
 'Cycle ingénieur Sécurité des Systèmes d''Information et Cloud Computing.',
 6),


('INPT - Institut National des Postes et Télécommunications',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://www.inpt.ac.ma',
 'École de référence pour les télécommunications, les réseaux et les services numériques.',
 7),

('ENSA El Jadida',
 'El Jadida', 'Maroc', 'Grande École Publique',
 'https://ensa-eljadida.ucd.ac.ma',
 'Filière Télécommunications et Réseaux Informatiques.',
 7),


('Faculté de Médecine et de Pharmacie de Rabat',
 'Rabat', 'Maroc', 'Faculté Publique',
 'https://fmp.um5.ac.ma',
 'La plus ancienne faculté de médecine du Maroc, rattachée à l''Université Mohammed V.',
 8),

('Faculté de Médecine et de Pharmacie de Casablanca',
 'Casablanca', 'Maroc', 'Faculté Publique',
 'https://fmp.univh2c.ma',
 'Formation médicale initiale et spécialisée, avec CHU Ibn Rochd.',
 8),

('Faculté de Médecine et de Pharmacie de Fès',
 'Fès', 'Maroc', 'Faculté Publique',
 'https://fmp-fes.usmba.ac.ma',
 'Formation médicale de haut niveau avec CHU Hassan II.',
 8),

('Faculté de Médecine et de Pharmacie de Marrakech',
 'Marrakech', 'Maroc', 'Faculté Publique',
 'https://fmp.uca.ac.ma',
 'Formation en médecine générale et spécialisée au Sud du Maroc.',
 8),

('Faculté de Médecine et de Pharmacie d''Oujda',
 'Oujda', 'Maroc', 'Faculté Publique',
 'https://fmp.ump.ac.ma',
 'Médecine générale et spécialités médicales à l''Université Mohammed Premier.',
 8),


('Faculté de Médecine et de Pharmacie de Rabat',
 'Rabat', 'Maroc', 'Faculté Publique',
 'https://fmp.um5.ac.ma',
 'Filière Pharmacie en 6 ans, avec laboratoires de recherche et partenariats industriels.',
 9),

('Faculté de Médecine et de Pharmacie de Casablanca',
 'Casablanca', 'Maroc', 'Faculté Publique',
 'https://fmp.univh2c.ma',
 'Formation pharmaceutique complète incluant stages officinaux et hospitaliers.',
 9),

('Faculté de Médecine et de Pharmacie de Fès',
 'Fès', 'Maroc', 'Faculté Publique',
 'https://fmp-fes.usmba.ac.ma',
 'Filière pharmacie avec orientation vers la pharmacologie et biologie médicale.',
 9),


('Centre Hospitalier Universitaire de Rabat - Faculté de Médecine Dentaire',
 'Rabat', 'Maroc', 'Faculté Publique',
 'https://fmd.um5.ac.ma',
 'Formation en odontologie, chirurgie buccale et orthodontie.',
 10),

('Faculté de Médecine Dentaire de Casablanca',
 'Casablanca', 'Maroc', 'Faculté Publique',
 'https://fmd.univh2c.ma',
 'Chirurgie dentaire, prothèses et parodontologie avec plateau clinique moderne.',
 10),


('Institut National de Formation aux Professions de Santé (INFPS)',
 'Casablanca', 'Maroc', 'Institut Public',
 'https://www.infps.ma',
 'Formation aux métiers infirmiers, sage-femme et techniciens de santé.',
 11),

('ISPITS - Institut Supérieur des Professions Infirmières et Techniques de Santé',
 'Rabat', 'Maroc', 'Institut Public',
 'https://www.sante.gov.ma',
 'Réseau national d''instituts formant les infirmiers et professionnels paramédicaux.',
 11),


('ISCAE - Institut Supérieur de Commerce et d''Administration des Entreprises',
 'Casablanca', 'Maroc', 'Grande École Publique',
 'https://www.iscae.ac.ma',
 'Grande école de commerce et management, référence au Maroc depuis 1971.',
 12),

('ENCG Casablanca - École Nationale de Commerce et de Gestion',
 'Casablanca', 'Maroc', 'Grande École Publique',
 'https://encg-casablanca.univh2c.ma',
 'Formation en management, commerce international et entrepreneuriat.',
 12),

('ENCG Fès',
 'Fès', 'Maroc', 'Grande École Publique',
 'https://encg-fes.usmba.ac.ma',
 'Commerce, gestion d''entreprise et logistique au sein de l''Université Sidi Mohamed Ben Abdellah.',
 12),

('HEM - Hautes Études de Management',
 'Casablanca', 'Maroc', 'École Privée',
 'https://www.hem.ac.ma',
 'École privée de management avec campus à Casablanca, Rabat, Fès et Marrakech.',
 12),

('ISGA - Institut Supérieur de Gestion et Informatique Appliquée',
 'Casablanca', 'Maroc', 'École Privée',
 'https://www.isga.ma',
 'Formation en gestion, commerce et systèmes d''information.',
 12),


('ISCAE - Institut Supérieur de Commerce et d''Administration des Entreprises',
 'Casablanca', 'Maroc', 'Grande École Publique',
 'https://www.iscae.ac.ma',
 'Masters spécialisés en Finance, Audit et Contrôle de Gestion.',
 13),

('ENCG Agadir',
 'Agadir', 'Maroc', 'Grande École Publique',
 'https://encg-agadir.uiz.ac.ma',
 'Filières Finance d''Entreprise et Comptabilité Contrôle Audit.',
 13),

('École Supérieure de Technologie de Casablanca (EST)',
 'Casablanca', 'Maroc', 'Faculté Publique',
 'https://est.univh2c.ma',
 'DUT Finance Comptabilité et Techniques de Commercialisation.',
 13),


('HEM - Hautes Études de Management',
 'Rabat', 'Maroc', 'École Privée',
 'https://www.hem.ac.ma',
 'Bachelor et Master en Marketing, Communication et Digital Business.',
 14),

('ISCAE',
 'Casablanca', 'Maroc', 'Grande École Publique',
 'https://www.iscae.ac.ma',
 'Master Marketing & Communication Digitale.',
 14),

('Sup''Com - Supérieure en Communication',
 'Casablanca', 'Maroc', 'École Privée',
 'https://www.supcom.ma',
 'Formation en publicité, relations publiques et marketing de marque.',
 14),

-- ── DROIT (field_id = 15) ─────────────────────────────────────
('Faculté de Droit Souissi - Université Mohammed V',
 'Rabat', 'Maroc', 'Faculté Publique',
 'https://fsjes-souissi.um5.ac.ma',
 'Droit privé, droit public, sciences politiques et droit des affaires.',
 15),

('Faculté de Droit Ain Sebaa',
 'Casablanca', 'Maroc', 'Faculté Publique',
 'https://fsjes-ainsebaa.univh2c.ma',
 'Formation juridique complète avec clinique du droit et moot courts.',
 15),

('Faculté de Droit Agdal',
 'Rabat', 'Maroc', 'Faculté Publique',
 'https://fsjes-agdal.um5.ac.ma',
 'Sciences juridiques et politiques, droits international et administratif.',
 15),

-- ── SCIENCES POLITIQUES (field_id = 16) ──────────────────────
('Sciences Po Rabat - Institut d''Études Politiques',
 'Rabat', 'Maroc', 'Institut Public',
 'https://sciencespo-rabat.um5.ac.ma',
 'Formation en sciences politiques, gouvernance et relations internationales.',
 16),

('Faculté de Droit et Sciences Politiques de Tanger',
 'Tanger', 'Maroc', 'Faculté Publique',
 'https://fdsp-tanger.uae.ac.ma',
 'Relations internationales, droit diplomatique et géopolitique régionale.',
 16),

-- ── ARCHITECTURE (field_id = 17) ─────────────────────────────
('École Nationale d''Architecture de Rabat (ENA)',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://www.ena.ac.ma',
 'Principale école d''architecture au Maroc, diplôme d''architecte DPLG en 6 ans.',
 17),

('École Nationale d''Architecture de Marrakech',
 'Marrakech', 'Maroc', 'Grande École Publique',
 'https://www.enam.ac.ma',
 'Architecture, patrimoine et urbanisme durable en zone méditerranéenne.',
 17),

('École Nationale d''Architecture de Fès',
 'Fès', 'Maroc', 'Grande École Publique',
 'https://ena-fes.ac.ma',
 'Formation en architecture avec accent sur la médina et le patrimoine architectural marocain.',
 17),

-- ── DESIGN GRAPHIQUE & ARTS VISUELS (field_id = 18) ──────────
('ESAV Marrakech - École Supérieure des Arts Visuels',
 'Marrakech', 'Maroc', 'Grande École Publique',
 'https://www.esav-marrakech.ac.ma',
 'Cinéma, photographie, design graphique et arts visuels numériques.',
 18),

('ENSA de Tétouan - École Nationale Supérieure des Arts',
 'Tétouan', 'Maroc', 'Grande École Publique',
 'https://ensa-tetouan.ac.ma',
 'Beaux-arts, arts appliqués, céramique, arts plastiques et design.',
 18),

('ISCAE Créatif - Pôle Design',
 'Casablanca', 'Maroc', 'École Privée',
 'https://www.iscae.ac.ma',
 'Design graphique, communication visuelle et direction artistique.',
 18),

-- ── PSYCHOLOGIE (field_id = 19) ───────────────────────────────
('Faculté des Lettres et des Sciences Humaines - Université Mohammed V',
 'Rabat', 'Maroc', 'Faculté Publique',
 'https://flsh.um5.ac.ma',
 'Licence et Master en Psychologie clinique, sociale et du travail.',
 19),

('Faculté des Lettres et Sciences Humaines d''Ain Chock',
 'Casablanca', 'Maroc', 'Faculté Publique',
 'https://flshac.univh2c.ma',
 'Psychologie générale, psychopathologie et orientation scolaire.',
 19),

('Université Privée de Marrakech - Pôle Sciences Humaines',
 'Marrakech', 'Maroc', 'Université Privée',
 'https://www.upm.ac.ma',
 'Bachelor en Psychologie et Sciences Cognitives.',
 19),

-- ── SOCIOLOGIE (field_id = 20) ────────────────────────────────
('Faculté des Lettres et Sciences Humaines de Rabat',
 'Rabat', 'Maroc', 'Faculté Publique',
 'https://flsh.um5.ac.ma',
 'Sociologie, anthropologie et études culturelles.',
 20),

('Faculté des Lettres et Sciences Humaines de Meknès',
 'Meknès', 'Maroc', 'Faculté Publique',
 'https://flsh.umi.ac.ma',
 'Sociologie du développement, géographie humaine et études du genre.',
 20),

-- ── AGRONOMIE (field_id = 21) ─────────────────────────────────
('IAV Hassan II - Institut Agronomique et Vétérinaire',
 'Rabat', 'Maroc', 'Grande École Publique',
 'https://www.iavhassan2.ac.ma',
 'Principale école d''ingénieurs en agronomie, agroalimentaire et médecine vétérinaire au Maroc.',
 21),

('ENA Meknès - École Nationale d''Agriculture',
 'Meknès', 'Maroc', 'Grande École Publique',
 'https://www.enameknes.ac.ma',
 'Ingénierie agronome, agronomie du sol et développement rural.',
 21),

('INRA Maroc - Institut National de la Recherche Agronomique',
 'Rabat', 'Maroc', 'Institut Public',
 'https://www.inra.org.ma',
 'Recherche et formation postgrade en sciences agronomiques et biotechnologies végétales.',
 21),

-- ── ENVIRONNEMENT (field_id = 22) ─────────────────────────────
('UM6P - Université Mohammed VI Polytechnique',
 'Ben Guerir', 'Maroc', 'Université Publique',
 'https://www.um6p.ma',
 'Sciences de la durabilité, énergie renouvelable et changement climatique.',
 22),

('FST Errachidia',
 'Errachidia', 'Maroc', 'Faculté Publique',
 'https://fste.umi.ac.ma',
 'Géosciences, environnement aride et gestion des ressources en eau.',
 22),

('ENFI - École Nationale Forestière des Ingénieurs',
 'Salé', 'Maroc', 'Grande École Publique',
 'https://www.enfi.ac.ma',
 'Ingénierie forestière, gestion de l''environnement et lutte contre la désertification.',
 22);
