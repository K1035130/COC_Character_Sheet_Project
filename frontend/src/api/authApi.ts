import { apiClient } from './client';

export interface AuthResponse {
  token: string;
  username: string;
  expiresAt: string;
}

export async function register(username: string, password: string, email?: string): Promise<AuthResponse> {
  const response = await apiClient.post<AuthResponse>('/auth/register', { username, password, email });
  return response.data;
}

export async function login(username: string, password: string): Promise<AuthResponse> {
  const response = await apiClient.post<AuthResponse>('/auth/login', { username, password });
  return response.data;
}
