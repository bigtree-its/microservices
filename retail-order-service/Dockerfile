FROM adoptopenjdk/openjdk11:ubi
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
ADD build/libs/order-service-0.0.1-SNAPSHOT.jar order-service.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -Dspring.profiles.active=container -jar order-service.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar order-service.jar
