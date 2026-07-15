import { useState } from 'react';
import type { FormEvent } from 'react';
import type { Skill } from '../types/character';

interface SkillListProps {
  skills: Skill[];
  onUpsert: (name: string, addValue: number) => Promise<void>;
  onDelete: (name: string) => Promise<void>;
}

export function SkillList({ skills, onUpsert, onDelete }: SkillListProps) {
  const [name, setName] = useState('');
  const [addValue, setAddValue] = useState(0);
  const [submitting, setSubmitting] = useState(false);
  const [removing, setRemoving] = useState<string | null>(null);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    if (!name.trim()) {
      return;
    }
    setSubmitting(true);
    try {
      await onUpsert(name.trim(), addValue);
      setName('');
      setAddValue(0);
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (skillName: string) => {
    setRemoving(skillName);
    try {
      await onDelete(skillName);
    } finally {
      setRemoving(null);
    }
  };

  const totalAddValue = skills.reduce((sum, skill) => sum + skill.addValue, 0);

  return (
    <div className="skill-list">
      <div className="skill-list-header">
        <h3>Skills</h3>
        <span className="skill-summary">
          {skills.length} skill{skills.length === 1 ? '' : 's'} · {totalAddValue} points used
        </span>
      </div>
      {skills.length === 0 ? (
        <p className="muted">No skills yet — add one below.</p>
      ) : (
        <ul>
          {skills.map((skill) => (
            <li key={skill.name}>
              <span>
                {skill.name} <strong>{skill.totalValue}</strong>
              </span>
              <button
                type="button"
                className="btn btn-outline btn-small"
                onClick={() => handleDelete(skill.name)}
                disabled={removing === skill.name}
              >
                {removing === skill.name ? 'Removing...' : 'Remove'}
              </button>
            </li>
          ))}
        </ul>
      )}
      <form onSubmit={handleSubmit} className="skill-form">
        <input placeholder="Skill name" value={name} onChange={(e) => setName(e.target.value)} />
        <input
          type="number"
          placeholder="Add value"
          value={addValue}
          onChange={(e) => setAddValue(Number(e.target.value))}
        />
        <button type="submit" className="btn btn-primary" disabled={submitting}>
          {submitting ? 'Saving...' : 'Add / Update'}
        </button>
      </form>
    </div>
  );
}
