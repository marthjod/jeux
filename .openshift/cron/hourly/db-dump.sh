#!/bin/bash

mysqldump -h $OPENSHIFT_MYSQL_DB_HOST \
	-u $OPENSHIFT_MYSQL_DB_USERNAME \
 	-p"$OPENSHIFT_MYSQL_DB_PASSWORD" \
	-P $OPENSHIFT_MYSQL_DB_PORT \
	jeux \
	> $OPENSHIFT_DATA_DIR/jeux-$(date +%Y%m%d-%H%M%S).sql

