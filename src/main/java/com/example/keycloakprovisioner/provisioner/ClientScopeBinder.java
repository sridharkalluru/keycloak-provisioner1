package com.example.keycloakprovisioner.provisioner;

import com.example.keycloakprovisioner.config.ProvisioningProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientScopeRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientScopeBinder {

    private final Keycloak keycloak;

    public ClientScopeBinder(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void ensureBindings(String realm, ProvisioningProperties.ClientScopeBindingConfig cfg) {
        var realmResource = keycloak.realm(realm);
        ClientsResource clients = realmResource.clients();

        var clientRep = clients.findByClientId(cfg.getClientId()).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Client " + cfg.getClientId() + " not found in realm " + realm));

        ClientResource clientResource = clients.get(clientRep.getId());

        var clientScopes = realmResource.clientScopes().findAll();

        var currentDefaultScopes = clientResource.getDefaultClientScopes();
        if (cfg.getDefaultScopes() != null) {
            for (String scopeName : cfg.getDefaultScopes()) {
                ClientScopeRepresentation scope = clientScopes.stream()
                        .filter(cs -> cs.getName().equals(scopeName))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "Client scope " + scopeName + " not found"));

                boolean alreadyDefault = currentDefaultScopes.stream()
                        .anyMatch(cs -> cs.getName().equals(scopeName));

                if (!alreadyDefault) {
                    clientResource.addDefaultClientScope(scope.getId());
                    System.out.println("Added default scope " + scopeName +
                            " to client " + cfg.getClientId());
                }
            }
        }

        var currentOptionalScopes = clientResource.getOptionalClientScopes();
        if (cfg.getOptionalScopes() != null) {
            for (String scopeName : cfg.getOptionalScopes()) {
                ClientScopeRepresentation scope = clientScopes.stream()
                        .filter(cs -> cs.getName().equals(scopeName))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "Client scope " + scopeName + " not found"));

                boolean alreadyOptional = currentOptionalScopes.stream()
                        .anyMatch(cs -> cs.getName().equals(scopeName));

                if (!alreadyOptional) {
                    clientResource.addOptionalClientScope(scope.getId());
                    System.out.println("Added optional scope " + scopeName +
                            " to client " + cfg.getClientId());
                }
            }
        }
    }
}
