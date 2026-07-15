import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import './App.css';
import { AuthProvider } from './auth/AuthContext';
import { ProtectedRoute } from './auth/ProtectedRoute';
import { NavBar } from './components/layout/NavBar';
import { ToastProvider } from './components/Toast';
import { GothicRails } from './components/GothicVine';
import { LoginPage } from './pages/LoginPage';
import { RegisterPage } from './pages/RegisterPage';
import { CharacterListPage } from './pages/CharacterListPage';
import { CharacterFormPage } from './pages/CharacterFormPage';
import { CharacterDetailPage } from './pages/CharacterDetailPage';

function App() {
  return (
    <BrowserRouter>
      <ToastProvider>
        <AuthProvider>
          <GothicRails />
          <NavBar />
          <main className="app-main">
            <Routes>
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route
                path="/"
                element={
                  <ProtectedRoute>
                    <CharacterListPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/characters/new"
                element={
                  <ProtectedRoute>
                    <CharacterFormPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/characters/:id"
                element={
                  <ProtectedRoute>
                    <CharacterDetailPage />
                  </ProtectedRoute>
                }
              />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>
        </AuthProvider>
      </ToastProvider>
    </BrowserRouter>
  );
}

export default App;
