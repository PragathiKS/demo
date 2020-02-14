
cat PallyUrlsPublicWeb.txt | while read LINE; do


if [[ -z $LINE ]]
then
exit
fi
echo "url to test : $LINE"

docker exec zap zap-cli spider "$LINE":
done

