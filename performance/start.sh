#!/bin/sh

# customer hub scripts under this section
echo "Starting Performance Tests for Tetra Pak Customer Hub..."

echo "Starting /opt/apache-jmeter-5.0/bin/jmeter: Home Page"
nohup /app/apache-jmeter-5.1/bin/jmeter -n -t "./performance/customerhub/scripts/HomePage.jmx" -Jjmeter.save.saveservice.output_format=csv -o "./performance/customerhub/results/HtmlReport" &

echo "Starting /opt/apache-jmeter-5.0/bin/jmeter: All Pages"
nohup /app/apache-jmeter-5.1/bin/jmeter -n -t "./performance/customerhub/scripts/allPages.jmx" -Jjmeter.save.saveservice.output_format=csv -o "./performance/customerhub/results/HtmlReport" &


# public web scripts under this section
echo "Starting Performance Tests for Tetra Pak Public Web..."
