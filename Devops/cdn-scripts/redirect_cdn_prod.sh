#!/bin/bash
username="tppwcdnuser"
apiKey=""
date=`env LANG="en_US.UTF-8" date -u "+%a, %d %b %Y %H:%M:%S GMT"`
password=`echo -en "$date" | openssl dgst -sha1 -hmac $apiKey -binary | openssl enc -base64`
curl -i --url "https://api.cdnetworks.com/api/config/InnerRedirect/www.tetrapak.com" \
-X "PUT" \
-u "$username:$password" \
-H "Date: $date" \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-d '{
    "rewrite-rule-settings": [
{
        "path-pattern":"pa($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/pa($|/.*)",
        "after-value":"301:https://www.tetrapak.com/es-pa$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"chfr($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/chfr($|/.*)",
        "after-value":"301:https://www.tetrapak.com/fr-ch$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"ca($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/ca($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-ca$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"pt($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/pt($|/.*)",
        "after-value":"301:https://www.tetrapak.com/pt-pt$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"co($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/co($|/.*)",
        "after-value":"301:https://www.tetrapak.com/es-co$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"cn($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/cn($|/.*)",
        "after-value":"301:https://www.tetrapak.com/zh-cn$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"se($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/se($|/.*)",
        "after-value":"301:https://www.tetrapak.com/sv-se$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"ie($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/ie($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-ie$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"in($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/in($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-in$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"ec($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/ec($|/.*)",
        "after-value":"301:https://www.tetrapak.com/es-ec$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":".*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"900",
        "before-value":"^http://[^/]+(/.*)",
        "after-value":"301:https://www.tetrapak.com$1",
        "rewrite-type":"multi-matching",
        "request-header":"Host ^www.tetrapak.com$",
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"hr($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/hr($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-hr$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"jp($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/jp($|/.*)",
        "after-value":"301:https://www.tetrapak.com/ja-jp$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"id($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/id($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-id$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"gr($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/gr($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-gr$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"pl($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/pl($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-pl$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"br($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/br($|/.*)",
        "after-value":"301:https://www.tetrapak.com/pt-br$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"be($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/be($|/.*)",
        "after-value":"301:https://www.tetrapak.com/fr-be$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"uk($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/uk($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-gb$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"cz($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/cz($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-cz$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"it($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/it($|/.*)",
        "after-value":"301:https://www.tetrapak.com/it-it$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"fr($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/fr($|/.*)",
        "after-value":"301:https://www.tetrapak.com/fr-fr$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"cl($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/cl($|/.*)",
        "after-value":"301:https://www.tetrapak.com/es-cl$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"sk($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/sk($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-sk$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"ar($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/ar($|/.*)",
        "after-value":"301:https://www.tetrapak.com/es-ar$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"kr($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/kr($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-kr$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"ir($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/ir($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-ir$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"tw($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/tw($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-tw$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"hu($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/hu($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-hu$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"vn($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/vn($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-vn$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"ro($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/ro($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-ro$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"tr($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/tr($|/.*)",
        "after-value":"301:https://www.tetrapak.com/tr-tr$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"amp($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/amp($|/.*)",
        "after-value":"301:https://www.tetrapak.com$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"za($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/za($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-za$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"it/sustainability/good-choice$",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"100",
        "before-value":"^https://www.tetrapak.com/it/sustainability/good-choice$",
        "after-value":"301:https://www.tetrapak.com/it-it/sustainability/planet/environmental-impact/a-value-chain-approach/life-cycle-assessment/lca-examples",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"sa($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/sa($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-sa$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"pa-sp($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/pa-sp($|/.*)",
        "after-value":"301:https://www.tetrapak.com/es$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"lv($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/lv($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-lv$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"th($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/th($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-th$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"my($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/my($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-my$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"mx($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/mx($|/.*)",
        "after-value":"301:https://www.tetrapak.com/es-mx$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"us($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/us($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-us$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"nl($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/nl($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-nl$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"ch($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/ch($|/.*)",
        "after-value":"301:https://www.tetrapak.com/fr-ch$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"pk($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/pk($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-pk$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"fi($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/fi($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-fi$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"py($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/py($|/.*)",
        "after-value":"301:https://www.tetrapak.com/es-py$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"ve($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/ve($|/.*)",
        "after-value":"301:https://www.tetrapak.com/es-ve$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"rs($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/rs($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-rs$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"pe($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/pe($|/.*)",
        "after-value":"301:https://www.tetrapak.com/es-pe$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"sg($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/sg($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-sg$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
},
{
        "path-pattern":"au($|/).*",
        "except-path-pattern":null,
        "ignore-letter-case":"true",
        "publish-type":"Cache",
        "priority":"10",
        "before-value":"^https://www.tetrapak.com/au($|/.*)",
        "after-value":"301:https://www.tetrapak.com/en-au$1",
        "rewrite-type":"before",
        "request-header":null,
        "exception-request-header":null,
        "specify-url":null
}
    ]
}'


