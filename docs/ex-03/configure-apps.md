# Configure the apps to take connection strings as ENV vars

To be able to deploy the apps to different environments and different stages they need to be configurable from an external source so the same image can be reused. This inherently works for the backend as every Spring Boot variable can also be set as an environment variable. It is not that easy for the frontend.

## Understanding how the frontend works

For local builds the frontend will need some kind of configuration on how to access the backend. This could be either hardcoded all over the code or centralized in a single configuration file.

To run the frontend as a container, the frontend is built and then packaged into an nginx server image. When the user calls the website the frontend will be delivered to their browser once and will then run from there. Using env variables won't work out of the box because the app doesn't know about them.

## Implementing a solution

### Centralized configuration for local builds

We want every configuration in the same place, namely within the file [../../frontend/public/env-config.js](../../frontend/public/env-config.js)

Then we can access the variable from everywhere in the app like this: `window.VITE_API_URL`.

It's best to configure the values needed for the local development in this file so there's no need for extra efforts before the app can be run.

### Overriding the local configuration

Because the frontend doesn't know about the environment variables within the nginx server and the nginx server knows about them but doesn't implement any logic to pass them to the frontend, we need to implement this ourselves.

For that we create the file [../../frontend/docker/generate-env-file.sh](../../frontend/docker/generate-env-file.sh). This script is responsible for picking up any environment variable matching the names of the local environment variables and for replacing the local variables with the new values. This script needs to be called from somewhere and we will do that from the Docker entrypoint.

In the [frontend Dockerfile](../../frontend/Dockerfile) we copy the script into the `docker-entrypoint.d` folder. Each script within this folder will automatically be executed once the container starts up. This does not require a rebuild because the env variables can be passed from outside the container and are not hardcoded inside the container.

### Setting a variable from outside the container

Just as we did in the [../../docker-compose.yml](../../docker-compose.yml) file, we can now override every env variable set in [../../frontend/public/env-config.js](../../frontend/public/env-config.js) by passing it as an environment variable with the same name to the container.
