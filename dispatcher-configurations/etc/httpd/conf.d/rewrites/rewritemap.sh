#!/bin/bash

# Configuration values
cd /etc/httpd/conf.d/rewrites
PUBLISHER_IP=10.45.2.4
LOG_DIR=/var/log/httpd
rm -rvf redirectMap.txt rewritemap.dbm.dir rewritemap.dbm.pag
# Update the redirect maps
wget http://10.45.2.4:4503/etc/acs-commons/redirect-maps/publicweb-redirect-map/jcr:content/redirectMap.txt >> ${LOG_DIR}/update-redirect-map.log 2>&1
httxt2dbm -i redirectMap.txt -o rewritemap.dbm >> ${LOG_DIR}/update-redirect-map.log 2>&1
#systemctl restart httpd


