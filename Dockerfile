FROM tomcat:jdk8-openjdk
COPY calm.war /usr/local/tomcat/webapps/
#COPY calm_import.war /usr/local/tomcat/webapps/
COPY core/src/main/resources/default.properties /usr/local/tomcat/webapps/
