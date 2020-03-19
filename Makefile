.PHONY: up build test api-dev docs

up:
	@docker-compose up

build:
	@docker-compose up --build

test:
	@cd api && \
	./mvnw test

api-dev:
	@cd api && \
	./mvnw spring-boot:run

clean:
	@cd api && \
	./mvnw clean install

docs:
	@cd docs && \
	npm run doc && \
	open doc.html
