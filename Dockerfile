FROM dockerfile/java

RUN wget http://repo1.maven.org/maven2/org/eclipse/jetty/jetty-runner/9.2.0.M0/jetty-runner-9.2.0.M0.jar -O /root/jetty.jar

EXPOSE 8080

ADD target/geoservices-0.0.1-standalone.war /root/geoservices.war

CMD ["java" "-server" "-jar" "/root/jetty.jar" "/root/geoservices.war"]

