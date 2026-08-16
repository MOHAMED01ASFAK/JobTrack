import React, { useEffect } from 'react';
import { AlertTriangle, X } from 'lucide-react';

export default function DeleteConfirmModal({
  isOpen,
  job,
  onClose,
  onConfirm,
  isDeleting = false,
}) {
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

  return (
    <div className="modal-backdrop" onClick={onClose} role="dialog" aria-modal="true">
      <div className="modal-container delete-modal" onClick={(e) => e.stopPropagation()}>
        <div className="delete-modal-content">
          <div className="delete-icon-wrapper">
            <AlertTriangle size={28} className="delete-warning-icon" />
          </div>
          <h3 className="delete-modal-title">Delete Job Application?</h3>
          <p className="delete-modal-text">
            Are you sure you want to permanently remove the application for{' '}
            <strong className="delete-highlight">{job.jobTitle}</strong> at{' '}
            <strong className="delete-highlight">{job.companyName}</strong>?
          </p>
          <p className="delete-subtext">This action cannot be undone and will delete all linked interviews & follow-ups.</p>
        </div>

        <div className="modal-footer delete-footer">
          <button
            type="button"
            className="btn btn-ghost"
            onClick={onClose}
            disabled={isDeleting}
          >
            Cancel
          </button>
          <button
            type="button"
            className="btn btn-danger"
            onClick={() => onConfirm(job.id)}
            disabled={isDeleting}
          >
            {isDeleting ? 'Deleting...' : 'Delete Application'}
          </button>
        </div>
      </div>
    </div>
  );
}
