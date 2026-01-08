package com.example.keycloakprovisioner.provisioner;

import com.example.keycloakprovisioner.config.ProvisioningProperties;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleBinder {

    private final Keycloak keycloak;

    public UserRoleBinder(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void ensureUserRoles(String realm, ProvisioningProperties.UserConfig cfg) {
        var realmResource = keycloak.realm(realm);
        var usersResource = realmResource.users();

        var users = usersResource.search(cfg.getUsername(), true);
        if (users.isEmpty()) {
            throw new IllegalStateException(
                    "User " + cfg.getUsername() + " not found in realm " + realm);
        }

        String userId = users.get(0).getId();
        var userResource = usersResource.get(userId);
        var realmRolesResource = realmResource.roles();

        var currentRoles = userResource.roles().realmLevel().listAll();

        if (cfg.getRoles() == null) {
            return;
        }

        for (String roleName : cfg.getRoles()) {
            var roleRep = realmRolesResource.get(roleName).toRepresentation();

            boolean alreadyHas = currentRoles.stream()
                    .anyMatch(r -> r.getName().equals(roleName));

            if (!alreadyHas) {
                userResource.roles().realmLevel().add(List.of(roleRep));
                System.out.println("Assigned role " + roleName + " to user " + cfg.getUsername());
            } else {
                System.out.println("User " + cfg.getUsername() +
                        " already has role " + roleName);
            }
        }
    }
}
