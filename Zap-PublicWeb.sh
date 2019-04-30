
cat PallyUrls-PublicWeb.txt | while read LINE; do


if [[ -z $LINE ]]
then
exit
fi
echo "url to test : $LINE"

zap-cli --api-key 12345 spider "$LINE" done > zap-publicweb.html

