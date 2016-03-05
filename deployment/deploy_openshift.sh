#!/bin/bash

set -e

mysql="mysql --host=$OPENSHIFT_MYSQL_DB_HOST --port=$OPENSHIFT_MYSQL_DB_PORT --user=$OPENSHIFT_MYSQL_DB_USERNAME --password=$OPENSHIFT_MYSQL_DB_PASSWORD"

echo "Dropping DB"
$mysql -e "DROP DATABASE jeux; CREATE DATABASE jeux;"
echo "Fetching DB schema"
wget https://raw.githubusercontent.com/marthjod/jeux/master/jeuxdb-empty.sql -O /tmp/jeuxdb-empty.sql
echo "Importing DB schema"
$mysql jeux < /tmp/jeuxdb-empty.sql
rm /tmp/jeuxdb-empty.sql

echo "Fecthing bootstrap CSS"
wget https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css -O $OPENSHIFT_REPO_DIR/jeux-web/WebContent/css/bootstrap.min.css

read -p "JEUX admin: " admin
read -p "$admin's password: " password

echo "Setting credentials"
echo "$admin=jeux-admin" > $OPENSHIFT_JBOSSAS_DIR/standalone/configuration/roles.properties
echo "$admin=$password" > $OPENSHIFT_JBOSSAS_DIR/standalone/configuration/users.properties

read -p "JEUX_SERVER_SECRET: " ssecret
echo $ssecret > $HOME/.env/user_vars/JEUX_SERVER_SECRET

echo "Building & redeploying app"
$OPENSHIFT_REPO_DIR/.openshift/action_hooks/build
