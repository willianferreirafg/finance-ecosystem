import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { PublicRoutes, PrivateRoutes } from './CustomRoutes';
import { useAuth } from '../contexts/AuthContext';
import { Login } from '../pages/Login';
import { Register } from '../pages/Register';


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
        <Route element={<PublicRoutes />}>
          {/* Usa o componente <Login /> aqui */}
          <Route path="/login" element={<Login />} /> 
          <Route path="/register" element={<Register />} />
        </Route>

        <Route element={<PrivateRoutes />}>
          <Route path="/dashboard" element={<DashboardPlaceholder />} />
        </Route>

        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
};