import React, { useState, useEffect } from 'react';
import {
  X,
  Star,
  Sparkles,
  Building,
  MapPin,
  DollarSign,
  Calendar,
  Link as LinkIcon,
  FileText,
  CheckCircle2,
  Globe,
  Building2,
} from 'lucide-react';
import {
  APPLICATION_STATUS,
  WORKPLACE_TYPES,
  EMPLOYMENT_TYPES,
  PRIORITY_CONFIG,
  cleanMeetingLink,
} from '../utils/constants';

export default function JobModal({
  isOpen,
  mode = 'create', // 'create' | 'edit'
  initialData = null,
  onClose,
  onSubmit,
  isSubmitting = false,
}) {
  const defaultState = {
    companyName: '',
    jobTitle: '',
    jobLocation: '',
    workplaceType: 'HYBRID',
    employmentType: 'FULL_TIME',
    applicationStatus: 'APPLIED',
    priority: 3,
    salaryMin: '',
    salaryMax: '',
    salaryCurrency: 'USD',
    appliedDate: new Date().toISOString().split('T')[0],
    deadlineDate: '',
    jobPostingUrl: '',
    jobDescription: '',
    notes: '',
  };

  const [formData, setFormData] = useState(defaultState);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (isOpen) {
      if (mode === 'edit' && initialData) {
        setFormData({
          companyName: initialData.companyName || '',
          jobTitle: initialData.jobTitle || '',
          jobLocation: initialData.jobLocation || '',
          workplaceType: initialData.workplaceType || 'HYBRID',
          employmentType: initialData.employmentType || 'FULL_TIME',
          applicationStatus: initialData.applicationStatus || 'APPLIED',
          priority: initialData.priority || 3,
          salaryMin: initialData.salaryMin !== null && initialData.salaryMin !== undefined ? initialData.salaryMin : '',
          salaryMax: initialData.salaryMax !== null && initialData.salaryMax !== undefined ? initialData.salaryMax : '',
          salaryCurrency: initialData.salaryCurrency || 'USD',
          appliedDate: initialData.appliedDate || '',
          deadlineDate: initialData.deadlineDate || '',
          jobPostingUrl: initialData.jobPostingUrl || '',
          jobDescription: initialData.jobDescription || '',
          notes: initialData.notes || '',
        });
      } else {
        setFormData({
          ...defaultState,
          applicationStatus: initialData?.applicationStatus || 'APPLIED',
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

  const validate = () => {
    const errs = {};
    if (!formData.companyName.trim()) {
      errs.companyName = 'Company name is required';
    }
    if (!formData.jobTitle.trim()) {
      errs.jobTitle = 'Job title is required';
    }
    if (formData.salaryMin && formData.salaryMax) {
      if (Number(formData.salaryMin) > Number(formData.salaryMax)) {
        errs.salaryMax = 'Maximum salary cannot be less than minimum salary';
      }
    }
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;

    // Clean up payload (convert numbers, trim empty strings to null where applicable)
    const payload = {
      ...formData,
      companyName: formData.companyName.trim(),
      jobTitle: formData.jobTitle.trim(),
      jobLocation: formData.jobLocation.trim() || null,
      salaryMin: formData.salaryMin !== '' ? Number(formData.salaryMin) : null,
      salaryMax: formData.salaryMax !== '' ? Number(formData.salaryMax) : null,
      appliedDate: formData.appliedDate || null,
      deadlineDate: formData.deadlineDate || null,
      jobPostingUrl: formData.jobPostingUrl.trim() ? cleanMeetingLink(formData.jobPostingUrl) : null,
      jobDescription: formData.jobDescription.trim() || null,
      notes: formData.notes.trim() || null,
    };

    onSubmit(payload, setErrors);
  };

  return (
    <div className="modal-backdrop" onClick={onClose} role="dialog" aria-modal="true">
      <div className="modal-container form-modal" onClick={(e) => e.stopPropagation()}>
        {/* Modal Header */}
        <div className="modal-header">
          <div className="modal-header-title-group">
            <div className="modal-header-badge">
              <Sparkles size={14} />
              <span>{mode === 'edit' ? 'Update Application' : 'New Application'}</span>
            </div>
            <h2 className="modal-title">
              {mode === 'edit' ? `Edit Application: ${formData.companyName || 'Job'}` : 'Track New Job Application'}
            </h2>
            <p className="modal-subtitle">
              {mode === 'edit'
                ? `Update details for ${formData.companyName || 'this opportunity'}`
                : 'Add a new career opportunity to your application tracking pipeline'}
            </p>
          </div>
          <button className="modal-close-btn" onClick={onClose} title="Close (Esc)" aria-label="Close modal">
            <X size={20} />
          </button>
        </div>

        {/* Modal Body Form */}
        <form onSubmit={handleSubmit} className="modal-form">
          <div className="modal-body form-grid">
            {/* Row 1: Company & Job Title */}
            <div className="form-group">
              <label className="form-label required">Company Name</label>
              <div className="input-wrapper">
                <input
                  type="text"
                  className={`form-input ${errors.companyName ? 'input-error' : ''}`}
                  placeholder="e.g. Google, Stripe, Microsoft"
                  value={formData.companyName}
                  onChange={(e) => handleChange('companyName', e.target.value)}
                  autoFocus
                  required
                />
              </div>
              {errors.companyName && (
                <span className="field-error-msg">{errors.companyName}</span>
              )}
            </div>

            <div className="form-group">
              <label className="form-label required">Job Title / Role</label>
              <div className="input-wrapper">
                <input
                  type="text"
                  className={`form-input ${errors.jobTitle ? 'input-error' : ''}`}
                  placeholder="e.g. Senior Full-Stack Engineer"
                  value={formData.jobTitle}
                  onChange={(e) => handleChange('jobTitle', e.target.value)}
                  required
                />
              </div>
              {errors.jobTitle && (
                <span className="field-error-msg">{errors.jobTitle}</span>
              )}
            </div>

            {/* Row 2: Location, Workplace Type, Employment Type */}
            <div className="form-group">
              <label className="form-label">
                <MapPin size={13} className="input-icon" />
                <span>Job Location</span>
              </label>
              <input
                type="text"
                className="form-input"
                placeholder="e.g. San Francisco, CA / Remote"
                value={formData.jobLocation}
                onChange={(e) => handleChange('jobLocation', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label className="form-label">Workplace Model</label>
              <select
                className="form-select"
                value={formData.workplaceType}
                onChange={(e) => handleChange('workplaceType', e.target.value)}
              >
                {Object.entries(WORKPLACE_TYPES).map(([key, config]) => (
                  <option key={key} value={key}>
                    {config.label}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">Employment Type</label>
              <select
                className="form-select"
                value={formData.employmentType}
                onChange={(e) => handleChange('employmentType', e.target.value)}
              >
                {Object.entries(EMPLOYMENT_TYPES).map(([key, config]) => (
                  <option key={key} value={key}>
                    {config.label}
                  </option>
                ))}
              </select>
            </div>

            {/* Application Status & Priority */}
            <div className="form-group">
              <label className="form-label required">Application Status</label>
              <select
                className="form-select"
                value={formData.applicationStatus}
                onChange={(e) => handleChange('applicationStatus', e.target.value)}
              >
                {Object.entries(APPLICATION_STATUS).map(([key, config]) => (
                  <option key={key} value={key}>
                    {config.label}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group span-2">
              <label className="form-label">
                <Star size={13} className="input-icon" />
                <span>Priority Level: {PRIORITY_CONFIG[formData.priority]?.label || 'Medium'} (P{formData.priority})</span>
              </label>
              <div className="priority-slider-wrapper">
                <input
                  type="range"
                  min="1"
                  max="5"
                  step="1"
                  className="priority-range-input"
                  value={formData.priority}
                  onChange={(e) => handleChange('priority', Number(e.target.value))}
                />
                <div className="priority-steps-row">
                  {[1, 2, 3, 4, 5].map((lvl) => (
                    <button
                      key={lvl}
                      type="button"
                      className={`priority-step-btn ${formData.priority === lvl ? 'active' : ''}`}
                      onClick={() => handleChange('priority', lvl)}
                      style={{
                        color: formData.priority === lvl ? PRIORITY_CONFIG[lvl]?.color : undefined,
                      }}
                    >
                      P{lvl} - {PRIORITY_CONFIG[lvl]?.label}
                    </button>
                  ))}
                </div>
              </div>
            </div>

            {/* Compensation Section */}
            <div className="form-group">
              <label className="form-label">
                <DollarSign size={13} className="input-icon" />
                <span>Minimum Salary</span>
              </label>
              <input
                type="number"
                min="0"
                step="1000"
                className="form-input"
                placeholder="e.g. 140000"
                value={formData.salaryMin}
                onChange={(e) => handleChange('salaryMin', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label className="form-label">
                <DollarSign size={13} className="input-icon" />
                <span>Maximum Salary</span>
              </label>
              <input
                type="number"
                min="0"
                step="1000"
                className={`form-input ${errors.salaryMax ? 'input-error' : ''}`}
                placeholder="e.g. 180000"
                value={formData.salaryMax}
                onChange={(e) => handleChange('salaryMax', e.target.value)}
              />
              {errors.salaryMax && (
                <span className="field-error-msg">{errors.salaryMax}</span>
              )}
            </div>

            <div className="form-group">
              <label className="form-label">Currency</label>
              <select
                className="form-select"
                value={formData.salaryCurrency}
                onChange={(e) => handleChange('salaryCurrency', e.target.value)}
              >
                <option value="USD">USD ($)</option>
                <option value="EUR">EUR (€)</option>
                <option value="GBP">GBP (£)</option>
                <option value="CAD">CAD ($)</option>
                <option value="AUD">AUD ($)</option>
                <option value="INR">INR (₹)</option>
                <option value="SGD">SGD ($)</option>
              </select>
            </div>

            {/* Timeline Dates */}
            <div className="form-group">
              <label className="form-label">
                <Calendar size={13} className="input-icon" />
                <span>Applied Date</span>
              </label>
              <input
                type="date"
                className="form-input"
                value={formData.appliedDate}
                onChange={(e) => handleChange('appliedDate', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label className="form-label">
                <Calendar size={13} className="input-icon" />
                <span>Application Deadline</span>
              </label>
              <input
                type="date"
                className="form-input"
                value={formData.deadlineDate}
                onChange={(e) => handleChange('deadlineDate', e.target.value)}
              />
            </div>

            {/* Job Posting URL */}
            <div className="form-group span-2">
              <label className="form-label">
                <LinkIcon size={13} className="input-icon" />
                <span>Job Posting URL</span>
              </label>
              <input
                type="text"
                className="form-input"
                placeholder="https://company.com/careers/role-id"
                value={formData.jobPostingUrl}
                onChange={(e) => handleChange('jobPostingUrl', e.target.value)}
              />
            </div>

            {/* Job Description */}
            <div className="form-group span-2">
              <label className="form-label">
                <FileText size={13} className="input-icon" />
                <span>Job Description / Key Requirements</span>
              </label>
              <textarea
                className="form-textarea"
                rows={3}
                placeholder="Role summary, required tech stack, engineering culture..."
                value={formData.jobDescription}
                onChange={(e) => handleChange('jobDescription', e.target.value)}
              />
            </div>

            {/* Notes */}
            <div className="form-group span-2">
              <label className="form-label">
                <FileText size={13} className="input-icon" />
                <span>Personal Notes & Referrals</span>
              </label>
              <textarea
                className="form-textarea"
                rows={3}
                placeholder="Referral contact, recruiter notes, interview timeline..."
                value={formData.notes}
                onChange={(e) => handleChange('notes', e.target.value)}
              />
            </div>
          </div>

          {/* Footer Actions */}
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
                  <span>{mode === 'edit' ? 'Save Application' : 'Track Application'}</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
