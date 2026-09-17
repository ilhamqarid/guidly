INTENTS = {
    "passport_first_request": {
        "label": "Demande de passeport - première demande",
        "keywords": [
            # Français — phrases spécifiques à la première demande
            "premier passeport", "première demande de passeport",
            "jamais eu de passeport", "passeport pour la première fois",
            # Arabe
            "جواز سفر جديد", "باسبور لأول مرة",
            # Darija latinisée
            "sanaa passport", "premiere fois passport", "jawaz safar jdid",
            # Mots-clés génériques (fallback si aucune précision de type
            # n'est donnée) — volontairement en dernier et en un seul mot,
            # donc score plus faible que les phrases de renouvellement
            # ci-dessous en cas d'ambiguïté (voir formule de score dans
            # matcher.py : un match multi-mots score plus haut qu'un match
            # d'un seul mot).
            "passeport", "passport", "nouveau passeport",
            "جواز السفر", "جواز سفر", "باسبور", "bghit passport", "jawaz safar",
        ],
    },
    "passport_renewal": {
        "label": "Renouvellement de passeport",
        "keywords": [
            # Français
            "renouveler mon passeport", "renouvellement de passeport",
            "renouveler passeport", "passeport expiré", "passeport expire",
            "refaire mon passeport",
            # Arabe
            "تجديد جواز السفر", "تجديد الباسبور", "جواز السفر المنتهي",
            # Darija latinisée
            "njadad passport", "bghit njadad passport", "renouveler passport",
            "tjded passport", "passport khlass",
        ],
    },
    "cnie_first_application": {
        "label": "Carte Nationale d'Identité Électronique",
        "keywords": [
            "carte d'identité", "carte nationale", "cnie", "carte didentite",
            "بطاقة التعريف الوطنية", "البطاقة الوطنية",
            "carte nationale dl identite", "bitaqa",
        ],
    },
    "business_creation_sarl_au": {
        "label": "Création d'entreprise",
        "keywords": [
            "créer une entreprise", "créer mon entreprise", "création d'entreprise",
            "société", "sarl", "auto-entrepreneur", "monter une société",
            "شركة", "تأسيس شركة", "مقاولتي",
            "dir chariqa", "nhawel chi charika", "mochrou3",
        ],
    },
    "university_registration": {
        "label": "Inscription universitaire",
        "keywords": [
            "université", "inscription université", "m'inscrire à l'université",
            "faculté", "bac", "baccalauréat", "étudiant",
            "الجامعة", "التسجيل بالجامعة", "طالب جديد",
            "nsjel", "nsjel fl jami3a", "talib jdid", "sjel fljami3a",
        ],
    },
    "vehicle_registration": {
        "label": "Carte grise",
        "keywords": [
            "carte grise", "immatriculer", "immatriculation", "nouveau véhicule",
            "acheter une voiture", "voiture",
            "البطاقة الرمادية", "تسجيل السيارة",
            "carte grisa", "tsjil dyal tomobil", "chrit tomobil",
        ],
    },
    "birth_certificate_request": {
        "label": "Acte de naissance",
        "keywords": [
            "acte de naissance", "extrait de naissance", "copie d'acte de naissance",
            "شهادة الازدياد", "عقد الازدياد",
            "chahada dyal lwilada", "acte naissance", "wilada",
        ],
    },
    "criminal_record_request": {
        "label": "Casier judiciaire",
        "keywords": [
            "casier judiciaire", "extrait de casier", "bulletin n3", "bulletin numero 3",
            "السجل العدلي",
            "sijil el 3adli", "sijil adli",
        ],
    },
    "driving_license_application": {
        "label": "Permis de conduire",
        "keywords": [
            "permis de conduire", "passer le permis", "permis conduire",
            "رخصة السياقة",
            "njib permis", "permis dyal souk",
        ],
    },
    "anapec_registration": {
        "label": "Inscription ANAPEC",
        "keywords": [
            "anapec", "inscription anapec", "chercheur d'emploi", "recherche d'emploi",
            "trouver un emploi", "trouver du travail",
            "الأنابيك", "الوكالة الوطنية لإنعاش الشغل",
            "bghit nkhdem", "n9elleb 3la khedma",
        ],
    },
    "cnss_first_affiliation": {
        "label": "Affiliation CNSS",
        "keywords": [
            "cnss", "affiliation cnss", "sécurité sociale", "securite sociale",
            "الضمان الاجتماعي",
            "immatriculation cnss", "ndir cnss",
        ],
    },
}

# Seuils de confiance 
THRESHOLD_DIRECT = 0.85       
THRESHOLD_SUGGEST = 0.60      
