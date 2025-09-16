# Using ZAP in a Jenkins Pipeline

There are many ways to implement ZAP to automate security testing. We decided to use ZAP inside a Docker container and implement the scans using their [Automation Framework](https://www.zaproxy.org/docs/automate/automation-framework/). It allows a declarative definition of the tests in a YAML file. 

## Changes in the Pipeline

The ZAP scan will need to be implemented in two stages, the test itself and the publication of the test report. 

Because of the way we configured our system for this project bundled in a Docker-Compose file there are some nuances in the configuration.

To allow Jenkins to create containers and manage the data on the host it's running on, the container it's running in had to be started with root privileges. This means that all the directories used by jenkins are available only for the root user. 

Since the ZAP container is using a non-root user (zap) it has no access the directories on the host created by Jenkins. Simply running the ZAP container as root wouldn't work, because of ZAP's use of Firefox's headless mode, which is not allowed to be run by the root user. 

With all those constraints and the need to mount the file containing the test definitions to the contiainer the following implementation is used for the test:

```groovy
    stage('Security Test - ZAP - develop') {
      agent {
        docker {
          image 'zaproxy/zap-stable:2.15.0'
          args '-v ${JENKINS_HOST_WORKDIR}/env/develop/zap-wrk:/zap/wrk:rw --user root'
        }
      }
      steps {
        echo 'Running ZAP security tests...'
        sh """
        mkdir -p /zap/wrk/reports 
        chown -R zap:zap /zap/wrk/reports
        rm -rf /zap/wrk/reports/*
        su zap -c 'zap.sh -addonupdate -addoninstall reports -cmd -autorun /zap/wrk/zap.yaml' 
        """
      }
    }
```

1. The directory containing the test definition is mounted into the container using the working directory path on the physical host.
2. The container is started as root so it is able to read the contents of the mounted path
3. A directory is created in the mounted location and the ownership is transfered to the `zap` user and is emptied to clear out previous reports.
4. Using the `su` command, the `zap.sh` script is started as the `zap` user, to avoid the problem of Firefox not working for the root user. 

## Publishing the Results

To publish the results we used the same method as in the JMeter part - the `publishHTML` step. 

```groovy
    stage('Publish ZAP Report - Develop') {
      when {
        branch 'develop'
      }
      steps {
        publishHTML(target: [
          allowMissing: false,
          alwaysLinkToLastBuild: true,
          keepAll: true,
          reportDir: 'env/develop/zap-wrk/reports',
          reportFiles: 'zap-report.html',
          reportName: 'ZAP Scan Report - Develop'
        ])
      }
    }
```

## Prerequisits for the Scans

To successfully run the scans on our application we needed it to run as well as being accessible to the docker container. After rewriting some parts of the application to use values included in config files instead of hard-coded values, we created a [docker-compose file](../../env/develop/docker-compose.yaml) and start it manually. This step can be automated in the future to make the deployment automatic and apply changes to the application after running the necessary steps.

Just like Jenkins and Sonarqube, we created a subdomain for the different environments and made them available publically using Cloudflares Zero Trust Tunnels (see [Server Setup](../ex-02/server-setup.md) for more information).

## Running Tests

The tests are all defined in the [zap.yaml](../../env/develop/zap-wrk/zap.yaml) file.

```yaml
env:
  contexts:
    - name: "develop"
      urls:
      - "https://schuumapp-develop.sw3.ch/"
      includePaths:
      - "https://schuumapp-develop.sw3.ch/.*"
  parameters:
    failOnError: true
    failOnWarning: false
    continueOnFailure: false
    progressToStdout: true
jobs:
  - type: "activeScan"
    name: "develop-active-scan"
    parameters:
      context: "develop"
    policyDefinition:
      rules: []
  - type: "spiderAjax"
    name: "develop-spider"
    parameters:
      context: "develop"
    tests:
      - name: "At least 50 URLs found"
        type: "stats"
        statistic: "spiderAjax.urls.added"
        operator: ">="
        value: 50
        onFail: "info"
  - type: "report"
    name: "develop-report"
    parameters:
      template: "traditional-html"
      reportDir: "/zap/wrk/reports"
      reportFile: "zap-report.html"
      reportTitle: "ZAP Report"
      displayReport: false
```


