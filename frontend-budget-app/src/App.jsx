import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import Transactions from './pages/Transactions'; // Keep previous or update later
import Budgets from './pages/Budgets'; // Keep previous or update later

const Analysis = () => <div className="ml-72 p-10 font-bold text-xl">AI Analysis Coming Soon...</div>;

function App() {
  return (
    <Router>
      <div className="flex bg-slate-50 min-h-screen">
        {/* Sidebar is fixed width */}
        <Sidebar />
        
        {/* Main Content Area: needs left margin to clear fixed sidebar */}
        <main className="flex-1 ml-72">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/transactions" element={<Transactions />} />
            <Route path="/budgets" element={<Budgets />} />
            <Route path="/analysis" element={<Analysis />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;