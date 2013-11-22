## jeux README

### Import 3rd-party sources

1. Import `commons-math3-3.2.jar` into _Jeux/EarContent_
2. Import `gson-2.2.4.jar` into _Jeux/EarContent_
3. Place `jquery-2.0.3.js` under _JeuxWeb/WebContent/js_

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


#### <a name="mysql">MySQL: Add user</a>

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
- User, password: see <a href="#mysql">MySQL</a>


### Testing

- Use the handy cURL snippets in testing-rest-api.* to fire requests against the REST API, 
e.g. for quick tests of invalid input handling or debugging Ajax behavior.
