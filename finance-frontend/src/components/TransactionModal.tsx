import React, { useState } from 'react';
import type { Category } from '../services/types';
import { transactionService } from '../services/transactionService';
import { X, DollarSign, Calendar, Tag, FileText } from 'lucide-react';

interface TransactionModalProps {
    isOpen: boolean;
    onClose: () => void;
    categories: Category[];
    onTransactionCreated: () => void; // Callback para recarregar o Dashboard após salvar
}

export const TransactionModal: React.FC<TransactionModalProps> = ({
    isOpen,
    onClose,
    categories,
    onTransactionCreated,
}) => {
    const [description, setDescription] = useState('');
    const [amount, setAmount] = useState('');
    const [date, setDate] = useState(new Date().toISOString().split('T')[0]);
    const [type, setType] = useState<'REVENUE' | 'EXPENSE'>('REVENUE');
    const [categoryId, setCategoryId] = useState('');
    const [paid, setPaid] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [errorMsg, setErrorMsg] = useState<string | null>(null);

    if (!isOpen) return null;

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setErrorMsg(null);

        if (!categoryId || !description || !amount) {
            setErrorMsg('Por favor, preencha todos os campos obrigatórios.');
            return;
        }

        try {
            setIsSubmitting(true);
            await transactionService.create({
                categoryId,
                description,
                amount: Number(amount),
                date,
                type,
                paid,
            });

            setDescription('');
            setAmount('');
            setCategoryId('');
            onClose();
            onTransactionCreated();
        } catch (error) {
            console.error('Erro ao salvar transação:', error);
            setErrorMsg('Falha ao salvar a movimentação. Verifique os dados digitados.');
        } finally {
            setIsSubmitting(false);
        }
    }

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
            <div className="bg-zinc-900 border border-zinc-800 w-full max-w-md rounded-2xl shadow-2xl overflow-hidden animate-in fade-in zoom-in-95 duration-150">

                {/* Header da Modal */}
                <div className="flex justify-between items-center p-6 border-b border-zinc-800">
                    <h2 className="text-xl font-bold tracking-tight text-zinc-100">Nova Movimentação</h2>
                    <button onClick={onClose} className="text-zinc-400 hover:text-zinc-200 transition-colors cursor-pointer">
                        <X size={20} />
                    </button>
                </div>

                {/* Formulário */}
                <form onSubmit={handleSubmit} className="p-6 space-y-4">

                    {/* BANNER DE ERRO ESTILIZADO */}
                    {errorMsg && (
                        <div className="p-3 bg-red-950/40 border border-red-900/50 rounded-xl text-red-400 text-sm flex items-center gap-2 animate-in fade-in duration-200">
                            <span className="w-1.5 h-1.5 rounded-full bg-red-500 shrink-0" />
                            <p>{errorMsg}</p>
                        </div>
                    )}

                    {/* Seletor Tipo (Receita / Despesa) */}
                    <div className="grid grid-cols-2 gap-3 p-1 bg-zinc-950 rounded-xl border border-zinc-800/60">
                        <button
                            type="button"
                            onClick={() => setType('REVENUE')}
                            className={`py-2 text-sm font-medium rounded-lg transition-all cursor-pointer ${type === 'REVENUE'
                                    ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20'
                                    : 'text-zinc-400 hover:text-zinc-200'
                                }`}
                        >
                            Faturamento (+)
                        </button>
                        <button
                            type="button"
                            onClick={() => setType('REVENUE')}
                            className={`py-2 text-sm font-medium rounded-lg transition-all cursor-pointer ${type === 'REVENUE'
                                    ? 'bg-rose-500/10 text-rose-400 border border-rose-500/20'
                                    : 'text-zinc-400 hover:text-zinc-200'
                                }`}
                        >
                            Despesa (-)
                        </button>
                    </div>

                    {/* Valor */}
                    <div>
                        <label className="block text-xs font-medium text-zinc-400 mb-1.5">Valor (R$)</label>
                        <div className="relative">
                            <DollarSign className="absolute left-3.5 top-1/2 -translate-y-1/2 text-zinc-500" size={16} />
                            <input
                                type="number"
                                step="0.01"
                                required
                                placeholder="0,00"
                                value={amount}
                                onChange={(e) => setAmount(e.target.value)}
                                className="w-full bg-zinc-950 border border-zinc-800 rounded-xl pl-10 pr-4 py-2.5 text-zinc-100 placeholder-zinc-600 focus:outline-none focus:border-emerald-500 text-sm transition-colors"
                            />
                        </div>
                    </div>

                    {/* Descrição */}
                    <div>
                        <label className="block text-xs font-medium text-zinc-400 mb-1.5">Descrição</label>
                        <div className="relative">
                            <FileText className="absolute left-3.5 top-1/2 -translate-y-1/2 text-zinc-500" size={16} />
                            <input
                                type="text"
                                required
                                placeholder="Ex: Assinatura Cloud, Saldo Cliente..."
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                className="w-full bg-zinc-950 border border-zinc-800 rounded-xl pl-10 pr-4 py-2.5 text-zinc-100 placeholder-zinc-600 focus:outline-none focus:border-emerald-500 text-sm transition-colors"
                            />
                        </div>
                    </div>

                    {/* Categoria */}
                    <div>
                        <label className="block text-xs font-medium text-zinc-400 mb-1.5">Categoria</label>
                        <div className="relative">
                            <Tag className="absolute left-3.5 top-1/2 -translate-y-1/2 text-zinc-500" size={16} />
                            <select
                                required
                                value={categoryId}
                                onChange={(e) => setCategoryId(e.target.value)}
                                className="w-full bg-zinc-950 border border-zinc-800 rounded-xl pl-10 pr-4 py-2.5 text-zinc-100 focus:outline-none focus:border-emerald-500 text-sm transition-colors appearance-none cursor-pointer"
                            >
                                <option value="" disabled className="text-zinc-600">Selecione uma categoria</option>
                                {categories.map((c) => (
                                    <option key={c.id} value={c.id} className="bg-zinc-950 text-zinc-100">
                                        {c.icon} {c.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>

                    {/* Data */}
                    <div>
                        <label className="block text-xs font-medium text-zinc-400 mb-1.5">Data</label>
                        <div className="relative">
                            <Calendar className="absolute left-3.5 top-1/2 -translate-y-1/2 text-zinc-500" size={16} />
                            <input
                                type="date"
                                required
                                value={date}
                                onChange={(e) => setDate(e.target.value)}
                                className="w-full bg-zinc-950 border border-zinc-800 rounded-xl pl-10 pr-4 py-2.5 text-zinc-100 focus:outline-none focus:border-emerald-500 text-sm transition-colors"
                            />
                        </div>
                    </div>

                    {/* Checkbox Efetivada/Paga */}
                    <div className="flex items-center gap-2 pt-2">
                        <input
                            type="checkbox"
                            id="paid"
                            checked={paid}
                            onChange={(e) => setPaid(e.target.checked)}
                            className="w-4 h-4 rounded border-zinc-800 bg-zinc-950 text-emerald-500 focus:ring-0 cursor-pointer"
                        />
                        <label htmlFor="paid" className="text-sm font-medium text-zinc-300 cursor-pointer select-none">
                            {type === 'REVENUE' ? 'Recebido' : 'Pago'} (Lançamento compensado)
                        </label>
                    </div>

                    {/* Botões de Ação */}
                    <div className="flex justify-end gap-3 pt-4 border-t border-zinc-800 mt-6">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-4 py-2 bg-zinc-950 border border-zinc-800 rounded-xl text-zinc-400 hover:bg-zinc-900 transition-colors text-sm font-medium cursor-pointer"
                        >
                            Cancelar
                        </button>
                        <button
                            type="submit"
                            disabled={isSubmitting}
                            className="px-4 py-2 bg-emerald-600 text-white font-medium rounded-xl hover:bg-emerald-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors text-sm cursor-pointer"
                        >
                            {isSubmitting ? 'Salvando...' : 'Confirmar'}
                        </button>
                    </div>

                </form>
            </div>
        </div>
    );
};