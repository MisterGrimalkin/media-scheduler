#!/usr/bin/env bash
echo "Starting Server..."
sshpass -p raspberry ssh pi@192.168.1.72 "cd /home/pi/scheduler; ./start.sh >server.log 2>server-error.log" &
echo