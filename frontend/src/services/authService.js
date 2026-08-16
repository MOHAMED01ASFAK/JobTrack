/**
 * JobTrack Authentication Service
 * Manages JWT tokens, user session storage, and calls to /api/v1/auth
 */

const AUTH_BASE_URL = '/api/v1/auth';
const TOKEN_KEY = 'jobtrack_jwt_token';
const USER_KEY = 'jobtrack_user_data';

async function handleResponse(response) {
  const contentType = response.headers.get('content-type');
  const isJson = contentType && contentType.includes('application/json');
  const body = isJson ? await response.json() : null;

  if (!response.ok) {
    const errorMessage =
      (body && (body.message || (body.fieldErrors && Object.values(body.fieldErrors).join(', ')))) ||
      `Request failed with status ${response.status}`;

    const error = new Error(errorMessage);
    error.status = response.status;
    error.data = body;
    throw error;
  }

  return body;
}

export const authService = {
  /**
   * Log in user with username/email and password.
   */
  async login(usernameOrEmail, password) {
    const response = await fetch(`${AUTH_BASE_URL}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      body: JSON.stringify({ usernameOrEmail, password }),
    });

    const result = await handleResponse(response);
    const authData = result?.data;

    if (authData?.accessToken) {
      localStorage.setItem(TOKEN_KEY, authData.accessToken);
      localStorage.setItem(
        USER_KEY,
        JSON.stringify({
          id: authData.id,
          username: authData.username,
          email: authData.email,
          fullName: authData.fullName,
          role: authData.role,
        })
      );
    }

    return authData;
  },

  /**
   * Register a new user account.
   */
  async register({ username, email, password, fullName }) {
    const response = await fetch(`${AUTH_BASE_URL}/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      body: JSON.stringify({ username, email, password, fullName }),
    });

    const result = await handleResponse(response);
    const authData = result?.data;

    if (authData?.accessToken) {
      localStorage.setItem(TOKEN_KEY, authData.accessToken);
      localStorage.setItem(
        USER_KEY,
        JSON.stringify({
          id: authData.id,
          username: authData.username,
          email: authData.email,
          fullName: authData.fullName,
          role: authData.role,
        })
      );
    }

    return authData;
  },

  /**
   * Fetch profile of currently authenticated user.
   */
  async getCurrentUser() {
    const token = this.getToken();
    if (!token) return null;

    try {
      const response = await fetch(`${AUTH_BASE_URL}/me`, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
      });

      const result = await handleResponse(response);
      return result?.data;
    } catch {
      this.logout();
      return null;
    }
  },

  /**
   * Log out user and clear local tokens.
   */
  logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  },

  /**
   * Get stored JWT token string.
   */
  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  },

  /**
   * Get stored user object.
   */
  getStoredUser() {
    try {
      const raw = localStorage.getItem(USER_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  },

  /**
   * Check if user has active token.
   */
  isAuthenticated() {
    return Boolean(this.getToken());
  },
};
