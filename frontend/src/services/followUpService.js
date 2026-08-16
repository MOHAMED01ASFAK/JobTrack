/**
 * JobTrack Follow-Up REST API Service
 * Handles all communication with the Spring Boot backend (/api/v1/follow-ups and /api/v1/jobs/{jobId}/follow-ups) with JWT Authentication.
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

export const followUpService = {
  /**
   * Fetch all follow-up reminders for the authenticated user.
   * @param {boolean|null} completed Optional filter for completion status (true / false / null)
   * @returns {Promise<Array>} Array of follow-up response objects
   */
  async getAllFollowUps(completed = null) {
    let url = '/api/v1/follow-ups';
    if (completed !== null && completed !== undefined) {
      url += `?completed=${Boolean(completed)}`;
    }

    const response = await fetch(url, {
      method: 'GET',
      headers: getAuthHeaders(),
    });
    const result = await handleResponse(response);
    return result?.data || [];
  },

  /**
   * Create a new follow-up reminder under a job application.
   * @param {number|string} jobId
   * @param {Object} followUpData
   * @returns {Promise<Object>} Created follow-up object
   */
  async createFollowUp(jobId, followUpData) {
    const response = await fetch(`/api/v1/jobs/${jobId}/follow-ups`, {
      method: 'POST',
      headers: getAuthHeaders({
        'Content-Type': 'application/json',
      }),
      body: JSON.stringify(followUpData),
    });
    const result = await handleResponse(response);
    return result?.data;
  },

  /**
   * Toggle the isCompleted status of a follow-up reminder.
   * @param {number|string} id
   * @returns {Promise<Object>} Updated follow-up object
   */
  async toggleFollowUp(id) {
    const response = await fetch(`/api/v1/follow-ups/${id}/toggle`, {
      method: 'PATCH',
      headers: getAuthHeaders(),
    });
    const result = await handleResponse(response);
    return result?.data;
  },
};
