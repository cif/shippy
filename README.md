# Shippy

Spring and Node/Typescript backends with React/Typescript client as per ye ole `instructions.txt`

## Quick Start

Run all services and the client via docker-compose.

**This takes a few minutes the first time (~3m on Late 2013 MBP)**
```
make run
```
Wait for output on client service "You can now view client in the browser" in console then click the link or
```
open http://localhost:3000/
```


## Run Tests

Make sure all enrollment, mysql and redis are running. If you did the quick start they already are. If not, you can use:
```
make up
```

Run typescript enrollment JEST integration in container and Spring integration tests locally (Requires Java 1.8+)
```
make test
```

Run the spring app locally on port 8080, (will export some environment variables for you)
```
make dev
```

## Makefile Target Reference

**`make docs`** generates service docs HTML from apib format and opens in browser

**`make test`** runs containerized typscript app and local spring app integration tests

**`make run`** bring up mysql and redis, spring boot, enrollment express typescript app in dev mode, and react client dev server

**`make up`** bring up all the services except for the spring app (*see below*)

**`make dev`** runs spring boot on localhost (*see below*)

**`make clean`** runs clean install on spring app

**`make down`** brings down all running service containers


## Design Principals

* DRY. If you have to update something in more than once place, you did it wrong.
* KISS. Self explainatory.
* Write tests that exercise programs the same way they are used.**
* Do much as possible with Docker to avoid OS dependency drift.

##### ** *I ran out of time on front end dev, but would have used Cypress instead of jest unit tetts for this app. Ask me why in the interview.*.

## Architecture

Persistence elections this time were MySQL and Redis.

The backend is complosed of two REST services, a Spring products / shipping configuration service `api/`, and a Node `enrollment/` service.

Each are hypothetical units of deployment, the enrollment service is particularlly small (two endpoints). Given high traffic to the new enrollment option, we wanted to scale that service independently of the main product service. They share the mysql DB (cluster in prod).

**Why not a micro framwork?** Today, I am more of the opinion that service discovery, load balancing etc. are best left to the actual network overlays, ipsec, dns, infra etc. and shouldn't be concerns of service implementations themselves. If you really need retry and circuit breaking logic, maybe you should be using pubsub/kafka for that delivery?

I have yet to see a large scale deployment of independent services all embracing the same framework. The only go-micro deployment I've seen in the wold was running on a single VM.

Thats said, tomorrow I can be of a different opinion.


## Deployment

Containers. Kube? Cloud Run? Fargate?

I wrote most of the back end in Java so I didn't have time to make a cool pipeline and infra with Terraform. Or write the Cypress tests (see above)

## Learning Opportunity?

Ordianarily I would containerize all dev environments and `docker-compose exec` all the various targets against them as with the enrollment service, however the docker-compose volume mappings for the spring app weren't recompiling my tests (required container rebuild) which is pretty bad devX.. no TDD! (╯°□°)╯︵ ┻━┻

#### Check out `dockerized-dev` branch for attempts there.


## Questions? Did I screw something up badly?

Asynchronous friendly support always available: email@benipsen.com



