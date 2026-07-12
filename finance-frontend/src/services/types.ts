export interface Category {
  id: string;
  name: string;
  icon: string;
  color: string;
  userId: string;
}

export interface Transaction {
  id: string;
  description: string;
  amount: number;
  type: 'REVENUE' | 'EXPENSE';
  date: string;
  categoryId: string;
}

export interface DashboardSummary {
  balance: number;
  incomes: number;
  expenses: number;
}