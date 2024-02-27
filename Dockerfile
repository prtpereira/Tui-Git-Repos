FROM maven:3.8.4-openjdk-17 as build

ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN mvn clean package -DskipTests


FROM openjdk:17-oracle as release_img

COPY --from=build /app/target/gitmanager-0.0.1-SNAPSHOT.jar gitmanager.jar
COPY .mvn/ /app/.mvn
COPY mvnw pom.xml /app/

ENTRYPOINT ["java","-jar","/gitmanager.jar"]