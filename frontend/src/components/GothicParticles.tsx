import { useMemo } from 'react';
import type { CSSProperties } from 'react';

type Shape = 'snowflake' | 'clover';

interface Particle {
  id: number;
  leftPct: number;
  size: number;
  duration: number;
  delay: number;
  rotate: number;
  shape: Shape;
  opacityPeak: number;
}

function makeParticles(count: number, seedOffset: number): Particle[] {
  return Array.from({ length: count }, (_, i) => {
    const rand = (n: number) => {
      const x = Math.sin(i * 12.9898 + n * 78.233 + seedOffset) * 43758.5453;
      return x - Math.floor(x);
    };
    return {
      id: i,
      leftPct: rand(1) * 100,
      size: 14 + rand(2) * 22,
      duration: 16 + rand(3) * 18,
      delay: -rand(4) * 32,
      rotate: rand(5) * 360,
      shape: rand(6) > 0.5 ? 'snowflake' : 'clover',
      opacityPeak: 0.35 + rand(7) * 0.35,
    };
  });
}

function Snowflake() {
  return (
    <svg viewBox="0 0 100 100" width="100%" height="100%">
      {[0, 60, 120, 180, 240, 300].map((deg) => (
        <g key={deg} transform={`rotate(${deg} 50 50)`}>
          <line x1="50" y1="50" x2="50" y2="8" stroke="currentColor" strokeWidth="4" strokeLinecap="round" />
          <line x1="50" y1="26" x2="39" y2="16" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
          <line x1="50" y1="26" x2="61" y2="16" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
          <line x1="50" y1="37" x2="42" y2="30" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
          <line x1="50" y1="37" x2="58" y2="30" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
        </g>
      ))}
    </svg>
  );
}

function Clover() {
  return (
    <svg viewBox="0 0 100 100" width="100%" height="100%">
      <g fill="currentColor">
        <circle cx="50" cy="28" r="17" />
        <circle cx="72" cy="50" r="17" />
        <circle cx="50" cy="72" r="17" />
        <circle cx="28" cy="50" r="17" />
        <circle cx="50" cy="50" r="10" />
      </g>
      <line x1="50" y1="66" x2="50" y2="92" stroke="currentColor" strokeWidth="4" strokeLinecap="round" />
    </svg>
  );
}

function ParticleRail({ side, particles }: { side: 'left' | 'right'; particles: Particle[] }) {
  return (
    <div className={`gothic-rail gothic-rail-${side}`} aria-hidden="true">
      {particles.map((p) => {
        const style: CSSProperties & { '--rot': string; '--peak-opacity': string } = {
          left: `${p.leftPct}%`,
          width: p.size,
          height: p.size,
          animationDuration: `${p.duration}s`,
          animationDelay: `${p.delay}s`,
          '--rot': `${p.rotate}deg`,
          '--peak-opacity': `${p.opacityPeak}`,
        };
        return (
          <span key={p.id} className="gothic-particle" style={style}>
            {p.shape === 'snowflake' ? <Snowflake /> : <Clover />}
          </span>
        );
      })}
    </div>
  );
}

export function GothicRails() {
  const leftParticles = useMemo(() => makeParticles(9, 11), []);
  const rightParticles = useMemo(() => makeParticles(9, 47), []);

  return (
    <>
      <ParticleRail side="left" particles={leftParticles} />
      <ParticleRail side="right" particles={rightParticles} />
    </>
  );
}
