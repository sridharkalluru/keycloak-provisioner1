# keycloak-provisioner

## How to run it

### Start Keycloak (dev example):

docker run --rm \
 -p 8080:8080 \
 -e KEYCLOAK_ADMIN=admin \
 -e KEYCLOAK_ADMIN_PASSWORD=admin \
 quay.io/keycloak/keycloak:26.0.0 \
 start-dev

### Start the Spring Boot provisioner:

mvn spring-boot:run

### Watch logs; it should print:

- Created realm myrealm

- Created roles app-user, app-admin

- Created client scopes, client, users, and bindings.

### Log in to Keycloak Admin Console and inspect:

Realm: myrealm

Client: my-app

Realm roles & users

Client scopes and bindings
