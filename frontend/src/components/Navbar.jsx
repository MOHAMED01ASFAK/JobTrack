import React, { useRef, useEffect } from 'react';
import {
  Briefcase,
  Plus,
  Search,
  RefreshCw,
  TrendingUp,
  Download,
  LogIn,
  LogOut,
  User,
  Bell,
  Command,
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function Navbar({
  searchTerm,
  setSearchTerm,
  onOpenCreateModal,
  onOpenAnalytics,
  onOpenFollowUps,
  pendingFollowUpsCount = 0,
  onExportCsv,
  onOpenAuthModal,
  isBackendConnected,
  isRefreshing,
  isExporting = false,
  onRefresh,
  totalJobsCount,
}) {
  const { user, isAuthenticated, logout } = useAuth();
  const searchInputRef = useRef(null);

  // Global search shortcut ('/' or 'Ctrl+K' / 'Cmd+K')
  useEffect(() => {
    const handleKeyDown = (e) => {
      if (
        (e.key === '/' && document.activeElement?.tagName !== 'INPUT' && document.activeElement?.tagName !== 'TEXTAREA') ||
        ((e.metaKey || e.ctrlKey) && e.key.toLowerCase() === 'k')
      ) {
        e.preventDefault();
        searchInputRef.current?.focus();
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, []);

  const getInitials = (name) => {
    if (!name) return 'U';
    const parts = name.trim().split(' ');
    if (parts.length >= 2) {
      return `${parts[0][0]}${parts[1][0]}`.toUpperCase();
    }
    return name.slice(0, 2).toUpperCase();
  };

  return (
    <header className="navbar-container" role="banner">
      <div className="navbar-content">
        {/* Brand Logo */}
        <div className="navbar-brand">
          <div className="brand-icon-wrapper">
            <Briefcase className="brand-icon" size={20} />
          </div>
          <div className="brand-info">
            <div className="brand-title-row">
              <span className="brand-title">JobTrack</span>
              <span className="brand-tag">PRO</span>
            </div>
            <span className="brand-subtitle">Career Application Engine</span>
          </div>
        </div>

        {/* Search Bar */}
        <div className="navbar-search">
          <Search className="search-icon" size={16} />
          <input
            ref={searchInputRef}
            type="text"
            className="search-input"
            placeholder="Search company, role, location, or notes..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            aria-label="Search applications"
          />
          {searchTerm ? (
            <button
              className="search-clear-btn"
              onClick={() => setSearchTerm('')}
              title="Clear search"
              aria-label="Clear search"
            >
              ×
            </button>
          ) : (
            <span className="search-shortcut-hint" title="Press / or Ctrl+K to search">/</span>
          )}
        </div>

        {/* Actions & Live Status */}
        <div className="navbar-actions">
          {/* Live Status Badge */}
          <div
            className={`status-pill ${isBackendConnected ? 'connected' : 'disconnected'}`}
            title={isBackendConnected ? 'Backend REST API & MySQL 8.0 Connected' : 'Cannot reach backend API'}
          >
            <span className="status-indicator-dot"></span>
            <span className="status-pill-text">
              {isBackendConnected ? 'API Live' : 'Offline'}
            </span>
          </div>

          {/* Refresh Button */}
          <button
            className={`icon-btn ${isRefreshing ? 'spinning' : ''}`}
            onClick={onRefresh}
            title="Refresh job applications"
            disabled={isRefreshing}
            aria-label="Refresh job applications"
          >
            <RefreshCw size={17} />
          </button>

          {/* Follow-Ups Hub Button */}
          <button
            className="btn btn-ghost navbar-action-btn followup-nav-btn"
            onClick={onOpenFollowUps}
            title="Open Follow-Up Reminders Hub"
          >
            <Bell size={16} />
            <span>Follow-Ups</span>
            {pendingFollowUpsCount > 0 && (
              <span className="navbar-badge-pill">{pendingFollowUpsCount}</span>
            )}
          </button>

          {/* Analytics Modal Button */}
          <button
            className="btn btn-ghost navbar-action-btn"
            onClick={onOpenAnalytics}
            title="Open Career Pipeline Analytics"
          >
            <TrendingUp size={16} />
            <span>Analytics</span>
          </button>

          {/* Export CSV Button */}
          <button
            className="btn btn-ghost navbar-action-btn"
            onClick={onExportCsv}
            disabled={isExporting || totalJobsCount === 0}
            title="Export applications to CSV"
          >
            <Download size={16} />
            <span>{isExporting ? 'Exporting...' : 'Export'}</span>
          </button>

          {/* New Application CTA Button */}
          <button
            className="btn btn-primary create-job-btn"
            onClick={onOpenCreateModal}
          >
            <Plus size={17} />
            <span>New Application</span>
          </button>

          {/* User Profile / Auth CTA */}
          {isAuthenticated && user ? (
            <div className="navbar-user-card" title={`Logged in as ${user.fullName || user.username}`}>
              <div className="navbar-user-avatar">
                {getInitials(user.fullName || user.username)}
              </div>
              <div className="navbar-user-info">
                <span className="navbar-user-name">{user.fullName || user.username}</span>
                <span className="navbar-user-role">
                  {user.role === 'ROLE_ADMIN' ? 'Admin' : 'Personal'}
                </span>
              </div>
              <button
                className="navbar-logout-btn"
                onClick={logout}
                title="Sign out of JobTrack"
                aria-label="Sign out"
              >
                <LogOut size={15} />
              </button>
            </div>
          ) : (
            <button
              className="btn btn-ghost navbar-action-btn auth-nav-btn"
              onClick={onOpenAuthModal}
              title="Sign in to your account"
            >
              <LogIn size={16} />
              <span>Sign In</span>
            </button>
          )}
        </div>
      </div>
    </header>
  );
}
