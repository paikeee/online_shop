FROM maven:3.8.2-jdk-8

WORKDIR /online_shop
COPY . .
RUN mvn clean install
EXPOSE 8080
CMD mvn spring-boot:run