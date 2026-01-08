package com.example.keycloakprovisioner.provisioner;

import com.example.keycloakprovisioner.config.ProvisioningProperties;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientProvisioner {

    private final Keycloak keycloak;

    public ClientProvisioner(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void ensureClient(String realm, ProvisioningProperties.ClientConfig cfg) {
        var realmResource = keycloak.realm(realm);
        var clients = realmResource.clients();

        List<ClientRepresentation> existing = clients.findByClientId(cfg.getClientId());

        if (existing.isEmpty()) {
            ClientRepresentation client = new ClientRepresentation();
            client.setClientId(cfg.getClientId());
            client.setName(cfg.getClientId());
            client.setRedirectUris(cfg.getRedirectUris());
            client.setWebOrigins(cfg.getWebOrigins());
            client.setServiceAccountsEnabled(cfg.isServiceAccountsEnabled());
            client.setPublicClient(cfg.isPublicClient());
            client.setProtocol("openid-connect");

            Response response = clients.create(client);
            System.out.println("Created client " + cfg.getClientId() +
                    " status=" + response.getStatus());
            response.close();
        } else {
            ClientRepresentation client = existing.get(0);
            boolean changed = false;

            if (cfg.getRedirectUris() != null &&
                    !cfg.getRedirectUris().equals(client.getRedirectUris())) {
                client.setRedirectUris(cfg.getRedirectUris());
                changed = true;
            }

            if (cfg.getWebOrigins() != null &&
                    !cfg.getWebOrigins().equals(client.getWebOrigins())) {
                client.setWebOrigins(cfg.getWebOrigins());
                changed = true;
            }

            if (client.isServiceAccountsEnabled() != cfg.isServiceAccountsEnabled()) {
                client.setServiceAccountsEnabled(cfg.isServiceAccountsEnabled());
                changed = true;
            }

            if (client.isPublicClient() != cfg.isPublicClient()) {
                client.setPublicClient(cfg.isPublicClient());
                changed = true;
            }

            if (changed) {
                clients.get(client.getId()).update(client);
                System.out.println("Updated client: " + cfg.getClientId());
            } else {
                System.out.println("Client already up-to-date: " + cfg.getClientId());
            }
        }
    }
}
