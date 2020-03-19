# Shippy

Spring and Typescript backend and a React front end as per ye ole `instructions.txt`

## Quick Start (Just Run)

Run all services and client
```
make run
```
Open the client
```
open http://localhost:3000/
```

## Development

Start supporting services mysql/mongo
```
make up
```

Test backend locally (Java 1.8+ and Maven)
```
make test
make dev
```

## Make

`Makefile` has helpful shortcuts from root of the repo:

**`make run`** bring up mysql and redis, spring boot and react client dev server

**`make up`** bring up mysql and redis container

**`make down`** brings down all running service containers

**`make dev`** runs spring boot on local host (must have services up, see note below)

**`make dev`** runs api tests on local host

**`make clean`** runs api clean install in the api container

**`make docs`** generates api docs (apib) and opens HTML format in browser

## Design Principals

* DRY. If you have to update something in more than once place, you did it wrong.
* KISS. Self explainatory.
* Write tests that exercise programs the same way they are used.
* Do much as possible with Docker to avoid OS dependency drift.

## Architecture

Persistence elections this time were MySQL and Redis.

The app is complosed of two services, a Spring products/shipping configuration service, and a Typescript/Node enrollment service.

Each are hypothetical units of deployment, the enrollment service is particularlly small (two endpoints). They share the mysql DB.

Today, I am more of the opinion that service discovery, load balancing etc. are best left to the actual network overlays / infra and shouldn't be concerns of service implementations If you really need a retry and circuit breaking, maybe you should be using pubsub for that delivery?

As cool as micro is, transactions don't go well between partitions (CAP)

If protocols matter just have a service delegate implementation speak it.

## Deployment

Containers. Kube? Cloud Run? Fargate?

TODO: I wrote the back end in Java so I didn't have time to make a cool pipeline and infra with Terraform.

## Learning Opportunity?

Ordianarily I would containerize all dev environments and `docker-compose exec` all the various targets against them, however the docker-compose volume mappings for the spring app weren't recompiling my tests (required container rebuild) which is pretty bad devX.

#### Check out `dockerized-dev` branch for attempts there.


## Questions? Did I screw something up badly?

Asynchronous friendly support always available: email@benipsen.com



