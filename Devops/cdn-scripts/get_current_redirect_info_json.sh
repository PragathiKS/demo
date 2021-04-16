#!/bin/bash
####################################################################
# How to use:                                                      #
# get_current_redirect_info_json.sh <domain name>                  #
# eg. get_current_redirect_info_json.sh www-dev.tetrapak.com       #
####################################################################
username="tppwcdnuser"
apiKey=""
date=`env LANG="en_US.UTF-8" date -u "+%a, %d %b %Y %H:%M:%S GMT"`
password=`echo -en "$date" | openssl dgst -sha1 -hmac $apiKey -binary | openssl enc -base64`
curl -i --url "https://api.cdnetworks.com/api/config/InnerRedirect/$1" \
-X "GET" \
-u "$username:$password" \
-H "Date: $date" \
-H "Accept: application/json" \
-H "Content-Type:application/json"
