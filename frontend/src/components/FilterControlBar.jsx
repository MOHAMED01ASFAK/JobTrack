import React from 'react';
import {
  Filter,
  SlidersHorizontal,
  LayoutGrid,
  Kanban,
  Table as TableIcon,
  ArrowUpDown,
  RotateCcw,
} from 'lucide-react';
import { APPLICATION_STATUS, WORKPLACE_TYPES, EMPLOYMENT_TYPES } from '../utils/constants';

export default function FilterControlBar({
  statusFilter,
  setStatusFilter,
  workplaceFilter,
  setWorkplaceFilter,
  employmentFilter,
  setEmploymentFilter,
  sortBy,
  setSortBy,
  viewMode,
  setViewMode,
  filteredCount,
  totalCount,
  onResetFilters,
}) {
  const hasActiveFilters =
    statusFilter !== 'ALL' || workplaceFilter !== 'ALL' || employmentFilter !== 'ALL';

  return (
    <div className="filter-bar-container">
      <div className="filter-controls-left">
        {/* Status Dropdown */}
        <div className="filter-group">
          <label htmlFor="status-select" className="filter-label">Status</label>
          <select
            id="status-select"
            className="filter-select"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="ALL">All Statuses ({totalCount})</option>
            {Object.entries(APPLICATION_STATUS).map(([key, config]) => (
              <option key={key} value={key}>
                {config.label}
              </option>
            ))}
          </select>
        </div>

        {/* Workplace Type Dropdown */}
        <div className="filter-group">
          <label htmlFor="workplace-select" className="filter-label">Workplace</label>
          <select
            id="workplace-select"
            className="filter-select"
            value={workplaceFilter}
            onChange={(e) => setWorkplaceFilter(e.target.value)}
          >
            <option value="ALL">All Locations</option>
            {Object.entries(WORKPLACE_TYPES).map(([key, config]) => (
              <option key={key} value={key}>
                {config.label}
              </option>
            ))}
          </select>
        </div>

        {/* Employment Type Dropdown */}
        <div className="filter-group">
          <label htmlFor="employment-select" className="filter-label">Employment</label>
          <select
            id="employment-select"
            className="filter-select"
            value={employmentFilter}
            onChange={(e) => setEmploymentFilter(e.target.value)}
          >
            <option value="ALL">All Types</option>
            {Object.entries(EMPLOYMENT_TYPES).map(([key, config]) => (
              <option key={key} value={key}>
                {config.label}
              </option>
            ))}
          </select>
        </div>

        {/* Sort Dropdown */}
        <div className="filter-group">
          <label htmlFor="sort-select" className="filter-label">Sort By</label>
          <select
            id="sort-select"
            className="filter-select"
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
          >
            <option value="applied_desc">Applied Date (Newest)</option>
            <option value="applied_asc">Applied Date (Oldest)</option>
            <option value="priority_desc">Priority (High to Low)</option>
            <option value="salary_desc">Salary (High to Low)</option>
            <option value="company_asc">Company (A to Z)</option>
            <option value="updated_desc">Recently Updated</option>
          </select>
        </div>

        {/* Reset Filters CTA */}
        {hasActiveFilters && (
          <button
            className="btn btn-ghost reset-filter-btn"
            onClick={onResetFilters}
            title="Reset all filters"
          >
            <RotateCcw size={14} />
            <span>Reset</span>
          </button>
        )}
      </div>

      <div className="filter-controls-right">
        {/* Results Counter */}
        <div className="results-count-pill">
          Showing <strong>{filteredCount}</strong> of <strong>{totalCount}</strong>
        </div>

        {/* View Mode Toggle */}
        <div className="view-toggle-container">
          <button
            className={`view-toggle-btn ${viewMode === 'kanban' ? 'active' : ''}`}
            onClick={() => setViewMode('kanban')}
            title="Kanban Board View"
          >
            <Kanban size={16} />
            <span>Kanban</span>
          </button>
          <button
            className={`view-toggle-btn ${viewMode === 'grid' ? 'active' : ''}`}
            onClick={() => setViewMode('grid')}
            title="Card Grid View"
          >
            <LayoutGrid size={16} />
            <span>Grid</span>
          </button>
          <button
            className={`view-toggle-btn ${viewMode === 'table' ? 'active' : ''}`}
            onClick={() => setViewMode('table')}
            title="Table List View"
          >
            <TableIcon size={16} />
            <span>Table</span>
          </button>
        </div>
      </div>
    </div>
  );
}
