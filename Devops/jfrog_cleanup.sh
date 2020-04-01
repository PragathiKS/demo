#!/bin/bash
echo > result.txt
#DAYS_BEFORE=50
startdate=$(date +%Y%m%d -d "+1 days")
enddate=$(date +%Y%m%d -d "-$DAYS_BEFORE days")
d=
n=0
until [ "$d" = "$enddate" ]
do
    ((n++))
    d=$(date -d "$startdate - $n days" +%Y%m%d)
    echo $d
done | xargs > exclude.txt
EXCLUDE=`sed 's/ /|/g' exclude.txt`

for i in `grep "</version>$" maven-metadata.xml | awk '{gsub("<version>|</version>", "");print}' | egrep -v "$EXCLUDE"`; 
do echo $i; echo $packageName; echo $repoName >> result.txt ; done