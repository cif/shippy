.PHONY: up build test

up:
	@docker-compose up

build:
	@docker-compose up --build

test:
	@cd api && \
	./mvnw test