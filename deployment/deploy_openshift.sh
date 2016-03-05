#!/bin/bash

set -e

mysql --host=$OPENSHIFT_MYSQL_DB_HOST --port=$OPENSHIFT_MYSQL_DB_PORT --user=$OPENSHIFT_MYSQL_DB_USERNAME --password=$OPENSHIFT_MYSQL_DB_PASSWORD -e "DROP DATABASE jeux; CREATE DATABASE jeux;"
wget https://raw.githubusercontent.com/marthjod/jeux/master/jeuxdb-empty.sql -O /tmp/jeuxdb-empty.sql
mysql jeux < /tmp/jeuxdb-empty.sql
rm /tmp/jeuxdb-empty.sql

read -p "JEUX admin " admin
read -p "$admin's password: " password

echo "$admin=jeux-admin" > $OPENSHIFT_JBOSSAS_DIR/standalone/configuration/roles.properties
echo "$admin=$password" > $OPENSHIFT_JBOSSAS_DIR/standalone/configuration/users.properties

$OPENSHIFT_REPO_DIR/.openshift/action_hooks/build
