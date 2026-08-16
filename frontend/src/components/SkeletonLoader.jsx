import React from 'react';

export default function SkeletonLoader({ viewMode = 'kanban' }) {
  if (viewMode === 'table') {
    return (
      <div className="table-wrapper skeleton-table">
        {[1, 2, 3, 4, 5].map((i) => (
          <div key={i} className="skeleton-row">
            <div className="skeleton-line skeleton-title-col"></div>
            <div className="skeleton-line skeleton-badge-col"></div>
            <div className="skeleton-line skeleton-salary-col"></div>
            <div className="skeleton-line skeleton-date-col"></div>
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className="skeleton-grid">
      {[1, 2, 3, 4, 5, 6].map((i) => (
        <div key={i} className="skeleton-card">
          <div className="skeleton-card-header">
            <div className="skeleton-avatar"></div>
            <div className="skeleton-header-text">
              <div className="skeleton-line line-sm"></div>
              <div className="skeleton-line line-md"></div>
            </div>
          </div>
          <div className="skeleton-tags">
            <div className="skeleton-pill"></div>
            <div className="skeleton-pill"></div>
          </div>
          <div className="skeleton-line line-full"></div>
          <div className="skeleton-card-footer">
            <div className="skeleton-line line-xs"></div>
            <div className="skeleton-line line-xs"></div>
          </div>
        </div>
      ))}
    </div>
  );
}
