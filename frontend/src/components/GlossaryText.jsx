import { useState } from "react";
import "./GlossaryText.css";

export default function GlossaryText({ text, terms }) {
  const [openTermId, setOpenTermId] = useState(null);

  if (!text) return null;
  if (!terms || terms.length === 0) {
    return <span style={{ whiteSpace: "pre-line" }}>{text}</span>;
  }

  const sortedTerms = [...terms].sort((a, b) => b.term.length - a.term.length);
  const pattern = sortedTerms.map((t) => escapeRegExp(t.term)).join("|");
  const regex = new RegExp(`(${pattern})`, "gi");

  const parts = text.split(regex);

  return (
    <span style={{ whiteSpace: "pre-line" }}>
      {parts.map((part, i) => {
        const match = sortedTerms.find(
          (t) => t.term.toLowerCase() === part.toLowerCase()
        );
        if (!match) return <span key={i}>{part}</span>;

        const isOpen = openTermId === `${match.id}-${i}`;
        return (
          <span key={i} className="glossary-wrapper">
            <span
              className="glossary-term"
              onClick={() => setOpenTermId(isOpen ? null : `${match.id}-${i}`)}
            >
              {part}
            </span>
            {isOpen && (
              <span className="glossary-tooltip">
                <strong>{match.term}</strong>
                <p>{match.explanation}</p>
                {match.sourceUrl && (
                  <a href={match.sourceUrl} target="_blank" rel="noreferrer">
                    Source : {match.sourceTitle || match.sourceUrl}
                  </a>
                )}
              </span>
            )}
          </span>
        );
      })}
    </span>
  );
}

function escapeRegExp(str) {
  return str.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
}
