import React from 'react';
import { NavLink } from 'react-router-dom';
import { LayoutDashboard, Wallet, CreditCard, Sparkles, PieChart } from 'lucide-react';

const Sidebar = () => {
  const linkClasses = ({ isActive }) => 
    `flex items-center gap-3 px-4 py-3.5 mx-3 rounded-xl transition-all duration-200 group font-medium ${
      isActive 
        ? "bg-indigo-600 text-white shadow-lg shadow-indigo-500/30 translate-x-1" 
        : "text-slate-400 hover:bg-white/5 hover:text-white"
    }`;

  return (
    <aside className="fixed left-0 top-0 h-screen w-72 bg-slate-900 text-white flex flex-col shadow-2xl z-50">
      {/* Brand Logo */}
      <div className="p-8 pb-10 flex items-center gap-3">
        <div className="bg-gradient-to-tr from-indigo-500 to-violet-500 p-2.5 rounded-lg shadow-lg shadow-indigo-500/20">
          <Wallet className="w-6 h-6 text-white" />
        </div>
        <h1 className="text-xl font-bold tracking-tight text-white">
          Budget<span className="text-indigo-400">AI</span>
        </h1>
      </div>

      {/* Navigation Links */}
      <nav className="flex-1 space-y-2">
        <div className="px-6 mb-2 text-xs font-bold text-slate-500 uppercase tracking-wider">
          Overview
        </div>
        <NavLink to="/" className={linkClasses}>
          <LayoutDashboard size={20} /> Dashboard
        </NavLink>
        <NavLink to="/transactions" className={linkClasses}>
          <CreditCard size={20} /> Transactions
        </NavLink>
        <NavLink to="/budgets" className={linkClasses}>
          <PieChart size={20} /> Budgets
        </NavLink>

        <div className="px-6 mt-8 mb-2 text-xs font-bold text-slate-500 uppercase tracking-wider">
          Intelligence
        </div>
        <NavLink to="/analysis" className={linkClasses}>
          <Sparkles size={20} className="text-amber-400" /> AI Insights
        </NavLink>
      </nav>

      {/* User Profile Snippet (Static for now) */}
      <div className="p-4 m-4 bg-slate-800/50 rounded-2xl border border-white/5 flex items-center gap-3">
        <div className="w-10 h-10 rounded-full bg-gradient-to-br from-pink-500 to-rose-500 flex items-center justify-center font-bold text-sm">
          JD
        </div>
        <div>
          <p className="text-sm font-semibold">John Doe</p>
          <p className="text-xs text-slate-400">Pro Plan</p>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;