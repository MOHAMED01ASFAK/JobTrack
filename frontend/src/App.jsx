import React, { useState, useEffect, useMemo, useCallback } from 'react';
import Navbar from './components/Navbar';
import MetricsRibbon from './components/MetricsRibbon';
import FilterControlBar from './components/FilterControlBar';
import KanbanBoard from './components/KanbanBoard';
import JobCard from './components/JobCard';
import JobTableView from './components/JobTableView';
import JobModal from './components/JobModal';
import JobDetailModal from './components/JobDetailModal';
import DeleteConfirmModal from './components/DeleteConfirmModal';
import AnalyticsModal from './components/AnalyticsModal';
import AuthModal from './components/AuthModal';
import InterviewModal from './components/InterviewModal';
import FollowUpModal from './components/FollowUpModal';
import FollowUpsHubModal from './components/FollowUpsHubModal';
import ToastContainer from './components/ToastContainer';
import EmptyState from './components/EmptyState';
import SkeletonLoader from './components/SkeletonLoader';
import { AuthProvider, useAuth } from './context/AuthContext';
import { jobService } from './services/jobService';
import { interviewService } from './services/interviewService';
import { followUpService } from './services/followUpService';
import './App.css';

function JobTrackApp() {
  const { user, isAuthenticated } = useAuth();

  // State
  const [jobs, setJobs] = useState([]);
  const [followUps, setFollowUps] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [isBackendConnected, setIsBackendConnected] = useState(true);

  // Filter & Search & View State
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [workplaceFilter, setWorkplaceFilter] = useState('ALL');
  const [employmentFilter, setEmploymentFilter] = useState('ALL');
  const [sortBy, setSortBy] = useState('applied_desc');
  const [viewMode, setViewMode] = useState('kanban'); // 'kanban' | 'grid' | 'table'

  // Job Modals
  const [isJobModalOpen, setIsJobModalOpen] = useState(false);
  const [jobModalMode, setJobModalMode] = useState('create');
  const [selectedJobForEdit, setSelectedJobForEdit] = useState(null);

  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [selectedJobForDetail, setSelectedJobForDetail] = useState(null);
  const [selectedJobInterviews, setSelectedJobInterviews] = useState([]);

  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [selectedJobForDelete, setSelectedJobForDelete] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Interview Modals
  const [isInterviewModalOpen, setIsInterviewModalOpen] = useState(false);
  const [interviewModalMode, setInterviewModalMode] = useState('schedule');
  const [selectedInterviewForEdit, setSelectedInterviewForEdit] = useState(null);
  const [selectedJobForInterview, setSelectedJobForInterview] = useState(null);
  const [isSubmittingInterview, setIsSubmittingInterview] = useState(false);

  // Follow-Up Modals
  const [isFollowUpsHubOpen, setIsFollowUpsHubOpen] = useState(false);
  const [isFollowUpModalOpen, setIsFollowUpModalOpen] = useState(false);
  const [selectedJobForFollowUp, setSelectedJobForFollowUp] = useState(null);
  const [isSubmittingFollowUp, setIsSubmittingFollowUp] = useState(false);

  // Analytics & Export State
  const [isAnalyticsModalOpen, setIsAnalyticsModalOpen] = useState(false);
  const [analyticsData, setAnalyticsData] = useState(null);
  const [isExporting, setIsExporting] = useState(false);

  // Auth Modal State
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);

  // Toast Notifications
  const [toasts, setToasts] = useState([]);

  const addToast = useCallback((message, type = 'success') => {
    const id = Date.now() + Math.random().toString(36).substring(2, 6);
    setToasts((prev) => [...prev, { id, message, type }]);

    setTimeout(() => {
      setToasts((prev) => prev.filter((t) => t.id !== id));
    }, 4000);
  }, []);

  const dismissToast = (id) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  };

  // Load all jobs from REST API
  const fetchJobsData = useCallback(async (isManualRefresh = false) => {
    if (isManualRefresh) setIsRefreshing(true);
    if (!isAuthenticated) {
      setJobs([]);
      setLoading(false);
      if (isManualRefresh) setIsRefreshing(false);
      return;
    }
    try {
      const data = await jobService.getAllJobs();
      setJobs(data);
      setIsBackendConnected(true);
      if (isManualRefresh) {
        addToast('Refreshed job applications from database', 'info');
      }
    } catch (err) {
      console.error('Failed to fetch job applications:', err);
      if (err.status === 401) {
        // Session expired or unauthenticated; backend is still healthy
        setIsBackendConnected(true);
      } else {
        setIsBackendConnected(false);
        addToast(
          err.message || 'Unable to connect to backend server on /api/v1/jobs',
          'error'
        );
      }
    } finally {
      setLoading(false);
      if (isManualRefresh) setIsRefreshing(false);
    }
  }, [isAuthenticated, addToast]);

  // Load all follow-up reminders
  const fetchFollowUpsData = useCallback(async () => {
    if (!isAuthenticated) {
      setFollowUps([]);
      return;
    }
    try {
      const data = await followUpService.getAllFollowUps();
      setFollowUps(data);
    } catch (err) {
      console.error('Failed to load follow-up reminders:', err);
    }
  }, [isAuthenticated]);

  // Fetch interviews for the currently viewed job detail
  const fetchInterviewsForJob = useCallback(async (jobId) => {
    if (!jobId || !isAuthenticated) {
      setSelectedJobInterviews([]);
      return;
    }
    try {
      const data = await interviewService.getInterviewsByJobId(jobId);
      setSelectedJobInterviews(data);
    } catch (err) {
      console.error('Failed to load interviews for job:', err);
      setSelectedJobInterviews([]);
    }
  }, [isAuthenticated]);

  // Initial backend health check
  useEffect(() => {
    async function verifyBackendHealth() {
      const isHealthy = await jobService.checkHealth();
      setIsBackendConnected(isHealthy);
    }
    verifyBackendHealth();
  }, []);

  // Re-fetch data on user change
  useEffect(() => {
    fetchJobsData();
    fetchFollowUpsData();
  }, [fetchJobsData, fetchFollowUpsData, user]);

  // Load interviews whenever selectedJobForDetail changes
  useEffect(() => {
    if (selectedJobForDetail?.id) {
      fetchInterviewsForJob(selectedJobForDetail.id);
    } else {
      setSelectedJobInterviews([]);
    }
  }, [selectedJobForDetail, fetchInterviewsForJob]);

  // Filter and Sort Jobs
  const filteredAndSortedJobs = useMemo(() => {
    let result = [...jobs];

    // Search filter
    if (searchTerm.trim()) {
      const q = searchTerm.toLowerCase().trim();
      result = result.filter(
        (j) =>
          (j.companyName && j.companyName.toLowerCase().includes(q)) ||
          (j.jobTitle && j.jobTitle.toLowerCase().includes(q)) ||
          (j.jobLocation && j.jobLocation.toLowerCase().includes(q)) ||
          (j.notes && j.notes.toLowerCase().includes(q)) ||
          (j.jobDescription && j.jobDescription.toLowerCase().includes(q))
      );
    }

    // Status filter
    if (statusFilter !== 'ALL') {
      result = result.filter((j) => j.applicationStatus === statusFilter);
    }

    // Workplace filter
    if (workplaceFilter !== 'ALL') {
      result = result.filter((j) => j.workplaceType === workplaceFilter);
    }

    // Employment filter
    if (employmentFilter !== 'ALL') {
      result = result.filter((j) => j.employmentType === employmentFilter);
    }

    // Sorting
    result.sort((a, b) => {
      switch (sortBy) {
        case 'applied_desc':
          return (b.appliedDate || '').localeCompare(a.appliedDate || '') || b.id - a.id;
        case 'applied_asc':
          return (a.appliedDate || '').localeCompare(b.appliedDate || '') || a.id - b.id;
        case 'priority_desc':
          return (b.priority || 0) - (a.priority || 0);
        case 'salary_desc':
          const maxA = a.salaryMax || a.salaryMin || 0;
          const maxB = b.salaryMax || b.salaryMin || 0;
          return Number(maxB) - Number(maxA);
        case 'company_asc':
          return (a.companyName || '').localeCompare(b.companyName || '');
        case 'updated_desc':
          return (b.updatedAt || '').localeCompare(a.updatedAt || '') || b.id - a.id;
        default:
          return b.id - a.id;
      }
    });

    return result;
  }, [jobs, searchTerm, statusFilter, workplaceFilter, employmentFilter, sortBy]);

  // Modal Handlers
  const handleOpenCreateModal = (initialStatus = 'APPLIED') => {
    if (!isAuthenticated) {
      addToast('Please sign in or create an account to track applications.', 'info');
      setIsAuthModalOpen(true);
      return;
    }
    setJobModalMode('create');
    setSelectedJobForEdit({ applicationStatus: initialStatus });
    setIsJobModalOpen(true);
  };

  const handleOpenEditModal = (job) => {
    setJobModalMode('edit');
    setSelectedJobForEdit(job);
    setIsJobModalOpen(true);
    setIsDetailModalOpen(false);
  };

  const handleOpenDetailModal = (job) => {
    setSelectedJobForDetail(job);
    setIsDetailModalOpen(true);
  };

  const handleOpenDeleteModal = (job) => {
    setSelectedJobForDelete(job);
    setIsDeleteModalOpen(true);
    setIsDetailModalOpen(false);
  };

  const handleResetFilters = () => {
    setSearchTerm('');
    setStatusFilter('ALL');
    setWorkplaceFilter('ALL');
    setEmploymentFilter('ALL');
    setSortBy('applied_desc');
  };

  // CRUD Operations with Backend Integration
  const handleSaveJob = async (jobData, setFormErrors) => {
    setIsSubmitting(true);
    try {
      if (jobModalMode === 'create') {
        const created = await jobService.createJob(jobData);
        setJobs((prev) => [created, ...prev]);
        addToast(`Created application for ${created.companyName}!`, 'success');
      } else {
        const updated = await jobService.updateJob(selectedJobForEdit.id, jobData);
        setJobs((prev) => prev.map((j) => (j.id === updated.id ? updated : j)));
        if (selectedJobForDetail && selectedJobForDetail.id === updated.id) {
          setSelectedJobForDetail(updated);
        }
        addToast(`Updated application for ${updated.companyName}!`, 'success');
      }
      setIsJobModalOpen(false);
      setSelectedJobForEdit(null);
    } catch (err) {
      console.error('Error saving job application:', err);
      if (err.data && err.data.fieldErrors) {
        setFormErrors(err.data.fieldErrors);
      } else if (err.data && err.data.errors) {
        setFormErrors(err.data.errors);
      } else {
        addToast(err.message || 'Failed to save job application', 'error');
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleQuickStatusChange = async (id, newStatus) => {
    const existing = jobs.find((j) => j.id === id);
    if (!existing || existing.applicationStatus === newStatus) return;

    // Optimistic UI update
    const previousJobs = [...jobs];
    setJobs((prev) =>
      prev.map((j) => (j.id === id ? { ...j, applicationStatus: newStatus } : j))
    );

    try {
      const updatePayload = {
        companyName: existing.companyName,
        jobTitle: existing.jobTitle,
        jobLocation: existing.jobLocation,
        workplaceType: existing.workplaceType,
        employmentType: existing.employmentType,
        applicationStatus: newStatus,
        priority: existing.priority,
        salaryMin: existing.salaryMin,
        salaryMax: existing.salaryMax,
        salaryCurrency: existing.salaryCurrency,
        appliedDate: existing.appliedDate,
        deadlineDate: existing.deadlineDate,
        jobPostingUrl: existing.jobPostingUrl,
        jobDescription: existing.jobDescription,
        notes: existing.notes,
      };

      const updated = await jobService.updateJob(id, updatePayload);
      setJobs((prev) => prev.map((j) => (j.id === updated.id ? updated : j)));
      if (selectedJobForDetail && selectedJobForDetail.id === updated.id) {
        setSelectedJobForDetail(updated);
      }
      addToast(`Moved ${existing.companyName} to ${newStatus}!`, 'success');
    } catch (err) {
      console.error('Quick status update failed:', err);
      setJobs(previousJobs); // Revert
      addToast(err.message || 'Failed to update status on server', 'error');
    }
  };

  const handleConfirmDelete = async (id) => {
    setIsDeleting(true);
    const targetJob = jobs.find((j) => j.id === id);
    try {
      await jobService.deleteJob(id);
      setJobs((prev) => prev.filter((j) => j.id !== id));
      setIsDeleteModalOpen(false);
      setSelectedJobForDelete(null);
      addToast(
        `Deleted application for ${targetJob?.companyName || 'opportunity'}`,
        'success'
      );
    } catch (err) {
      console.error('Error deleting job application:', err);
      addToast(err.message || 'Failed to delete application', 'error');
    } finally {
      setIsDeleting(false);
    }
  };

  // Interview Handlers
  const handleOpenScheduleInterview = (job) => {
    if (!isAuthenticated) {
      addToast('Please sign in to schedule interviews.', 'info');
      setIsAuthModalOpen(true);
      return;
    }
    setSelectedJobForInterview(job || selectedJobForDetail);
    setSelectedInterviewForEdit(null);
    setInterviewModalMode('schedule');
    setIsInterviewModalOpen(true);
  };

  const handleOpenEditInterview = (interview, job) => {
    setSelectedJobForInterview(job || selectedJobForDetail);
    setSelectedInterviewForEdit(interview);
    setInterviewModalMode('edit');
    setIsInterviewModalOpen(true);
  };

  const handleSaveInterview = async (interviewData, setErrors) => {
    setIsSubmittingInterview(true);
    try {
      if (interviewModalMode === 'schedule') {
        const targetJobId = selectedJobForInterview?.id || selectedJobForDetail?.id;
        const created = await interviewService.scheduleInterview(targetJobId, interviewData);
        setSelectedJobInterviews((prev) => [...prev, created]);
        addToast(`Scheduled ${created.roundName} successfully!`, 'success');
      } else {
        const updated = await interviewService.updateInterview(selectedInterviewForEdit.id, interviewData);
        setSelectedJobInterviews((prev) =>
          prev.map((iv) => (iv.id === updated.id ? updated : iv))
        );
        addToast(`Updated ${updated.roundName}!`, 'success');
      }
      setIsInterviewModalOpen(false);
      setSelectedInterviewForEdit(null);
    } catch (err) {
      console.error('Error saving interview:', err);
      if (err.data && err.data.fieldErrors) {
        setErrors(err.data.fieldErrors);
      } else if (err.data && err.data.errors) {
        setErrors(err.data.errors);
      } else {
        addToast(err.message || 'Failed to save interview round', 'error');
      }
    } finally {
      setIsSubmittingInterview(false);
    }
  };

  const handleDeleteInterview = async (interviewId) => {
    try {
      await interviewService.deleteInterview(interviewId);
      setSelectedJobInterviews((prev) => prev.filter((iv) => iv.id !== interviewId));
      addToast('Interview round deleted successfully', 'success');
    } catch (err) {
      console.error('Error deleting interview:', err);
      addToast(err.message || 'Failed to delete interview', 'error');
    }
  };

  // Follow-Up Handlers
  const handleOpenCreateFollowUp = (job = null) => {
    if (!isAuthenticated) {
      addToast('Please sign in to set follow-up reminders.', 'info');
      setIsAuthModalOpen(true);
      return;
    }
    setSelectedJobForFollowUp(job || selectedJobForDetail);
    setIsFollowUpModalOpen(true);
  };

  const handleSaveFollowUp = async (jobId, followUpData, setErrors) => {
    setIsSubmittingFollowUp(true);
    try {
      const created = await followUpService.createFollowUp(jobId, followUpData);
      setFollowUps((prev) => [created, ...prev]);
      addToast('Follow-up reminder set successfully!', 'success');
      setIsFollowUpModalOpen(false);
      setSelectedJobForFollowUp(null);
    } catch (err) {
      console.error('Error creating follow-up:', err);
      if (err.data && err.data.fieldErrors) {
        setErrors(err.data.fieldErrors);
      } else if (err.data && err.data.errors) {
        setErrors(err.data.errors);
      } else {
        addToast(err.message || 'Failed to set follow-up reminder', 'error');
      }
    } finally {
      setIsSubmittingFollowUp(false);
    }
  };

  const handleToggleFollowUp = async (followUpId) => {
    // Optimistic toggle
    const prevList = [...followUps];
    setFollowUps((prev) =>
      prev.map((f) => (f.id === followUpId ? { ...f, isCompleted: !f.isCompleted } : f))
    );

    try {
      const updated = await followUpService.toggleFollowUp(followUpId);
      setFollowUps((prev) => prev.map((f) => (f.id === updated.id ? updated : f)));
      addToast(
        updated.isCompleted ? 'Marked follow-up as completed!' : 'Follow-up marked as pending',
        'info'
      );
    } catch (err) {
      console.error('Error toggling follow-up:', err);
      setFollowUps(prevList); // Revert
      addToast(err.message || 'Failed to update follow-up status', 'error');
    }
  };

  const handleViewJobFromFollowUp = (jobId) => {
    const target = jobs.find((j) => j.id === jobId);
    if (target) {
      setIsFollowUpsHubOpen(false);
      handleOpenDetailModal(target);
    }
  };

  // Analytics Handlers
  const handleOpenAnalytics = async () => {
    try {
      const summary = await jobService.getAnalyticsSummary();
      setAnalyticsData(summary);
      setIsAnalyticsModalOpen(true);
    } catch (err) {
      console.error('Failed to load analytics summary:', err);
      addToast('Failed to load analytics summary from backend', 'error');
    }
  };

  const handleExportCsv = async () => {
    setIsExporting(true);
    try {
      await jobService.exportJobsCsv();
      addToast('Exported job applications CSV successfully!', 'success');
    } catch (err) {
      console.error('CSV export failed:', err);
      addToast(err.message || 'Failed to export applications as CSV', 'error');
    } finally {
      setIsExporting(false);
    }
  };

  const isFiltered =
    searchTerm.trim() !== '' ||
    statusFilter !== 'ALL' ||
    workplaceFilter !== 'ALL' ||
    employmentFilter !== 'ALL';

  const pendingFollowUpsCount = useMemo(
    () => followUps.filter((f) => !f.isCompleted).length,
    [followUps]
  );

  return (
    <div className="app-wrapper">
      {/* Top Navbar */}
      <Navbar
        searchTerm={searchTerm}
        setSearchTerm={setSearchTerm}
        onOpenCreateModal={() => handleOpenCreateModal('APPLIED')}
        onOpenAnalytics={handleOpenAnalytics}
        onOpenFollowUps={() => setIsFollowUpsHubOpen(true)}
        pendingFollowUpsCount={pendingFollowUpsCount}
        onExportCsv={handleExportCsv}
        onOpenAuthModal={() => setIsAuthModalOpen(true)}
        isBackendConnected={isBackendConnected}
        isRefreshing={isRefreshing}
        isExporting={isExporting}
        onRefresh={async () => {
          const healthy = await jobService.checkHealth();
          setIsBackendConnected(healthy);
          fetchJobsData(true);
          fetchFollowUpsData();
        }}
        totalJobsCount={jobs.length}
      />

      {/* Main App Container */}
      <main className="main-content">
        {/* Analytics & Metrics Ribbon */}
        <MetricsRibbon
          jobs={jobs}
          activeStatusFilter={statusFilter}
          onSelectStatusFilter={(status) => {
            setStatusFilter((prev) => (prev === status ? 'ALL' : status));
          }}
          pendingFollowUpsCount={pendingFollowUpsCount}
          onOpenFollowUps={() => setIsFollowUpsHubOpen(true)}
        />

        {/* Filters & Control Bar */}
        <FilterControlBar
          statusFilter={statusFilter}
          setStatusFilter={setStatusFilter}
          workplaceFilter={workplaceFilter}
          setWorkplaceFilter={setWorkplaceFilter}
          employmentFilter={employmentFilter}
          setEmploymentFilter={setEmploymentFilter}
          sortBy={sortBy}
          setSortBy={setSortBy}
          viewMode={viewMode}
          setViewMode={setViewMode}
          filteredCount={filteredAndSortedJobs.length}
          totalCount={jobs.length}
          onResetFilters={handleResetFilters}
        />

        {/* Content Views: Loading / Empty / Kanban / Grid / Table */}
        {loading ? (
          <SkeletonLoader viewMode={viewMode} />
        ) : filteredAndSortedJobs.length === 0 ? (
          <EmptyState
            isFiltered={isFiltered}
            onResetFilters={handleResetFilters}
            onOpenCreateModal={() => handleOpenCreateModal('APPLIED')}
          />
        ) : viewMode === 'kanban' ? (
          <KanbanBoard
            jobs={filteredAndSortedJobs}
            onViewDetails={handleOpenDetailModal}
            onEdit={handleOpenEditModal}
            onDelete={handleOpenDeleteModal}
            onQuickStatusChange={handleQuickStatusChange}
            onOpenCreateModalWithStatus={(status) => handleOpenCreateModal(status)}
          />
        ) : viewMode === 'table' ? (
          <JobTableView
            jobs={filteredAndSortedJobs}
            onViewDetails={handleOpenDetailModal}
            onEdit={handleOpenEditModal}
            onDelete={handleOpenDeleteModal}
            onQuickStatusChange={handleQuickStatusChange}
          />
        ) : (
          /* Card Grid View */
          <div className="job-grid-view">
            {filteredAndSortedJobs.map((job) => (
              <JobCard
                key={job.id}
                job={job}
                onViewDetails={handleOpenDetailModal}
                onEdit={handleOpenEditModal}
                onDelete={handleOpenDeleteModal}
                onQuickStatusChange={handleQuickStatusChange}
              />
            ))}
          </div>
        )}
      </main>

      {/* Create / Edit Job Modal */}
      <JobModal
        isOpen={isJobModalOpen}
        mode={jobModalMode}
        initialData={selectedJobForEdit}
        onClose={() => {
          setIsJobModalOpen(false);
          setSelectedJobForEdit(null);
        }}
        onSubmit={handleSaveJob}
        isSubmitting={isSubmitting}
      />

      {/* Job Detail Modal with Interviews & Follow-Ups Tabs */}
      <JobDetailModal
        isOpen={isDetailModalOpen}
        job={selectedJobForDetail}
        interviews={selectedJobInterviews}
        followUps={followUps}
        onClose={() => {
          setIsDetailModalOpen(false);
          setSelectedJobForDetail(null);
        }}
        onEdit={(job) => handleOpenEditModal(job)}
        onDelete={(job) => handleOpenDeleteModal(job)}
        onScheduleInterview={handleOpenScheduleInterview}
        onEditInterview={handleOpenEditInterview}
        onDeleteInterview={handleDeleteInterview}
        onAddFollowUp={handleOpenCreateFollowUp}
        onToggleFollowUp={handleToggleFollowUp}
      />

      {/* Schedule / Edit Interview Modal */}
      <InterviewModal
        isOpen={isInterviewModalOpen}
        mode={interviewModalMode}
        initialData={selectedInterviewForEdit}
        job={selectedJobForInterview}
        onClose={() => {
          setIsInterviewModalOpen(false);
          setSelectedInterviewForEdit(null);
          setSelectedJobForInterview(null);
        }}
        onSubmit={handleSaveInterview}
        isSubmitting={isSubmittingInterview}
      />

      {/* Follow-Ups Hub Modal */}
      <FollowUpsHubModal
        isOpen={isFollowUpsHubOpen}
        followUps={followUps}
        onClose={() => setIsFollowUpsHubOpen(false)}
        onToggleFollowUp={handleToggleFollowUp}
        onOpenCreateFollowUp={() => handleOpenCreateFollowUp(null)}
        onViewJob={handleViewJobFromFollowUp}
      />

      {/* Create Follow-Up Modal */}
      <FollowUpModal
        isOpen={isFollowUpModalOpen}
        job={selectedJobForFollowUp}
        jobs={jobs}
        onClose={() => {
          setIsFollowUpModalOpen(false);
          setSelectedJobForFollowUp(null);
        }}
        onSubmit={handleSaveFollowUp}
        isSubmitting={isSubmittingFollowUp}
      />

      {/* Delete Confirmation Modal */}
      <DeleteConfirmModal
        isOpen={isDeleteModalOpen}
        job={selectedJobForDelete}
        onClose={() => {
          setIsDeleteModalOpen(false);
          setSelectedJobForDelete(null);
        }}
        onConfirm={handleConfirmDelete}
        isDeleting={isDeleting}
      />

      {/* Analytics Modal */}
      <AnalyticsModal
        isOpen={isAnalyticsModalOpen}
        analyticsData={analyticsData}
        onClose={() => setIsAnalyticsModalOpen(false)}
        onExportCsv={handleExportCsv}
        isExporting={isExporting}
      />

      {/* Authentication Modal */}
      <AuthModal
        isOpen={isAuthModalOpen}
        onClose={() => setIsAuthModalOpen(false)}
        onAuthSuccess={(msg) => {
          addToast(msg, 'success');
          fetchJobsData(true);
          fetchFollowUpsData();
        }}
      />

      {/* Floating Toast Alerts */}
      <ToastContainer toasts={toasts} onDismiss={dismissToast} />
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <JobTrackApp />
    </AuthProvider>
  );
}
