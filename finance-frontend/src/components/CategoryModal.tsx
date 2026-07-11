import React, { useState } from 'react';
import { categoryService } from '../services/categoryService';
import { X, Tag } from 'lucide-react';

interface CategoryModalProps {
  isOpen: boolean;
  onClose: () => void;
  onCategoryCreated: () => void;
}

const PRESET_ICONS = ['💰', '🛒', '🚗', '🏠', '🍔', '🔌', '🍿', '🏥', '✈️', '🎓', '🏋️', '👔', '📱', '🐾', '🎁'];

export const CategoryModal: React.FC<CategoryModalProps> = ({ isOpen, onClose, onCategoryCreated }) => {
  const [name, setName] = useState('');
  const [icon, setIcon] = useState('💰');
  const [isSubmitting, setIsSubmitting] = useState(false);

  if (!isOpen) return null;

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!name.trim()) return;

    try {
      setIsSubmitting(true);
      await categoryService.create({ name: name.trim(), icon });
      setName('');
      setIcon('💰');
      onClose();
      onCategoryCreated(); // Atualiza a listagem no Dashboard
    } catch (error) {
      console.error('Erro ao criar categoria:', error);
      alert('Falha ao criar categoria. Verifique o back-end.');
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
      <div className="bg-zinc-900 border border-zinc-800 w-full max-w-sm rounded-2xl shadow-2xl overflow-hidden animate-in fade-in zoom-in-95 duration-150">
        
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b border-zinc-800">
          <h2 className="text-xl font-bold tracking-tight text-zinc-100">Nova Categoria</h2>
          <button onClick={onClose} className="text-zinc-400 hover:text-zinc-200 transition-colors cursor-pointer">
            <X size={20} />
          </button>
        </div>

        {/* Formulário */}
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          
          {/* Nome da Categoria */}
          <div>
            <label className="block text-xs font-medium text-zinc-400 mb-1.5">Nome da Categoria</label>
            <div className="relative">
              <Tag className="absolute left-3.5 top-1/2 -translate-y-1/2 text-zinc-500" size={16} />
              <input
                type="text"
                required
                placeholder="Ex: Alimentação, Lazer, Aluguel..."
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="w-full bg-zinc-950 border border-zinc-800 rounded-xl pl-10 pr-4 py-2.5 text-zinc-100 placeholder-zinc-600 focus:outline-none focus:border-emerald-500 text-sm transition-colors"
              />
            </div>
          </div>

          {/* Seleção de Ícone/Emoji */}
          <div>
            <label className="block text-xs font-medium text-zinc-400 mb-2">Selecione um Ícone</label>
            <div className="grid grid-cols-5 gap-2 p-3 bg-zinc-950 rounded-xl border border-zinc-800/60 max-h-[140px] overflow-y-auto">
              {PRESET_ICONS.map((emoji) => (
                <button
                  key={emoji}
                  type="button"
                  onClick={() => setIcon(emoji)}
                  className={`text-xl p-2 rounded-lg transition-all hover:bg-zinc-900 cursor-pointer ${
                    icon === emoji ? 'bg-emerald-500/20 border border-emerald-500/40' : 'border border-transparent'
                  }`}
                >
                  {emoji}
                </button>
              ))}
            </div>
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
              {isSubmitting ? 'Salvando...' : 'Criar'}
            </button>
          </div>

        </form>
      </div>
    </div>
  );
};