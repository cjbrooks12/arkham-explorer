
This is the source for the www.caseyjbrooks.com website, as well as api.caseyjbrooks.com and its related services. Read
below to get started. It requires the following tools to be installed on your system:

- Java 8+
- Docker Compose
- NPM/Yarn

## Running Frontend Website

- Serve locally: `./gradlew :lib:browserBrowserWebpack :site:orchidServe`
- Build for production: `./gradlew :lib:browserBrowserProductionWebpack :site:orchidBuild -Penv=prod`

* [Orchid documentation](https://orchid.run/)

## Running Backend API

- Serve API-only locally: `./gradlew :lib:run`
- Build API-only for production: `./gradlew :lib:shadowJar`
- start all services: `docker-compose up -d`
- stop all services: `docker-compose down`
- stop all services and delete all data: `docker-compose down -v`
- access Hasura admin console: `cd ./lib/src/hasura && hasura console`

* [Ktor documentation](https://ktor.io/servers)

