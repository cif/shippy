.PHONY: run up down build test api-dev docs

run:
	@echo "\n\nWelcome to shippy!\nLots of things to build on first pass... This might take a minute." && \
	docker-compose -f docker-compose-run.yaml up

up:
	@docker-compose up

down:
	@docker-compose -f docker-compose-run.yaml down

test:
	@docker-compose exec enrollment npm run test && \
	export MYSQL_HOST=localhost && \
	export REDIS_HOST=localhost && \
	export ENROLLMENT_ENDPOINT=http://localhost:3001/enroll/status/ && \
	cd api && \
	./mvnw test

dev:
	@export MYSQL_HOST=localhost && \
	export REDIS_HOST=localhost && \
	export ENROLLMENT_ENDPOINT=http://localhost:3001/enroll/status/ && \
	cd api && \
	./mvnw spring-boot:run

clean:
	@export MYSQL_HOST=localhost && \
	export REDIS_HOST=localhost && \
	export ENROLLMENT_ENDPOINT=http://localhost:3001/enroll/status/ && \
	cd api && \
	./mvnw clean install

docs:
	@cd docs && \
	npm i && \
	npm run doc && \
	open index.html
