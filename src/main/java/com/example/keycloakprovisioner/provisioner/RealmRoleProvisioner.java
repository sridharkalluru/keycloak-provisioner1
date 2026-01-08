package com.example.keycloakprovisioner.provisioner;

import com.example.keycloakprovisioner.config.ProvisioningProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

@Service
public class RealmRoleProvisioner {

    private final Keycloak keycloak;

    public RealmRoleProvisioner(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void ensureRealmRole(String realm, ProvisioningProperties.RealmRoleConfig cfg) {
        var realmResource = keycloak.realm(realm);
        RolesResource rolesResource = realmResource.roles();

        boolean exists = rolesResource.list().stream()
                .anyMatch(r -> r.getName().equals(cfg.getName()));

        if (!exists) {
            RoleRepresentation role = new RoleRepresentation();
            role.setName(cfg.getName());
            role.setDescription(cfg.getDescription());
            role.setComposite(false);

            rolesResource.create(role);
            System.out.println("Created realm role: " + cfg.getName());
        } else {
            RoleRepresentation existing = rolesResource.get(cfg.getName()).toRepresentation();
            if (cfg.getDescription() != null &&
                    !cfg.getDescription().equals(existing.getDescription())) {
                existing.setDescription(cfg.getDescription());
                rolesResource.get(cfg.getName()).update(existing);
                System.out.println("Updated realm role: " + cfg.getName());
            } else {
                System.out.println("Realm role already up-to-date: " + cfg.getName());
            }
        }
    }
}
