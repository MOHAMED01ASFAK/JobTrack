import React, { useState, useEffect } from 'react';
import {
  X,
  Edit2,
  Trash2,
  ExternalLink,
  MapPin,
  Calendar,
  DollarSign,
  Clock,
  Briefcase,
  Star,
  Globe,
  Building2,
  FileText,
  MessageSquare,
  Video,
  Plus,
  User,
  Mail,
  HelpCircle,
  CheckCircle2,
  Circle,
  AlertTriangle,
  Users,
  Bell,
  Copy,
  Check,
} from 'lucide-react';
import {
  APPLICATION_STATUS,
  WORKPLACE_TYPES,
  EMPLOYMENT_TYPES,
  PRIORITY_CONFIG,
  INTERVIEW_ROUND_TYPES,
  INTERVIEW_STATUSES,
  formatSalary,
  formatDate,
  formatDateTime,
  formatRelativeTime,
  isOverdue,
  cleanMeetingLink,
} from '../utils/constants';

export default function JobDetailModal({
  isOpen,
  job,
  onClose,
  onEdit,
  onDelete,
  interviews = [],
  followUps = [],
  onScheduleInterview,
  onEditInterview,
  onDeleteInterview,
  onAddFollowUp,
  onToggleFollowUp,
}) {
  const [activeTab, setActiveTab] = useState('overview'); // 'overview' | 'interviews' | 'followups'
  const [copiedLink, setCopiedLink] = useState(null);

  // Global Escape key support
  useEffect(() => {
    const handleKeyDown = (e) => {
      if (e.key === 'Escape' && isOpen) {
        onClose();
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, onClose]);

  if (!isOpen || !job) return null;

  const statusConfig =
    APPLICATION_STATUS[job.applicationStatus] || APPLICATION_STATUS.APPLIED;
  const workplaceConfig = WORKPLACE_TYPES[job.workplaceType];
  const employmentConfig = EMPLOYMENT_TYPES[job.employmentType];
  const priorityConfig = PRIORITY_CONFIG[job.priority] || PRIORITY_CONFIG[3];

  // Filter job-specific follow-ups if passed a global list or use directly
  const jobFollowUps = followUps.filter((f) => f.jobApplicationId === job.id || !f.jobApplicationId);

  const handleCopyMeetingLink = (link) => {
    if (!link) return;
    navigator.clipboard.writeText(cleanMeetingLink(link));
    setCopiedLink(link);
    setTimeout(() => setCopiedLink(null), 2500);
  };

  return (
    <div className="modal-backdrop" onClick={onClose} role="dialog" aria-modal="true">
      <div
        className="modal-container detail-modal"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div className="modal-header">
          <div className="modal-header-title-group">
            <div className="detail-company-row">
              <span className="detail-company-name">{job.companyName}</span>
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
                <span>{statusConfig.label}</span>
              </div>
            </div>
            <h2 className="detail-job-title">{job.jobTitle}</h2>
          </div>
          <div className="detail-header-actions">
            <button
              className="icon-btn"
              title="Edit Application"
              onClick={() => onEdit(job)}
              aria-label="Edit Application"
            >
              <Edit2 size={16} />
            </button>
            <button
              className="icon-btn delete-btn"
              title="Delete Application"
              onClick={() => onDelete(job)}
              aria-label="Delete Application"
            >
              <Trash2 size={16} />
            </button>
            <button className="modal-close-btn" onClick={onClose} title="Close (Esc)" aria-label="Close modal">
              <X size={20} />
            </button>
          </div>
        </div>

        {/* Sub-Tabs Bar */}
        <div className="detail-tabs-bar">
          <button
            className={`detail-tab-btn ${activeTab === 'overview' ? 'active' : ''}`}
            onClick={() => setActiveTab('overview')}
          >
            <Briefcase size={14} />
            <span>Overview</span>
          </button>
          <button
            className={`detail-tab-btn ${activeTab === 'interviews' ? 'active' : ''}`}
            onClick={() => setActiveTab('interviews')}
          >
            <Users size={14} />
            <span>Interviews</span>
            <span className="tab-count-badge">{interviews.length}</span>
          </button>
          <button
            className={`detail-tab-btn ${activeTab === 'followups' ? 'active' : ''}`}
            onClick={() => setActiveTab('followups')}
          >
            <Bell size={14} />
            <span>Follow-Ups</span>
            <span className="tab-count-badge secondary">{jobFollowUps.length}</span>
          </button>
        </div>

        {/* Modal Body */}
        <div className="detail-body">
          {/* TAB 1: OVERVIEW */}
          {activeTab === 'overview' && (
            <>
              {/* Quick Info Grid */}
              <div className="detail-info-grid">
                {/* Compensation Card */}
                <div className="detail-card">
                  <div className="detail-card-label">
                    <DollarSign size={15} />
                    <span>Compensation</span>
                  </div>
                  <div className="detail-card-value highlight">
                    {formatSalary(job.salaryMin, job.salaryMax, job.salaryCurrency)}
                  </div>
                </div>

                {/* Workplace & Location */}
                <div className="detail-card">
                  <div className="detail-card-label">
                    <MapPin size={15} />
                    <span>Location & Model</span>
                  </div>
                  <div className="detail-card-value">
                    {job.jobLocation || 'Not specified'}{' '}
                    {workplaceConfig && (
                      <span className="subtle-badge">({workplaceConfig.label})</span>
                    )}
                  </div>
                </div>

                {/* Employment Type & Priority */}
                <div className="detail-card">
                  <div className="detail-card-label">
                    <Briefcase size={15} />
                    <span>Employment & Priority</span>
                  </div>
                  <div className="detail-card-value">
                    {employmentConfig?.label || 'Full-time'} •{' '}
                    <span style={{ color: priorityConfig.color }}>
                      Priority {job.priority || 3} ({priorityConfig.label})
                    </span>
                  </div>
                </div>

                {/* Key Dates */}
                <div className="detail-card">
                  <div className="detail-card-label">
                    <Calendar size={15} />
                    <span>Dates & Timeline</span>
                  </div>
                  <div className="detail-card-value">
                    Applied: {formatDate(job.appliedDate)}
                    {job.deadlineDate && (
                      <span className="deadline-text">
                        {' '}
                        • Deadline: {formatDate(job.deadlineDate)}
                      </span>
                    )}
                  </div>
                </div>
              </div>

              {/* Job Posting Link */}
              {job.jobPostingUrl && (
                <div className="detail-section">
                  <div className="section-title-row">
                    <ExternalLink size={16} className="section-icon" />
                    <h4 className="detail-section-title">Job Posting URL</h4>
                  </div>
                  <a
                    href={cleanMeetingLink(job.jobPostingUrl)}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="external-link-box"
                  >
                    <span className="link-url">{job.jobPostingUrl}</span>
                    <span className="link-action-tag">Open Link ↗</span>
                  </a>
                </div>
              )}

              {/* Job Description */}
              {job.jobDescription && (
                <div className="detail-section">
                  <div className="section-title-row">
                    <FileText size={16} className="section-icon" />
                    <h4 className="detail-section-title">Role Summary</h4>
                  </div>
                  <div className="detail-text-box">{job.jobDescription}</div>
                </div>
              )}

              {/* Notes & Interview Logs */}
              {job.notes && (
                <div className="detail-section">
                  <div className="section-title-row">
                    <MessageSquare size={16} className="section-icon" />
                    <h4 className="detail-section-title">General Notes</h4>
                  </div>
                  <div className="detail-text-box notes-box">{job.notes}</div>
                </div>
              )}

              {/* Audit Timestamps */}
              <div className="detail-audit-footer">
                <Clock size={13} />
                <span>
                  Recorded on {formatDate(job.createdAt)} • Last updated{' '}
                  {formatDate(job.updatedAt)}
                </span>
              </div>
            </>
          )}

          {/* TAB 2: INTERVIEWS */}
          {activeTab === 'interviews' && (
            <div className="interviews-tab-content">
              <div className="tab-actions-header">
                <div>
                  <h3 className="tab-heading">Interview Rounds & Schedule</h3>
                  <p className="tab-subheading">
                    Track rounds, technical questions, meeting links, and feedback notes
                  </p>
                </div>
                <button
                  className="btn btn-primary"
                  onClick={() => onScheduleInterview && onScheduleInterview(job)}
                >
                  <Plus size={15} />
                  <span>Schedule Interview</span>
                </button>
              </div>

              {interviews.length === 0 ? (
                <div className="detail-empty-tab">
                  <Users size={32} className="empty-tab-icon" />
                  <h4>No Interview Rounds Scheduled</h4>
                  <p>
                    Track your upcoming technical screenings, behavioral rounds, and manager debriefs.
                  </p>
                  <button
                    className="btn btn-primary"
                    onClick={() => onScheduleInterview && onScheduleInterview(job)}
                  >
                    <Plus size={15} />
                    <span>Schedule First Round</span>
                  </button>
                </div>
              ) : (
                <div className="interviews-timeline-list">
                  {interviews.map((iv, idx) => {
                    const roundTypeConf =
                      INTERVIEW_ROUND_TYPES[iv.roundType] ||
                      INTERVIEW_ROUND_TYPES.TECHNICAL;
                    const statusConf =
                      INTERVIEW_STATUSES[iv.status] ||
                      INTERVIEW_STATUSES.SCHEDULED;

                    return (
                      <div key={iv.id} className="interview-card">
                        <div className="interview-card-header">
                          <div className="interview-round-info">
                            <span className="interview-round-number">
                              Round {idx + 1}
                            </span>
                            <h4 className="interview-round-title">
                              {iv.roundName}
                            </h4>
                            <div className="interview-badges-row">
                              {/* Round Type Pill */}
                              <span
                                className="interview-type-badge"
                                style={{
                                  color: roundTypeConf.color,
                                  borderColor: `${roundTypeConf.color}40`,
                                  backgroundColor: `${roundTypeConf.color}15`,
                                }}
                              >
                                {roundTypeConf.label}
                              </span>

                              {/* Status Pill */}
                              <span
                                className="status-badge"
                                style={{
                                  color: statusConf.color,
                                  backgroundColor: statusConf.bg,
                                  borderColor: statusConf.border,
                                }}
                              >
                                <span
                                  className="status-badge-dot"
                                  style={{ backgroundColor: statusConf.color }}
                                ></span>
                                <span>{statusConf.label}</span>
                              </span>
                            </div>
                          </div>

                          <div className="interview-header-actions">
                            <button
                              className="icon-btn"
                              title="Edit Interview Round"
                              onClick={() =>
                                onEditInterview && onEditInterview(iv, job)
                              }
                              aria-label="Edit Interview"
                            >
                              <Edit2 size={14} />
                            </button>
                            <button
                              className="icon-btn delete-btn"
                              title="Delete Interview Round"
                              onClick={() =>
                                onDeleteInterview && onDeleteInterview(iv.id)
                              }
                              aria-label="Delete Interview"
                            >
                              <Trash2 size={14} />
                            </button>
                          </div>
                        </div>

                        {/* Interview Details Grid */}
                        <div className="interview-meta-row">
                          <div className="interview-meta-item">
                            <Calendar size={13} className="meta-icon" />
                            <span>{formatDateTime(iv.scheduledTime)}</span>
                          </div>

                          {iv.interviewerInfo && (
                            <div className="interview-meta-item">
                              <User size={13} className="meta-icon" />
                              <span>{iv.interviewerInfo}</span>
                            </div>
                          )}

                          {iv.meetingLink && (
                            <div className="meeting-actions-group">
                              <a
                                href={cleanMeetingLink(iv.meetingLink)}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="interview-meeting-btn"
                              >
                                <Video size={13} />
                                <span>Join Meeting ↗</span>
                              </a>
                              <button
                                type="button"
                                className="meeting-copy-btn"
                                onClick={() => handleCopyMeetingLink(iv.meetingLink)}
                                title="Copy meeting link to clipboard"
                              >
                                {copiedLink === iv.meetingLink ? (
                                  <>
                                    <Check size={13} color="#34d399" />
                                    <span style={{ color: '#34d399' }}>Copied</span>
                                  </>
                                ) : (
                                  <>
                                    <Copy size={13} />
                                    <span>Copy</span>
                                  </>
                                )}
                              </button>
                            </div>
                          )}
                        </div>

                        {/* Questions Asked */}
                        {iv.questionsAsked && (
                          <div className="interview-notes-section">
                            <div className="notes-section-label">
                              <HelpCircle size={13} />
                              <span>Questions Asked & Challenges</span>
                            </div>
                            <p className="interview-notes-text">
                              {iv.questionsAsked}
                            </p>
                          </div>
                        )}

                        {/* Feedback Notes */}
                        {iv.feedbackNotes && (
                          <div className="interview-notes-section feedback">
                            <div className="notes-section-label">
                              <FileText size={13} />
                              <span>Feedback & Debrief Notes</span>
                            </div>
                            <p className="interview-notes-text">
                              {iv.feedbackNotes}
                            </p>
                          </div>
                        )}
                      </div>
                    );
                  })}
                </div>
              )}
            </div>
          )}

          {/* TAB 3: FOLLOW-UPS */}
          {activeTab === 'followups' && (
            <div className="followups-tab-content">
              <div className="tab-actions-header">
                <div>
                  <h3 className="tab-heading">Application Follow-Up Reminders</h3>
                  <p className="tab-subheading">
                    Keep track of thank-you emails and recruiter check-ins
                  </p>
                </div>
                <button
                  className="btn btn-primary"
                  onClick={() => onAddFollowUp && onAddFollowUp(job)}
                >
                  <Plus size={15} />
                  <span>Add Reminder</span>
                </button>
              </div>

              {jobFollowUps.length === 0 ? (
                <div className="detail-empty-tab">
                  <Bell size={32} className="empty-tab-icon" />
                  <h4>No Follow-Up Reminders Set</h4>
                  <p>
                    Set reminders to email the recruiter or check in on interview feedback.
                  </p>
                  <button
                    className="btn btn-primary"
                    onClick={() => onAddFollowUp && onAddFollowUp(job)}
                  >
                    <Plus size={15} />
                    <span>Create Reminder</span>
                  </button>
                </div>
              ) : (
                <div className="followups-list">
                  {jobFollowUps.map((fu) => {
                    const overdue = !fu.isCompleted && isOverdue(fu.dueDate);

                    return (
                      <div
                        key={fu.id}
                        className={`followup-card ${fu.isCompleted ? 'completed' : ''} ${overdue ? 'overdue' : ''}`}
                      >
                        <button
                          className="followup-check-btn"
                          onClick={() =>
                            onToggleFollowUp && onToggleFollowUp(fu.id)
                          }
                          title={
                            fu.isCompleted
                              ? 'Mark as pending'
                              : 'Mark as completed'
                          }
                          aria-label={fu.isCompleted ? 'Mark as pending' : 'Mark as completed'}
                        >
                          {fu.isCompleted ? (
                            <CheckCircle2
                              size={22}
                              className="check-icon-completed"
                            />
                          ) : (
                            <Circle size={22} className="check-icon-pending" />
                          )}
                        </button>

                        <div className="followup-card-content">
                          <div className="followup-top-row">
                            <div
                              className={`followup-due-badge ${
                                fu.isCompleted
                                  ? 'badge-done'
                                  : overdue
                                  ? 'badge-overdue'
                                  : 'badge-upcoming'
                              }`}
                            >
                              {overdue ? (
                                <AlertTriangle size={12} />
                              ) : (
                                <Calendar size={12} />
                              )}
                              <span>
                                {fu.isCompleted
                                  ? `Completed`
                                  : overdue
                                  ? `Overdue (${formatDate(fu.dueDate)})`
                                  : `Due ${formatDate(fu.dueDate)}`}
                              </span>
                            </div>
                          </div>

                          {(fu.contactName || fu.contactEmail) && (
                            <div className="followup-contact-row">
                              {fu.contactName && (
                                <div className="contact-item">
                                  <User size={13} className="contact-icon" />
                                  <span className="contact-name">
                                    {fu.contactName}
                                  </span>
                                </div>
                              )}
                              {fu.contactEmail && (
                                <a
                                  href={`mailto:${fu.contactEmail}`}
                                  className="contact-item email-link"
                                  title={`Send email to ${fu.contactEmail}`}
                                >
                                  <Mail size={13} className="contact-icon" />
                                  <span>{fu.contactEmail}</span>
                                </a>
                              )}
                            </div>
                          )}

                          {fu.notes && (
                            <div className="followup-notes-box">
                              <FileText size={13} className="notes-icon" />
                              <p className="followup-notes-text">{fu.notes}</p>
                            </div>
                          )}
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </div>
          )}
        </div>

        {/* Modal Footer */}
        <div className="modal-footer">
          <button className="btn btn-ghost" onClick={onClose}>
            Close
          </button>
          <button className="btn btn-primary" onClick={() => onEdit(job)}>
            <Edit2 size={16} />
            <span>Edit Application</span>
          </button>
        </div>
      </div>
    </div>
  );
}
