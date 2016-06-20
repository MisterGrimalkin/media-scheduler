#!/usr/bin/env bash
echo "Shutting down server..."
curl -d "" http://192.168.1.72:8001/scheduler/control/shutdown
echo
echo