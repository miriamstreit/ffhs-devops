# Jenkins Pipeline Setup

## Creating a Multibranch-Pipeline

Before creating the Multibranch-Pipeline make sure to [Set up Jenkins](jenkins-setup.md).

A new Multibranch-Pipeline can be created by clicking on "New Item" on the dashboard. After choosing a name and selecting the "Multibranch Pipeline" type we can fill in the needed information to set up the automatic builds in jenkins. 

### Branch Sources

To add the GitLab Project to the pipeline we can click on "Add source". As the "Server" you can choose the one created during the [Jenkins Setup](jenkins-setup.md) with the same credentials (Personal Access Token).

After entering the Owner (in our case the person that created the project with the username format {firstname}.{lastname}) we can select the project from the dropdown and save the settings. 

## Jenkinsfile

The Jenkinsfile is a script that defines the pipeline.
It is stored in the root of the repository (`HS24-DEVOPS/Jenkinsfile`) and is automatically detected by Jenkins. The Jenkinsfile is a text file that contains the definition of the pipeline.

## Declarative pipeline

We are using a declarative pipeline. The syntax follows Groovy. The `agent` directive specifies which Jenkins node to use for the pipeline. As we are using a single-node setup, we can just specify `any` which will be used for any stage that does not specify another agent.

```groovy
pipeline {
  agent any

  stages {
    ...
  }
}
```

## Stages

The Jenkinsfile is divided into stages. Each stage represents a step in the pipeline. The stages are executed sequentially.

### Parameterized step

In order to add a parameterized step there needs to be a parameter setup in the beginning of the pipeline script. The stage `Setup parameters` demonstrates the configuration. We have exactly one parameter of type boolean named `DISCORD`. It is used to determine whether a discord notification about the build status should be posted. The default value is true and the value of the parameter can be set in the UI before starting a new pipeline run.

The `notify discord` step specifies a `when` clause before listing the neccessary steps. The expression used in the clause is just the parameter `DISCORD` which will be either true or false as it is a bool value. If it evaluates to false, the step will be skipped entirely. Otherwise the specified steps will be executed. The `discord notifier` plugin is called with the arguments needed for a message: A description, a footer text, a link, the result of the build, a title and the webhook URL of the discord channel to be notified.

```groovy
stage('Setup parameters') {
  steps {
    script {
      properties([
        parameters([
          booleanParam(
            defaultValue: true,
            description: 'send discord notification',
            name: 'DISCORD'
          ),
        ])
      ])
    }
  }
}
...
stage('Notify discord') {
  when {
    expression {
      return params.DISCORD
    }
  }
  steps {
    sh """
    echo "notify discord"
    """
    discordSend description: "Jenkins Pipeline Build", footer: "Footer Text 1", link: env.BUILD_URL, result: currentBuild.currentResult, title: env.JOB_NAME, webhookURL: "https://discord.com/api/webhooks/1287034794160685077/<token>"
  }
}
```

### Lint Frontend

```groovy
stage('Lint Frontend') {
  agent  {
    docker {
      image 'node:20.12.0-slim' // Be sure to always specify a version to ensure reproducibility and stability
    }
  }
  steps {
    dir('frontend') {
      sh """
      npm install
      npm run lint-no-fix
      """
    }
  }
}
```

This stage uses a Docker image with Node.js to run the linting of the frontend code. The agent is necessary to define the environment where the commands will be executed. The steps are the commands that will be executed.
The `dir` directive changes the working directory to `frontend` before running the commands.
`sh` is the shell step that runs the commands.
`npm run lint-no-fix` runs the linting of the frontend code which can be found in the `frontend/package.json`. It is defined as a script in the `package.json` file like this:

```json
    "lint-no-fix": "eslint --ext .js,.vue src/"
```

> There is also a `lint` script that runs the linting and tries to fix the issues. We use the `lint-no-fix` script to avoid changing the code automatically.

### Backend test

The backend is written in Java and uses Maven as its package manager. Maven allows to call different plugins such as tests or other reports from the command line. To ensure we always have Maven available, we chose to generate a Maven wrapper that lies within our project directory. We just need to switch to the backend directory using `dir()` and then call the script `./mvnw` instead of installing Maven and calling `mvn`. In this case we're calling the `test` plugin which executes our tests.

To display our test results in the pipeline overview, we can use a `post` step which is specified after closing the `stages` section. We want to always (even on failure) extract the artifact that our unittests produce which is a folder full of test results for every test class. For that we need to specify the path where the report lies and the keyword `junit`.

```groovy
  ...
  stage('Test') {
    steps {
      dir('backend') {
        sh "./mvnw test"
      }
    }
  }
  ...
} // bracket closing the stages section
post {
  always {
    junit 'backend/target/surefire-reports/\*.xml'
  }
}
```

### Backend build

Building the backend works just like running its tests. We call the `./mvnw` script and its `package` plugin which packages our app into a JAR-file. As we have already run the tests in a previous step and as they are included in the `package` plugin by default, we have to pass the flag `skipTests` to avoid running them again.

```groovy
stage('Build backend') {
  steps {
    dir('backend') {
      sh "./mvnw package -DskipTests"
    }
  }
}
```

### Build Frontend

```groovy
stage('Build frontend') {
  agent  {
    docker {
      image 'node:20.12.0-slim' // Be sure to always specify a version to ensure reproducibility and stability
    }
  }
  steps {
    dir('frontend') {
      sh """
      npm install
      npm run build
      """
    }
  }
}
```

This stage uses a Docker image with Node.js to build the frontend code. The `npm run build` command is used to build the frontend code. The `build` script is defined in the `frontend/package.json` file like this:

```json
    "build": "vite build"
```

> `npm install` is used to install the dependencies before building the code. Scince we are using a Docker image, the dependencies are not stored in the workspace so we need to install them twice (linting and building). It is also possible to use a volume to store the dependencies between the stages.

### Backend SonarQube scan

To scan a project in Jenkins using SonarQube a plugin is required and needs to be configured. This is documented within the [jenkins-setup.md](jenkins-setup.md) file. The plugin provides a function `withSonarQubeEnv` with which we can execute our scan using the same configuration as all other SonarQube steps. Within the function we can use Maven again to execute our SonarQube scan. For that the SonarQube plugin of Maven has to be called.

A Quality Gate follows the analysis. It will wait for at most an hour until the results of the previous step are in and will fail if the results are negative or if an hour is exceeded. This ensures that our pipeline will not proceed if the quality of the code is not maintained.

```groovy
stage('SonarQube analysis backend') {
  steps {
    dir('backend') {
      withSonarQubeEnv('Sonarqube local') {
        sh """
        ./mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar \
        -Dsonar.projectKey='schuumapp-backend' \
        -Dsonar.projectName='schuumapp-backend' \
        -Dsonar.host.url='https://sonar.sw3.ch' \
        -Dsonar.token='<sonar-backend-token>'
        """
      }
    }
  }
}
stage('Quality Gate') {
  steps {
    timeout(time: 1, unit: 'HOURS') {
      waitForQualityGate abortPipeline: true
    }
  }
}
```

### SonarQube Scan Frontend

```groovy
stage('SonarQube analysis frontend') {
  agent  {
    docker {
      image 'sonarsource/sonar-scanner-cli:11.1' // Be sure to always specify a version to ensure reproducibility and stability
    }
  }
  steps {
    dir('frontend') {
      withSonarQubeEnv('Sonarqube local') {
        sh """
        sonar-scanner \
        -Dsonar.projectName='schuumapp-frontend' \
        -Dsonar.projectKey='schuumapp-frontend' \
        -Dsonar.host.url='https://sonar.sw3.ch' \
        -Dsonar.token='<sonar-frontend-token>'
        """
      }
    }
  }
}
```

This stage uses a Docker image as well to run the SonarQube scan of the frontend code.
The `sonar-scanner` command is used to run the scan. The parameters are passed as arguments to the command. The `sonar.projectKey` is the key of the project in SonarQube. The `sonar.host.url` is the URL of the SonarQube server. The `sonar.token` is the token that is used to authenticate the scanner with the SonarQube server.

```groovy
    stage('Quality Gate Frontend') {
      steps {
        timeout(time: 1, unit: 'HOURS') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
```

This stage waits for the Quality Gate to be computed in SonarQube. The `waitForQualityGate` step waits for the Quality Gate to be computed and returns the status of the Quality Gate. The `timeout` step is used to set a timeout for the stage. If the Quality Gate is not computed within the timeout, the stage will fail and the pipeline will be aborted.
