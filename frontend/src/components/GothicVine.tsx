const TILE_HEIGHT = 600;
const TILE_WIDTH = 80;

function VineTile() {
  return (
    <svg
      width={TILE_WIDTH}
      height={TILE_HEIGHT}
      viewBox={`0 0 ${TILE_WIDTH} ${TILE_HEIGHT}`}
      xmlns="http://www.w3.org/2000/svg"
      style={{ display: 'block' }}
    >
      <path
        d="M40 0 Q18 37 40 75 Q62 112 40 150 Q18 187 40 225 Q62 262 40 300 Q18 337 40 375 Q62 412 40 450 Q18 487 40 525 Q62 562 40 600"
        fill="none"
        stroke="currentColor"
        strokeWidth="1.5"
        opacity="0.8"
      />
      {[75, 225, 375, 525].map((y) => (
        <g key={`left-${y}`} transform={`translate(18 ${y})`}>
          <path
            d="M0 0 C-14 -8 -22 -22 -16 -36 C-8 -30 -2 -18 0 0 Z"
            fill="currentColor"
            opacity="0.55"
          />
          <path
            d="M0 0 C-10 6 -22 8 -30 -2 C-20 -10 -8 -10 0 0 Z"
            fill="currentColor"
            opacity="0.4"
          />
          <circle cx="-2" cy="4" r="2.5" fill="currentColor" opacity="0.7" />
        </g>
      ))}
      {[150, 300, 450, 600].map((y) => (
        <g key={`right-${y}`} transform={`translate(62 ${y})`}>
          <path
            d="M0 0 C14 -8 22 -22 16 -36 C8 -30 2 -18 0 0 Z"
            fill="currentColor"
            opacity="0.55"
          />
          <path
            d="M0 0 C10 6 22 8 30 -2 C20 -10 8 -10 0 0 Z"
            fill="currentColor"
            opacity="0.4"
          />
          <circle cx="2" cy="4" r="2.5" fill="currentColor" opacity="0.7" />
        </g>
      ))}
    </svg>
  );
}

function GothicRail({ side }: { side: 'left' | 'right' }) {
  return (
    <div className={`gothic-rail gothic-rail-${side}`} aria-hidden="true">
      <div className="gothic-rail-track">
        <VineTile />
        <VineTile />
      </div>
    </div>
  );
}

export function GothicRails() {
  return (
    <>
      <GothicRail side="left" />
      <GothicRail side="right" />
    </>
  );
}
