Resources:
 MyCluster:
  Type: AWS::ECS::Cluster

 MyTaskDefinition:
  Type: AWS::ECS::TaskDefinition
  Properties:
   Family: my-task
   Cpu: 256
   Memory: 512
   NetworkMode: awsvpc
   ContainerDefinitions:
    - Name: my-container
      Image: your-docker-image-url
      PortMappings:
       - ContainerPort: 8080

 MyService:
  Type: AWS::ECS::Service
  Properties:
   Cluster: !Ref MyCluster
   DesiredCount: 1
   TaskDefinition: !Ref MyTaskDefinition
   LaunchType: FARGATE
   NetworkConfiguration:
    AwsvpcConfiguration:
     Subnets:
      - subnet-id
     SecurityGroups:
      - security-group-id
