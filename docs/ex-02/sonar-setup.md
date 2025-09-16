# Sonar Setup

**Note:** Make sure to [Set up the Server](server-setup.md) first.

## Initial Configuration

After navigating to the URL for the first time there will be a field where we can enter a new password. This will be the new admin password. 

## Set up Projects

Since we have a frontend and a backend we will need to create two projects. 

To Create a project click on "Create Project" in the top right corner and choose "local project". We chose the names "schuumapp-backend" and "schuumapp-frontend". We kept the project keys the same as the names. 

## Create Access Token

A Global Token will be needed to access the results of the SonarQube scans. To create a token click on the profile picture in the top right corner and then "My Account > Security".

Enter a token name, choose the type "Global Analysis Token" and click "Generate". 

Save this token for later as it will be needed during the setup of the SonarQube plugin in the [Jenkins Setup](jenkins-setup.md).

## Creating Webhooks

The SonarQube server needs to send a reply to the Jenkins server. We can achieve this by creating a webhook in SonarQube by clicking "Project Settings > Webhooks" in the top right corner and clicking on "Create". 

In the webhook URL field we need to enter the Jenkins SonarQube webhook URL. The URL is provided with the SonarQube Plugin that will be installed during the [Jenkins Setup](jenkins-setup.md). It has the format `https://<jenkins-url>/sonarqube-webhook/`.

The Webhooks need to be created in the backend and in the frontend project. 

## Adding SonarQube Scans to Builds

To create the project tokens used in the [Pipeline Setup](pipeline-setup.md) we can navigate to the respective project and click on "local" as the analysis method. After entering a name for the token we can copy it to the Jenkisfile to allow access to the project from the pipeline.

**NOTE:** Entering Secrets and Tokens in configurations files and committing them to git is **not** secure. Since this setup is used for educational purposes we selected to do it as it is easier and allows us to focus on the main goals of the project. The setup might change in the future to make it more secure.

