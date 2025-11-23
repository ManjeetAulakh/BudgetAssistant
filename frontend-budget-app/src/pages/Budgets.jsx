import React from 'react';
import { mockBudgets } from '../data/mockData';

const Budgets = () => {
  return (
    <div>
      <div className="page-header">
        <h1>Your Budgets</h1>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px' }}>
        {mockBudgets.map((budget) => {
          const percent = Math.min((budget.spent / budget.limitAmount) * 100, 100);
          
          return (
            <div key={budget.id} className="card">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
                <h3 style={{ margin: 0, color: budget.color }}>{budget.category}</h3>
                <span style={{ fontSize: '0.9rem', color: '#6B7280' }}>
                  ${budget.spent} / ${budget.limitAmount}
                </span>
              </div>
              
              <div className="progress-bar-bg">
                <div 
                  className="progress-bar-fill" 
                  style={{ 
                    width: `${percent}%`, 
                    backgroundColor: percent > 90 ? '#EF4444' : budget.color 
                  }}
                ></div>
              </div>
              
              <div style={{ marginTop: '10px', fontSize: '0.85rem', color: '#6B7280' }}>
                {percent >= 100 ? 'Over budget!' : `$${budget.remaining.toFixed(2)} remaining`}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default Budgets;