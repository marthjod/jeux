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


