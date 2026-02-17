# External OpenAPI Client Registration — Mermaid Architecture (Keycloak + OPA)

## Overview

Architecture assumptions:

- OIDC authentication and client credentials are managed by Keycloak.
- Authorization enforcement is performed by an OPA server.
- Internal database stores:
  - organizations
  - principals
  - roles and profiles
  - client ownership
  - API entitlements
- API Gateway delegates authorization decisions to OPA.

---

## 1) Platform Mind Map (Mermaid)

```mermaid
mindmap
  root((External API Platform))
    Identity Layer (Keycloak)
      OIDC Authentication
        Users (iss, sub)
        Service Accounts
        JWT Tokens
      Client Credentials
        client_id
        client_secret
        Redirect URIs
      Token Claims
        iss
        sub
        azp / client_id
        scope
    Authorization Data Layer
      Organization
        org_id
        name
        status
      Principals
        OIDC identity mapping
        User principal
        Service principal
      Profiles
        org membership
        role assignment
      Roles & Permissions
        ORG_OWNER
        ORG_ADMIN
        DEVELOPER
        AUDITOR
      External Clients
        keycloak_client_id
        org ownership
      API Exposure
        API Products
        API Scopes
      Entitlements
        org -> product
        client -> scope
      Governance
        Approval Requests
        Audit Log
    Policy Enforcement (OPA)
      Rego Policies
      Policy Bundles
      Decision API
      Input Context
        JWT claims
        request attributes
        entitlement data
    Runtime Enforcement
      API Gateway
      OPA Authorization Check
      Allow / Deny
```
