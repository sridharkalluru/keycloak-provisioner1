package com.example.keycloakprovisioner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "provisioning")
public class ProvisioningProperties {

    private String targetRealm;
    private List<ClientConfig> clients;
    private List<RealmRoleConfig> realmRoles;
    private List<ClientScopeConfig> clientScopes;
    private List<UserConfig> users;
    private List<ClientScopeBindingConfig> clientScopeBindings;

    public static class ClientConfig {
        private String clientId;
        private List<String> redirectUris;
        private List<String> webOrigins;
        private boolean serviceAccountsEnabled;
        private boolean publicClient;

        public String getClientId() { return clientId; }
        public void setClientId(String clientId) { this.clientId = clientId; }

        public List<String> getRedirectUris() { return redirectUris; }
        public void setRedirectUris(List<String> redirectUris) { this.redirectUris = redirectUris; }

        public List<String> getWebOrigins() { return webOrigins; }
        public void setWebOrigins(List<String> webOrigins) { this.webOrigins = webOrigins; }

        public boolean isServiceAccountsEnabled() { return serviceAccountsEnabled; }
        public void setServiceAccountsEnabled(boolean serviceAccountsEnabled) { this.serviceAccountsEnabled = serviceAccountsEnabled; }

        public boolean isPublicClient() { return publicClient; }
        public void setPublicClient(boolean publicClient) { this.publicClient = publicClient; }
    }

    public static class RealmRoleConfig {
        private String name;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class ClientScopeConfig {
        private String name;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class UserConfig {
        private String username;
        private String password;
        private List<String> roles;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }
    }

    public static class ClientScopeBindingConfig {
        private String clientId;
        private List<String> defaultScopes;
        private List<String> optionalScopes;

        public String getClientId() { return clientId; }
        public void setClientId(String clientId) { this.clientId = clientId; }

        public List<String> getDefaultScopes() { return defaultScopes; }
        public void setDefaultScopes(List<String> defaultScopes) { this.defaultScopes = defaultScopes; }

        public List<String> getOptionalScopes() { return optionalScopes; }
        public void setOptionalScopes(List<String> optionalScopes) { this.optionalScopes = optionalScopes; }
    }

    public String getTargetRealm() { return targetRealm; }
    public void setTargetRealm(String targetRealm) { this.targetRealm = targetRealm; }

    public List<ClientConfig> getClients() { return clients; }
    public void setClients(List<ClientConfig> clients) { this.clients = clients; }

    public List<RealmRoleConfig> getRealmRoles() { return realmRoles; }
    public void setRealmRoles(List<RealmRoleConfig> realmRoles) { this.realmRoles = realmRoles; }

    public List<ClientScopeConfig> getClientScopes() { return clientScopes; }
    public void setClientScopes(List<ClientScopeConfig> clientScopes) { this.clientScopes = clientScopes; }

    public List<UserConfig> getUsers() { return users; }
    public void setUsers(List<UserConfig> users) { this.users = users; }

    public List<ClientScopeBindingConfig> getClientScopeBindings() { return clientScopeBindings; }
    public void setClientScopeBindings(List<ClientScopeBindingConfig> clientScopeBindings) { this.clientScopeBindings = clientScopeBindings; }
}
