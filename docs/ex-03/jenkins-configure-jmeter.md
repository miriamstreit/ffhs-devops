# Configuring the Jenkins pipeline to run JMeter tests

## Preconditions

We assume that the JMeter tests exist within the folder [../backend/jmeter/](../backend/jmeter/) and that JMeter is installed on the host as documented in [./install-jmeter.md](./install-jmeter.md).

## Configuring an ENV variable

We need to configure the JMeter installation path as an environment variable in the block `environment` in the Jenkinsfile as follows:

```
environment {
    // some other variable definitions
    JMETER_HOME = "/usr/local/bin/jmeter/bin"
  }
```

## Running the JMeter tests

We have two different kinds of tests so separating their execution makes sense. The procedure however is the same for both kinds. Firstly the output folder needs to be created. We then call the JMeter binary using the previously defined `JMETER_HOME` variable and with the following flags:

[From the [docs](https://jmeter.apache.org/usermanual/get-started.html#non_gui)]

- -n: This specifies JMeter is to run in cli mode
- -t: name of JMX file that contains the Test Plan
- -l: name of JTL file to log sample results to
- -e: generate report dashboard after load test
- -o: output folder where to generate the report dashboard after load test

```
stage('Run JMeter API Tests') {
  steps {
    script {
      sh """
      mkdir -p ${WORKSPACE}/output/reports/api-tests
      ${JMETER_HOME}/jmeter -n -t ${WORKSPACE}/backend/jmeter/jmeter-unit-tests.jmx -l ${WORKSPACE}/output/results-api-tests.jtl -e -o ${WORKSPACE}/output/reports/api-tests
      """
    }
  }
}
stage('Run JMeter Load Tests') {
  steps {
    script {
      sh """
      mkdir -p ${WORKSPACE}/output/reports/load-tests
      ${JMETER_HOME}/jmeter -n -t ${WORKSPACE}/backend/jmeter/jmeter-load-tests.jmx -l ${WORKSPACE}/output/results-load-tests.jtl -e -o ${WORKSPACE}/output/reports/load-tests
      """
    }
  }
}

```

## Publishing the JMeter test results

The previous step will run the tests and generate reports but they will not be available until we get them out ourselves. We do that by adding another stage to the pipeline which runs the `publishHTML` command once per test. For that we need to specify the folder path, the file name and the report name.

```
stage('Publish JMeter Reports') {
  steps {
    publishHTML(target: [
      allowMissing: false,
      alwaysLinkToLastBuild: true,
      keepAll: true,
      reportDir: 'output/reports/api-tests',
      reportFiles: 'index.html',
      reportName: 'JMeter API Test Report'
    ])
    publishHTML(target: [
      allowMissing: false,
      alwaysLinkToLastBuild: true,
      keepAll: true,
      reportDir: 'output/reports/load-tests',
      reportFiles: 'index.html',
      reportName: 'JMeter Load Test Report'
    ])
  }
}
```

## Cleanup

After every test run the environment needs to be cleaned up. Using `archiveArtifacts` we get the JTL-files as artifacts. After that the output directory is deleted so it will not clash with the next test execution.

```
post {
    always {
      // other cleanup tasks
      // archive jmeter test results
      archiveArtifacts artifacts: 'output/results-api-tests.jtl', allowEmptyArchive: true
      archiveArtifacts artifacts: 'output/results-load-tests.jtl', allowEmptyArchive: true
      // remove jmeter output directory
      sh '''
      rm -rf ${WORKSPACE}/output
      '''
    }
  }

```
