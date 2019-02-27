# Dockerfile

FROM jetty:9.4.14-jre11

ADD ./target/ghost.war /tmp/ghost.war
ADD ./src/etc/jetty-realm.properties /usr/local/jetty/etc/jetty-realm.properties
ADD ./src/etc/jetty-ghost-context.xml /var/lib/jetty/webapps/ghost.xml
