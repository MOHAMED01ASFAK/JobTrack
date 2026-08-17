#!/bin/sh
set -e

# ===================================================================
# JobTrack Frontend - Dynamic Backend Upstream Resolver
# Automatically configures the Nginx proxy_pass upstream target
# Supports local Docker Compose (backend:8080) and Railway / Cloud URLs
# ===================================================================

TARGET_URL=""

if [ -n "$BACKEND_URL" ]; then
    TARGET_URL="$BACKEND_URL"
elif [ -n "$BACKEND_HOST" ]; then
    PORT="${BACKEND_PORT:-8080}"
    TARGET_URL="http://${BACKEND_HOST}:${PORT}"
fi

# Remove trailing slash if provided to prevent double slashes
TARGET_URL="$(echo "$TARGET_URL" | sed 's:/*$::')"

if [ -n "$TARGET_URL" ] && [ "$TARGET_URL" != "http://backend:8080" ]; then
    echo "[JobTrack Nginx] Configuring dynamic backend upstream target: ${TARGET_URL}"
    sed -i "s|http://backend:8080|${TARGET_URL}|g" /etc/nginx/conf.d/default.conf
else
    echo "[JobTrack Nginx] Using default Docker Compose backend upstream: http://backend:8080"
fi
