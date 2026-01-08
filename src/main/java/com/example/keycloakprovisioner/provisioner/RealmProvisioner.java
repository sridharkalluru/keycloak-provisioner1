package com.example.keycloakprovisioner.provisioner;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.stereotype.Service;

@Service
public class RealmProvisioner {

    private final Keycloak keycloak;

    public RealmProvisioner(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void ensureRealm(String realmName) {
        boolean exists = keycloak.realms().findAll()
                .stream()
                .anyMatch(r -> r.getRealm().equals(realmName));

        if (!exists) {
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(realmName);
            realm.setEnabled(true);
            keycloak.realms().create(realm);
            System.out.println("Created realm: " + realmName);
        } else {
            System.out.println("Realm already exists: " + realmName);
        }
    }
}
