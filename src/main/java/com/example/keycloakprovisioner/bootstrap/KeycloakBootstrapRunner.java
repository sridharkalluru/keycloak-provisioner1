package com.example.keycloakprovisioner.bootstrap;

import com.example.keycloakprovisioner.config.ProvisioningProperties;
import com.example.keycloakprovisioner.provisioner.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class KeycloakBootstrapRunner implements CommandLineRunner {

    private final ProvisioningProperties provisioningProperties;
    private final RealmProvisioner realmProvisioner;
    private final RealmRoleProvisioner realmRoleProvisioner;
    private final ClientScopeProvisioner clientScopeProvisioner;
    private final ClientProvisioner clientProvisioner;
    private final UserProvisioner userProvisioner;
    private final UserRoleBinder userRoleBinder;
    private final ClientScopeBinder clientScopeBinder;

    public KeycloakBootstrapRunner(ProvisioningProperties provisioningProperties,
                                   RealmProvisioner realmProvisioner,
                                   RealmRoleProvisioner realmRoleProvisioner,
                                   ClientScopeProvisioner clientScopeProvisioner,
                                   ClientProvisioner clientProvisioner,
                                   UserProvisioner userProvisioner,
                                   UserRoleBinder userRoleBinder,
                                   ClientScopeBinder clientScopeBinder) {
        this.provisioningProperties = provisioningProperties;
        this.realmProvisioner = realmProvisioner;
        this.realmRoleProvisioner = realmRoleProvisioner;
        this.clientScopeProvisioner = clientScopeProvisioner;
        this.clientProvisioner = clientProvisioner;
        this.userProvisioner = userProvisioner;
        this.userRoleBinder = userRoleBinder;
        this.clientScopeBinder = clientScopeBinder;
    }

    @Override
    public void run(String... args) {
        String realm = provisioningProperties.getTargetRealm();
        System.out.println("=== Starting Keycloak provisioning for realm: " + realm + " ===");

        realmProvisioner.ensureRealm(realm);

        if (provisioningProperties.getRealmRoles() != null) {
            provisioningProperties.getRealmRoles()
                    .forEach(cfg -> realmRoleProvisioner.ensureRealmRole(realm, cfg));
        }

        if (provisioningProperties.getClientScopes() != null) {
            provisioningProperties.getClientScopes()
                    .forEach(cfg -> clientScopeProvisioner.ensureClientScope(realm, cfg));
        }

        if (provisioningProperties.getClients() != null) {
            provisioningProperties.getClients()
                    .forEach(cfg -> clientProvisioner.ensureClient(realm, cfg));
        }

        if (provisioningProperties.getUsers() != null) {
            provisioningProperties.getUsers().forEach(cfg -> {
                userProvisioner.ensureUser(realm, cfg);
                if (cfg.getRoles() != null && !cfg.getRoles().isEmpty()) {
                    userRoleBinder.ensureUserRoles(realm, cfg);
                }
            });
        }

        if (provisioningProperties.getClientScopeBindings() != null) {
            provisioningProperties.getClientScopeBindings()
                    .forEach(cfg -> clientScopeBinder.ensureBindings(realm, cfg));
        }

        System.out.println("=== Keycloak provisioning finished for realm: " + realm + " ===");
    }
}
