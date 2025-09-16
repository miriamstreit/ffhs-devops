pipeline {
  agent any

  environment {
    SONARQUBE_FRONTEND_TOKEN  = credentials('jenkins-sonarqube-frontend-token')
    SONARQUBE_BACKEND_TOKEN   = credentials('jenkins-sonarqube-backend-token')
    JENKINS_HOST_SSH_CREDENTIALS = credentials('jenkins-host-ssh-credentials')
    JENKINS_HOST_WORKDIR = credentials('jenkins-host-workdir')
    JMETER_HOME = "/usr/local/bin/jmeter/bin"
    DOCKERHUB_ACCESS_TOKEN = credentials('dockerhub-access-token')
    KUBECONFIG = "${JENKINS_HOME}/.kube/config"
  }
  
  /////
  // Setup
  /////
  stages {
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
              booleanParam(
                defaultValue: false,
                description: 'redeploy monitoring stack',
                name: 'REDEPLOY_MONITORING'
              )
            ])
          ])
        }
      }
    }

    /////
    // Build
    /////

    // Frontend
    stage('Lint Frontend') {

      agent  {
        docker {
          image 'node:20.12.0-slim'                
        }
      }
      steps {
        dir('frontend') {
          sh '''
          npm install
          npm run lint-no-fix
          '''
        }
      }
    }
    stage('Build frontend') {
      agent  {
        docker {
          image 'node:20.12.0-slim'                
        }
      }
      steps {
        dir('frontend') {
          sh '''
          npm install
          npm run build
          '''
        }
      }
    }

    // Backend
    stage('Test and Generate Coverage Report') {
      steps {
        dir('backend') {
          sh "./mvnw test jacoco:report"                
        }
      }
    }
    stage('Build backend') {
      steps {
        dir('backend') {
          sh "./mvnw package -DskipTests"                
        }
      }
    }
    
    /////
    // Quality Scan
    /////

    // Backend
    stage('SonarQube analysis backend') {
      steps {
        dir('backend') {
          withSonarQubeEnv('Sonarqube local') {
            sh '''
            ./mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar \
            -Dsonar.projectKey='schuumapp-backend' \
            -Dsonar.projectName='schuumapp-backend' \
            -Dsonar.host.url='https://sonar.sw3.ch' \
            -Dsonar.token=$SONARQUBE_BACKEND_TOKEN
            '''
          }
        }
      }
    }
    stage('Quality Gate Backend') {
      steps {
        timeout(time: 1, unit: 'HOURS') {
          waitForQualityGate abortPipeline: true
        }
      }
    }

    // Frontend
    stage('SonarQube analysis frontend') {
      agent  {

        docker {
          image 'sonarsource/sonar-scanner-cli:11.1'                
        }

      }
      steps {
        dir('frontend') {
          withSonarQubeEnv('Sonarqube local') {
            sh '''
            sonar-scanner \
            -Dsonar.projectKey='schuumapp-frontend' \
            -Dsonar.projectName='schuumapp-frontend' \
            -Dsonar.host.url='https://sonar.sw3.ch' \
            -Dsonar.token=$SONARQUBE_FRONTEND_TOKEN
            '''
          }
        }
      }
    }
    stage('Quality Gate Frontend') {
      steps {
        timeout(time: 1, unit: 'HOURS') {
          waitForQualityGate abortPipeline: true
        }
      }
    }

    /////
    // Containerize
    /////

    // Build Images
    stage('Build Backend Image - Develop') {
      when {
        branch 'develop'
      }
      environment {
        IMAGE_TAG = "sebastianschuum/schuum-backend:${BUILD_ID}"
      }
      steps {
        dir('backend') {
          sh """
          docker build -t ${IMAGE_TAG} .
          """
        }
      }
    }

    stage('Build Frontend Image - Develop') {
      when {
        branch 'develop'
      }
      environment {
        IMAGE_TAG = "sebastianschuum/schuum-frontend:${BUILD_ID}"
      }
      steps {
        dir('frontend') {
          sh """
          docker build -t ${IMAGE_TAG} .
          """
        }
      }
    }
    
    // Scan Images with Docker Scout
    stage('Scan Backend Image - Develop') {
      when {
        branch 'develop'
      }
      environment {
        IMAGE_TAG = "sebastianschuum/schuum-backend:${BUILD_ID}"
      }
      steps {
        sh 'curl -sSfL https://raw.githubusercontent.com/docker/scout-cli/main/install.sh | sh -s -- -b /usr/local/bin'
        sh 'echo $DOCKERHUB_ACCESS_TOKEN_PSW | docker login -u $DOCKERHUB_ACCESS_TOKEN_USR --password-stdin'
        sh 'docker-scout cves $IMAGE_TAG --only-severity critical,high'
      }
    }

    stage('Scan Frontend Image - Develop') {
      when {
        branch 'develop'
      }
      environment {
        IMAGE_TAG = "sebastianschuum/schuum-frontend:${BUILD_ID}"
      }
      steps {
        sh 'curl -sSfL https://raw.githubusercontent.com/docker/scout-cli/main/install.sh | sh -s -- -b /usr/local/bin'
        sh 'echo $DOCKERHUB_ACCESS_TOKEN_PSW | docker login -u $DOCKERHUB_ACCESS_TOKEN_USR --password-stdin'
        sh 'docker-scout cves $IMAGE_TAG --only-severity critical,high'
      }
    }

    // Upload Images to Docker Hub
    stage('Upload Backend Image - Develop') {
      when {
        branch 'develop'
      }
      environment {
        IMAGE_TAG = "sebastianschuum/schuum-backend:${BUILD_ID}"
      }
      steps {
        sh 'echo $DOCKERHUB_ACCESS_TOKEN_PSW | docker login -u $DOCKERHUB_ACCESS_TOKEN_USR --password-stdin'
        sh 'docker push ${IMAGE_TAG}'
      }
    }

    stage('Upload Frontend Image - Develop') {
      when {
        branch 'develop'
      }
      environment {
        IMAGE_TAG = "sebastianschuum/schuum-frontend:${BUILD_ID}"
      }
      steps {
        sh 'echo $DOCKERHUB_ACCESS_TOKEN_PSW | docker login -u $DOCKERHUB_ACCESS_TOKEN_USR --password-stdin'
        sh 'docker push ${IMAGE_TAG}'
      }
    }

    /////
    // Deployment
    /////

    stage('Redeploy Monitoring Stack') {
      when {
        expression { 
          return params.REDEPLOY_MONITORING
        }
      }
      steps {
        dir("env/${BRANCH_NAME}/prometheus/manifests") {
          sh '${JENKINS_HOME}/.kube/kubectl apply --server-side -f setup'
          sleep 30
          sh '${JENKINS_HOME}/.kube/kubectl apply -f .'
        }
      }
    }

    stage('Deploy to k8s') {
        when {
          anyOf {
            branch 'develop'
            branch 'release'
            branch 'main'
          }
        }
        steps {
          // is BRANCH_NAME the correct variable? + will not work for main
          dir("env/${BRANCH_NAME}") {
            sh '${JENKINS_HOME}/.kube/envsubst < kustomization-template.yml > kustomization.yml'
            sh '${JENKINS_HOME}/.kube/kustomize build . | ${JENKINS_HOME}/.kube/kubectl apply -f -'
          }
        }
    }

    /*
    stage('Deploy Development') {
      when {
        branch 'develop'
      }
      steps {
        script {
          withEnv(["KUBECONFIG=${KUBECONFIG}"]) {
          }
        }
      }
    }
    stage('Deploy Release') {
      when {
        branch 'release'
      }
      steps {
        echo "deploy release"
      }
    }
    stage('Deploy Production') {
      when {
        branch 'main'
      }
      steps {
        echo "deploy release"
      }
    }
    */

    /////
    // ZAP Security Scan
    /////
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

    /////
    // JMeter Tests
    /////
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

    /////
    // Notification
    /////
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
        discordSend description: "Jenkins Pipeline Build", footer: "Footer Text 1", link: env.BUILD_URL, result: currentBuild.currentResult, title: env.JOB_NAME, webhookURL: "https://discord.com/api/webhooks/1287034794160685077/cOubFhBjwRWNncTGS3tyhSTJcF9LHOw5cvxN7_WUTejQ4V-1CgEQdHnyP18Z57gQqEGU"
      }
    }
  }

  /////
  // Cleanup, Archive Artifacts
  /////
  post {
    always {
      // archive unit test results
      junit 'backend/target/surefire-reports/*.xml'
      // archive jmeter test results
      archiveArtifacts artifacts: 'output/results-api-tests.jtl', allowEmptyArchive: true
      archiveArtifacts artifacts: 'output/results-load-tests.jtl', allowEmptyArchive: true
      // remove jmeter output directory
      sh '''
      rm -rf ${WORKSPACE}/output
      '''
    }
  }
}
