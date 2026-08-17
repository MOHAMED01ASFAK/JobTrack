/**
 * JobTrack REST API Service
 * Handles all communication with the Spring Boot backend (/api/v1/jobs) with JWT Authentication.
 */

const API_BASE_URL = '/api/v1/jobs';
const TOKEN_KEY = 'jobtrack_jwt_token';

/**
 * Get HTTP headers with Authorization Bearer token if present.
 */
function getAuthHeaders(customHeaders = {}) {
  const token = localStorage.getItem(TOKEN_KEY);
  const headers = {
    'Accept': 'application/json',
    ...customHeaders,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  return headers;
}

/**
 * Helper to process HTTP responses and parse JSON or error payloads.
 */
async function handleResponse(response) {
  const contentType = response.headers.get('content-type');
  const isJson = contentType && contentType.includes('application/json');
  const body = isJson ? await response.json() : null;

  if (!response.ok) {
    // If unauthorized, token may be expired
    if (response.status === 401) {
      console.warn('Received 401 Unauthorized from backend API.');
    }

    const errorMessage =
      (body && (body.message || (body.fieldErrors && Object.values(body.fieldErrors).join(', ')))) ||
      `Request failed with status ${response.status} (${response.statusText})`;

    const error = new Error(errorMessage);
    error.status = response.status;
    error.data = body;
    throw error;
  }

  return body;
}

export const jobService = {
  /**
   * Fetch all job applications for the authenticated user.
   * @returns {Promise<Array>} Array of job application objects
   */
  async getAllJobs() {
    const response = await fetch(API_BASE_URL, {
      method: 'GET',
      headers: getAuthHeaders(),
    });
    const result = await handleResponse(response);
    return result?.data || [];
  },

  /**
   * Fetch a single job application by ID.
   * @param {number|string} id 
   * @returns {Promise<Object>} Job application object
   */
  async getJobById(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'GET',
      headers: getAuthHeaders(),
    });
    const result = await handleResponse(response);
    return result?.data;
  },

  /**
   * Create a new job application for the authenticated user.
   * @param {Object} jobData 
   * @returns {Promise<Object>} Created job application object
   */
  async createJob(jobData) {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: getAuthHeaders({
        'Content-Type': 'application/json',
      }),
      body: JSON.stringify(jobData),
    });
    const result = await handleResponse(response);
    return result?.data;
  },

  /**
   * Update an existing job application.
   * @param {number|string} id 
   * @param {Object} jobData 
   * @returns {Promise<Object>} Updated job application object
   */
  async updateJob(id, jobData) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders({
        'Content-Type': 'application/json',
      }),
      body: JSON.stringify(jobData),
    });
    const result = await handleResponse(response);
    return result?.data;
  },

  /**
   * Delete a job application by ID.
   * @param {number|string} id 
   * @returns {Promise<string>} Success message
   */
  async deleteJob(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    const result = await handleResponse(response);
    return result?.message || 'Job application deleted successfully';
  },

  /**
   * Test backend connectivity / health check against public Actuator endpoint.
   * @returns {Promise<boolean>}
   */
  async checkHealth() {
    try {
      const response = await fetch('/actuator/health', {
        method: 'GET',
      });
      return response.ok;
    } catch {
      return false;
    }
  },

  /**
   * Fetch aggregated analytics and career pipeline metrics for authenticated user.
   * @returns {Promise<Object>} AnalyticsSummaryResponse
   */
  async getAnalyticsSummary() {
    const response = await fetch('/api/v1/analytics/summary', {
      method: 'GET',
      headers: getAuthHeaders(),
    });
    const result = await handleResponse(response);
    return result?.data;
  },

  /**
   * Trigger CSV download of all job applications for the authenticated user.
   */
  async exportJobsCsv() {
    const response = await fetch(`${API_BASE_URL}/export/csv`, {
      method: 'GET',
      headers: getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error(`CSV export failed with status ${response.status}`);
    }

    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `jobtrack_applications_${new Date().toISOString().split('T')[0]}.csv`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  },
};
