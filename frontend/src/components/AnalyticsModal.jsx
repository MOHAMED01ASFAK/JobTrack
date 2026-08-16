import React, { useEffect } from 'react';
import {
  X,
  TrendingUp,
  Download,
  Award,
  Users,
  Briefcase,
  DollarSign,
  Globe,
  Building2,
  MapPin,
  Calendar,
  CheckCircle2,
} from 'lucide-react';
import { formatSalary } from '../utils/constants';

export default function AnalyticsModal({
  isOpen,
  analyticsData,
  onClose,
  onExportCsv,
  isExporting = false,
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

  if (!isOpen || !analyticsData) return null;

  const {
    totalApplications = 0,
    activeApplications = 0,
    interviewRatePercentage = 0,
    offerRatePercentage = 0,
    minSalary,
    maxSalary,
    avgSalary,
    statusBreakdown = {},
    workplaceBreakdown = {},
    employmentBreakdown = {},
    monthlyTrends = {},
  } = analyticsData;

  const appliedCount = statusBreakdown['APPLIED'] || 0;
  const screeningCount = statusBreakdown['SCREENING'] || 0;
  const interviewingCount = statusBreakdown['INTERVIEWING'] || 0;
  const offerCount = statusBreakdown['OFFER'] || 0;
  const rejectedCount = statusBreakdown['REJECTED'] || 0;
  const withdrawnCount = statusBreakdown['WITHDRAWN'] || 0;

  // Max count for scaling funnel bars
  const maxFunnel = Math.max(totalApplications, 1);

  return (
    <div className="modal-backdrop" onClick={onClose} role="dialog" aria-modal="true">
      <div className="modal-container analytics-modal" onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="modal-header">
          <div className="modal-header-title-group">
            <div className="analytics-badge-row">
              <div className="analytics-header-icon">
                <TrendingUp size={18} />
              </div>
              <h2 className="modal-title">Career Pipeline Analytics</h2>
            </div>
            <p className="modal-subtitle">
              Real-time insights, conversion funnels, and compensation benchmarks
            </p>
          </div>

          <div className="analytics-header-actions">
            <button
              className="btn btn-ghost export-csv-btn"
              onClick={onExportCsv}
              disabled={isExporting || totalApplications === 0}
              title="Export all data to CSV"
            >
              <Download size={15} />
              <span>{isExporting ? 'Exporting...' : 'Export CSV'}</span>
            </button>
            <button className="modal-close-btn" onClick={onClose} title="Close (Esc)" aria-label="Close modal">
              <X size={20} />
            </button>
          </div>
        </div>

        {/* Modal Body */}
        <div className="analytics-body">
          {/* 4 High-Level Metric Tiles */}
          <div className="analytics-stats-grid">
            <div className="analytics-stat-tile">
              <span className="stat-tile-label">Total Applications</span>
              <span className="stat-tile-number">{totalApplications}</span>
              <span className="stat-tile-sub">{activeApplications} active pipeline</span>
            </div>

            <div className="analytics-stat-tile">
              <span className="stat-tile-label">Interview Rate</span>
              <span className="stat-tile-number" style={{ color: '#60a5fa' }}>
                {interviewRatePercentage}%
              </span>
              <span className="stat-tile-sub">Passed to screen / technical</span>
            </div>

            <div className="analytics-stat-tile">
              <span className="stat-tile-label">Offer Rate</span>
              <span className="stat-tile-number" style={{ color: '#34d399' }}>
                {offerRatePercentage}%
              </span>
              <span className="stat-tile-sub">{offerCount} total offers</span>
            </div>

            <div className="analytics-stat-tile">
              <span className="stat-tile-label">Average Salary</span>
              <span className="stat-tile-number" style={{ color: '#fbbf24' }}>
                {avgSalary ? `$${(avgSalary / 1000).toFixed(avgSalary % 1000 === 0 ? 0 : 1)}k` : '—'}
              </span>
              <span className="stat-tile-sub">Across target roles</span>
            </div>
          </div>

          {/* Section: Career Funnel */}
          <div className="analytics-section">
            <h4 className="analytics-section-title">Application Progression Funnel</h4>
            <div className="funnel-container">
              {/* Funnel Stage 1: Applied */}
              <div className="funnel-stage">
                <div className="funnel-stage-info">
                  <span className="stage-name">Applied</span>
                  <span className="stage-count">{totalApplications} total</span>
                </div>
                <div className="funnel-bar-wrapper">
                  <div
                    className="funnel-bar applied"
                    style={{ width: `${(totalApplications / maxFunnel) * 100}%` }}
                  ></div>
                </div>
              </div>

              {/* Funnel Stage 2: Screening */}
              <div className="funnel-stage">
                <div className="funnel-stage-info">
                  <span className="stage-name">Screening</span>
                  <span className="stage-count">
                    {screeningCount + interviewingCount + offerCount} (
                    {totalApplications > 0
                      ? (((screeningCount + interviewingCount + offerCount) / totalApplications) * 100).toFixed(0)
                      : 0}
                    %)
                  </span>
                </div>
                <div className="funnel-bar-wrapper">
                  <div
                    className="funnel-bar screening"
                    style={{
                      width: `${((screeningCount + interviewingCount + offerCount) / maxFunnel) * 100}%`,
                    }}
                  ></div>
                </div>
              </div>

              {/* Funnel Stage 3: Interviewing */}
              <div className="funnel-stage">
                <div className="funnel-stage-info">
                  <span className="stage-name">Technical / Debriefs</span>
                  <span className="stage-count">
                    {interviewingCount + offerCount} (
                    {totalApplications > 0
                      ? (((interviewingCount + offerCount) / totalApplications) * 100).toFixed(0)
                      : 0}
                    %)
                  </span>
                </div>
                <div className="funnel-bar-wrapper">
                  <div
                    className="funnel-bar interviewing"
                    style={{
                      width: `${((interviewingCount + offerCount) / maxFunnel) * 100}%`,
                    }}
                  ></div>
                </div>
              </div>

              {/* Funnel Stage 4: Offers */}
              <div className="funnel-stage">
                <div className="funnel-stage-info">
                  <span className="stage-name">Offers Received</span>
                  <span className="stage-count">
                    {offerCount} (
                    {totalApplications > 0
                      ? ((offerCount / totalApplications) * 100).toFixed(0)
                      : 0}
                    %)
                  </span>
                </div>
                <div className="funnel-bar-wrapper">
                  <div
                    className="funnel-bar offer"
                    style={{ width: `${(offerCount / maxFunnel) * 100}%` }}
                  ></div>
                </div>
              </div>
            </div>
          </div>

          {/* Section: Status Breakdown Grid */}
          <div className="analytics-section">
            <h4 className="analytics-section-title">Status Breakdown</h4>
            <div className="breakdown-cards-grid">
              <div className="breakdown-card">
                <div className="breakdown-dot applied"></div>
                <div className="breakdown-meta">
                  <span className="breakdown-label">Applied</span>
                  <span className="breakdown-value">{appliedCount}</span>
                </div>
              </div>
              <div className="breakdown-card">
                <div className="breakdown-dot screening"></div>
                <div className="breakdown-meta">
                  <span className="breakdown-label">Screening</span>
                  <span className="breakdown-value">{screeningCount}</span>
                </div>
              </div>
              <div className="breakdown-card">
                <div className="breakdown-dot interviewing"></div>
                <div className="breakdown-meta">
                  <span className="breakdown-label">Interviewing</span>
                  <span className="breakdown-value">{interviewingCount}</span>
                </div>
              </div>
              <div className="breakdown-card">
                <div className="breakdown-dot offer"></div>
                <div className="breakdown-meta">
                  <span className="breakdown-label">Offers</span>
                  <span className="breakdown-value">{offerCount}</span>
                </div>
              </div>
              <div className="breakdown-card">
                <div className="breakdown-dot rejected"></div>
                <div className="breakdown-meta">
                  <span className="breakdown-label">Rejected</span>
                  <span className="breakdown-value">{rejectedCount}</span>
                </div>
              </div>
              <div className="breakdown-card">
                <div className="breakdown-dot withdrawn"></div>
                <div className="breakdown-meta">
                  <span className="breakdown-label">Withdrawn</span>
                  <span className="breakdown-value">{withdrawnCount}</span>
                </div>
              </div>
            </div>
          </div>

          {/* Section: Workplace & Salary Distribution */}
          <div className="analytics-two-col">
            {/* Workplace Models */}
            <div className="analytics-section">
              <h4 className="analytics-section-title">Workplace Model Distribution</h4>
              <div className="workplace-breakdown-list">
                {Object.entries(workplaceBreakdown).map(([model, count]) => {
                  const percentage = totalApplications > 0 ? ((count / totalApplications) * 100).toFixed(0) : 0;
                  return (
                    <div key={model} className="workplace-row">
                      <div className="workplace-label-row">
                        <span>{model}</span>
                        <span className="workplace-count">{count} ({percentage}%)</span>
                      </div>
                      <div className="workplace-bar-wrapper">
                        <div
                          className="workplace-bar"
                          style={{ width: `${percentage}%` }}
                        ></div>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>

            {/* Compensation Overview */}
            <div className="analytics-section">
              <h4 className="analytics-section-title">Target Compensation Band</h4>
              <div className="salary-stats-box">
                <div className="salary-stat-row">
                  <span className="salary-stat-label">Minimum Expected:</span>
                  <span className="salary-stat-value">
                    {minSalary ? `$${Number(minSalary).toLocaleString()} USD` : 'Not specified'}
                  </span>
                </div>
                <div className="salary-stat-row">
                  <span className="salary-stat-label">Maximum Expected:</span>
                  <span className="salary-stat-value">
                    {maxSalary ? `$${Number(maxSalary).toLocaleString()} USD` : 'Not specified'}
                  </span>
                </div>
                <div className="salary-stat-row highlight">
                  <span className="salary-stat-label">Target Average:</span>
                  <span className="salary-stat-value">
                    {avgSalary ? `$${Math.round(avgSalary).toLocaleString()} USD` : '—'}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Modal Footer */}
        <div className="modal-footer">
          <button className="btn btn-ghost" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
}
