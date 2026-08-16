import React from 'react';
import {
  ExternalLink,
  Edit2,
  Trash2,
  Eye,
  Star,
  Globe,
  Building2,
  MapPin,
} from 'lucide-react';
import {
  APPLICATION_STATUS,
  WORKPLACE_TYPES,
  EMPLOYMENT_TYPES,
  PRIORITY_CONFIG,
  formatSalary,
  formatDate,
  cleanMeetingLink,
} from '../utils/constants';

export default function JobTableView({
  jobs = [],
  onViewDetails,
  onEdit,
  onDelete,
  onQuickStatusChange,
}) {
  return (
    <div className="table-wrapper" role="region" aria-label="Job Applications Table">
      <table className="job-table">
        <thead>
          <tr>
            <th>Company & Role</th>
            <th>Status</th>
            <th>Workplace</th>
            <th>Priority</th>
            <th>Salary Range</th>
            <th>Applied Date</th>
            <th className="th-actions">Actions</th>
          </tr>
        </thead>
        <tbody>
          {jobs.map((job) => {
            const statusConfig =
              APPLICATION_STATUS[job.applicationStatus] || APPLICATION_STATUS.APPLIED;
            const workplaceConfig = WORKPLACE_TYPES[job.workplaceType];
            const priorityConfig = PRIORITY_CONFIG[job.priority] || PRIORITY_CONFIG[3];

            return (
              <tr
                key={job.id}
                className="table-row"
                onClick={() => onViewDetails(job)}
                onKeyDown={(e) => {
                  if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    onViewDetails(job);
                  }
                }}
                tabIndex={0}
                role="row"
                aria-label={`${job.jobTitle} at ${job.companyName}`}
              >
                {/* Company & Role */}
                <td className="td-main">
                  <div className="table-role-cell">
                    <span className="table-job-title">{job.jobTitle}</span>
                    <span className="table-company-name">
                      {job.companyName}
                      {job.jobLocation && (
                        <span className="table-location"> • {job.jobLocation}</span>
                      )}
                    </span>
                  </div>
                </td>

                {/* Status Dropdown */}
                <td onClick={(e) => e.stopPropagation()}>
                  <div
                    className="status-badge"
                    style={{
                      color: statusConfig.color,
                      backgroundColor: statusConfig.bg,
                      borderColor: statusConfig.border,
                    }}
                  >
                    <span
                      className="status-badge-dot"
                      style={{ backgroundColor: statusConfig.color }}
                    ></span>
                    <select
                      className="quick-status-select"
                      value={job.applicationStatus}
                      onChange={(e) => onQuickStatusChange(job.id, e.target.value)}
                      aria-label="Change status"
                    >
                      {Object.entries(APPLICATION_STATUS).map(([key, config]) => (
                        <option key={key} value={key}>
                          {config.label}
                        </option>
                      ))}
                    </select>
                  </div>
                </td>

                {/* Workplace */}
                <td>
                  {workplaceConfig ? (
                    <span className="tag-pill">
                      {job.workplaceType === 'REMOTE' ? (
                        <Globe size={12} />
                      ) : (
                        <Building2 size={12} />
                      )}
                      {workplaceConfig.label}
                    </span>
                  ) : (
                    <span className="table-subtle">—</span>
                  )}
                </td>

                {/* Priority */}
                <td>
                  <div
                    className="priority-indicator inline"
                    style={{ color: priorityConfig.color }}
                  >
                    <Star size={12} fill={priorityConfig.color} />
                    <span>P{job.priority || 3}</span>
                  </div>
                </td>

                {/* Salary */}
                <td className="td-salary">
                  <span className="table-salary-text">
                    {formatSalary(job.salaryMin, job.salaryMax, job.salaryCurrency)}
                  </span>
                </td>

                {/* Applied Date */}
                <td>
                  <span className="table-date">{formatDate(job.appliedDate)}</span>
                </td>

                {/* Actions */}
                <td className="td-actions" onClick={(e) => e.stopPropagation()}>
                  <div className="table-actions-row">
                    {job.jobPostingUrl && (
                      <a
                        href={cleanMeetingLink(job.jobPostingUrl)}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="card-action-btn"
                        title="Open posting URL"
                        aria-label="Open posting URL"
                        onClick={(e) => e.stopPropagation()}
                      >
                        <ExternalLink size={14} />
                      </a>
                    )}
                    <button
                      className="card-action-btn"
                      title="View Details"
                      aria-label="View Details"
                      onClick={() => onViewDetails(job)}
                    >
                      <Eye size={14} />
                    </button>
                    <button
                      className="card-action-btn"
                      title="Edit Application"
                      aria-label="Edit Application"
                      onClick={() => onEdit(job)}
                    >
                      <Edit2 size={14} />
                    </button>
                    <button
                      className="card-action-btn delete"
                      title="Delete Application"
                      aria-label="Delete Application"
                      onClick={() => onDelete(job)}
                    >
                      <Trash2 size={14} />
                    </button>
                  </div>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
