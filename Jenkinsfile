pipeline {
  agent any

  environment {
    DOCKERHUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIALS')
    VERSION = "${env.BUILD_ID}"

  }

  tools {
    maven "Maven"
  }

  stages {

    stage('Maven Build'){
        steps{
        sh 'mvn clean package  -DskipTests'
        }
    }

     stage('Run Tests') {
      steps {
        sh 'mvn test'
      }
    }

    stage('SonarQube Analysis') {
  steps {
    sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install sonar:sonar -Dsonar.host.url=http://51.44.86.173:9000/ -Dsonar.login=squ_2308f25e169f56b45dac0de6fc7e95bc80ff1106'
  }
}


   stage('Check code coverage') {
            steps {
                script {
                    def token = "squ_2308f25e169f56b45dac0de6fc7e95bc80ff1106"
                    def sonarQubeUrl = "http://51.44.86.173:9000/api"
                    def componentKey = "com.example:userinfo"
                    def coverageThreshold = 00.0

                    def response = sh (
                        script: "curl -H 'Authorization: Bearer ${token}' '${sonarQubeUrl}/measures/component?component=${componentKey}&metricKeys=coverage'",
                        returnStdout: true
                    ).trim()

                    def coverage = sh (
                        script: "echo '${response}' | jq -r '.component.measures[0].value'",
                        returnStdout: true
                    ).trim().toDouble()

                    echo "Coverage: ${coverage}"

                    if (coverage < coverageThreshold) {
                        error "Coverage is below the threshold of ${coverageThreshold}%. Aborting the pipeline."
                    }
                }
            }
        } 


      stage('Docker Build and Push') {
      steps {
          sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
          sh 'docker build -t lavanyasimham/userinfo-service:${VERSION} .'
          sh 'docker push lavanyasimham/userinfo-service:${VERSION}'
      }
    } 


     stage('Cleanup Workspace') {
      steps {
        deleteDir()
       
      }
    }




    stage('Update Image Tag in GitOps') {
      steps {
         checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[ credentialsId: 'GITHUB_PAT', url: 'https://github.com/SmartEats-cloudNative/deployment-folder.git']])
        script {
       sh '''
          sed -i "s/image:.*/image: lavanyasimham\\/userinfo-service:${VERSION}/" aws/user-manifest.yml
        '''
          sh 'git checkout main'
          sh 'git add .'
          sh 'git commit -m "Update image tag"'
        withCredentials([usernamePassword(credentialsId: 'GITHUB_PAT',
                       usernameVariable: 'GIT_USER',
                       passwordVariable: 'GIT_TOKEN')]) {
        sh '''
          # ensure remote uses HTTPS with PAT for auth
          git remote set-url origin https://${GIT_USER}:${GIT_TOKEN}@github.com/SmartEats-cloudNative/deployment-folder.git
          git push origin main
        '''
      }
        }
      }
    }
   
  }

}


