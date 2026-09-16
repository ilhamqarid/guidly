import { useEffect, useRef, useState } from "react";
import { useSearchParams, Link } from "react-router-dom";
import api from "../api/client";
import GlossaryText from "../components/GlossaryText";
import "./AssistantChat.css";

let uid = 0;
const nextId = () => `m${uid++}`;

export default function AssistantChat() {
  const [searchParams] = useSearchParams();
  const initialQuery = searchParams.get("q") || "";

  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [sending, setSending] = useState(false);
  const [activeProcedure, setActiveProcedure] = useState(null); // { id, title }
  const lastEntitiesRef = useRef({});
  const glossaryTermsRef = useRef([]);
  const bottomRef = useRef(null);
  const startedRef = useRef(false);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  useEffect(() => {
    if (startedRef.current) return;
    startedRef.current = true;
    addBot("Bonjour ! Dites-moi ce que vous cherchez à faire");
    if (initialQuery) {
      handleUserMessage(initialQuery);
    }
  }, []);

  function addBot(text, extra = {}) {
    setMessages((prev) => [...prev, { id: nextId(), sender: "bot", text, ...extra }]);
  }

  function addUser(text) {
    setMessages((prev) => [...prev, { id: nextId(), sender: "user", text }]);
  }

  // ---- Présentation d'une démarche trouvée, un élément à la fois ----
  async function presentProcedure(procedure, introMessage) {
    if (introMessage) {
      addBot(introMessage);
      await sleep(400);
    }

    let glossaryTerms = [];
    try {
      const glossaryRes = await api.get(`/procedures/${procedure.id}/glossary`);
      glossaryTerms = glossaryRes.data || [];
    } catch {
      glossaryTerms = [];
    }
    glossaryTermsRef.current = glossaryTerms;

    addBot(`📋 ${procedure.title}\n${procedure.description || ""}`, { glossaryTerms });
    await sleep(500);
    addBot(`⏱ Durée estimée : ${procedure.estimatedDuration || "non précisée"}\n💰 Frais : ${procedure.estimatedFees || "non précisés"}`, { glossaryTerms });
    await sleep(500);

    try {
      const [docsRes, stepsRes, sourcesRes] = await Promise.all([
        api.get(`/procedures/${procedure.id}/documents`),
        api.get(`/procedures/${procedure.id}/steps`),
        api.get(`/procedures/${procedure.id}/sources`),
      ]);

      if (docsRes.data?.length) {
        const list = docsRes.data
          .map((d) => `• ${d.name}${d.required === false ? " (optionnel)" : ""}${d.condition ? ` — ${d.condition}` : ""}`)
          .join("\n");
        addBot(`📄 Documents nécessaires :\n${list}`, { glossaryTerms });
        await sleep(500);
      }

      if (stepsRes.data?.length) {
        const list = stepsRes.data
          .map((s) => `${s.stepNumber}. ${s.title}`)
          .join("\n");
        addBot(`🧭 Étapes à suivre :\n${list}`, { glossaryTerms });
        await sleep(500);
      }

      if (sourcesRes.data?.length) {
        const s = sourcesRes.data[0];

        addBot(`🔗 Source officielle : ${s.title}\n${s.url}\n(dernière vérification : ${s.lastVerifiedAt})`);
        await sleep(400);
      }
    } catch {
    }

    addBot(
      "Vous pouvez maintenant me poser des questions sur cette démarche (ex: « qu'est-ce qu'il me manque ? », « quelle est la prochaine étape ? »), ou cliquer ci-dessous pour la démarrer.",
      { showStartButton: true, procedureId: procedure.id }
    );
    setActiveProcedure({ id: procedure.id, title: procedure.title });
  }

  const FALLBACK_TEXT = "Je ne dispose pas de cette information dans ma base actuelle.";

  async function runIntentDetection(text) {
    const res = await api.post("/ai/analyze", { text });
    const data = res.data;

    if (data.action === "direct" && data.matchedProcedure) {
      lastEntitiesRef.current = data.entities || {};
      await maybeAskRequestType(data.matchedProcedure, data.message);
      return true;
    } else if (data.action === "suggest" && data.suggestions?.length) {
      lastEntitiesRef.current = data.entities || {};
      addBot(data.message, { suggestions: data.suggestions });
      return true;
    }
    return false; 
  }

  // ---- Envoi d'un message : deux modes selon qu'une démarche est active ----
  async function handleUserMessage(text) {
    addUser(text);
    setSending(true);
    try {
      if (activeProcedure) {
        const res = await api.post("/assistant/chat", {
          procedureId: activeProcedure.id,
          question: text,
        });
        const answer = res.data.answer;

        if (answer === FALLBACK_TEXT) {
          // Rien trouvé dans la démarche active : on essaie automatiquement
          // une nouvelle recherche, plutôt que d'obliger l'utilisateur à
          // cliquer sur "Chercher une autre démarche" à chaque fois.
          const foundElsewhere = await runIntentDetection(text);
          if (!foundElsewhere) {
            addBot(answer, { glossaryTerms: glossaryTermsRef.current });
          }
        } else {
          addBot(answer, { glossaryTerms: glossaryTermsRef.current });
        }
      } else {
        // Mode "détection d'intention" 
        const found = await runIntentDetection(text);
        if (!found) {
          addBot("Je n'ai pas bien compris. Pouvez-vous reformuler votre demande différemment ?");
        }
      }
    } catch {
      addBot("Une erreur est survenue. Réessayez dans un instant.");
    } finally {
      setSending(false);
    }
  }

  function onSubmit(e) {
    e.preventDefault();
    const text = input.trim();
    if (!text || sending) return;
    setInput("");
    handleUserMessage(text);
  }

  async function onSuggestionClick(procedure) {
    addUser(procedure.title);
    await maybeAskRequestType(procedure, "Voici les détails de cette démarche :");
  }

  async function maybeAskRequestType(procedure, introMessage) {
    const requestType = lastEntitiesRef.current?.request_type;
    if (requestType) {
      // L'info était déjà dans la phrase d'origine, pas besoin de redemander
      await presentProcedure(procedure, introMessage);
      return;
    }
    addBot("Pour mieux vous aider, une précision : ", {
      requestTypeChoice: { procedureId: procedure.id },
      pendingProcedure: procedure,
      introMessage,
    });
  }

  async function onRequestTypeChoice(choice, procedure, introMessage) {
    addUser(
      choice === "first_time"
        ? "Première demande"
        : choice === "renewal"
        ? "Renouvellement"
        : "Autre chose"
    );

    if (choice === "other") {
      addBot("D'accord, pouvez-vous préciser un peu plus votre situation ?");
      return;
    }

    if (choice === "renewal") {
      addBot(
        "Je ne dispose pas encore de la procédure de renouvellement dans ma base actuelle. " +
        "Voici tout de même les informations pour une première demande, à titre indicatif :"
      );
      await presentProcedure(procedure, "");
      return;
    }

    await presentProcedure(procedure, introMessage);
  }

  async function onStartProcedure(procedureId) {
    try {
      await api.post("/user-procedures", { procedureId });
      addBot("✅ Démarche démarrée ! Retrouvez-la dans « Mes démarches ».");
    } catch (err) {
      if (err.response?.status === 401 || err.response?.status === 403) {
        addBot("Vous devez être connecté(e) pour démarrer cette démarche.", { showLoginLink: true });
      } else {
        addBot("Erreur au démarrage de la démarche.");
      }
    }
  }

  function resetConversation() {
    setActiveProcedure(null);
    addBot("D'accord, dites-moi ce que vous cherchez à faire.");
  }

  return (
    <div className="page assistant-page">
      <div className="assistant-header">
        <h2>Assistant Guidly</h2>
        {activeProcedure && (
          <button className="btn-secondary" onClick={resetConversation}>
            🔄 Chercher une autre démarche
          </button>
        )}
      </div>

      <div className="chat-container">
        <div className="chat-messages">
          {messages.map((m) => (
            <div key={m.id} className={`chat-bubble ${m.sender}`}>
              <GlossaryText text={m.text} terms={m.glossaryTerms} />

              {m.suggestions && (
                <div className="chat-suggestions">
                  {m.suggestions.map((p) => (
                    <button key={p.id} className="btn-suggestion" onClick={() => onSuggestionClick(p)}>
                      {p.title}
                    </button>
                  ))}
                </div>
              )}

              {m.requestTypeChoice && (
                <div className="chat-suggestions">
                  <button
                    className="btn-suggestion"
                    onClick={() => onRequestTypeChoice("first_time", m.pendingProcedure, m.introMessage)}
                  >
                    Première demande
                  </button>
                  <button
                    className="btn-suggestion"
                    onClick={() => onRequestTypeChoice("renewal", m.pendingProcedure, m.introMessage)}
                  >
                    Renouvellement
                  </button>
                  <button
                    className="btn-suggestion"
                    onClick={() => onRequestTypeChoice("other", m.pendingProcedure, m.introMessage)}
                  >
                    Autre chose
                  </button>
                </div>
              )}

              {m.showStartButton && (
                <button className="btn-primary" onClick={() => onStartProcedure(m.procedureId)}>
                  Commencer cette démarche
                </button>
              )}

              {m.showLoginLink && (
                <Link to="/login" className="btn-secondary" style={{ display: "inline-block", marginTop: "0.5rem" }}>
                  Se connecter
                </Link>
              )}
            </div>
          ))}
          {sending && <div className="chat-bubble bot chat-typing">…</div>}
          <div ref={bottomRef} />
        </div>

        <form className="chat-input-row" onSubmit={onSubmit}>
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder={
              activeProcedure
                ? "Posez une question sur cette démarche..."
                : "Ex: bghit ndir passport, je veux m'inscrire à l'université..."
            }
            disabled={sending}
          />
          <button type="submit" disabled={sending || !input.trim()}>
            Envoyer
          </button>
        </form>
      </div>
    </div>
  );
}

function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}
