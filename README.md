# Shippy

Simple Spring backend with a React front end as per `instructions.txt`

Persistence elections this time were MySQL and Redis.

## Design Principals

* DRY. Goto definition is better than find and replace.
* KISS. Self explainatory.
* Write tests that work software the same way it's used.
* Do much as possible with Docker to avoid OS dependency drift.

## Development

#### `Makefile` has helpful shortcuts from root of the repo:

**`make run`** docker compose mysql, redis, client and server (detached) in containers, then tries to launch the client at http://localhost:3000/.

**`make up`** docker compose services bound to proc (not detached)

**`make down`** brings down services

**`make build`** rebuilds container and brinds up all services bound to proc (not detached)

**`make test`** runs api tests locally (must have services up)

**`make clean`** runs api clean install in the api container

**`make docs`** generates api docs (apib) and opens HTML format in browser

## Deployment

Containers. Kube? Cloud Run? Fargate?

TODO: I wrote the back end in Java so I didn't have time to make a cool pipeline and infra with Terraform.

## Questions? Did I screw something up badly?

Asynchronous friendly support always available: email@benipsen.com



