#===================================
#This is for Multi Stage Build
#===================================
#Stage 1: Build JAR
FROM eclipse-temurin:22-jdk-jammy AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Create a minimal Docker image with the JAR
FROM eclipse-temurin:22-jre-jammy
RUN groupadd -r cuecolabgroup && useradd -r -g cuecolabgroup cuecolabuser  \
    && mkdir -p /tmp && chown cuecolabuser:cuecolabgroup /tmp && chmod -R 777 /tmp
USER cuecolabuser
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar", "-Dspring-boot.run.profiles=mysql"]
