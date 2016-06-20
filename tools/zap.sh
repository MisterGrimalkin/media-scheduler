#!/usr/bin/env bash
curl -d "$1" http://192.168.1.72:8001/scheduler/control/zap
echo