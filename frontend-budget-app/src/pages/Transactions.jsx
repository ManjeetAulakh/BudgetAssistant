import React from 'react';
import { mockTransactions } from '../data/mockData';

const Transactions = () => {
  return (
    <div>
      <div className="page-header">
        <h1>Transaction History</h1>
      </div>

      <div className="card table-container">
        <table>
          <thead>
            <tr>
              <th>Date</th>
              <th>Description</th>
              <th>Category</th>
              <th>Type</th>
              <th style={{ textAlign: 'right' }}>Amount</th>
            </tr>
          </thead>
          <tbody>
            {mockTransactions.map((t) => (
              <tr key={t.id}>
                <td>{t.date}</td>
                <td style={{ fontWeight: '500' }}>{t.description}</td>
                <td>{t.category}</td>
                <td>
                  <span className={`badge ${t.type === 'INCOME' ? 'badge-income' : 'badge-expense'}`}>
                    {t.type}
                  </span>
                </td>
                <td style={{ textAlign: 'right', fontWeight: 'bold' }}>
                  ${t.amount.toFixed(2)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Transactions;