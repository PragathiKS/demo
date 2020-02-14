#!/bin/bash
count=`docker ps -a | grep zap | wc -l`

if [ $count -gt 00 ]
then
    docker stop zap
    docker rm zap
fi
