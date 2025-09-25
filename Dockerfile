FROM eclipse-temurin:21-jdk-jammy

RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/* \

ENV JAVA_HOME=/usr/local/openjdk-21
ENV PATH=$JAVA_HOME/bin:$PATH

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/urlShortener-0.0.1-SNAPSHOT.jar"]
