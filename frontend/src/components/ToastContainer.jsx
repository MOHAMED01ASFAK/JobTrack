import React from 'react';
import { CheckCircle2, AlertCircle, Info, X } from 'lucide-react';

export default function ToastContainer({ toasts = [], onDismiss }) {
  if (!toasts || toasts.length === 0) return null;

  return (
    <div className="toast-container">
      {toasts.map((toast) => {
        let Icon = CheckCircle2;
        let toastClass = 'toast-success';

        if (toast.type === 'error') {
          Icon = AlertCircle;
          toastClass = 'toast-error';
        } else if (toast.type === 'info') {
          Icon = Info;
          toastClass = 'toast-info';
        }

        return (
          <div key={toast.id} className={`toast-item ${toastClass}`}>
            <div className="toast-icon-wrapper">
              <Icon size={18} />
            </div>
            <div className="toast-message">{toast.message}</div>
            <button
              className="toast-close-btn"
              onClick={() => onDismiss(toast.id)}
              title="Dismiss"
            >
              <X size={14} />
            </button>
          </div>
        );
      })}
    </div>
  );
}
