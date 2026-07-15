import { useState } from 'react';
import type { FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import * as characterApi from '../api/characterApi';
import { useToast } from '../components/Toast';

export function CharacterFormPage() {
  const navigate = useNavigate();
  const toast = useToast();
  const [name, setName] = useState('');
  const [occupation, setOccupation] = useState('');
  const [age, setAge] = useState(20);
  const [gender, setGender] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setSubmitting(true);
    try {
      const created = await characterApi.createCharacter({ name, occupation, age, gender, backstory: '' });
      toast.success(`${created.name} created`);
      navigate(`/characters/${created.id}`);
    } catch {
      toast.error('Could not create character');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="character-form-page">
      <div className="card">
        <h1>New Character</h1>
        <form onSubmit={handleSubmit}>
          <label>
            Name
            <input value={name} onChange={(e) => setName(e.target.value)} required autoFocus />
          </label>
          <label>
            Occupation
            <input value={occupation} onChange={(e) => setOccupation(e.target.value)} />
          </label>
          <label>
            Age
            <input type="number" min={0} value={age} onChange={(e) => setAge(Number(e.target.value))} />
          </label>
          <label>
            Gender
            <input value={gender} onChange={(e) => setGender(e.target.value)} />
          </label>
          <button type="submit" className="btn btn-primary" disabled={submitting}>
            {submitting ? 'Creating...' : 'Create'}
          </button>
        </form>
      </div>
    </div>
  );
}
