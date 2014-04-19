## JEUX README

### Import 3rd-party sources

1. Import ("Import/File system...") [`commons-math3-3.2.jar`](http://commons.apache.org/proper/commons-math) into _Jeux/EarContent_
2. Import [`commons-codec-1.8.jar`](http://commons.apache.org/proper/commons-codec) into _Jeux/EarContent_
3. Import [`gson-2.2.4.jar`](http://code.google.com/p/google-gson/) into _Jeux/EarContent_
4. Import [`jquery-VERSION.js`](http://jquery.com/) as `jquery.js` into _JeuxWeb/WebContent/js_
5. Import [`bootstrap.css`](http://getbootstrap.com/) into _JeuxWeb/WebContent/css_

### Deployment configuration after Eclipse import

- If needed, modify the following entries in _JeuxEJB/.classpath_ and _JeuxWeb/.classpath_ to reflect your local situation:

```xml
<classpathentry kind="con" 
    path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/java-7-openjdk-i386">
...
<classpathentry kind="con"
    path="org.eclipse.jst.server.core.container/org.jboss.ide.eclipse.as.core.server.runtime.runtimeTarget/JBoss 7.1 Runtime 1">
```

### Persistence

#### Eclipse


- _Project facets_ should include _JPA_
- _ejbModule/META-INF/persistence.xml_ should exist and contain an entry for each used persistence entity, e.g.:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1" xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
	<persistence-unit name="JeuxEJB">
		<jta-data-source>java:jboss/datasources/JeuxDS</jta-data-source>
        	<class>de.fhb.jeux.model.ShowdownGroup</class>
	</persistence-unit>
</persistence>
```


#### MySQL

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


### JBoss

- Eclipse: install [JBoss Application Server Adapters](http://download.jboss.org/jbosstools/updates/webtools/kepler/)
    - Create new runtime environment _JBoss AS 7.1_ (local server)
    - _Listen on all interfaces..._
    - Add as _Targeted Runtime_ to projects

#### Add data source
- Copy driver JAR ([mysql-connector-java-VERSION-bin.jar](http://dev.mysql.com/downloads/connector/j/)) to  _.../jboss-as-7.1.1.Final/standalone/deployments_ first

##### JBoss Management Console (http://localhost:9999 if working)
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
                

### Authentication

#### [JBoss](http://www.jboss.org/jbossas/downloads): Handle users for HTTP Digest authentication

- Add to _.../jboss-as-7.1.1.Final/standalone/configuration/standalone.xml_:

```xml
<security-domains>
...
    <security-domain name="JeuxWeb" cache-type="default">
        <authentication>
            <login-module code="UsersRoles" flag="required">
                <module-option name="usersProperties" value="users.properties"/>
                <module-option name="rolesProperties" value="roles.properties"/>
                <module-option name="realm" value="JEUX Administrative view"/>
                <module-option name="hashAlgorithm" value="MD5"/>
                <module-option name="hashEncoding" value="RFC2617"/>
                <module-option name="hashUserPassword" value="false"/>
                <module-option name="hashStorePassword" value="true"/>
                <module-option name="passwordIsA1Hash" value="true"/>
                <module-option name="storeDigestCallback" value="org.jboss.security.auth.callback.RFC2617Digest"/>
            </login-module>
        </authentication>
    </security-domain>
...
</security-domains>
```

- Cf. [https://community.jboss.org/message/744521?_sscc=t](https://community.jboss.org/message/744521?_sscc=t)
- Appropriate _users.properties_ and _roles.properties_ must be in class path (OR put under _properties/_ and configure deployment assembly accordingly, i.e. _properties/_ -> _WEB-INF/classes_)
- Add new administrative user: 
```bash
# By default the properties realm expects the entries to be in the format: -
# username=HEX( MD5( username ':' realm ':' password))
hash=`python -c "import hashlib; print hashlib.md5('newuser' + ':' + 'JEUX Administrative view' + ':' + '***').hexdigest()"`
```
- _roles.properties_: `newuser=jeux-admin`
- _users.properties_: `newuser=$hash`

### Logging

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

### JBoss: public access (USE AT YOUR OWN RISK)

- _.../jboss-as-7.1.1.Final/standalone/configuration/standalone.xml_: 
```xml
<virtual-server name="default-host" enable-welcome-root="false">
```



