import React, { createContext, useState, useEffect, useContext } from 'react';
import type { ReactNode } from 'react';
import { api } from '../services/api';

// 1. Tipagem do Usuário logado
interface User {
  email: string;
  role: string;
}

// 2. Tipagem dos dados que o contexto vai expor para a aplicação
interface AuthContextData {
  signed: boolean;
  user: User | null;
  loading: boolean;
  signIn(credentials: { email: string; password: string }): Promise<void>;
  signUp(userData: { name: string; email: string; password: string }): Promise<void>;
  signOut(): void;
}

// 3. Tipagem do componente Provider
interface AuthProviderProps {
  children: ReactNode;
}

const AuthContext = createContext<AuthContextData>({} as AuthContextData);

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  // Efeito executado ao carregar o app: verifica se já existem credenciais salvas
  useEffect(() => {
    async function loadStorageData() {
      const storagedToken = localStorage.getItem('@finance:accessToken');
      const storagedRefreshToken = localStorage.getItem('@finance:refreshToken');

      if (storagedToken && storagedRefreshToken) {
        try {
          // Busca os dados do usuário na rota protegida que validamos no Spring
          const response = await api.get('/users/me');
          setUser({
            email: response.data.email,
            role: response.data.role
          });
        } catch (error) {
          // Se o token estiver totalmente inválido ou expirado e o refresh falhar, limpa tudo
          localStorage.removeItem('@finance:accessToken');
          localStorage.removeItem('@finance:refreshToken');
          setUser(null);
        }
      }
      
      setLoading(false);
    }

    loadStorageData();
  }, []);

  // Método de Login integrado ao nosso back-end
  async function signIn({ email, password }: any) {
    setLoading(true);
    try {
      const response = await api.post('/auth/login', { email, password });
      
      const { accessToken, refreshToken } = response.data;

      localStorage.setItem('@finance:accessToken', accessToken);
      localStorage.setItem('@finance:refreshToken', refreshToken);

      // Busca os detalhes do usuário usando o token recém-criado
      const userResponse = await api.get('/users/me');
      
      setUser({
        email: userResponse.data.email,
        role: userResponse.data.role
      });
    } catch (error) {
      setUser(null);
      throw error; // Repassa o erro para ser tratado visualmente no formulário da página
    } finally {
      setLoading(false);
    }
  }

  // Método de Registro integrado ao nosso back-end
  async function signUp({ name, email, password }: any) {
    setLoading(true);
    try {
      await api.post('/auth/register', { name, email, password });
    } catch (error) {
      throw error;
    } finally {
      setLoading(false);
    }
  }

  // Método de Logout (Limpa os tokens locais e reseta o estado)
  function signOut() {
    localStorage.removeItem('@finance:accessToken');
    localStorage.removeItem('@finance:refreshToken');
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ signed: !!user, user, loading, signIn, signUp, signOut }}>
      {children}
    </AuthContext.Provider>
  );
};

// Hook customizado para facilitar o consumo do contexto nas páginas
export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth deve ser utilizado dentro de um AuthProvider');
  }

  return context;
}