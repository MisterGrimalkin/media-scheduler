#!/usr/bin/env bash
if [ "$1" = "" ]
then
    echo "No Id"
    exit 1
fi
curl -d "" http://192.168.1.123:8001/scheduler/showers/message/delete/$1
echo
