#!/bin/sh
cd /performance/scripts
echo "Starting Performance Tests for Tetra Pak..."
echo "Starting /opt/apache-jmeter-5.0/bin/jmeter: Home Page"
nohup /app/apache-jmeter-5.1/bin/jmeter -n -t "HomePage.jmx" &
echo "Starting /opt/apache-jmeter-5.0/bin/jmeter: All Pages"
nohup /app/apache-jmeter-5.1/bin/jmeter -n -t "allPages.jmx" &
