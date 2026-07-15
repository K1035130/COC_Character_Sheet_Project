import { createContext, useContext, useMemo, useState } from 'react';
import type { ReactNode } from 'react';
import { AUTH_TOKEN_KEY, AUTH_USERNAME_KEY } from '../api/client';
import * as authApi from '../api/authApi';

interface AuthContextValue {
  username: string | null;
  isAuthenticated: boolean;
  login: (username: string, password: string) => Promise<void>;
  register: (username: string, password: string, email?: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [username, setUsername] = useState<string | null>(() => localStorage.getItem(AUTH_USERNAME_KEY));

  const applyAuth = (token: string, user: string) => {
    localStorage.setItem(AUTH_TOKEN_KEY, token);
    localStorage.setItem(AUTH_USERNAME_KEY, user);
    setUsername(user);
  };

  const login = async (usernameInput: string, password: string) => {
    const response = await authApi.login(usernameInput, password);
    applyAuth(response.token, response.username);
  };

  const registerUser = async (usernameInput: string, password: string, email?: string) => {
    const response = await authApi.register(usernameInput, password, email);
    applyAuth(response.token, response.username);
  };

  const logout = () => {
    localStorage.removeItem(AUTH_TOKEN_KEY);
    localStorage.removeItem(AUTH_USERNAME_KEY);
    setUsername(null);
  };

  const value = useMemo<AuthContextValue>(
    () => ({
      username,
      isAuthenticated: username !== null,
      login,
      register: registerUser,
      logout,
    }),
    [username],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
