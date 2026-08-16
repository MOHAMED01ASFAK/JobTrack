import React, { useState, useEffect } from 'react';
import {
  X,
  Calendar,
  Clock,
  Video,
  User,
  HelpCircle,
  FileText,
  Sparkles,
  CheckCircle2,
  Bookmark,
} from 'lucide-react';
import {
  INTERVIEW_ROUND_TYPES,
  INTERVIEW_STATUSES,
  INTERVIEW_ROUND_PRESETS,
  cleanMeetingLink,
} from '../utils/constants';

export default function InterviewModal({
  isOpen,
  mode = 'schedule', // 'schedule' | 'edit'
  initialData = null,
  job = null,
  onClose,
  onSubmit,
  isSubmitting = false,
}) {
  // Format local ISO datetime string for datetime-local input (YYYY-MM-DDTHH:mm)
  const getInitialScheduledTime = () => {
    if (initialData?.scheduledTime) {
      const d = new Date(initialData.scheduledTime);
      if (!isNaN(d.getTime())) {
        const pad = (n) => String(n).padStart(2, '0');
        return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
      }
    }
    // Default to tomorrow 10:00 AM
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    tomorrow.setHours(10, 0, 0, 0);
    const pad = (n) => String(n).padStart(2, '0');
    return `${tomorrow.getFullYear()}-${pad(tomorrow.getMonth() + 1)}-${pad(tomorrow.getDate())}T10:00`;
  };

  const defaultState = {
    roundName: '',
    roundType: 'TECHNICAL',
    scheduledTime: '',
    interviewerInfo: '',
    meetingLink: '',
    status: 'SCHEDULED',
    questionsAsked: '',
    feedbackNotes: '',
  };

  const [formData, setFormData] = useState(defaultState);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (isOpen) {
      if (mode === 'edit' && initialData) {
        setFormData({
          roundName: initialData.roundName || '',
          roundType: initialData.roundType || 'TECHNICAL',
          scheduledTime: getInitialScheduledTime(),
          interviewerInfo: initialData.interviewerInfo || '',
          meetingLink: initialData.meetingLink || '',
          status: initialData.status || 'SCHEDULED',
          questionsAsked: initialData.questionsAsked || '',
          feedbackNotes: initialData.feedbackNotes || '',
        });
      } else {
        setFormData({
          ...defaultState,
          roundName: initialData?.roundName || 'Technical Screen',
          roundType: initialData?.roundType || 'TECHNICAL',
          scheduledTime: getInitialScheduledTime(),
          status: 'SCHEDULED',
        });
      }
      setErrors({});
    }
  }, [isOpen, mode, initialData]);

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

  const handleApplyPreset = (preset) => {
    setFormData((prev) => ({
      ...prev,
      roundName: preset.label,
      roundType: preset.type,
    }));
    if (errors.roundName) {
      setErrors((prev) => ({ ...prev, roundName: null }));
    }
  };

  const setQuickTime = (daysOffset, hour = 10) => {
    const d = new Date();
    d.setDate(d.getDate() + daysOffset);
    d.setHours(hour, 0, 0, 0);
    const pad = (n) => String(n).padStart(2, '0');
    const timeStr = `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(hour)}:00`;
    handleChange('scheduledTime', timeStr);
  };

  const validate = () => {
    const errs = {};
    if (!formData.roundName.trim()) {
      errs.roundName = 'Round name is required (e.g. Technical Screen)';
    }
    if (!formData.scheduledTime) {
      errs.scheduledTime = 'Scheduled date and time is required';
    }
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;

    // Convert local datetime string to ISO 8601 string for Spring Boot backend
    let isoScheduledTime = formData.scheduledTime;
    try {
      isoScheduledTime = new Date(formData.scheduledTime).toISOString();
    } catch {
      // Keep as-is if parsing fails
    }

    const payload = {
      roundName: formData.roundName.trim(),
      roundType: formData.roundType,
      scheduledTime: isoScheduledTime,
      interviewerInfo: formData.interviewerInfo.trim() || null,
      meetingLink: formData.meetingLink ? cleanMeetingLink(formData.meetingLink) : null,
      status: formData.status,
      questionsAsked: formData.questionsAsked.trim() || null,
      feedbackNotes: formData.feedbackNotes.trim() || null,
    };

    onSubmit(payload, setErrors);
  };

  return (
    <div className="modal-backdrop" onClick={onClose} role="dialog" aria-modal="true">
      <div
        className="modal-container form-modal interview-modal"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div className="modal-header">
          <div className="modal-header-title-group">
            <div className="modal-header-badge">
              <Sparkles size={14} />
              <span>{mode === 'create' || mode === 'schedule' ? 'Schedule Interview' : 'Edit Interview'}</span>
            </div>
            <h2 className="modal-title">
              {mode === 'create' || mode === 'schedule'
                ? `Schedule Round for ${job?.companyName || 'Application'}`
                : `Update Interview: ${formData.roundName || 'Round'}`}
            </h2>
            {job && (
              <p className="modal-subtitle">
                {job.jobTitle} • {job.companyName}
              </p>
            )}
          </div>
          <button className="modal-close-btn" onClick={onClose} title="Close (Esc)" aria-label="Close modal">
            <X size={20} />
          </button>
        </div>

        {/* Form Body */}
        <form onSubmit={handleSubmit} className="modal-form">
          <div className="modal-body form-grid">
            {/* Presets Row */}
            <div className="form-group span-2 preset-bar-group">
              <label className="preset-bar-label">
                <Bookmark size={13} />
                <span>Quick Round Presets:</span>
              </label>
              <div className="preset-chips-row">
                {INTERVIEW_ROUND_PRESETS.map((preset) => (
                  <button
                    key={preset.label}
                    type="button"
                    className={`preset-chip ${formData.roundName === preset.label ? 'active' : ''}`}
                    onClick={() => handleApplyPreset(preset)}
                  >
                    {preset.label}
                  </button>
                ))}
              </div>
            </div>

            {/* Round Name & Type */}
            <div className="form-group span-2">
              <label className="form-label required">
                Round Title / Name
              </label>
              <input
                type="text"
                className={`form-input ${errors.roundName ? 'input-error' : ''}`}
                placeholder="e.g. Round 1 - System Architecture & Live Coding"
                value={formData.roundName}
                onChange={(e) => handleChange('roundName', e.target.value)}
                maxLength={255}
                required
              />
              {errors.roundName && <span className="field-error-msg">{errors.roundName}</span>}
            </div>

            {/* Round Type */}
            <div className="form-group">
              <label className="form-label required">Interview Type</label>
              <select
                className="form-select"
                value={formData.roundType}
                onChange={(e) => handleChange('roundType', e.target.value)}
              >
                {Object.entries(INTERVIEW_ROUND_TYPES).map(([key, config]) => (
                  <option key={key} value={key}>
                    {config.label}
                  </option>
                ))}
              </select>
            </div>

            {/* Status */}
            <div className="form-group">
              <label className="form-label required">Status</label>
              <select
                className="form-select"
                value={formData.status}
                onChange={(e) => handleChange('status', e.target.value)}
              >
                {Object.entries(INTERVIEW_STATUSES).map(([key, config]) => (
                  <option key={key} value={key}>
                    {config.label}
                  </option>
                ))}
              </select>
            </div>

            {/* Scheduled Date & Time */}
            <div className="form-group span-2">
              <div className="label-with-quick-actions">
                <label className="form-label required">
                  <Calendar size={14} className="input-icon" />
                  <span>Scheduled Date & Time</span>
                </label>
                <div className="quick-time-options">
                  <button type="button" onClick={() => setQuickTime(1, 10)}>Tomorrow 10 AM</button>
                  <button type="button" onClick={() => setQuickTime(2, 14)}>In 2 Days 2 PM</button>
                  <button type="button" onClick={() => setQuickTime(7, 11)}>Next Week</button>
                </div>
              </div>
              <input
                type="datetime-local"
                className={`form-input ${errors.scheduledTime ? 'input-error' : ''}`}
                value={formData.scheduledTime}
                onChange={(e) => handleChange('scheduledTime', e.target.value)}
                required
              />
              {errors.scheduledTime && (
                <span className="field-error-msg">{errors.scheduledTime}</span>
              )}
            </div>

            {/* Interviewer Info */}
            <div className="form-group">
              <label className="form-label">
                <User size={14} className="input-icon" />
                <span>Interviewer / Panel Info</span>
              </label>
              <input
                type="text"
                className="form-input"
                placeholder="e.g. Sarah Connor (Staff Architect)"
                value={formData.interviewerInfo}
                onChange={(e) => handleChange('interviewerInfo', e.target.value)}
                maxLength={255}
              />
            </div>

            {/* Meeting URL */}
            <div className="form-group">
              <label className="form-label">
                <Video size={14} className="input-icon" />
                <span>Meeting Video Link</span>
              </label>
              <input
                type="text"
                className="form-input"
                placeholder="e.g. https://meet.google.com/abc-defg-hij"
                value={formData.meetingLink}
                onChange={(e) => handleChange('meetingLink', e.target.value)}
                maxLength={1000}
              />
            </div>

            {/* Questions Asked */}
            <div className="form-group span-2">
              <label className="form-label">
                <HelpCircle size={14} className="input-icon" />
                <span>Questions Asked & Topics Covered</span>
              </label>
              <textarea
                className="form-textarea"
                rows={3}
                placeholder="Key questions, coding challenges, system design prompts..."
                value={formData.questionsAsked}
                onChange={(e) => handleChange('questionsAsked', e.target.value)}
              />
            </div>

            {/* Feedback / Notes */}
            <div className="form-group span-2">
              <label className="form-label">
                <FileText size={14} className="input-icon" />
                <span>Feedback & Performance Notes</span>
              </label>
              <textarea
                className="form-textarea"
                rows={3}
                placeholder="Strengths, follow-up areas, post-interview debrief notes..."
                value={formData.feedbackNotes}
                onChange={(e) => handleChange('feedbackNotes', e.target.value)}
              />
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
                <span>Saving...</span>
              ) : (
                <>
                  <CheckCircle2 size={16} />
                  <span>
                    {mode === 'create' || mode === 'schedule'
                      ? 'Confirm & Schedule'
                      : 'Save Changes'}
                  </span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
