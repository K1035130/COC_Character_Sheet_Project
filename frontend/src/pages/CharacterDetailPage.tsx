import { useCallback, useEffect, useState } from 'react';
import type { FormEvent } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import * as characterApi from '../api/characterApi';
import type { AttributeKey, Character, CharacterProfileInput, CoreAttribute } from '../types/character';
import { AttributeEditor } from '../components/AttributeEditor';
import { SkillList } from '../components/SkillList';
import { CoreAttributeRadarChart } from '../components/RadarChart';
import { AvatarUpload } from '../components/AvatarUpload';
import { Spinner } from '../components/Spinner';
import { useToast } from '../components/Toast';

const BACKSTORY_MAX_LENGTH = 5000;

export function CharacterDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const toast = useToast();
  const [character, setCharacter] = useState<Character | null>(null);
  const [loading, setLoading] = useState(true);
  const [editingProfile, setEditingProfile] = useState(false);

  const load = useCallback(async () => {
    if (!id) {
      return;
    }
    try {
      setCharacter(await characterApi.getCharacter(id));
    } catch {
      toast.error('Could not load character');
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    load();
  }, [load]);

  const handleAttributeChange = async (attribute: AttributeKey, value: number) => {
    if (!id) {
      return;
    }
    try {
      setCharacter(await characterApi.updateAttribute(id, attribute, value));
    } catch {
      toast.error(`Could not update ${attribute}`);
    }
  };

  const handleBulkAttributeChange = async (values: Partial<Record<CoreAttribute, number>>) => {
    if (!id) {
      return;
    }
    try {
      setCharacter(await characterApi.updateAttributesBulk(id, values));
    } catch {
      toast.error('Could not randomize attributes');
    }
  };

  const handleUpsertSkill = async (name: string, addValue: number) => {
    if (!id) {
      return;
    }
    try {
      setCharacter(await characterApi.upsertSkill(id, name, addValue));
      toast.success(`Skill "${name}" saved`);
    } catch {
      toast.error('Could not save skill');
    }
  };

  const handleDeleteSkill = async (name: string) => {
    if (!id) {
      return;
    }
    try {
      setCharacter(await characterApi.deleteSkill(id, name));
      toast.success(`Skill "${name}" removed`);
    } catch {
      toast.error('Could not remove skill');
    }
  };

  const handleDeleteCharacter = async () => {
    if (!id || !character) {
      return;
    }
    if (!window.confirm(`Delete ${character.name}? This cannot be undone.`)) {
      return;
    }
    try {
      await characterApi.deleteCharacter(id);
      toast.success(`${character.name} deleted`);
      navigate('/');
    } catch {
      toast.error('Could not delete character');
    }
  };

  const handleProfileSave = async (input: CharacterProfileInput) => {
    if (!id) {
      return;
    }
    try {
      setCharacter(await characterApi.updateCharacterProfile(id, input));
      setEditingProfile(false);
      toast.success('Profile updated');
    } catch {
      toast.error('Could not update profile');
    }
  };

  const handleBackstorySave = async (backstory: string) => {
    if (!id || !character) {
      return;
    }
    try {
      setCharacter(
        await characterApi.updateCharacterProfile(id, {
          name: character.name,
          occupation: character.occupation,
          age: character.age,
          gender: character.gender,
          backstory,
        }),
      );
      toast.success('Backstory updated');
    } catch {
      toast.error('Could not update backstory');
    }
  };

  const handleAvatarUpload = async (dataUrl: string) => {
    if (!id) {
      return;
    }
    setCharacter(await characterApi.updateAvatar(id, dataUrl));
  };

  if (loading) {
    return <Spinner label="Loading character..." />;
  }

  if (!character) {
    return <p>Character not found.</p>;
  }

  return (
    <div className="character-detail-page">
      <Link to="/" className="back-link">
        ← Back to Characters
      </Link>

      <div className="page-header">
        <h1>{character.name}</h1>
        <button type="button" onClick={handleDeleteCharacter} className="btn btn-danger">
          Delete
        </button>
      </div>

      <div className="card profile-card">
        <AvatarUpload name={character.name} avatarDataUrl={character.avatarDataUrl} onUpload={handleAvatarUpload} />

        <div className="profile-card-body">
          {editingProfile ? (
            <ProfileForm
              character={character}
              onSave={handleProfileSave}
              onCancel={() => setEditingProfile(false)}
            />
          ) : (
            <div className="profile-summary">
              <dl>
                <dt>Occupation</dt>
                <dd>{character.occupation || 'Unknown'}</dd>
                <dt>Age</dt>
                <dd>{character.age}</dd>
                <dt>Gender</dt>
                <dd>{character.gender || 'Unspecified'}</dd>
              </dl>
              <button type="button" className="btn btn-secondary" onClick={() => setEditingProfile(true)}>
                Edit Profile
              </button>
            </div>
          )}
        </div>
      </div>

      <div className="card">
        <div className="radar-charts">
          <CoreAttributeRadarChart attributes={character.attributes} />
          <BackstorySection character={character} onSave={handleBackstorySave} />
        </div>
      </div>

      <div className="card">
        <AttributeEditor
          key={character.updatedAt}
          attributes={character.attributes}
          onChange={handleAttributeChange}
          onBulkChange={handleBulkAttributeChange}
        />
      </div>

      <div className="card">
        <SkillList skills={character.skills} onUpsert={handleUpsertSkill} onDelete={handleDeleteSkill} />
      </div>
    </div>
  );
}

interface ProfileFormProps {
  character: Character;
  onSave: (input: CharacterProfileInput) => Promise<void>;
  onCancel: () => void;
}

function ProfileForm({ character, onSave, onCancel }: ProfileFormProps) {
  const [name, setName] = useState(character.name);
  const [occupation, setOccupation] = useState(character.occupation);
  const [age, setAge] = useState(character.age);
  const [gender, setGender] = useState(character.gender);
  const [saving, setSaving] = useState(false);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setSaving(true);
    try {
      await onSave({ name, occupation, age, gender, backstory: character.backstory ?? '' });
    } finally {
      setSaving(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="profile-form">
      <label>
        Name
        <input value={name} onChange={(e) => setName(e.target.value)} required />
      </label>
      <label>
        Occupation
        <input value={occupation} onChange={(e) => setOccupation(e.target.value)} />
      </label>
      <label>
        Age
        <input type="number" value={age} onChange={(e) => setAge(Number(e.target.value))} />
      </label>
      <label>
        Gender
        <input value={gender} onChange={(e) => setGender(e.target.value)} />
      </label>
      <div className="form-actions">
        <button type="submit" className="btn btn-primary" disabled={saving}>
          {saving ? 'Saving...' : 'Save'}
        </button>
        <button type="button" className="btn btn-secondary" onClick={onCancel} disabled={saving}>
          Cancel
        </button>
      </div>
    </form>
  );
}

interface BackstorySectionProps {
  character: Character;
  onSave: (backstory: string) => Promise<void>;
}

function BackstorySection({ character, onSave }: BackstorySectionProps) {
  const [editing, setEditing] = useState(false);
  const [backstory, setBackstory] = useState(character.backstory ?? '');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    setBackstory(character.backstory ?? '');
  }, [character.backstory]);

  const handleSave = async () => {
    setSaving(true);
    try {
      await onSave(backstory);
      setEditing(false);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="backstory-panel">
      <div className="backstory-panel-header">
        <h3>Backstory</h3>
        {!editing && (
          <button type="button" className="btn btn-outline btn-small" onClick={() => setEditing(true)}>
            Edit
          </button>
        )}
      </div>

      {editing ? (
        <>
          <textarea
            value={backstory}
            onChange={(e) => setBackstory(e.target.value.slice(0, BACKSTORY_MAX_LENGTH))}
            rows={8}
            placeholder="Write your character's personal story..."
            autoFocus
          />
          <span className="char-count">
            {backstory.length} / {BACKSTORY_MAX_LENGTH}
          </span>
          <div className="form-actions">
            <button type="button" className="btn btn-primary" onClick={handleSave} disabled={saving}>
              {saving ? 'Saving...' : 'Save'}
            </button>
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => {
                setBackstory(character.backstory ?? '');
                setEditing(false);
              }}
              disabled={saving}
            >
              Cancel
            </button>
          </div>
        </>
      ) : character.backstory ? (
        <p className="backstory-text">{character.backstory}</p>
      ) : (
        <p className="muted">No backstory yet — click Edit to add one.</p>
      )}
    </div>
  );
}
