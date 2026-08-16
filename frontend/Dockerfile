# ===================================================================
# Multi-stage Dockerfile for JobTrack React/Vite Frontend
# Node.js 20 Alpine -> Nginx 1.27 Alpine Runtime
# ===================================================================

# -------------------------------------------------------------------
# Stage 1: Build Frontend Production Bundle
# -------------------------------------------------------------------
FROM node:20-alpine AS builder

WORKDIR /app

# Optimize layer caching: Install dependencies first
COPY package.json package-lock.json* ./
RUN npm ci

# Copy frontend source files
COPY . .

# Build production bundle with Vite
RUN npm run build

# -------------------------------------------------------------------
# Stage 2: Production Nginx Server
# -------------------------------------------------------------------
FROM nginx:1.27-alpine AS runner

# Install wget for container healthcheck
RUN apk add --no-cache wget

# Remove default Nginx static assets
RUN rm -rf /usr/share/nginx/html/*

# Copy compiled SPA static files from builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy custom Nginx configuration for SPA routing & API reverse proxy
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose HTTP port
EXPOSE 80

# Container healthcheck
HEALTHCHECK --interval=15s --timeout=5s --start-period=10s --retries=3 \
  CMD wget -q --spider http://localhost:80/ || exit 1

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
