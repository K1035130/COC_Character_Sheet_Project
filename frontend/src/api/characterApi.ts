import { apiClient } from './client';
import type { AttributeKey, Character, CharacterProfileInput, CoreAttribute } from '../types/character';

export async function listCharacters(): Promise<Character[]> {
  const response = await apiClient.get<Character[]>('/characters');
  return response.data;
}

export async function getCharacter(id: string): Promise<Character> {
  const response = await apiClient.get<Character>(`/characters/${id}`);
  return response.data;
}

export async function createCharacter(input: CharacterProfileInput): Promise<Character> {
  const response = await apiClient.post<Character>('/characters', input);
  return response.data;
}

export async function updateCharacterProfile(id: string, input: CharacterProfileInput): Promise<Character> {
  const response = await apiClient.put<Character>(`/characters/${id}`, input);
  return response.data;
}

export async function deleteCharacter(id: string): Promise<void> {
  await apiClient.delete(`/characters/${id}`);
}

export async function updateAttribute(id: string, attribute: AttributeKey, value: number): Promise<Character> {
  const response = await apiClient.patch<Character>(`/characters/${id}/attributes/${attribute}`, { value });
  return response.data;
}

export async function upsertSkill(id: string, skillName: string, addValue: number): Promise<Character> {
  const response = await apiClient.put<Character>(
    `/characters/${id}/skills/${encodeURIComponent(skillName)}`,
    { addValue },
  );
  return response.data;
}

export async function deleteSkill(id: string, skillName: string): Promise<Character> {
  const response = await apiClient.delete<Character>(`/characters/${id}/skills/${encodeURIComponent(skillName)}`);
  return response.data;
}

export async function updateAttributesBulk(
  id: string,
  values: Partial<Record<CoreAttribute, number>>,
): Promise<Character> {
  const response = await apiClient.patch<Character>(`/characters/${id}/attributes`, values);
  return response.data;
}

export async function updateAvatar(id: string, dataUrl: string): Promise<Character> {
  const response = await apiClient.put<Character>(`/characters/${id}/avatar`, { dataUrl });
  return response.data;
}
