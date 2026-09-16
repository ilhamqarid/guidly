const ICONS = {
  building: {
    color: "#2563EB",
    path: (
      <>
        <path d="M4 21V9l8-5 8 5v12" stroke="currentColor" strokeWidth="1.8" strokeLinejoin="round" />
        <path d="M9 21v-6h6v6M4 21h16" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" />
      </>
    ),
  },
  clock: {
    color: "#0EA5E9",
    path: (
      <>
        <circle cx="12" cy="12" r="9" stroke="currentColor" strokeWidth="1.8" />
        <path d="M12 7v5l3.5 2" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" />
      </>
    ),
  },
  coin: {
    color: "#D97706",
    path: (
      <>
        <circle cx="12" cy="12" r="9" stroke="currentColor" strokeWidth="1.8" />
        <path d="M9.5 15c0 1 1 1.5 2.5 1.5s2.5-.6 2.5-1.6c0-2.2-5-1-5-3.2 0-1 1-1.6 2.5-1.6s2.5.5 2.5 1.5" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round" />
      </>
    ),
  },
  doc: {
    color: "#4F46E5",
    path: (
      <>
        <path d="M6 2h9l5 5v15a1 1 0 0 1-1 1H6a1 1 0 0 1-1-1V3a1 1 0 0 1 1-1z" stroke="currentColor" strokeWidth="1.7" strokeLinejoin="round" />
        <path d="M14 2v5h5" stroke="currentColor" strokeWidth="1.7" strokeLinejoin="round" />
      </>
    ),
  },
  steps: {
    color: "#16A34A",
    path: (
      <>
        <path d="M4 6h4M4 12h4M4 18h4" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" />
        <path d="M12 6h8M12 12h8M12 18h8" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" />
      </>
    ),
  },
};

export default function MetaIcon({ type, size = 15 }) {
  const icon = ICONS[type] || ICONS.doc;
  return (
    <svg width={size} height={size} viewBox="0 0 24 24" fill="none" style={{ color: icon.color, flexShrink: 0 }}>
      {icon.path}
    </svg>
  );
}
