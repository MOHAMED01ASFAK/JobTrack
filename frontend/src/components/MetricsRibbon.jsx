import React from 'react';
import { Briefcase, Send, Users, Award, XCircle, DollarSign, TrendingUp, Bell } from 'lucide-react';

export default function MetricsRibbon({
  jobs = [],
  activeStatusFilter,
  onSelectStatusFilter,
  pendingFollowUpsCount = 0,
  onOpenFollowUps,
}) {
  // Calculate analytics
  const total = jobs.length;
  const applied = jobs.filter((j) => j.applicationStatus === 'APPLIED').length;
  const screening = jobs.filter((j) => j.applicationStatus === 'SCREENING').length;
  const interviewing = jobs.filter((j) => j.applicationStatus === 'INTERVIEWING').length;
  const inProgress = applied + screening + interviewing;
  const offers = jobs.filter((j) => j.applicationStatus === 'OFFER').length;
  const rejected = jobs.filter((j) => j.applicationStatus === 'REJECTED').length;

  // Calculate average target salary (midpoint if both min and max present, or whichever is available)
  const salaryValues = jobs
    .map((j) => {
      const min =
        j.salaryMin !== null &&
        j.salaryMin !== undefined &&
        j.salaryMin !== '' &&
        !isNaN(Number(j.salaryMin)) &&
        Number(j.salaryMin) > 0
          ? Number(j.salaryMin)
          : null;
      const max =
        j.salaryMax !== null &&
        j.salaryMax !== undefined &&
        j.salaryMax !== '' &&
        !isNaN(Number(j.salaryMax)) &&
        Number(j.salaryMax) > 0
          ? Number(j.salaryMax)
          : null;

      if (min !== null && max !== null) {
        return (min + max) / 2;
      }
      return max !== null ? max : min !== null ? min : null;
    })
    .filter((v) => v !== null);

  const avgSalary =
    salaryValues.length > 0
      ? Math.round(salaryValues.reduce((acc, curr) => acc + curr, 0) / salaryValues.length)
      : null;

  const formattedAvgSalary = avgSalary
    ? avgSalary >= 1000
      ? `$${(avgSalary / 1000).toFixed(avgSalary % 1000 === 0 ? 0 : 1)}k`
      : `$${avgSalary.toLocaleString()}`
    : '—';

  const metrics = [
    {
      id: 'ALL',
      title: 'Total Tracked',
      value: total,
      subtitle: `${inProgress} active pipeline`,
      icon: Briefcase,
      color: '#818cf8',
      bgGradient: 'linear-gradient(135deg, rgba(99, 102, 241, 0.15), rgba(129, 140, 248, 0.05))',
      borderColor: 'rgba(99, 102, 241, 0.3)',
      filterTarget: 'ALL',
      onClick: () => onSelectStatusFilter && onSelectStatusFilter('ALL'),
    },
    {
      id: 'INTERVIEWING',
      title: 'Interviews & Screening',
      value: interviewing + screening,
      subtitle: `${interviewing} in technical rounds`,
      icon: Users,
      color: '#60a5fa',
      bgGradient: 'linear-gradient(135deg, rgba(59, 130, 246, 0.15), rgba(96, 165, 250, 0.05))',
      borderColor: 'rgba(59, 130, 246, 0.3)',
      filterTarget: 'INTERVIEWING',
      onClick: () => onSelectStatusFilter && onSelectStatusFilter('INTERVIEWING'),
    },
    {
      id: 'FOLLOWUPS',
      title: 'Follow-Up Reminders',
      value: pendingFollowUpsCount,
      subtitle: `${pendingFollowUpsCount} pending actions`,
      icon: Bell,
      color: '#f472b6',
      bgGradient: 'linear-gradient(135deg, rgba(244, 114, 182, 0.15), rgba(236, 72, 153, 0.05))',
      borderColor: 'rgba(244, 114, 182, 0.3)',
      filterTarget: null,
      onClick: () => onOpenFollowUps && onOpenFollowUps(),
    },
    {
      id: 'OFFER',
      title: 'Offers Secured',
      value: offers,
      subtitle: total > 0 ? `${((offers / total) * 100).toFixed(0)}% conversion` : 'Ready to close',
      icon: Award,
      color: '#34d399',
      bgGradient: 'linear-gradient(135deg, rgba(16, 185, 129, 0.18), rgba(52, 211, 153, 0.06))',
      borderColor: 'rgba(16, 185, 129, 0.35)',
      filterTarget: 'OFFER',
      onClick: () => onSelectStatusFilter && onSelectStatusFilter('OFFER'),
    },
    {
      id: 'APPLIED',
      title: 'Awaiting Response',
      value: applied,
      subtitle: 'Applications submitted',
      icon: Send,
      color: '#38bdf8',
      bgGradient: 'linear-gradient(135deg, rgba(14, 165, 233, 0.15), rgba(56, 189, 248, 0.05))',
      borderColor: 'rgba(14, 165, 233, 0.3)',
      filterTarget: 'APPLIED',
      onClick: () => onSelectStatusFilter && onSelectStatusFilter('APPLIED'),
    },
    {
      id: 'SALARY',
      title: 'Avg Target Salary',
      value: formattedAvgSalary,
      subtitle: 'Based on active listings',
      icon: DollarSign,
      color: '#fbbf24',
      bgGradient: 'linear-gradient(135deg, rgba(245, 158, 11, 0.15), rgba(251, 191, 36, 0.05))',
      borderColor: 'rgba(245, 158, 11, 0.3)',
      filterTarget: null,
      onClick: null,
    },
  ];

  return (
    <div className="metrics-ribbon-grid" role="region" aria-label="Pipeline Metrics">
      {metrics.map((m) => {
        const IconComponent = m.icon;
        const isSelected = m.filterTarget && activeStatusFilter === m.filterTarget;

        return (
          <div
            key={m.id}
            className={`metric-card ${isSelected ? 'selected' : ''} ${m.onClick ? 'clickable' : ''}`}
            style={{
              background: m.bgGradient,
              borderColor: isSelected ? m.color : m.borderColor,
            }}
            onClick={() => {
              if (m.onClick) {
                m.onClick();
              }
            }}
            onKeyDown={(e) => {
              if (m.onClick && (e.key === 'Enter' || e.key === ' ')) {
                e.preventDefault();
                m.onClick();
              }
            }}
            role={m.onClick ? 'button' : undefined}
            tabIndex={m.onClick ? 0 : undefined}
            aria-label={`${m.title}: ${m.value}`}
          >
            <div className="metric-header">
              <span className="metric-title">{m.title}</span>
              <div
                className="metric-icon-box"
                style={{ color: m.color, backgroundColor: `${m.color}1a` }}
              >
                <IconComponent size={18} />
              </div>
            </div>
            <div className="metric-value-row">
              <span className="metric-value" style={{ color: isSelected ? m.color : '#f8fafc' }}>
                {m.value}
              </span>
            </div>
            <div className="metric-subtitle">{m.subtitle}</div>
          </div>
        );
      })}
    </div>
  );
}
