import re
import unicodedata
from difflib import SequenceMatcher

from intents import INTENTS, THRESHOLD_DIRECT, THRESHOLD_SUGGEST

# Mots qui, s'ils apparaissent, indiquent une "première demande"
FIRST_TIME_MARKERS = ["première", "premiere", "nouveau", "nouvelle", "jamais eu", "jdid"]
# Mots qui indiquent un renouvellement
RENEWAL_MARKERS = ["renouveler", "renouvellement", "refaire", "expiré", "expire", "jdd"]

ARABIC_SCRIPT_RE = re.compile(r"[\u0600-\u06FF]")

def normalize(text: str) -> str:
    """Minuscule, sans accents, espaces normalisés."""
    text = text.lower().strip()
    text = unicodedata.normalize("NFD", text)
    text = "".join(c for c in text if unicodedata.category(c) != "Mn")
    text = re.sub(r"\s+", " ", text)
    return text


def detect_language(raw_text: str) -> str:
    if ARABIC_SCRIPT_RE.search(raw_text):
        return "ar"
    return "fr"


def _keyword_score(normalized_text: str, keywords: list[str]) -> float:
    best = 0.0
    for kw in keywords:
        kw_norm = normalize(kw)
        if kw_norm in normalized_text:
            word_count = len(kw_norm.split())
            if word_count >= 2:
                # Expression multi-mots : signal fort, quasiment jamais un faux positif.
                score = min(1.0, 0.80 + 0.03 * word_count)
            elif kw_norm in GENERIC_SINGLE_WORDS:
                # Mot unique mais courant/ambigu hors contexte : signal faible
                # à lui seul (reste sous THRESHOLD_SUGGEST).
                score = 0.45
            else:
                # Mot unique et spécifique (acronyme, terme métier rare) :
                # score fort, proportionnel à sa longueur.
                score = min(1.0, 0.75 + 0.05 * word_count)
            best = max(best, score)
        else:
            # Similarité approximative (fautes d'orthographe)
            ratio = SequenceMatcher(None, kw_norm, normalized_text).ratio()
            # On ne prend en compte la similarité globale que pour des textes courts
            # sinon un mot-clé de 5 lettres noyé dans une longue phrase pénalise à tort
            for word in normalized_text.split():
                word_ratio = SequenceMatcher(None, kw_norm, word).ratio()
                best = max(best, word_ratio * 0.7)
            best = max(best, ratio * 0.5)
    return min(best, 1.0)


def detect_intent(raw_text: str) -> dict:
    normalized = normalize(raw_text)
    scores = []
    for intent_code, data in INTENTS.items():
        score = _keyword_score(normalized, data["keywords"])
        scores.append({"intent": intent_code, "label": data["label"], "confidence": round(score, 2)})

    scores.sort(key=lambda x: x["confidence"], reverse=True)
    top = scores[0] if scores else {"intent": None, "label": None, "confidence": 0.0}

    return {
        "top": top,
        "alternatives": scores[1:3],
        "all_scores": scores,
    }


def extract_entities(raw_text: str) -> dict:
    normalized = normalize(raw_text)
    entities = {}

    if any(m in normalized for m in FIRST_TIME_MARKERS):
        entities["request_type"] = "first_time"
    elif any(m in normalized for m in RENEWAL_MARKERS):
        entities["request_type"] = "renewal"

    age_match = re.search(r"\b(\d{1,2})\s*ans?\b", normalized)
    if age_match:
        entities["age"] = int(age_match.group(1))

    return entities


def analyze(raw_text: str) -> dict:
    """Point d'entrée principal : combine langue + intention + entités,
    puis applique les seuils de confiance (section 28) pour décider de
    l'action : réponse directe / suggestions / clarification."""
    language = detect_language(raw_text)
    intent_result = detect_intent(raw_text)
    entities = extract_entities(raw_text)

    top = intent_result["top"]
    confidence = top["confidence"]

    MAX_GAP_FOR_REAL_AMBIGUITY = 0.15

    close_alternatives = [
        alt for alt in intent_result["alternatives"]
        if (confidence - alt["confidence"]) <= MAX_GAP_FOR_REAL_AMBIGUITY
    ]

    if confidence >= THRESHOLD_DIRECT:
        action = "direct"
    elif confidence >= THRESHOLD_SUGGEST and close_alternatives:
        action = "suggest"
    elif confidence >= THRESHOLD_SUGGEST:
        # Confiance suffisante mais aucune alternative sérieuse concurrente
        # -> pas d'ambiguïté réelle, on répond directement.
        action = "direct"
    else:
        action = "clarify"

    return {
        "input": raw_text,
        "language": language,
        "intent": top["intent"],
        "intent_label": top["label"],
        "confidence": confidence,
        "action": action,
        "alternatives": close_alternatives if action == "suggest" else [],
        "entities": entities,
    }
