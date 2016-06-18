#!/usr/bin/env bash
if [ "$1" = "" ]
then
    echo "No id"
    exit 1
fi
if [ "$2" = "" ]
then
    echo "No message"
    exit 1
fi
curl -d "$2" http://192.168.1.123:8001/scheduler/showers/message/replace/$1
echo