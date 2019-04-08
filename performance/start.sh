#!/bin/sh

# customer hub scripts under this section
cd /performance/customerhub/scripts
echo "Starting Performance Tests for Tetra Pak Customer Hub..."

echo "Starting /opt/apache-jmeter-5.0/bin/jmeter: Home Page"
nohup /app/apache-jmeter-5.1/bin/jmeter -n -t "customerhub/HomePage.jmx" -Jjmeter.save.saveservice.output_format=csv -n -l HomePage.csv &
echo "Starting /opt/apache-jmeter-5.0/bin/jmeter: All Pages"
nohup /app/apache-jmeter-5.1/bin/jmeter -n -t "customerhub/allPages.jmx" -Jjmeter.save.saveservice.output_format=csv -n -l allPages.csv &


# public web scripts under this section
cd /performance/publicweb/scripts
echo "Starting Performance Tests for Tetra Pak Public Web..."