import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

// Guarda Privado: Usuário precisa estar logado para ver o conteúdo (Outlet)
export const PrivateRoutes: React.FC = () => {
  const { signed, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-950 text-white">
        <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-emerald-500"></div>
      </div>
    );
  }

  return signed ? <Outlet /> : <Navigate to="/login" replace />;
};

// Guarda Público: Usuário logado NÃO deve ver telas de Login/Cadastro (Redireciona pro Dashboard)
export const PublicRoutes: React.FC = () => {
  const { signed, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-950 text-white">
        <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-emerald-500"></div>
      </div>
    );
  }

  return !signed ? <Outlet /> : <Navigate to="/dashboard" replace />;
};