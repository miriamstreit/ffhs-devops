# Server Setup

## Architecture

For the system we are using a setup on a HomeLab server since it gives us the most possibilities and could be used in the future for other projects.

To run the different services we are using Docker Compose files. The services can then be accessed locally through their respective ports. 

## Public Access

Since the services need to be accessed by all team members they need to be publically available. This is achieved through a [Cloudflare Zero Trust Tunnel](https://www.cloudflare.com/en-gb/products/tunnel/) with the URLs mapped onto their respective ports. 

The following URLs are used throughout the project:

- https://jenkins.sw3.ch/ - Jenkins
- https://sonar.sw3.ch/ - SonarQube

## Docker Compose File

Because the services need to communicate with eachother we can use a single Docker Compose file to attach them to the same Docker Network.

The following file was used to set up all services. Comments were added where more explenation is needed. 

```yaml
services:
  jenkins:
    container_name: jenkins
    restart: unless-stopped
    image: jenkins/jenkins:lts-jdk17
    # needs to run as root to run docker containers for the pipeline builds
    privileged: true
    user: root
    ports:
      - "8976:8080"
      - "8977:50000"
    volumes:
      - <local-path>/data/jenkins_home/:/var/jenkins_home
      - /var/run/docker.sock:/var/run/docker.sock
      - /usr/bin/docker:/usr/bin/docker
    environment:
      - JENKINS_EXT_URL=http://localhost:8976
  sonarqube:
    container_name: sonarqube
    restart: unless-stopped
    image: sonarqube:community
    depends_on:
      - db
    ports:
      - "9000:9000"
    environment:
      SONAR_JDBC_URL: jdbc:postgresql://db:5432/sonar
      SONAR_JDBC_USERNAME: sonar
      SONAR_JDBC_PASSWORD: sonar
    volumes:
      - <local-path>/data/sonarqube_data:/opt/sonarqube/data
      - <local-path>/data/sonarqube_extensions:/opt/sonarqube/extensions
      - <local-path>/data/sonarqube_logs:/opt/sonarqube/logs
  db:
    image: postgres:12
    environment:
      POSTGRES_USER: sonar
      POSTGRES_PASSWORD: sonar
    volumes:
      - <local-path>/data/postgresql:/var/lib/postgresql
      - <local-path>/data/postgresql_data:/var/lib/postgresql/data
volumes:
  jenkins_home:
  sonarqube_data:
  sonarqube_extensions:
  sonarqube_logs:
  postgresql:
  postgresql_data:
```

Sometimes there are problems with permissions of the different directories, so we created them ahead of time and set the permissions manually. 

When in the directory containing the Docker Compose file the services can be started with the command `docker compose up -d`. The `-d` flag starts the services detached. Without it the services would stop when exiting the container Shell.

