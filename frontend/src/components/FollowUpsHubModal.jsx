import React, { useState, useMemo, useEffect } from 'react';
import {
  X,
  Bell,
  CheckCircle2,
  Circle,
  Plus,
  Calendar,
  User,
  Mail,
  FileText,
  AlertTriangle,
  Clock,
  Briefcase,
  ExternalLink,
  Search,
} from 'lucide-react';
import { formatDate, isOverdue } from '../utils/constants';

export default function FollowUpsHubModal({
  isOpen,
  onClose,
  followUps = [],
  onToggleFollowUp,
  onOpenCreateFollowUp,
  onViewJob,
  isLoading = false,
}) {
  const [activeTab, setActiveTab] = useState('pending'); // 'pending' | 'completed' | 'all'
  const [searchTerm, setSearchTerm] = useState('');

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

  const pendingList = useMemo(
    () => followUps.filter((f) => !f.isCompleted),
    [followUps]
  );
  const completedList = useMemo(
    () => followUps.filter((f) => f.isCompleted),
    [followUps]
  );

  const displayedList = useMemo(() => {
    let list = [];
    if (activeTab === 'pending') list = pendingList;
    else if (activeTab === 'completed') list = completedList;
    else list = followUps;

    // Filter by search term
    if (searchTerm.trim()) {
      const q = searchTerm.toLowerCase().trim();
      list = list.filter(
        (f) =>
          (f.companyName && f.companyName.toLowerCase().includes(q)) ||
          (f.jobApplicationCompanyName && f.jobApplicationCompanyName.toLowerCase().includes(q)) ||
          (f.jobTitle && f.jobTitle.toLowerCase().includes(q)) ||
          (f.contactName && f.contactName.toLowerCase().includes(q)) ||
          (f.contactEmail && f.contactEmail.toLowerCase().includes(q)) ||
          (f.notes && f.notes.toLowerCase().includes(q))
      );
    }

    // Sort pending items: oldest due date first (overdue items at the top)
    // Sort completed items: newest completed first
    return [...list].sort((a, b) => {
      if (!a.isCompleted && !b.isCompleted) {
        return (a.dueDate || '').localeCompare(b.dueDate || '');
      }
      return (b.updatedAt || '').localeCompare(a.updatedAt || '');
    });
  }, [activeTab, pendingList, completedList, followUps, searchTerm]);

  if (!isOpen) return null;

  const isDueToday = (dateString) => {
    if (!dateString) return false;
    try {
      const today = new Date().toISOString().split('T')[0];
      return dateString === today;
    } catch {
      return false;
    }
  };

  return (
    <div className="modal-backdrop" onClick={onClose} role="dialog" aria-modal="true">
      <div
        className="modal-container followups-hub-modal"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div className="modal-header">
          <div className="modal-header-title-group">
            <div className="analytics-badge-row">
              <div className="followup-header-icon">
                <Bell size={18} />
              </div>
              <h2 className="modal-title">Follow-Up Reminders Hub</h2>
            </div>
            <p className="modal-subtitle">
              Never miss a recruiter check-in, thank-you note, or application deadline
            </p>
          </div>

          <div className="analytics-header-actions">
            <button
              className="btn btn-primary create-job-btn"
              onClick={onOpenCreateFollowUp}
            >
              <Plus size={16} />
              <span>New Follow-Up</span>
            </button>
            <button className="modal-close-btn" onClick={onClose} title="Close (Esc)" aria-label="Close modal">
              <X size={20} />
            </button>
          </div>
        </div>

        {/* Tab Filters & Search Bar */}
        <div className="followups-hub-toolbar">
          <div className="followup-tabs-bar">
            <button
              className={`followup-tab-btn ${activeTab === 'pending' ? 'active' : ''}`}
              onClick={() => setActiveTab('pending')}
            >
              <span>Pending Reminders</span>
              <span className="tab-count-badge">{pendingList.length}</span>
            </button>
            <button
              className={`followup-tab-btn ${activeTab === 'completed' ? 'active' : ''}`}
              onClick={() => setActiveTab('completed')}
            >
              <span>Completed</span>
              <span className="tab-count-badge secondary">{completedList.length}</span>
            </button>
            <button
              className={`followup-tab-btn ${activeTab === 'all' ? 'active' : ''}`}
              onClick={() => setActiveTab('all')}
            >
              <span>All Follow-Ups</span>
              <span className="tab-count-badge neutral">{followUps.length}</span>
            </button>
          </div>

          {/* Quick Search */}
          <div className="followup-search-box">
            <Search size={14} className="search-icon" />
            <input
              type="text"
              className="followup-search-input"
              placeholder="Filter by company, recruiter, notes..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            {searchTerm && (
              <button
                className="search-clear-btn"
                onClick={() => setSearchTerm('')}
                title="Clear search"
              >
                ×
              </button>
            )}
          </div>
        </div>

        {/* Modal Body */}
        <div className="followups-body">
          {displayedList.length === 0 ? (
            <div className="followups-empty-state">
              <div className="empty-state-icon-box">
                {activeTab === 'pending' ? <CheckCircle2 size={36} color="#34d399" /> : <Bell size={36} color="#818cf8" />}
              </div>
              <h3 className="empty-state-title">
                {searchTerm
                  ? 'No matching follow-ups found'
                  : activeTab === 'pending'
                  ? 'All caught up!'
                  : activeTab === 'completed'
                  ? 'No completed follow-ups yet'
                  : 'No follow-up reminders recorded'}
              </h3>
              <p className="empty-state-desc">
                {searchTerm
                  ? `No follow-up tasks match "${searchTerm}". Try a different keyword.`
                  : activeTab === 'pending'
                  ? 'You have zero pending recruiter follow-ups. Great job staying on top of your job hunt!'
                  : 'Stay proactive by setting reminder tasks for your applications.'}
              </p>
              {activeTab === 'pending' && !searchTerm && (
                <button
                  className="btn btn-primary"
                  onClick={onOpenCreateFollowUp}
                  style={{ marginTop: '12px' }}
                >
                  <Plus size={16} />
                  <span>Set a Reminder</span>
                </button>
              )}
            </div>
          ) : (
            <div className="followups-list">
              {displayedList.map((item) => {
                const overdue = !item.isCompleted && isOverdue(item.dueDate);
                const dueToday = !item.isCompleted && isDueToday(item.dueDate);

                return (
                  <div
                    key={item.id}
                    className={`followup-card ${item.isCompleted ? 'completed' : ''} ${overdue ? 'overdue' : ''}`}
                  >
                    {/* Checkbox Toggle Button */}
                    <button
                      className="followup-check-btn"
                      onClick={() => onToggleFollowUp(item.id)}
                      title={
                        item.isCompleted
                          ? 'Mark as pending'
                          : 'Mark as completed'
                      }
                      aria-label={item.isCompleted ? 'Mark as pending' : 'Mark as completed'}
                    >
                      {item.isCompleted ? (
                        <CheckCircle2 size={22} className="check-icon-completed" />
                      ) : (
                        <Circle size={22} className="check-icon-pending" />
                      )}
                    </button>

                    {/* Follow-up Details */}
                    <div className="followup-card-content">
                      <div className="followup-top-row">
                        {/* Parent Job Tag */}
                        {(item.companyName || item.jobApplicationCompanyName) && (
                          <div
                            className="followup-job-tag clickable"
                            onClick={() =>
                              onViewJob && onViewJob(item.jobApplicationId)
                            }
                            title="Click to view application details"
                          >
                            <Briefcase size={12} />
                            <span className="company-bold">
                              {item.companyName || item.jobApplicationCompanyName}
                            </span>
                            {(item.jobTitle || item.jobApplicationJobTitle) && (
                              <span className="job-role-sub">
                                • {item.jobTitle || item.jobApplicationJobTitle}
                              </span>
                            )}
                            <ExternalLink size={11} className="tag-link-icon" />
                          </div>
                        )}

                        {/* Due Date Badge */}
                        <div
                          className={`followup-due-badge ${
                            item.isCompleted
                              ? 'badge-done'
                              : overdue
                              ? 'badge-overdue'
                              : dueToday
                              ? 'badge-today'
                              : 'badge-upcoming'
                          }`}
                        >
                          {overdue ? (
                            <AlertTriangle size={12} />
                          ) : (
                            <Calendar size={12} />
                          )}
                          <span>
                            {item.isCompleted
                              ? `Completed`
                              : overdue
                              ? `Overdue (${formatDate(item.dueDate)})`
                              : dueToday
                              ? `Due Today`
                              : `Due ${formatDate(item.dueDate)}`}
                          </span>
                        </div>
                      </div>

                      {/* Contact Info */}
                      {(item.contactName || item.contactEmail) && (
                        <div className="followup-contact-row">
                          {item.contactName && (
                            <div className="contact-item">
                              <User size={13} className="contact-icon" />
                              <span className="contact-name">
                                {item.contactName}
                              </span>
                            </div>
                          )}
                          {item.contactEmail && (
                            <a
                              href={`mailto:${item.contactEmail}`}
                              className="contact-item email-link"
                              title={`Send email to ${item.contactEmail}`}
                            >
                              <Mail size={13} className="contact-icon" />
                              <span>{item.contactEmail}</span>
                            </a>
                          )}
                        </div>
                      )}

                      {/* Notes / Action Items */}
                      {item.notes && (
                        <div className="followup-notes-box">
                          <FileText size={13} className="notes-icon" />
                          <p className="followup-notes-text">{item.notes}</p>
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="modal-footer">
          <div className="footer-status-summary">
            <span>
              {pendingList.length} pending • {completedList.length} completed
            </span>
          </div>
          <button className="btn btn-ghost" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
}
