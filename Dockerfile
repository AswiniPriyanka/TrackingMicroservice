FROM openjdk:8
EXPOSE 8080
ADD target/tracking-microservice.jar tracking-microservice.jar
ENTRYPOINT ["java","-jar","/tracking-microservice.jar"]