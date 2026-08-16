import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => authService.getStoredUser());
  const [token, setToken] = useState(() => authService.getToken());
  const [loading, setLoading] = useState(true);

  // Validate session on mount
  useEffect(() => {
    async function verifySession() {
      const storedToken = authService.getToken();
      if (storedToken) {
        try {
          const profile = await authService.getCurrentUser();
          if (profile) {
            setUser(profile);
            setToken(storedToken);
          } else {
            setUser(null);
            setToken(null);
          }
        } catch {
          setUser(null);
          setToken(null);
        }
      }
      setLoading(false);
    }
    verifySession();
  }, []);

  const login = useCallback(async (usernameOrEmail, password) => {
    const data = await authService.login(usernameOrEmail, password);
    setUser({
      id: data.id,
      username: data.username,
      email: data.email,
      fullName: data.fullName,
      role: data.role,
    });
    setToken(data.accessToken);
    return data;
  }, []);

  const register = useCallback(async (userData) => {
    const data = await authService.register(userData);
    setUser({
      id: data.id,
      username: data.username,
      email: data.email,
      fullName: data.fullName,
      role: data.role,
    });
    setToken(data.accessToken);
    return data;
  }, []);

  const logout = useCallback(() => {
    authService.logout();
    setUser(null);
    setToken(null);
  }, []);

  const value = {
    user,
    token,
    isAuthenticated: Boolean(token && user),
    loading,
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
