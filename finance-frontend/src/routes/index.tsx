import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { PublicRoutes, PrivateRoutes } from './CustomRoutes';
import { useAuth } from '../contexts/AuthContext';

// Placeholders temporários para o app não quebrar até criarmos os arquivos visuais
const LoginPlaceholder = () => <div className="p-8 text-white bg-zinc-900 min-h-screen">Tela de Login (Pública)</div>;
const RegisterPlaceholder = () => <div className="p-8 text-white bg-zinc-900 min-h-screen">Tela de Cadastro (Pública)</div>;
const DashboardPlaceholder = () => {
  const { signOut, user } = useAuth();
  return (
    <div className="p-8 text-white bg-zinc-950 min-h-screen">
      <h1 className="text-2xl font-bold mb-4">Dashboard Financeiro (Protegido)</h1>
      <p className="mb-4">Bem-vindo, {user?.email}!</p>
      <button onClick={signOut} className="px-4 py-2 bg-red-600 rounded hover:bg-red-700 cursor-pointer">
        Sair do Sistema
      </button>
    </div>
  );
};

export const AppRoutes: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        {/* Grupo de Rotas Públicas */}
        <Route element={<PublicRoutes />}>
          <Route path="/login" element={<LoginPlaceholder />} />
          <Route path="/register" element={<RegisterPlaceholder />} />
        </Route>

        {/* Grupo de Rotas Privadas/Protegidas */}
        <Route element={<PrivateRoutes />}>
          <Route path="/dashboard" element={<DashboardPlaceholder />} />
        </Route>

        {/* Fallback global: qualquer rota desconhecida manda para o Dashboard (que decide se vai pro login ou não) */}
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
};