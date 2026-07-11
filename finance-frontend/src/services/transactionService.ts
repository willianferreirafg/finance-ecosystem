import { api } from './api';
import type { Transaction, DashboardSummary } from './types';

export const transactionService = {
  async getAll(): Promise<Transaction[]> {
    const response = await api.get<Transaction[]>('/transactions');
    return response.data;
  },

  async getSummary(): Promise<DashboardSummary> {
    const response = await api.get<DashboardSummary>('/transactions/summary');
    return response.data;
  },

  async downloadPdfReport(startDate: string, endDate: string): Promise<Blob> {
    // Consome o endpoint do JasperReports configurado na sprint anterior
    const response = await api.get('/reports/financial-pdf', {
      params: { startDate, endDate },
      responseType: 'blob' // Fundamental para processar binários de arquivos
    });
    return response.data;
  },

  async create(data: {
    categoryId: string;
    description: string;
    amount: number;
    date: string;
    type: 'INCOME' | 'EXPENSE';
    paid: boolean;
  }): Promise<Transaction> {
    const response = await api.post<Transaction>('/api/v1/transactions', data);
    return response.data;
  }
};