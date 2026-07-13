import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { Lock, Mail, User, Eye, EyeOff, Wallet } from 'lucide-react';

export const Register: React.FC = () => {
  const navigate = useNavigate();
  const { signUp } = useAuth();

  // Estados do formulário
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  // Estados de controle da UI
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    if (!name || !email || !password) {
      setError('Por favor, preencha todos os campos.');
      return;
    }

    try {
      setError(null);
      setIsSubmitting(true);

      // Executa o cadastro chamando o AuthContext
      await signUp({ name, email, password });

      // Se der certo, envia o usuário para a tela de login
      navigate('/login');
    } catch (error: any) {
      const errorMessage = error.message || 'Falha ao realizar cadastro. Tente novamente.';
      setError(() => errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="min-h-screen bg-zinc-950 flex flex-col justify-center items-center px-4 selection:bg-emerald-500/30 selection:text-emerald-400">
      <div className="w-full max-w-md bg-zinc-900 border border-zinc-800/80 rounded-2xl p-8 shadow-2xl shadow-black/40">

        {/* Header */}
        <div className="flex flex-col items-center mb-8">
          <div className="p-3 bg-emerald-500/10 border border-emerald-500/20 rounded-xl text-emerald-500 mb-3">
            <Wallet size={32} />
          </div>
          <h2 className="text-2xl font-bold text-zinc-100 tracking-tight">Criar Nova Conta</h2>
          <p className="text-zinc-400 text-sm mt-1">Comece a gerenciar suas finanças hoje mesmo</p>
        </div>

        {/* Alerta de Erro */}
        {error && (
          <div className="mb-6 p-4 bg-zinc-950 border border-red-500 rounded-xl flex items-center gap-2">
            <span className="w-2 h-2 rounded-full bg-red-500 shrink-0" />
            <p className="text-red-500 text-sm font-semibold">{error}</p>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-5">
          {/* Campo de Nome */}
          <div>
            <label className="block text-zinc-300 text-sm font-medium mb-1.5" htmlFor="name">
              Nome Completo
            </label>
            <div className="relative">
              <span className="absolute inset-y-0 left-0 flex items-center pl-3.5 text-zinc-500">
                <User size={18} />
              </span>
              <input
                id="name"
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Seu nome"
                className="w-full bg-zinc-950 text-zinc-100 placeholder-zinc-600 pl-11 pr-4 py-3 rounded-xl border border-zinc-800 focus:border-emerald-500/50 focus:ring-2 focus:ring-emerald-500/10 focus:outline-none transition-all duration-200"
                disabled={isSubmitting}
              />
            </div>
          </div>

          {/* Campo de E-mail */}
          <div>
            <label className="block text-zinc-300 text-sm font-medium mb-1.5" htmlFor="email">
              E-mail
            </label>
            <div className="relative">
              <span className="absolute inset-y-0 left-0 flex items-center pl-3.5 text-zinc-500">
                <Mail size={18} />
              </span>
              <input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="seu@email.com"
                className="w-full bg-zinc-950 text-zinc-100 placeholder-zinc-600 pl-11 pr-4 py-3 rounded-xl border border-zinc-800 focus:border-emerald-500/50 focus:ring-2 focus:ring-emerald-500/10 focus:outline-none transition-all duration-200"
                disabled={isSubmitting}
              />
            </div>
          </div>

          {/* Campo de Senha */}
          <div>
            <label className="block text-zinc-300 text-sm font-medium mb-1.5" htmlFor="password">
              Senha
            </label>
            <div className="relative">
              <span className="absolute inset-y-0 left-0 flex items-center pl-3.5 text-zinc-500">
                <Lock size={18} />
              </span>
              <input
                id="password"
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full bg-zinc-950 text-zinc-100 placeholder-zinc-600 pl-11 pr-11 py-3 rounded-xl border border-zinc-800 focus:border-emerald-500/50 focus:ring-2 focus:ring-emerald-500/10 focus:outline-none transition-all duration-200"
                disabled={isSubmitting}
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute inset-y-0 right-0 flex items-center pr-3.5 text-zinc-500 hover:text-zinc-300 transition-colors cursor-pointer"
              >
                {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          {/* Botão Submit */}
          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-emerald-600 hover:bg-emerald-500 disabled:bg-emerald-700/50 text-white font-medium py-3 rounded-xl shadow-lg transition-all duration-200 cursor-pointer flex items-center justify-center gap-2"
          >
            {isSubmitting ? (
              <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
            ) : (
              'Criar Minha Conta'
            )}
          </button>
        </form>

        {/* Link para voltar ao Login */}
        <div className="mt-6 text-center text-sm text-zinc-500">
          Já possui uma conta?{' '}
          <Link to="/login" className="text-emerald-500 hover:text-emerald-400 font-medium transition-colors">
            Fazer Login
          </Link>
        </div>

      </div>
    </div>
  );
};