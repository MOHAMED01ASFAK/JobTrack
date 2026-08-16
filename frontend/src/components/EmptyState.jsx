import React from 'react';
import { Briefcase, Search, Plus } from 'lucide-react';

export default function EmptyState({
  isFiltered,
  onResetFilters,
  onOpenCreateModal,
}) {
  return (
    <div className="empty-state-container">
      <div className="empty-icon-circle">
        {isFiltered ? (
          <Search size={32} className="empty-icon" />
        ) : (
          <Briefcase size={32} className="empty-icon" />
        )}
      </div>

      <h3 className="empty-title">
        {isFiltered ? 'No matching job applications' : 'No applications tracked yet'}
      </h3>

      <p className="empty-description">
        {isFiltered
          ? 'Try adjusting your search query, status filters, or location filters to see more results.'
          : 'Start organizing your career search. Add your first job application and monitor its interview lifecycle.'}
      </p>

      <div className="empty-actions">
        {isFiltered ? (
          <button className="btn btn-ghost" onClick={onResetFilters}>
            Reset All Filters
          </button>
        ) : (
          <button className="btn btn-primary" onClick={onOpenCreateModal}>
            <Plus size={18} />
            <span>Track First Job</span>
          </button>
        )}
      </div>
    </div>
  );
}
