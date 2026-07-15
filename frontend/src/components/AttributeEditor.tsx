import { useState } from 'react';
import { CORE_ATTRIBUTES, DERIVED_ATTRIBUTES } from '../types/character';
import type { AttributeKey, CoreAttribute } from '../types/character';
import { randomAllocate } from '../utils/randomAllocate';
import { useToast } from './Toast';

const MIN_ATTRIBUTE_VALUE = 30;
const MAX_ATTRIBUTE_VALUE = 130;
const MAX_TOTAL_POINTS = 900;
const MIN_TOTAL_POINTS = MIN_ATTRIBUTE_VALUE * CORE_ATTRIBUTES.length;

interface AttributeEditorProps {
  attributes: Record<AttributeKey, number>;
  onChange: (attribute: AttributeKey, value: number) => Promise<void>;
  onBulkChange: (values: Partial<Record<CoreAttribute, number>>) => Promise<void>;
}

export function AttributeEditor({ attributes, onChange, onBulkChange }: AttributeEditorProps) {
  const toast = useToast();
  const [pending, setPending] = useState<AttributeKey | null>(null);
  const [totalPoints, setTotalPoints] = useState(
    CORE_ATTRIBUTES.reduce((sum, attr) => sum + attributes[attr], 0) || 450,
  );
  const [randomizing, setRandomizing] = useState(false);

  const handleBlur = async (attribute: AttributeKey, value: string) => {
    const parsed = Number(value);
    if (Number.isNaN(parsed) || parsed === attributes[attribute]) {
      return;
    }
    setPending(attribute);
    try {
      await onChange(attribute, parsed);
    } finally {
      setPending(null);
    }
  };

  const handleRandomize = async () => {
    if (totalPoints < MIN_TOTAL_POINTS || totalPoints > MAX_TOTAL_POINTS) {
      toast.error(`Total points must be between ${MIN_TOTAL_POINTS} and ${MAX_TOTAL_POINTS}`);
      return;
    }
    setRandomizing(true);
    try {
      const rolled = randomAllocate(totalPoints, CORE_ATTRIBUTES.length, MIN_ATTRIBUTE_VALUE, MAX_ATTRIBUTE_VALUE);
      const values: Partial<Record<CoreAttribute, number>> = {};
      CORE_ATTRIBUTES.forEach((attr, index) => {
        values[attr] = rolled[index];
      });
      await onBulkChange(values);
      toast.success(`Randomized ${totalPoints} points across all attributes`);
    } finally {
      setRandomizing(false);
    }
  };

  return (
    <div className="attribute-editor">
      <div className="attribute-group">
        <h3>Core Attributes</h3>

        <div className="randomize-panel">
          <label>
            Total points ({MIN_TOTAL_POINTS}–{MAX_TOTAL_POINTS})
            <input
              type="number"
              min={MIN_TOTAL_POINTS}
              max={MAX_TOTAL_POINTS}
              value={totalPoints}
              onChange={(e) => setTotalPoints(Number(e.target.value))}
            />
          </label>
          <button type="button" className="btn btn-secondary" onClick={handleRandomize} disabled={randomizing}>
            {randomizing ? 'Rolling...' : '🎲 Randomize'}
          </button>
        </div>

        <div className="attribute-grid">
          {CORE_ATTRIBUTES.map((attribute) => (
            <label key={attribute} className="attribute-row">
              <span>{attribute}</span>
              <input
                type="number"
                defaultValue={attributes[attribute]}
                disabled={pending === attribute || randomizing}
                onBlur={(e) => handleBlur(attribute, e.target.value)}
              />
            </label>
          ))}
        </div>
      </div>
      <div className="attribute-group">
        <h3>Derived Stats</h3>
        <div className="attribute-grid">
          {DERIVED_ATTRIBUTES.map((attribute) => (
            <div key={attribute} className="attribute-row attribute-row-readonly">
              <span>{attribute}</span>
              <span>{attributes[attribute]}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
