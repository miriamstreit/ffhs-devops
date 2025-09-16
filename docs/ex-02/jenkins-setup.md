# Jenkins setup

## Jenkins Server

**NOTE:** Make sure to [Set up SonarQube](sonar-setup.md) first.

## Plugins

### GitLab plugin setup

The following GitLab Jenkins plugins are needed for the integration of GitLab into Jenkins:

- GitLab API Plugin
- GitLab Authentication plugin
- GitLab Branch Source Plugin
- GitLab Plugin

After installing the plugins they can be configured under `Manage Jenkins > System`.

There are two areas with the header title "GitLab", so make sure to fill both out accordingly. 

Make sure to set up a "Personal Access Token" in GitLab and add it to the credentials in Jenkins.

### Sonarqube plugin setup

The following SonarQube Jenkins plugins needs to be installed for the SonarQube setup:

- SonarQube Scanner for Jenkins

 The plugin can be configured under `Manage Jenkins > System`.

 Fill in the form fields and save the settings. 

 **NOTE:** The process of creating the Server Access Token is described in the [SonarQube Setup Documentation](sonar-setup.md).

