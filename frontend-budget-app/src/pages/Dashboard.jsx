import React from 'react';
import { mockTransactions, mockBudgets } from '../data/mockData';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid, Cell } from 'recharts';
import { ArrowUpRight, ArrowDownRight, DollarSign, Wallet, Activity } from 'lucide-react';

const Dashboard = () => {
  // Calculations
  const totalIncome = mockTransactions
    .filter(t => t.type === 'INCOME')
    .reduce((acc, curr) => acc + curr.amount, 0);
  const totalExpense = mockTransactions
    .filter(t => t.type === 'EXPENSE')
    .reduce((acc, curr) => acc + curr.amount, 0);
  const balance = totalIncome - totalExpense;

  // Custom Chart Tooltip
  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="bg-slate-800 text-white p-3 rounded-lg text-xs shadow-xl">
          <p className="font-semibold mb-1">{label}</p>
          <p>Spent: <span className="font-bold text-indigo-400">${payload[0].value}</span></p>
        </div>
      );
    }
    return null;
  };

  return (
    <div className="p-8 pt-10 bg-slate-50 min-h-screen">
      {/* Page Header */}
      <div className="flex justify-between items-end mb-10">
        <div>
          <h1 className="text-3xl font-bold text-slate-900 tracking-tight">Dashboard</h1>
          <p className="text-slate-500 mt-1">Financial overview for November 2025</p>
        </div>
        <button className="bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-2.5 rounded-xl font-medium shadow-lg shadow-indigo-200 transition-all active:scale-95">
          + Add Transaction
        </button>
      </div>

      {/* Stats Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        {/* Balance Card */}
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 relative overflow-hidden group hover:shadow-md transition-all">
          <div className="flex justify-between items-start mb-4">
            <div>
              <p className="text-sm font-medium text-slate-500 uppercase tracking-wider">Total Balance</p>
              <h3 className="text-3xl font-bold text-slate-900 mt-1">${balance.toLocaleString()}</h3>
            </div>
            <div className="p-3 bg-indigo-50 rounded-xl text-indigo-600 group-hover:bg-indigo-600 group-hover:text-white transition-colors">
              <DollarSign size={24} />
            </div>
          </div>
          <div className="flex items-center text-sm text-emerald-600 font-medium bg-emerald-50 w-fit px-2 py-1 rounded-full">
            <ArrowUpRight size={16} className="mr-1" /> +2.5% vs last month
          </div>
        </div>

        {/* Income Card */}
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 group hover:shadow-md transition-all">
          <div className="flex justify-between items-start mb-4">
            <div>
              <p className="text-sm font-medium text-slate-500 uppercase tracking-wider">Total Income</p>
              <h3 className="text-3xl font-bold text-slate-900 mt-1">${totalIncome.toLocaleString()}</h3>
            </div>
            <div className="p-3 bg-emerald-50 rounded-xl text-emerald-600 group-hover:bg-emerald-600 group-hover:text-white transition-colors">
              <Wallet size={24} />
            </div>
          </div>
        </div>

        {/* Expense Card */}
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 group hover:shadow-md transition-all">
          <div className="flex justify-between items-start mb-4">
            <div>
              <p className="text-sm font-medium text-slate-500 uppercase tracking-wider">Total Expenses</p>
              <h3 className="text-3xl font-bold text-slate-900 mt-1">${totalExpense.toLocaleString()}</h3>
            </div>
            <div className="p-3 bg-rose-50 rounded-xl text-rose-600 group-hover:bg-rose-600 group-hover:text-white transition-colors">
              <Activity size={24} />
            </div>
          </div>
        </div>
      </div>

      {/* Main Content Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        {/* Chart Section - Spans 2 Columns */}
        <div className="lg:col-span-2 bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
          <h2 className="text-lg font-bold text-slate-800 mb-6">Spending vs Limits</h2>
          <div className="h-80 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={mockBudgets} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#E2E8F0" />
                <XAxis dataKey="category" axisLine={false} tickLine={false} tick={{ fill: '#64748B', fontSize: 12 }} dy={10} />
                <YAxis axisLine={false} tickLine={false} tick={{ fill: '#64748B', fontSize: 12 }} />
                <Tooltip content={<CustomTooltip />} cursor={{ fill: '#F1F5F9' }} />
                <Bar dataKey="spent" name="Spent" radius={[6, 6, 6, 6]} barSize={40}>
                  {mockBudgets.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color || '#4F46E5'} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Recent Transactions - Spans 1 Column */}
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-lg font-bold text-slate-800">Recent Activity</h2>
            <button className="text-sm text-indigo-600 font-semibold hover:text-indigo-700">View All</button>
          </div>
          
          <div className="space-y-4">
            {mockTransactions.slice(0, 5).map(t => (
              <div key={t.id} className="flex items-center justify-between p-3 hover:bg-slate-50 rounded-xl transition-colors">
                <div className="flex items-center gap-4">
                  <div className={`w-10 h-10 rounded-full flex items-center justify-center text-lg ${
                    t.type === 'INCOME' ? 'bg-emerald-100 text-emerald-600' : 'bg-rose-100 text-rose-600'
                  }`}>
                    {t.type === 'INCOME' ? '💰' : '🛍️'}
                  </div>
                  <div>
                    <p className="font-semibold text-slate-800 text-sm">{t.description}</p>
                    <p className="text-xs text-slate-500">{t.date}</p>
                  </div>
                </div>
                <span className={`font-bold text-sm ${
                  t.type === 'INCOME' ? 'text-emerald-600' : 'text-rose-600'
                }`}>
                  {t.type === 'INCOME' ? '+' : '-'}${t.amount.toFixed(2)}
                </span>
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
};

export default Dashboard;