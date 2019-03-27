# Dockerfile

FROM jetty:9.4.14-jre11

ADD ./target/ghost.war $TMPDIR/ghost.war
ADD ./src/etc/jetty-realm.properties $JETTY_HOME/etc/jetty-realm.properties
ADD ./src/etc/jetty-ghost-context.xml $JETTY_BASE/webapps/ghost.xml
