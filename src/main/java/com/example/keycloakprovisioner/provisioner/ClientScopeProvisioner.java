package com.example.keycloakprovisioner.provisioner;

import com.example.keycloakprovisioner.config.ProvisioningProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientScopesResource;
import org.keycloak.representations.idm.ClientScopeRepresentation;
import org.springframework.stereotype.Service;

@Service
public class ClientScopeProvisioner {

    private final Keycloak keycloak;

    public ClientScopeProvisioner(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void ensureClientScope(String realm, ProvisioningProperties.ClientScopeConfig cfg) {
        var realmResource = keycloak.realm(realm);
        ClientScopesResource clientScopes = realmResource.clientScopes();

        var existing = clientScopes.findAll().stream()
                .filter(cs -> cs.getName().equals(cfg.getName()))
                .findFirst();

        if (existing.isEmpty()) {
            ClientScopeRepresentation scope = new ClientScopeRepresentation();
            scope.setName(cfg.getName());
            scope.setDescription(cfg.getDescription());
            scope.setProtocol("openid-connect");

            clientScopes.create(scope);
            System.out.println("Created client scope: " + cfg.getName());
        } else {
            ClientScopeRepresentation scope = existing.get();
            boolean changed = false;

            if (cfg.getDescription() != null &&
                    !cfg.getDescription().equals(scope.getDescription())) {
                scope.setDescription(cfg.getDescription());
                changed = true;
            }

            if (!"openid-connect".equals(scope.getProtocol())) {
                scope.setProtocol("openid-connect");
                changed = true;
            }

            if (changed) {
                clientScopes.get(scope.getId()).update(scope);
                System.out.println("Updated client scope: " + cfg.getName());
            } else {
                System.out.println("Client scope already up-to-date: " + cfg.getName());
            }
        }
    }
}
