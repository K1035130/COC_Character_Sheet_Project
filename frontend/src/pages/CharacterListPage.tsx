import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import * as characterApi from '../api/characterApi';
import type { Character } from '../types/character';
import { Spinner } from '../components/Spinner';
import { useToast } from '../components/Toast';

export function CharacterListPage() {
  const toast = useToast();
  const [characters, setCharacters] = useState<Character[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    characterApi
      .listCharacters()
      .then(setCharacters)
      .catch(() => toast.error('Could not load characters'))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <Spinner label="Loading characters..." />;
  }

  return (
    <div className="character-list-page">
      <div className="page-header">
        <h1>Your Characters</h1>
        <Link to="/characters/new" className="btn btn-primary">
          + New Character
        </Link>
      </div>
      {characters.length === 0 ? (
        <div className="empty-state">
          <p>No characters yet.</p>
          <Link to="/characters/new" className="btn btn-primary">
            Create your first character
          </Link>
        </div>
      ) : (
        <ul className="character-list">
          {characters.map((character) => (
            <li key={character.id}>
              <Link to={`/characters/${character.id}`} className="character-card">
                <span className="character-avatar">{character.name.charAt(0).toUpperCase()}</span>
                <span className="character-card-info">
                  <span className="character-card-name">{character.name}</span>
                  <span className="character-card-meta">
                    {character.occupation || 'Unknown occupation'} · age {character.age}
                  </span>
                </span>
              </Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
