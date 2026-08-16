import React, { useState, useEffect } from 'react';
import {
  X,
  Lock,
  Mail,
  User,
  UserCheck,
  Eye,
  EyeOff,
  LogIn,
  UserPlus,
  Sparkles,
  ShieldCheck,
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function AuthModal({ isOpen, onClose, onAuthSuccess }) {
  const { login, register } = useAuth();

  const [mode, setMode] = useState('login'); // 'login' | 'register'
  const [showPassword, setShowPassword] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  // Form Fields
  const [usernameOrEmail, setUsernameOrEmail] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');

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

  if (!isOpen) return null;

  const handleModeSwitch = (newMode) => {
    setMode(newMode);
    setErrorMessage('');
  };

  const handleFillDemo = (demoUsername, demoPass) => {
    setUsernameOrEmail(demoUsername);
    setPassword(demoPass);
    setMode('login');
    setErrorMessage('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage('');
    setSubmitting(true);

    try {
      if (mode === 'login') {
        if (!usernameOrEmail.trim() || !password.trim()) {
          setErrorMessage('Please enter both username/email and password.');
          setSubmitting(false);
          return;
        }
        await login(usernameOrEmail.trim(), password);
      } else {
        if (!username.trim() || !email.trim() || !password.trim() || !fullName.trim()) {
          setErrorMessage('Please complete all registration fields.');
          setSubmitting(false);
          return;
        }
        if (password.length < 6) {
          setErrorMessage('Password must be at least 6 characters long.');
          setSubmitting(false);
          return;
        }
        await register({
          username: username.trim(),
          email: email.trim(),
          password,
          fullName: fullName.trim(),
        });
      }

      onAuthSuccess?.(mode === 'login' ? 'Signed in successfully!' : 'Account created successfully!');
      onClose();
    } catch (err) {
      console.error('Authentication error:', err);
      setErrorMessage(err.message || 'Authentication failed. Please check your credentials.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="modal-backdrop" onClick={onClose} role="dialog" aria-modal="true">
      <div className="modal-container auth-modal" onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="modal-header">
          <div className="modal-header-title-group">
            <div className="analytics-badge-row">
              <div className="analytics-header-icon" style={{ background: 'rgba(99, 102, 241, 0.2)', color: '#818cf8' }}>
                <ShieldCheck size={18} />
              </div>
              <h2 className="modal-title">
                {mode === 'login' ? 'Welcome Back to JobTrack' : 'Create Your JobTrack Account'}
              </h2>
            </div>
            <p className="modal-subtitle">
              {mode === 'login'
                ? 'Sign in to access your personal career application pipeline'
                : 'Get started with isolated career pipeline tracking and analytics'}
            </p>
          </div>

          <button className="modal-close-btn" onClick={onClose} title="Close (Esc)" aria-label="Close modal">
            <X size={20} />
          </button>
        </div>

        {/* Tab Switcher */}
        <div className="auth-tab-bar">
          <button
            type="button"
            className={`auth-tab-btn ${mode === 'login' ? 'active' : ''}`}
            onClick={() => handleModeSwitch('login')}
          >
            <LogIn size={15} />
            <span>Sign In</span>
          </button>
          <button
            type="button"
            className={`auth-tab-btn ${mode === 'register' ? 'active' : ''}`}
            onClick={() => handleModeSwitch('register')}
          >
            <UserPlus size={15} />
            <span>Create Account</span>
          </button>
        </div>

        {/* Form Body */}
        <form className="modal-form auth-form" onSubmit={handleSubmit}>
          {errorMessage && (
            <div className="auth-error-banner">
              <span>{errorMessage}</span>
            </div>
          )}

          {mode === 'register' && (
            <>
              <div className="form-field">
                <label className="form-label required">Full Name</label>
                <div className="auth-input-wrapper">
                  <User size={16} className="auth-input-icon" />
                  <input
                    type="text"
                    className="form-input auth-input"
                    placeholder="e.g. Alex Hunter"
                    value={fullName}
                    onChange={(e) => setFullName(e.target.value)}
                    required
                  />
                </div>
              </div>

              <div className="form-row two-col">
                <div className="form-field">
                  <label className="form-label required">Username</label>
                  <div className="auth-input-wrapper">
                    <UserCheck size={16} className="auth-input-icon" />
                    <input
                      type="text"
                      className="form-input auth-input"
                      placeholder="e.g. alex_dev"
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                      required
                    />
                  </div>
                </div>

                <div className="form-field">
                  <label className="form-label required">Email Address</label>
                  <div className="auth-input-wrapper">
                    <Mail size={16} className="auth-input-icon" />
                    <input
                      type="email"
                      className="form-input auth-input"
                      placeholder="alex@example.com"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      required
                    />
                  </div>
                </div>
              </div>
            </>
          )}

          {mode === 'login' && (
            <div className="form-field">
              <label className="form-label required">Username or Email</label>
              <div className="auth-input-wrapper">
                <User size={16} className="auth-input-icon" />
                <input
                  type="text"
                  className="form-input auth-input"
                  placeholder="Enter your username or email"
                  value={usernameOrEmail}
                  onChange={(e) => setUsernameOrEmail(e.target.value)}
                  required
                />
              </div>
            </div>
          )}

          <div className="form-field">
            <label className="form-label required">Password</label>
            <div className="auth-input-wrapper">
              <Lock size={16} className="auth-input-icon" />
              <input
                type={showPassword ? 'text' : 'password'}
                className="form-input auth-input"
                placeholder={mode === 'register' ? 'At least 6 characters' : 'Enter your password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <button
                type="button"
                className="auth-password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                title={showPassword ? 'Hide password' : 'Show password'}
              >
                {showPassword ? <EyeOff size={16} /> : <Eye size={16} />}
              </button>
            </div>
          </div>

          {/* Demo Quick-Fill Bar */}
          {mode === 'login' && (
            <div className="auth-demo-bar">
              <span className="auth-demo-label">
                <Sparkles size={13} style={{ color: '#fbbf24' }} /> Quick Demo:
              </span>
              <button
                type="button"
                className="auth-demo-btn"
                onClick={() => handleFillDemo('john_dev', 'password123')}
              >
                john_dev
              </button>
              <button
                type="button"
                className="auth-demo-btn"
                onClick={() => handleFillDemo('demo_user', 'password123')}
              >
                demo_user
              </button>
            </div>
          )}

          {/* Submit Button */}
          <div className="auth-footer-actions">
            <button
              type="submit"
              className="btn btn-primary auth-submit-btn"
              disabled={submitting}
            >
              {submitting ? (
                <span>Authenticating...</span>
              ) : mode === 'login' ? (
                <>
                  <LogIn size={16} />
                  <span>Sign In</span>
                </>
              ) : (
                <>
                  <UserPlus size={16} />
                  <span>Create Account</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
