/**
 * JobTrack Interview REST API Service
 * Handles all communication with the Spring Boot backend (/api/v1/interviews and /api/v1/jobs/{jobId}/interviews) with JWT Authentication.
 */

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

export const interviewService = {
  /**
   * Fetch all scheduled interviews for a specific job application.
   * @param {number|string} jobId
   * @returns {Promise<Array>} Array of interview response objects
   */
  async getInterviewsByJobId(jobId) {
    const response = await fetch(`/api/v1/jobs/${jobId}/interviews`, {
      method: 'GET',
      headers: getAuthHeaders(),
    });
    const result = await handleResponse(response);
    return result?.data || [];
  },

  /**
   * Schedule a new interview for a job application.
   * @param {number|string} jobId
   * @param {Object} interviewData
   * @returns {Promise<Object>} Created interview object
   */
  async scheduleInterview(jobId, interviewData) {
    const response = await fetch(`/api/v1/jobs/${jobId}/interviews`, {
      method: 'POST',
      headers: getAuthHeaders({
        'Content-Type': 'application/json',
      }),
      body: JSON.stringify(interviewData),
    });
    const result = await handleResponse(response);
    return result?.data;
  },

  /**
   * Update an existing interview (status, notes, questions, schedule).
   * @param {number|string} id
   * @param {Object} interviewData
   * @returns {Promise<Object>} Updated interview object
   */
  async updateInterview(id, interviewData) {
    const response = await fetch(`/api/v1/interviews/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders({
        'Content-Type': 'application/json',
      }),
      body: JSON.stringify(interviewData),
    });
    const result = await handleResponse(response);
    return result?.data;
  },

  /**
   * Delete an interview by ID.
   * @param {number|string} id
   * @returns {Promise<string>} Success message
   */
  async deleteInterview(id) {
    const response = await fetch(`/api/v1/interviews/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    const result = await handleResponse(response);
    return result?.message || 'Interview deleted successfully';
  },
};
