/**
 * Constants & Enums matching Spring Boot backend model
 */

export const APPLICATION_STATUS = {
  APPLIED: { label: 'Applied', color: '#38bdf8', bg: 'rgba(56, 189, 248, 0.12)', border: 'rgba(56, 189, 248, 0.3)' },
  SCREENING: { label: 'Screening', color: '#fbbf24', bg: 'rgba(251, 191, 36, 0.12)', border: 'rgba(251, 191, 36, 0.3)' },
  INTERVIEWING: { label: 'Interviewing', color: '#818cf8', bg: 'rgba(129, 140, 248, 0.12)', border: 'rgba(129, 140, 248, 0.3)' },
  OFFER: { label: 'Offer Received', color: '#34d399', bg: 'rgba(52, 211, 153, 0.15)', border: 'rgba(52, 211, 153, 0.35)' },
  REJECTED: { label: 'Rejected', color: '#f87171', bg: 'rgba(248, 113, 113, 0.12)', border: 'rgba(248, 113, 113, 0.3)' },
  WITHDRAWN: { label: 'Withdrawn', color: '#94a3b8', bg: 'rgba(148, 163, 184, 0.12)', border: 'rgba(148, 163, 184, 0.3)' },
};

export const WORKPLACE_TYPES = {
  REMOTE: { label: 'Remote', icon: 'Globe' },
  HYBRID: { label: 'Hybrid', icon: 'Building2' },
  ONSITE: { label: 'On-site', icon: 'MapPin' },
};

export const EMPLOYMENT_TYPES = {
  FULL_TIME: { label: 'Full-time' },
  PART_TIME: { label: 'Part-time' },
  CONTRACT: { label: 'Contract' },
  INTERNSHIP: { label: 'Internship' },
  FREELANCE: { label: 'Freelance' },
};

export const PRIORITY_CONFIG = {
  1: { label: 'Low', color: '#94a3b8' },
  2: { label: 'Medium-Low', color: '#60a5fa' },
  3: { label: 'Medium', color: '#fbbf24' },
  4: { label: 'High', color: '#f97316' },
  5: { label: 'Top Priority', color: '#ef4444' },
};

export const INTERVIEW_ROUND_TYPES = {
  HR: { label: 'HR Screening', color: '#38bdf8', icon: 'UserCheck' },
  TECHNICAL: { label: 'Technical Round', color: '#818cf8', icon: 'Code' },
  BEHAVIORAL: { label: 'Behavioral', color: '#fbbf24', icon: 'MessageCircle' },
  MANAGERIAL: { label: 'Managerial Round', color: '#c084fc', icon: 'Briefcase' },
  SYSTEM_DESIGN: { label: 'System Design', color: '#f472b6', icon: 'Cpu' },
};

export const INTERVIEW_STATUSES = {
  SCHEDULED: { label: 'Scheduled', color: '#38bdf8', bg: 'rgba(56, 189, 248, 0.12)', border: 'rgba(56, 189, 248, 0.3)' },
  COMPLETED: { label: 'Completed', color: '#34d399', bg: 'rgba(52, 211, 153, 0.15)', border: 'rgba(52, 211, 153, 0.35)' },
  PASSED: { label: 'Passed', color: '#10b981', bg: 'rgba(16, 185, 129, 0.18)', border: 'rgba(16, 185, 129, 0.35)' },
  FAILED: { label: 'Failed', color: '#f87171', bg: 'rgba(248, 113, 113, 0.12)', border: 'rgba(248, 113, 113, 0.3)' },
  RESCHEDULED: { label: 'Rescheduled', color: '#fbbf24', bg: 'rgba(251, 191, 36, 0.12)', border: 'rgba(251, 191, 36, 0.3)' },
  CANCELLED: { label: 'Cancelled', color: '#94a3b8', bg: 'rgba(148, 163, 184, 0.12)', border: 'rgba(148, 163, 184, 0.3)' },
};

/**
 * Format currency and salary range nicely (e.g. "$180k - $240k USD")
 */
export function formatSalary(min, max, currency = 'USD') {
  if (!min && !max) return 'Not specified';
  
  const fmt = (val) => {
    if (!val && val !== 0) return null;
    const num = Number(val);
    if (num >= 1000) {
      const formatted = (num / 1000).toFixed(num % 1000 === 0 ? 0 : 1);
      return `$${formatted}k`;
    }
    return `$${num.toLocaleString()}`;
  };

  const formattedMin = fmt(min);
  const formattedMax = fmt(max);

  if (formattedMin && formattedMax) {
    return `${formattedMin} - ${formattedMax} ${currency}`;
  }
  if (formattedMin) {
    return `From ${formattedMin} ${currency}`;
  }
  return `Up to ${formattedMax} ${currency}`;
}

/**
 * Format dates into friendly human-readable strings
 */
export function formatDate(dateString) {
  if (!dateString) return '—';
  try {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return dateString;
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  } catch {
    return dateString;
  }
}

/**
 * Format timestamp into friendly date and time (e.g., "Aug 20, 2026 • 2:30 PM")
 */
export function formatDateTime(dateTimeString) {
  if (!dateTimeString) return '—';
  try {
    const date = new Date(dateTimeString);
    if (isNaN(date.getTime())) return dateTimeString;
    const formattedDate = date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
    const formattedTime = date.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: '2-digit',
      hour12: true,
    });
    return `${formattedDate} • ${formattedTime}`;
  } catch {
    return dateTimeString;
  }
}

/**
 * Check if a date string is past today (for overdue follow-ups)
 */
export function isOverdue(dateString) {
  if (!dateString) return false;
  try {
    const target = new Date(dateString);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return target < today;
  } catch {
    return false;
  }
}

/**
 * Format relative time (e.g. "2 days ago", "Just now")
 */
export function formatRelativeTime(dateString) {
  if (!dateString) return '';
  try {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now - date;
    const diffSec = Math.floor(diffMs / 1000);
    if (diffDays > 30) return formatDate(dateString);
    if (diffDays > 1) return `${diffDays} days ago`;
    if (diffDays === 1) return 'Yesterday';
    if (diffHours >= 1) return `${diffHours}h ago`;
    if (diffMin >= 1) return `${diffMin}m ago`;
    return 'Just now';
  } catch {
    return '';
  }
}

export const INTERVIEW_ROUND_PRESETS = [
  { label: 'Technical Screen', type: 'TECHNICAL' },
  { label: 'System Design', type: 'SYSTEM_DESIGN' },
  { label: 'Hiring Manager Debrief', type: 'MANAGERIAL' },
  { label: 'Behavioral & Leadership', type: 'BEHAVIORAL' },
  { label: 'HR Initial Screening', type: 'HR' },
];

export const FOLLOW_UP_PRESETS = [
  'Send thank-you email after interview round',
  'Follow up on application status & next steps',
  'Submit portfolio / coding challenge assignment',
  'Confirm interview schedule and meeting platform',
  'Inquire about offer timeline and compensation package',
];

/**
 * Format and sanitize meeting URL for clickable links
 */
export function cleanMeetingLink(url) {
  if (!url) return '';
  const trimmed = url.trim();
  if (trimmed && !/^https?:\/\//i.test(trimmed)) {
    return `https://${trimmed}`;
  }
  return trimmed;
}

