"""
Service IA — LocalHelp AI (Guidly)

Lancement : uvicorn main:app --reload --port 8000

Ce service NE PARLE PAS à la base de données MySQL. Il ne fait que
comprendre le texte de l'utilisateur (Étapes A et B du pipeline IA,
section 13). C'est le backend Spring Boot qui, à partir de l'intent_code
renvoyé ici, va chercher la vraie procédure en base (Étape C - Retrieval)
et appliquer les règles (Étape D). C'est exactement l'architecture prescrite
en section 14 : ne JAMAIS laisser le LLM générer la réponse administrative
lui-même.
"""

import os

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

from matcher import analyze

app = FastAPI(title="LocalHelp AI - Service IA", version="0.1.0")

_default_origins = "http://localhost:5173,http://localhost:3000,http://localhost:8000"
allowed_origins = os.getenv("CORS_ALLOWED_ORIGINS", _default_origins).split(",")

app.add_middleware(
    CORSMiddleware,
    allow_origins=allowed_origins,
    allow_methods=["GET", "POST"],
    allow_headers=["*"],
)


class AnalyzeRequest(BaseModel):
    text: str


class Alternative(BaseModel):
    intent: str | None
    label: str | None
    confidence: float


class AnalyzeResponse(BaseModel):
    input: str
    language: str
    intent: str | None
    intent_label: str | None
    confidence: float
    action: str  # "direct" | "suggest" | "clarify"
    alternatives: list[Alternative]
    entities: dict


@app.get("/health")
def health():
    return {"status": "ok"}


@app.post("/analyze", response_model=AnalyzeResponse)
def analyze_text(payload: AnalyzeRequest):
    result = analyze(payload.text)
    return result
