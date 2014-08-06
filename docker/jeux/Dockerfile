# Some WildFly setup taken from jboss/wildfly (Fedora-based) image
FROM ubuntu:trusty

MAINTAINER marthjod@gmail.com

RUN echo "deb http://debian.charite.de/ubuntu/ trusty main restricted universe" > /etc/apt/sources.list
RUN echo "deb http://debian.charite.de/ubuntu/ trusty-updates main restricted universe" >> /etc/apt/sources.list
RUN DEBIAN_FRONTEND=noninteractive apt-get update && apt-get install -fy \
	openjdk-7-jre-headless \
	libaugeas-dev xmlstarlet libsaxon-java \
	mysql-server mysql-client \
	vim sudo git wget

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 8.1.0.Final

# Add the WildFly distribution to /opt
RUN cd /opt && wget -qO- http://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz | tar zx

# Make sure the distribution is available from a well-known place
RUN ln -s /opt/wildfly-$WILDFLY_VERSION /opt/wildfly

# Set the JBOSS_HOME env variable
ENV JBOSS_HOME /opt/wildfly

# Create the wildfly user and group
RUN groupadd -r wildfly -g 433 && useradd -u 431 -r -g wildfly -d /opt/wildfly -s /sbin/nologin -c "WildFly user" wildfly

# Change the owner of the /opt/wildfly directory
RUN chown -R wildfly:wildfly /opt/wildfly*

# Expose the ports we're interested in
EXPOSE 8080 9990

# JEUX app
RUN git clone https://github.com/marthjod/jeux.git /jeux
# TODO this wont work with only the source at hand
# has to be compiled from source here
ADD app/Jeux.ear /opt/wildfly/standalone/deployments/

# mysql driver
ADD app/mysql-module.xml /opt/wildfly/modules/system/layers/base/com/mysql/main/module.xml
# /tmp does not persist between image build steps
WORKDIR /jeux/tmp
RUN wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.31.tar.gz -O mysql-connector-java-5.1.31.tar.gz
RUN tar xfz mysql-connector-java-5.1.31.tar.gz mysql-connector-java-5.1.31/mysql-connector-java-5.1.31-bin.jar --strip-components=1
RUN mv mysql-connector-java-5.1.31-bin.jar /opt/wildfly/modules/system/layers/base/com/mysql/main/
RUN rm /jeux/tmp/*

# appl. sever
ADD app/standalone-wildfly.xml /opt/wildfly/standalone/configuration/standalone.xml

# app start (modify config, start services...)
ADD app/start-jeux.sh /jeux/

WORKDIR /jeux
RUN chmod +x start-jeux.sh
CMD ./start-jeux.sh
