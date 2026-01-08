package com.example.keycloakprovisioner.provisioner;

import com.example.keycloakprovisioner.config.ProvisioningProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProvisioner {

    private final Keycloak keycloak;

    public UserProvisioner(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String ensureUser(String realm, ProvisioningProperties.UserConfig cfg) {
        var realmResource = keycloak.realm(realm);
        var usersResource = realmResource.users();

        List<UserRepresentation> existing = usersResource.search(cfg.getUsername(), true);

        if (existing.isEmpty()) {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(cfg.getUsername());
            user.setEnabled(true);

            var response = usersResource.create(user);
            String path = response.getLocation().getPath();
            String userId = path.replaceAll(".*/([^/]+)$", "$1");
            response.close();

            setPassword(usersResource.get(userId), cfg.getPassword());
            System.out.println("Created user: " + cfg.getUsername());
            return userId;
        } else {
            UserRepresentation user = existing.get(0);
            if (!user.isEnabled()) {
                user.setEnabled(true);
                usersResource.get(user.getId()).update(user);
                System.out.println("Enabled user: " + cfg.getUsername());
            }
            setPassword(usersResource.get(user.getId()), cfg.getPassword());
            System.out.println("User already exists (password reset): " + cfg.getUsername());
            return user.getId();
        }
    }

    private void setPassword(org.keycloak.admin.client.resource.UserResource userResource,
                             String password) {
        if (password == null || password.isEmpty()) {
            return;
        }
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setTemporary(false);
        cred.setValue(password);
        userResource.resetPassword(cred);
    }
}
