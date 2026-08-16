import React from 'react';
import {
  MapPin,
  Calendar,
  DollarSign,
  ExternalLink,
  MoreVertical,
  Edit2,
  Trash2,
  Eye,
  Star,
  Globe,
  Building2,
} from 'lucide-react';
import {
  APPLICATION_STATUS,
  WORKPLACE_TYPES,
  EMPLOYMENT_TYPES,
  PRIORITY_CONFIG,
  formatSalary,
  formatDate,
  formatRelativeTime,
  cleanMeetingLink,
} from '../utils/constants';

// Generate consistent avatar color based on company name
function getAvatarColor(company = '') {
  const colors = [
    'linear-gradient(135deg, #6366f1, #8b5cf6)',
    'linear-gradient(135deg, #3b82f6, #06b6d4)',
    'linear-gradient(135deg, #10b981, #14b8a6)',
    'linear-gradient(135deg, #f59e0b, #f97316)',
    'linear-gradient(135deg, #ec4899, #f43f5e)',
    'linear-gradient(135deg, #8b5cf6, #d946ef)',
  ];
  let hash = 0;
  for (let i = 0; i < company.length; i++) {
    hash = company.charCodeAt(i) + ((hash << 5) - hash);
  }
  return colors[Math.abs(hash) % colors.length];
}

export default function JobCard({
  job,
  onViewDetails,
  onEdit,
  onDelete,
  onQuickStatusChange,
}) {
  const statusConfig = APPLICATION_STATUS[job.applicationStatus] || APPLICATION_STATUS.APPLIED;
  const workplaceConfig = WORKPLACE_TYPES[job.workplaceType];
  const employmentConfig = EMPLOYMENT_TYPES[job.employmentType];
  const priorityConfig = PRIORITY_CONFIG[job.priority] || PRIORITY_CONFIG[3];

  const companyInitials = job.companyName
    ? job.companyName
        .split(' ')
        .map((n) => n[0])
        .slice(0, 2)
        .join('')
        .toUpperCase()
    : 'JT';

  return (
    <div
      className="job-card"
      onClick={() => onViewDetails(job)}
      onKeyDown={(e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          e.preventDefault();
          onViewDetails(job);
        }
      }}
      role="button"
      tabIndex={0}
      aria-label={`${job.jobTitle} at ${job.companyName}`}
    >
      {/* Top Card Row */}
      <div className="job-card-header">
        <div className="company-badge-row">
          <div
            className="company-avatar"
            style={{ background: getAvatarColor(job.companyName) }}
          >
            {companyInitials}
          </div>
          <div className="company-info">
            <h4 className="job-card-company">{job.companyName}</h4>
            <h3 className="job-card-title">{job.jobTitle}</h3>
          </div>
        </div>

        {/* Priority Badge */}
        <div
          className="priority-indicator"
          title={`Priority ${job.priority || 3}/5 - ${priorityConfig.label}`}
          style={{ borderColor: priorityConfig.color, color: priorityConfig.color }}
        >
          <Star size={12} fill={priorityConfig.color} />
          <span>P{job.priority || 3}</span>
        </div>
      </div>

      {/* Badges / Meta Tags */}
      <div className="job-tags-row">
        {/* Status Pill */}
        <div
          className="status-badge"
          style={{
            color: statusConfig.color,
            backgroundColor: statusConfig.bg,
            borderColor: statusConfig.border,
          }}
          onClick={(e) => e.stopPropagation()}
        >
          <span
            className="status-badge-dot"
            style={{ backgroundColor: statusConfig.color }}
          ></span>
          <select
            className="quick-status-select"
            value={job.applicationStatus}
            onChange={(e) => {
              e.stopPropagation();
              onQuickStatusChange(job.id, e.target.value);
            }}
            title="Quickly change application status"
            aria-label="Change application status"
          >
            {Object.entries(APPLICATION_STATUS).map(([key, config]) => (
              <option key={key} value={key}>
                {config.label}
              </option>
            ))}
          </select>
        </div>

        {/* Workplace Type */}
        {workplaceConfig && (
          <span className="tag-pill">
            {job.workplaceType === 'REMOTE' ? (
              <Globe size={12} />
            ) : (
              <Building2 size={12} />
            )}
            {workplaceConfig.label}
          </span>
        )}

        {/* Employment Type */}
        {employmentConfig && (
          <span className="tag-pill">{employmentConfig.label}</span>
        )}
      </div>

      {/* Location & Salary */}
      <div className="job-meta-grid">
        {job.jobLocation && (
          <div className="meta-item">
            <MapPin size={13} className="meta-icon" />
            <span className="meta-text">{job.jobLocation}</span>
          </div>
        )}

        {(job.salaryMin || job.salaryMax) && (
          <div className="meta-item salary-highlight">
            <DollarSign size={13} className="meta-icon" />
            <span className="meta-text">
              {formatSalary(job.salaryMin, job.salaryMax, job.salaryCurrency)}
            </span>
          </div>
        )}
      </div>

      {/* Description Snippet if available */}
      {job.notes && (
        <div className="job-notes-snippet" title={job.notes}>
          <span className="notes-label">Note:</span> {job.notes}
        </div>
      )}

      {/* Footer / Dates & Action Triggers */}
      <div className="job-card-footer" onClick={(e) => e.stopPropagation()}>
        <div className="job-date-info">
          <Calendar size={12} className="date-icon" />
          <span>
            {job.appliedDate ? `Applied ${formatDate(job.appliedDate)}` : 'Draft'}
          </span>
        </div>

        {/* Action Buttons */}
        <div className="job-card-actions">
          {job.jobPostingUrl && (
            <a
              href={cleanMeetingLink(job.jobPostingUrl)}
              target="_blank"
              rel="noopener noreferrer"
              className="card-action-btn"
              title="Open job posting"
              aria-label="Open job posting"
              onClick={(e) => e.stopPropagation()}
            >
              <ExternalLink size={14} />
            </a>
          )}

          <button
            className="card-action-btn"
            title="View Details"
            aria-label="View Details"
            onClick={(e) => {
              e.stopPropagation();
              onViewDetails(job);
            }}
          >
            <Eye size={14} />
          </button>

          <button
            className="card-action-btn"
            title="Edit Application"
            aria-label="Edit Application"
            onClick={(e) => {
              e.stopPropagation();
              onEdit(job);
            }}
          >
            <Edit2 size={14} />
          </button>

          <button
            className="card-action-btn delete"
            title="Delete Application"
            aria-label="Delete Application"
            onClick={(e) => {
              e.stopPropagation();
              onDelete(job);
            }}
          >
            <Trash2 size={14} />
          </button>
        </div>
      </div>
    </div>
  );
}
