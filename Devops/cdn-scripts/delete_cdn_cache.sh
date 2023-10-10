#!/bin/bash
username="supratim.patra@tetrapak.com"
apiKey='b_Das=E-@PH1P0O9iT!1'
date=`env LANG="en_US.UTF-8" date -u "+%a, %d %b %Y %H:%M:%S GMT"`
password=`echo -en "$date" | openssl dgst -sha1 -hmac $apiKey -binary | openssl enc -base64`
curl -i --url "https://api.cdnetworks.com/ccm/purge/ItemIdReceiver" \
-X "POST" \
-u "$username:$password" \
-H "Date:$date" \
-H "Content-Type: application/json" \
-d "{ \"dirs\": [ $1 ], \"dirAction\":\"expire\" }"