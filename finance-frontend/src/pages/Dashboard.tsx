import React, { useEffect, useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { transactionService } from '../services/transactionService';
import { categoryService } from '../services/categoryService';
import type { Transaction, Category, DashboardSummary } from '../services/types';
import { ArrowUpCircle, ArrowDownCircle, DollarSign, LogOut, FileText, Plus } from 'lucide-react';
import { TransactionModal } from '../components/TransactionModal'
import { CategoryModal } from '../components/CategoryModal';


export const Dashboard: React.FC = () => {
  const { signOut, user } = useAuth();
  const [loadingData, setLoadingData] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isCategoryModalOpen, setIsCategoryModalOpen] = useState(false);
  
  // Ajustado o estado para aceitar DashboardSummary ou null para o mapeamento seguro
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);

  async function fetchDashboardData() {
    try {
      setLoadingData(true);
      const [summaryData, transactionsData, categoriesData] = await Promise.all([
        transactionService.getSummary(),
        transactionService.getAll(),
        categoryService.getAll()
      ]);

      setSummary(summaryData);
      setTransactions(transactionsData);
      setCategories(categoriesData);
    } catch (error) {
      console.error("Erro ao carregar dados do painel:", error);
    } finally {
      setLoadingData(false);
    }
  }

  useEffect(() => {
    fetchDashboardData();
  }, []);

  async function handleExportPdf() {
    try {
      const blob = await transactionService.downloadPdfReport('2026-01-01', '2026-12-31');
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `relatorio_2026.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      alert("Falha ao exportar PDF.");
    }
  }

  if (loadingData) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-950 text-white">
        <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-emerald-500"></div>
      </div>
    );
  }

  const summaryRaw = summary as any;

  // Extração segura de propriedades com suporte para fallback (caso o back-end devolva nomes diferentes)
  const totalIncomes = summaryRaw?.totalIncomes ?? summaryRaw?.incomes ?? 0;
  const totalExpenses = summaryRaw?.totalExpenses ?? summaryRaw?.expenses ?? 0;
  const balance = summaryRaw?.balance ?? 0;

  return (
    <div className="min-h-screen bg-zinc-950 text-zinc-100 p-4 md:p-8">
      {/* Topbar/Header */}
      <header className="flex justify-between items-center mb-8 pb-6 border-b border-zinc-800">
        <div>
          <h1 className="text-2xl font-bold tracking-tight">Painel Financeiro</h1>
          <p className="text-zinc-500 text-sm">Olá, {user?.email}</p>
        </div>
        <div className="flex gap-3">
          <button onClick={() => setIsModalOpen(true)} className="flex items-center gap-2 px-4 py-2 bg-emerald-600 hover:bg-emerald-500 text-white font-medium rounded-xl transition-colors cursor-pointer text-sm">
            + Nova Transação
          </button>
          <button onClick={handleExportPdf} className="flex items-center gap-2 px-4 py-2 bg-zinc-900 border border-zinc-800 rounded-xl text-zinc-300 hover:bg-zinc-800 transition-colors cursor-pointer text-sm">
            <FileText size={16} /> Relatório PDF
          </button>
          <button onClick={signOut} className="flex items-center gap-2 px-4 py-2 bg-red-950/40 border border-red-900/30 rounded-xl text-red-400 hover:bg-red-900/30 transition-colors cursor-pointer text-sm">
            <LogOut size={16} /> Sair
          </button>
        </div>
        
      </header>

      {/* Cards de Resumo */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-zinc-900 border border-zinc-800/80 p-6 rounded-2xl shadow-xl">
          <div className="flex justify-between items-center text-zinc-400 mb-2">
            <span className="text-sm font-medium">Entradas</span>
            <ArrowUpCircle className="text-emerald-500" size={24} />
          </div>
          <p className="text-2xl font-bold text-zinc-100">R$ {totalIncomes.toFixed(2)}</p>
        </div>

        <div className="bg-zinc-900 border border-zinc-800/80 p-6 rounded-2xl shadow-xl">
          <div className="flex justify-between items-center text-zinc-400 mb-2">
            <span className="text-sm font-medium">Saídas</span>
            <ArrowDownCircle className="text-rose-500" size={24} />
          </div>
          <p className="text-2xl font-bold text-zinc-100">R$ {totalExpenses.toFixed(2)}</p>
        </div>

        <div className="bg-zinc-900 border border-zinc-800/80 p-6 rounded-2xl shadow-xl relative overflow-hidden">
          <div className="flex justify-between items-center text-zinc-400 mb-2">
            <span className="text-sm font-medium">Saldo Total</span>
            <DollarSign className="text-emerald-400" size={24} />
          </div>
          <p className={`text-2xl font-bold ${balance >= 0 ? 'text-emerald-400' : 'text-rose-400'}`}>
            R$ {balance.toFixed(2)}
          </p>
        </div>
      </div>

      {/* Seção Principal */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Lista de Transações */}
        <div className="lg:col-span-2 bg-zinc-900 border border-zinc-800/80 p-6 rounded-2xl">
          <h3 className="font-semibold text-lg mb-4">Últimas Transações</h3>
          <div className="space-y-3 max-h-[100px] overflow-y-auto pr-2">
            {transactions.length === 0 ? (
              <p className="text-zinc-500 text-sm py-4">Nenhuma movimentação registrada.</p>
            ) : (
              transactions.map(t => (
                <div key={t.id} className="flex justify-between items-center bg-zinc-950 p-4 rounded-xl border border-zinc-800/40">
                  <div>
                    <p className="text-sm font-medium">{t.description}</p>
                    <span className="text-xs text-zinc-500">{new Date(t.date).toLocaleDateString()}</span>
                  </div>
                  <span className={`text-sm font-semibold ${t.type === 'REVENUE' ? 'text-emerald-500' : 'text-rose-500'}`}>
                    {t.type === 'REVENUE' ? '+' : '-'} R$ {(t.amount ?? 0).toFixed(2)}
                  </span>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Categorias */}
        <div className="bg-zinc-900 border border-zinc-800/80 p-6 rounded-2xl">
          <h3 className="font-semibold text-lg mb-4">Minhas Categorias</h3>
          <button onClick={() => setIsCategoryModalOpen(true)} className="p-1.5 bg-zinc-950 hover:bg-zinc-800 border border-zinc-800 rounded-lg text-emerald-500 transition-colors cursor-pointer" title="Nova Categoria">
            <Plus size={16} />
          </button>

          <div className="grid grid-cols-2 gap-3">
            {categories.map(c => (
              <div key={c.id} className="flex items-center gap-3 p-3 bg-zinc-950 rounded-xl border border-zinc-800/50">
                <span className="text-xl p-1.5 rounded-lg bg-zinc-900">{c.icon}</span>
                <span className="text-xs font-medium truncate text-zinc-300">{c.name}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Modal de Novas Transações */}
      <TransactionModal 
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        categories={categories}
        onTransactionCreated={fetchDashboardData} 
      />

      {/* Modal de Novas Categorias */}
      <CategoryModal 
        isOpen={isCategoryModalOpen}
        onClose={() => setIsCategoryModalOpen(false)}
        onCategoryCreated={fetchDashboardData} // Recarrega os dados ao criar categoria
      />
    </div>
  );
};