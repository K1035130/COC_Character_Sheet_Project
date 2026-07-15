export type CoreAttribute = 'STR' | 'DEX' | 'CON' | 'APP' | 'POW' | 'SIZ' | 'INT' | 'EDU' | 'LUC';
export type DerivedAttribute = 'HP' | 'MP' | 'SAN';
export type AttributeKey = CoreAttribute | DerivedAttribute;

export const CORE_ATTRIBUTES: CoreAttribute[] = ['STR', 'DEX', 'CON', 'APP', 'POW', 'SIZ', 'INT', 'EDU', 'LUC'];
export const DERIVED_ATTRIBUTES: DerivedAttribute[] = ['HP', 'MP', 'SAN'];

export interface Skill {
  name: string;
  addValue: number;
  totalValue: number;
}

export interface AuditEvent {
  timestamp: string;
  description: string;
}

export interface Character {
  id: string;
  name: string;
  occupation: string;
  age: number;
  gender: string;
  backstory: string | null;
  avatarDataUrl: string | null;
  attributes: Record<AttributeKey, number>;
  skills: Skill[];
  auditLog: AuditEvent[];
  createdAt: string;
  updatedAt: string;
}

export interface CharacterProfileInput {
  name: string;
  occupation: string;
  age: number;
  gender: string;
  backstory: string;
}
