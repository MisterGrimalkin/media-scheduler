#!/usr/bin/env bash
if [ "$1" = "" ]
then
    echo "No message"
    exit 1
fi
curl -d "$1" http://192.168.1.123:8001/scheduler/showers/message/add
echo