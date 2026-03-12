#!/usr/bin/env bash
# Run the backend with JWT_SECRET set (generates one if not set).
# Usage: ./run.sh   or   ./run.sh --skip-tests   (for mvn spring-boot:run -DskipTests)

set -e
cd "$(dirname "$0")"

if [ -z "${JWT_SECRET}" ]; then
  export JWT_SECRET=$(openssl rand -hex 32)
  echo "JWT_SECRET was not set; generated a temporary one for this run."
fi

if [ -x "./mvnw" ]; then
  ./mvnw spring-boot:run "$@"
else
  mvn spring-boot:run "$@"
fi
