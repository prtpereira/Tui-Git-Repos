pipeline {
 agent any

 stages {
  stage('Build') {
   steps {
    // Build your application here
    sh 'mvn clean package'
   }
  }
  stage('Deploy to AWS') {
   steps {
    // Use AWS CLI to deploy CloudFormation templates
    withAWS(credentials: 'aws-credentials // ID of jenkins credentials', region: 'us-east-1') {
     sh 'aws cloudformation deploy --template-file cloud-fargate-service.yaml --stack-name my-fargate-service'
     sh 'aws cloudformation deploy --template-file cloud-api-gateway.yaml --stack-name my-api-gateway'
    }
   }
  }
 }
}
