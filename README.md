# Shippy

Simple Spring backend with a React front end as per ye ole `instructions.txt`

Persistence elections this time were MySQL and Redis.

## Design Principals

* DRY. If you have to update something in more than once place, you did it wrong.
* KISS. Self explainatory.
* Write tests that exercise programs the same way they are used.
* Do much as possible with Docker to avoid OS dependency drift.

## Quick Start (Just Run)

Start services (requires Java 1.8 + Maven)
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

**`make run`** bring up mysql and redis container (detached)

**`make up`** docker compose services client, and java app bound to proc (not detached)

**`make down`** brings down api dependent services and

**`make build`** rebuilds container and brinds up all services bound to proc (not detached)

**`make test`** runs api tests locally (must have services up)

**`make clean`** runs api clean install in the api container

**`make docs`** generates api docs (apib) and opens HTML format in browser

## (╯°□°)╯︵ ┻━┻

Ordianarily I would containerize all dev environments and `docker-compose exec` all the various targets against them, however the docker-compose volume mappings for the spring app weren't recompiling my tests (required container rebuild) which is pretty bad devX.


#### Check out `dockerized-dev` branch for attempts there.

## Deployment

Containers. Kube? Cloud Run? Fargate?

TODO: I wrote the back end in Java so I didn't have time to make a cool pipeline and infra with Terraform.

## Questions? Did I screw something up badly?

Asynchronous friendly support always available: email@benipsen.com



