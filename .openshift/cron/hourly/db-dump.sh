#!/bin/bash

db_name="jeux"
enabled=0
if [ $enabled -ne 1 ]; then
    exit
fi

dumpfile=$OPENSHIFT_DATA_DIR/$db_name-$(date +%FT%T).sql
mysqldump -h $OPENSHIFT_MYSQL_DB_HOST \
	-u $OPENSHIFT_MYSQL_DB_USERNAME \
 	-p"$OPENSHIFT_MYSQL_DB_PASSWORD" \
	-P $OPENSHIFT_MYSQL_DB_PORT \
	$db_name \
	> $dumpfile
echo "Dumped to $dumpfile."

