.PHONY: run up down build test api-dev docs

run:
	@docker-compose up -d

up:
	@docker-compose up

down:
	@docker-compose down

build:
	@docker-compose up --build

test:
	@docker-compose exec api mvn clean install test

clean:
	@docker-compose exec api mvn clean install

docs:
	@cd docs && \
	npm run doc && \
	open doc.html
