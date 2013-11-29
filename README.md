## JEUX README

### Import 3rd-party sources

1. Import ("Import/File system...") [`commons-math3-3.2.jar`](http://commons.apache.org/proper/commons-math) into _Jeux/EarContent_
3. Import [`commons-codec-1.8.jar`](http://commons.apache.org/proper/commons-codec) into _Jeux/EarContent_
3. Place [`jquery-2.0.3.js`](http://jquery.com/) under _JeuxWeb/WebContent/js_

### Deployment configuration after Eclipse import

Modify the following entries in _JeuxEJB/.classpath_ and _JeuxWeb/.classpath_ to reflect your local situation:

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


#### MySQL: Add user

```sql
CREATE USER 'jeuxdb_user'@'localhost' 
IDENTIFIED BY '***';

GRANT SELECT, INSERT, UPDATE, DELETE 
ON 'jeuxdb'.* 
TO 'jeuxdb_user'@'localhost' 
IDENTIFIED BY '***'
```


#### JBoss: Add data source

- Connection URL: `jdbc:mysql://localhost:3306/<database name>`
- Driver: `mysql-connector-java-<ver>-bin.jar`
- JNDI: `java:jboss/datasources/JeuxDS`
- User, password: see <a href="#mysql-add-user">MySQL</a>


### Authentication

#### JBoss: Handle users for HTTP Digest authentication

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
