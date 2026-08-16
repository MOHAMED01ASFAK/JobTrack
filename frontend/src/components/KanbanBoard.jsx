import React from 'react';
import { Plus } from 'lucide-react';
import JobCard from './JobCard';
import { APPLICATION_STATUS } from '../utils/constants';

export default function KanbanBoard({
  jobs = [],
  onViewDetails,
  onEdit,
  onDelete,
  onQuickStatusChange,
  onOpenCreateModalWithStatus,
}) {
  // Columns to display
  const columns = [
    { key: 'APPLIED', title: 'Applied' },
    { key: 'SCREENING', title: 'Screening' },
    { key: 'INTERVIEWING', title: 'Interviewing' },
    { key: 'OFFER', title: 'Offer Received' },
    { key: 'REJECTED', title: 'Rejected' },
    { key: 'WITHDRAWN', title: 'Withdrawn' },
  ];

  return (
    <div className="kanban-board-wrapper">
      <div className="kanban-board-container">
        {columns.map((col) => {
          const statusConfig = APPLICATION_STATUS[col.key];
          const columnJobs = jobs.filter((j) => j.applicationStatus === col.key);

          return (
            <div key={col.key} className="kanban-column">
              {/* Column Header */}
              <div
                className="kanban-column-header"
                style={{ borderTopColor: statusConfig.color }}
              >
                <div className="kanban-header-left">
                  <span
                    className="kanban-dot"
                    style={{ backgroundColor: statusConfig.color }}
                  ></span>
                  <h3 className="kanban-title">{col.title}</h3>
                </div>
                <div className="kanban-header-right">
                  <span
                    className="kanban-count"
                    style={{
                      color: statusConfig.color,
                      backgroundColor: statusConfig.bg,
                    }}
                  >
                    {columnJobs.length}
                  </span>
                  <button
                    className="kanban-add-btn"
                    title={`Add new ${col.title} application`}
                    onClick={() => onOpenCreateModalWithStatus(col.key)}
                  >
                    <Plus size={14} />
                  </button>
                </div>
              </div>

              {/* Column Content */}
              <div className="kanban-column-content">
                {columnJobs.length === 0 ? (
                  <div className="kanban-empty-dropzone">
                    <p className="empty-drop-text">No applications in {col.title.toLowerCase()}</p>
                    <button
                      className="empty-add-btn"
                      onClick={() => onOpenCreateModalWithStatus(col.key)}
                    >
                      + Add Card
                    </button>
                  </div>
                ) : (
                  columnJobs.map((job) => (
                    <JobCard
                      key={job.id}
                      job={job}
                      onViewDetails={onViewDetails}
                      onEdit={onEdit}
                      onDelete={onDelete}
                      onQuickStatusChange={onQuickStatusChange}
                    />
                  ))
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
