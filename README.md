# Jeux

## Compile/deploy

### Build (Maven)

- Run `mvn clean install` on _jeux-ejb_, _jeux-web_ and  _jeux-ear_, in sequence
- Deploy resulting EAR file on JBoss AS (_JBOSS_DIR/standalone/deployments_)

### Configure MySQL

- Create DB _jeuxdb_
- Import [_jeuxdb-empty.sql_](https://github.com/marthjod/jeux/blob/master/jeuxdb-empty.sql)
- Add user:

```sql
CREATE USER 'jeuxdb_user'@'localhost'
IDENTIFIED BY '***';

GRANT SELECT, INSERT, UPDATE, DELETE
ON `jeuxdb`.*
TO 'jeuxdb_user'@'localhost';
```

### Configure JBoss AS

#### Add data source
- Copy driver JAR ([mysql-connector-java-VERSION-bin.jar](http://dev.mysql.com/downloads/connector/j/)) to  _JBOSS_DIR/standalone/deployments_ first (**NB Does not work with versions >= 5.1.30, cf. https://bugzilla.redhat.com/show_bug.cgi?id=1107120**)

```xml
<!-- jboss-as-7.1.1.Final/standalone/configuration/standalone.xml -->
<datasource jta="false" jndi-name="java:jboss/datasources/MySQLDS" pool-name="MySQLDS" enabled="true" use-ccm="false">
    <connection-url>jdbc:mysql://HOST:PORT/jeux</connection-url>
    <driver-class>com.mysql.jdbc.Driver</driver-class>
    <driver>mysql-connector-java-VERSION-bin.jar</driver>
    <security>
        <user-name>jeuxdb_user</user-name>
        <password>***</password>
    </security>
    ...
</datasource>
```


#### Authentication

##### Handle users for HTTP Basic authentication

- Add to _JBOSS_DIR/standalone/configuration/standalone.xml_:

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

- Appropriate _users.properties_ and _roles.properties_ must be in JBoss config dir (_JBOSS_DIR/standalone/configuration/_)
- Adding a new administrative user:
    - _roles.properties_: `newuser=jeux-admin`
    - _users.properties_: `newuser=<cleartext password>`

#### Logging

- Set log level to DEBUG (_JBOSS_DIR/standalone/configuration/standalone.xml_)

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

- _JBOSS_DIR/standalone/configuration/standalone.xml_:
```xml
<virtual-server name="default-host" enable-welcome-root="false">
```


## Develop

- Use IDE with JBoss and Maven integration
