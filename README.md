# TUI Service - Git Repository Manager API


## Getting started

The app consists of being able to list all github repositories of a given username, wich are not forks. Information about the branch name and the last commit sha is also present.

## Start the application

### Running the app

```
 docker-compose build
 docker-compose up app
```

The above commands, will build and run a docker container, starting this web service on localhost at port 8081.

### Supported endpoints

- /api/users/<username>/repos

### Pagination support
You can also introduce pagination options into the endpoint.\
Follow the below example:
- /api/github/users/<username>/repos?page=<page_number>&size=<page_size>

## Calling the API

After the docker container is running, you can use an API requester of you choose, like Postman, or even using the `curl` command, to start to perform some requests into the web service.

Examples:
```
GET http://localhost:8081/api/users/prtpereira/repos
GET http://localhost:8081/api/users/prtpereira/repos?page=1&size=5
```

## Swagger documentation

https://app.swaggerhub.com/apis/PRTPEREIRA2/tui-git-repos/1.0.0#