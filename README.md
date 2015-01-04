# JEUX

## Compile/deploy

### Add 3rd-party sources

4. Copy [`jquery-VERSION.js`](http://jquery.com/) as `jquery.js` into _jeux-web/src/resources/js_
5. Copy [`bootstrap.css`](http://getbootstrap.com/) into _jeux-web/src/resources/css_

### Maven

- Run `mvn clean install` to build the EAR file to be deployed on the application server (e.g. in _.../jbossas/app-root/runtime/dependencies/jbossas/deployments_)

### Configure MySQL database

- Create DB _jeuxdb_
- Import _jeuxdb-empty.sql_
- Add user:

```sql
CREATE USER 'jeuxdb_user'@'localhost'
IDENTIFIED BY '***';

GRANT SELECT, INSERT, UPDATE, DELETE
ON `jeuxdb`.*
TO 'jeuxdb_user'@'localhost';
```

### Configure JBoss application server

#### Add data source
- Copy driver JAR ([mysql-connector-java-VERSION-bin.jar](http://dev.mysql.com/downloads/connector/j/)) to  _.../jboss-as-7.1.1.Final/standalone/deployments_ first

##### JBoss Management Console (http://localhost:9990/console/)
- Connection URL: `jdbc:mysql://localhost:3306/<database name>`
- Driver: `mysql-connector-java-VERSION-bin.jar`
- JNDI: `java:jboss/datasources/JeuxDS`
- User, password: see <a href="#mysql-add-user">MySQL</a>


##### -OR- manually

```xml
<!-- jboss-as-7.1.1.Final/standalone/configuration/standalone.xml -->
<datasource jta="false" jndi-name="java:jboss/datasources/JeuxDS" pool-name="JeuxDS" enabled="true" use-ccm="false">
    <connection-url>jdbc:mysql://localhost:3306/jeuxdb</connection-url>
    <driver-class>com.mysql.jdbc.Driver</driver-class>
    <driver>mysql-connector-java-VERSION-bin.jar</driver>
    <security>
        <user-name>jeuxdb_user</user-name>
        <password>***</password>
    </security>
    <validation>
        <validate-on-match>false</validate-on-match>
        <background-validation>false</background-validation>
    </validation>
    <statement>
        <share-prepared-statements>false</share-prepared-statements>
    </statement>
</datasource>
```


#### Authentication

##### [JBoss](http://www.jboss.org/jbossas/downloads): Handle users for HTTP Basic authentication

- Add to _.../jboss-as-7.1.1.Final/standalone/configuration/standalone.xml_:

```xml
<security-domains>
...
    <security-domain name="jeux-admin" cache-type="default">
        <authentication>
            <login-module code="UsersRoles" flag="required">
                <module-option name="usersProperties" value="${jboss.server.config.dir}/users.properties"/>
                <module-option name="rolesProperties" value="${jboss.server.config.dir}/roles.properties"/>
                <module-option name="realm" value="JEUX Administrative view"/>
            </login-module>
        </authentication>
    </security-domain>
...
</security-domains>
```

- Appropriate _users.properties_ and _roles.properties_ must be in JBoss config dir (_.../jboss-as-7.1.1.Final/standalone/configuration/_)
- Adding a new administrative user:
    - _roles.properties_: `newuser=jeux-admin`
    - _users.properties_: `newuser=<cleartext password>`

#### Logging

- Set log level to DEBUG (_.../jboss-as-7.1.1.Final/standalone/configuration/standalone.xml_)

```xml
...
    <profile>
        <subsystem xmlns="urn:jboss:domain:logging:1.1">
            <console-handler name="CONSOLE">
                <level name="DEBUG"/>
...
            <logger category="de.fhb.jeux">
                <level name="DEBUG"/>
            </logger>
```

#### Allow public access (USE AT YOUR OWN RISK)

- _.../jboss-as-7.1.1.Final/standalone/configuration/standalone.xml_:
```xml
<virtual-server name="default-host" enable-welcome-root="false">
```


## Develop

### Deployment configuration after Eclipse import

- If needed, modify the following entries in _JeuxEJB/.classpath_ and _JeuxWeb/.classpath_ to reflect your local situation:

```xml
<classpathentry kind="con"
    path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/java-7-openjdk-i386">
...
<classpathentry kind="con"
    path="org.eclipse.jst.server.core.container/org.jboss.ide.eclipse.as.core.server.runtime.runtimeTarget/JBoss 7.1 Runtime 1">
```

### JBoss

- Eclipse: install [JBoss Application Server Adapters](http://download.jboss.org/jbosstools/updates/webtools/kepler/)
    - Create new runtime environment _JBoss AS 7.1_ (local server)
    - _Listen on all interfaces..._
    - Add as _Targeted Runtime_ to projects

### Eclipse


- _Project facets_ should include _JPA_
- _ejbModule/META-INF/persistence.xml_ should exist and contain an entry for each used persistence entity, e.g.:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1" xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
	<persistence-unit name="JeuxEJB">
		<jta-data-source>java:jboss/datasources/MySQLDS</jta-data-source>
        	<class>de.fhb.jeux.model.ShowdownGroup</class>
	</persistence-unit>
</persistence>
```
