#!/usr/bin/env bash
ip=192.168.1.72
cd ..
echo "Compiling Java..."
mvn clean package
if [ "$1" = "-clean" ]
then
    echo "Removing Existing Installation..."
    sshpass -p raspberry ssh pi@${ip} "cd /home/pi; rm -r scheduler; mkdir scheduler"
fi
echo "Uploading..."
sshpass -p raspberry scp start.sh pi@${ip}:scheduler
sshpass -p raspberry ssh pi@${ip} "cd /home/pi/scheduler; chmod +x start.sh"
sshpass -p raspberry scp -r target/scheduler/ pi@${ip}:
echo
echo "Deployed to $ip"
echo