.PHONY: run up down build test api-dev docs

run:
	@docker-compose -f docker-compose-run.yaml up

up:
	@docker-compose up

down:
	@docker-compose -f docker-compose-run.yaml down

test:
	export MYSQL_HOST=localhost && \
	export REDIS_HOST=localhost && \
	cd api && \
	./mvnw test

dev:
	export MYSQL_HOST=localhost && \
	export REDIS_HOST=localhost && \
	cd api && \
	./mvnw spring-boot:run

clean:
	cd api && \
	./mvnw clean install

docs:
	@cd docs && \
	npm run doc && \
	open doc.html
