## Prerequisites

Make sure you have the following installed:
- Java JDK 17 or later
- Docker and Docker Compose

<!-- GETTING STARTED -->
## Getting Started
in Dockerfile  ==> COPY --from=build {{After running mvn clean install, please paste the copy path here}.jar} app.jar


# Start all containers
docker-compose up (in terminal).

(note)=> If the app doesn't work, try entering the database details, including the password, then connect and attempt to run the app in Docker.
