import { api } from './api';
import type { Category } from './types';

export const categoryService = {
  async getAll(): Promise<Category[]> {
    // Consome o endpoint estruturado no Spring com cache do Redis
    const response = await api.get<Category[]>('/categories');
    return response.data;
  },

  async create(category: Omit<Category, 'id' | 'userId'>): Promise<Category> {
    const response = await api.post<Category>('/categories', category);
    return response.data;
  }
};