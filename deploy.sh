#!/usr/bin/env bash
ip=192.168.1.69
mvn clean package
sshpass -p raspberry ssh pi@${ip} "cd /home/pi/scheduler; rm -r target"
sshpass -p raspberry scp scheduler.sh pi@${ip}:scheduler
sshpass -p raspberry scp -r target/ pi@${ip}:scheduler