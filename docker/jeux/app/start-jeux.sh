#!/bin/bash

# Configuration and start script for JEUX
#
# ENVs:
# MYSQL_ROOT_PASSWORD
# JEUX_DB_USER
# JEUX_DB_PASS
# JEUX_DB_CONN_URL
# i.e.
# -e MYSQL_ROOT_PASSWORD=secret -e JEUX_DB_USER=jeuxdb_user -e JEUX_DB_PASS=also-secret -e JEUX_DB_CONN_URL=jdbc:mysql://localhost:3306/jeuxdb

function handle_err () {
	err=$1; shift
	if [ $err -ne 0 ]; then
		exit $err
	fi
}

if [ -z "$MYSQL_ROOT_PASSWORD" ] || [ -z "$JEUX_DB_USER" ] || [ -z "$JEUX_DB_PASS" ] || [ -z "$JEUX_DB_CONN_URL" ]; then
	echo "One or more mandatory env vars not set:"
	echo "MYSQL_ROOT_PASSWORD JEUX_DB_USER JEUX_DB_PASS JEUX_DB_CONN_URL"
	handle_err 2
fi

export APPSERVER_IP=`hostname --ip-address`
wildfly_conf=/opt/wildfly/standalone/configuration/standalone.xml
tmp_sql=/tmp/db_setup.sql

echo "Starting MySQL"
service mysql start
handle_err $?

echo "Setting up JEUX DB, DB user and importing schema"
mysqladmin -u root password "$MYSQL_ROOT_PASSWORD"
handle_err $?

echo "CREATE DATABASE jeuxdb;" > $tmp_sql
echo "CREATE USER '$JEUX_DB_USER'@localhost IDENTIFIED BY '$JEUX_DB_PASS';" >> $tmp_sql
echo "GRANT SELECT, INSERT, UPDATE, DELETE ON jeuxdb.* TO '$JEUX_DB_USER'@localhost;" >> $tmp_sql
mysql -u root -p"$MYSQL_ROOT_PASSWORD" < $tmp_sql
err=$?
# rm temp file first before exiting potentially
rm -f $tmp_sql
handle_err $err
mysql -u root -p"$MYSQL_ROOT_PASSWORD" jeuxdb < /jeux/jeuxdb-empty.sql
handle_err $?

echo "Adjusting $wildfly_conf"
perl -i -pe "s#JEUX_DB_CONN_URL#$JEUX_DB_CONN_URL#" $wildfly_conf
perl -i -pe "s#JEUX_DB_USER#$JEUX_DB_USER#" $wildfly_conf
perl -i -pe "s#JEUX_DB_PASS#$JEUX_DB_PASS#" $wildfly_conf
echo "Using $APPSERVER_IP for public interface"
perl -i -pe "s#PUBLIC_IP#$APPSERVER_IP#" $wildfly_conf

echo "Starting /opt/wildfly/bin/standalone.sh"
/opt/wildfly/bin/standalone.sh &

echo "When WildFly has completed start-up, go to Docker host's http://localhost:8080/JeuxWeb/"

# keep container running
while ( true )
    do
    echo "Detach with Ctrl-p Ctrl-q. Dropping to shell"
    sleep 1
    /bin/bash
done