## Shippy

Shippy is comprised of two servcies and React client.

* Spring "products" app in `api/`
* Typescript/Node "enrollment" servcie in `enrollment/`
* Typescript/React app "client" in `client/`

See combined API endpoint docs at `docs/index.html`, run `make docs` to open in browser.


# Quick Start

Run all services and the client via docker-compose. **This takes a few minutes on first build**, faster if you have node and maven images already.

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

Run typescript enrollment Jest integration tests (in container) and Spring MockMVC integration tests locally
```
make test
```

Run the spring app locally
```
make dev
```

## Troubleshooting

Every so often mysql won't be ready to accept connections as the spring and typescript apps are coming up, despite `depends_on:` in the compose yaml.

Stopping docker-compose and `make run` ing again should get things a good state eventually.


# Makefile Target Reference

**`make docs`** generates service docs HTML from apib format and opens in browser

**`make test`** runs containerized typscript app and local spring app integration tests

**`make run`** bring up mysql and redis, spring boot, enrollment express typescript app in dev mode, and react client dev server

**`make up`** bring up all the services except for the spring app (*see below*)

**`make dev`** runs spring boot on localhost (*see below*)

**`make clean`** runs clean install on spring app

**`make down`** brings down all running service containers

# Design Principals

* DRY. If you have to update something in more than once place, you did it wrong.
* KISS. Self explainatory.
* Write tests that exercise programs the same way they are used.**
* Do much as possible with Docker to avoid OS dependency drift.

##### ** *I ran out of time on front end dev, but would have used Cypress instead of jest/enzyme/react testing lib for this app.*


## Architecture

Persistence elections this time were MySQL and Redis.

The two backends are hypothetical units of deployment, the enrollment service is particularlly small (two endpoints). Given high traffic to the new enrollment option, we wanted to scale that service independently of the main product eligibility service which really only takes admin traffic. They share the mysql DB (cluster in prod) for transational integrity (not eventually consistent, ACID conistent).

**Why not a micro framwork?** Today, I am more of the opinion that service discovery, load balancing etc. are best left to the actual network overlays, ipsec, dns, infra etc. and shouldn't be concerns of service implementations themselves. If you really need retry and circuit breaking logic, maybe you should be using pubsub/kafka for that delivery?

I have yet to see a large scale deployment of independent services all embracing the same framework. The only go-micro deployment I've seen in the "real" world so far was running on a single VM.

That said, tomorrow I can be of a different opinion.


## Deployment

Containers. Kube? Cloud Run? Fargate?

I wrote most of the back end in Java so I didn't have time to make a cool pipeline and infra with Terraform. Or write the Cypress tests (see above)

## Learning Opportunity / Better Java dev ex?

Ordianarily I would containerize all dev environments and `docker-compose exec` all the various targets against them as with the enrollment service, however the docker-compose volume mappings for the spring app weren't recompiling my tests (required container rebuild) which is pretty bad dev ex.. no TDD! (╯°□°)╯︵ ┻━┻

#### Check out `dockerized-dev` branch for attempts there.


# Questions? Did I screw something up badly?

Asynchronous friendly support always available: email@benipsen.com



