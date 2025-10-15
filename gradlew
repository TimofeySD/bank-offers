#!/usr/bin/env bash

set -euo pipefail

GRADLE_VERSION=${GRADLE_VERSION:-8.8}
export GRADLE_OPTS="-Dorg.gradle.daemon=true ${GRADLE_OPTS:-}"

if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
else
  echo "Gradle is required to run this build. Please install Gradle "${GRADLE_VERSION}" or higher." >&2
  exit 1
fi
