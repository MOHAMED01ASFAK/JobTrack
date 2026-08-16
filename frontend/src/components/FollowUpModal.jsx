import React, { useState, useEffect } from 'react';
import {
  X,
  Calendar,
  User,
  Mail,
  FileText,
  Sparkles,
  CheckCircle2,
  Briefcase,
  Bookmark,
} from 'lucide-react';
import { FOLLOW_UP_PRESETS } from '../utils/constants';

export default function FollowUpModal({
  isOpen,
  job = null,
  jobs = [],
  onClose,
  onSubmit,
  isSubmitting = false,
}) {
  // Default due date to 3 days from now
  const getDefaultDueDate = (daysOffset = 3) => {
    const d = new Date();
    d.setDate(d.getDate() + daysOffset);
    const pad = (n) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
  };

  const defaultState = {
    jobId: job?.id || (jobs.length > 0 ? jobs[0].id : ''),
    dueDate: getDefaultDueDate(3),
    contactName: '',
    contactEmail: '',
    notes: '',
  };

  const [formData, setFormData] = useState(defaultState);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (isOpen) {
      setFormData({
        jobId: job?.id || (jobs.length > 0 ? jobs[0].id : ''),
        dueDate: getDefaultDueDate(3),
        contactName: '',
        contactEmail: '',
        notes: '',
      });
      setErrors({});
    }
  }, [isOpen, job, jobs]);

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

  if (!isOpen) return null;

  const handleChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: null }));
    }
  };

  const handleApplyPresetNote = (noteText) => {
    setFormData((prev) => ({
      ...prev,
      notes: noteText,
    }));
  };

  const setQuickDate = (daysOffset) => {
    handleChange('dueDate', getDefaultDueDate(daysOffset));
  };

  const validate = () => {
    const errs = {};
    const effectiveJobId = job?.id || formData.jobId;
    if (!effectiveJobId) {
      errs.jobId = 'Please select a job application';
    }
    if (!formData.dueDate) {
      errs.dueDate = 'Due date is required';
    }
    if (formData.contactEmail && !/\S+@\S+\.\S+/.test(formData.contactEmail)) {
      errs.contactEmail = 'Please enter a valid email address';
    }
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;

    const targetJobId = job?.id || formData.jobId;
    const payload = {
      dueDate: formData.dueDate,
      contactName: formData.contactName.trim() || null,
      contactEmail: formData.contactEmail.trim() || null,
      notes: formData.notes.trim() || null,
      isCompleted: false,
    };

    onSubmit(targetJobId, payload, setErrors);
  };

  return (
    <div className="modal-backdrop" onClick={onClose} role="dialog" aria-modal="true">
      <div
        className="modal-container form-modal followup-modal"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div className="modal-header">
          <div className="modal-header-title-group">
            <div className="modal-header-badge">
              <Sparkles size={14} />
              <span>Reminder</span>
            </div>
            <h2 className="modal-title">
              {job ? `Add Follow-Up for ${job.companyName}` : 'Create Follow-Up Reminder'}
            </h2>
            <p className="modal-subtitle">
              Set reminders to email recruiters, send thank-you notes, or check application status
            </p>
          </div>
          <button className="modal-close-btn" onClick={onClose} title="Close (Esc)" aria-label="Close modal">
            <X size={20} />
          </button>
        </div>

        {/* Form Body */}
        <form onSubmit={handleSubmit} className="modal-form">
          <div className="modal-body form-grid">
            {/* Job selector (if not pre-selected) */}
            {!job && (
              <div className="form-group span-2">
                <label className="form-label required">
                  <Briefcase size={14} className="input-icon" />
                  <span>Job Application</span>
                </label>
                <select
                  className={`form-select ${errors.jobId ? 'input-error' : ''}`}
                  value={formData.jobId}
                  onChange={(e) => handleChange('jobId', e.target.value)}
                  required
                >
                  <option value="" disabled>
                    Select a job application...
                  </option>
                  {jobs.map((j) => (
                    <option key={j.id} value={j.id}>
                      {j.companyName} — {j.jobTitle}
                    </option>
                  ))}
                </select>
                {errors.jobId && <span className="field-error-msg">{errors.jobId}</span>}
              </div>
            )}

            {/* Due Date */}
            <div className="form-group span-2">
              <div className="label-with-quick-actions">
                <label className="form-label required">
                  <Calendar size={14} className="input-icon" />
                  <span>Follow-Up Due Date</span>
                </label>
                <div className="quick-time-options">
                  <button type="button" onClick={() => setQuickDate(1)}>Tomorrow</button>
                  <button type="button" onClick={() => setQuickDate(3)}>In 3 Days</button>
                  <button type="button" onClick={() => setQuickDate(7)}>In 1 Week</button>
                </div>
              </div>
              <input
                type="date"
                className={`form-input ${errors.dueDate ? 'input-error' : ''}`}
                value={formData.dueDate}
                onChange={(e) => handleChange('dueDate', e.target.value)}
                required
              />
              {errors.dueDate && <span className="field-error-msg">{errors.dueDate}</span>}
            </div>

            {/* Contact Name */}
            <div className="form-group">
              <label className="form-label">
                <User size={14} className="input-icon" />
                <span>Contact / Recruiter Name</span>
              </label>
              <input
                type="text"
                className="form-input"
                placeholder="e.g. Sarah Jenkins"
                value={formData.contactName}
                onChange={(e) => handleChange('contactName', e.target.value)}
                maxLength={255}
              />
            </div>

            {/* Contact Email */}
            <div className="form-group">
              <label className="form-label">
                <Mail size={14} className="input-icon" />
                <span>Contact Email</span>
              </label>
              <input
                type="email"
                className={`form-input ${errors.contactEmail ? 'input-error' : ''}`}
                placeholder="e.g. sjenkins@company.com"
                value={formData.contactEmail}
                onChange={(e) => handleChange('contactEmail', e.target.value)}
                maxLength={255}
              />
              {errors.contactEmail && (
                <span className="field-error-msg">{errors.contactEmail}</span>
              )}
            </div>

            {/* Notes / Action Items */}
            <div className="form-group span-2">
              <label className="form-label">
                <FileText size={14} className="input-icon" />
                <span>Notes & Action Items</span>
              </label>
              <textarea
                className="form-textarea"
                rows={3}
                placeholder="e.g. Send thank you note after technical screen, ask for update on next round timeline..."
                value={formData.notes}
                onChange={(e) => handleChange('notes', e.target.value)}
              />
            </div>

            {/* Quick Action Presets */}
            <div className="form-group span-2 preset-bar-group">
              <label className="preset-bar-label">
                <Bookmark size={13} />
                <span>Common Action Templates:</span>
              </label>
              <div className="preset-chips-row">
                {FOLLOW_UP_PRESETS.map((presetNote) => (
                  <button
                    key={presetNote}
                    type="button"
                    className="preset-chip"
                    onClick={() => handleApplyPresetNote(presetNote)}
                  >
                    {presetNote}
                  </button>
                ))}
              </div>
            </div>
          </div>

          {/* Footer */}
          <div className="modal-footer">
            <button
              type="button"
              className="btn btn-ghost"
              onClick={onClose}
              disabled={isSubmitting}
            >
              Cancel
            </button>
            <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
              {isSubmitting ? (
                <span>Creating...</span>
              ) : (
                <>
                  <CheckCircle2 size={16} />
                  <span>Set Follow-Up Reminder</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
