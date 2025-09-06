package org.hyperledger.fabric.samples.assettransfer;

public final class PolicyContract {
    private final String allowedRole = "subcontractor";
    private final String allowedOrg = "subcontractor.amu.com";

    public boolean isAllowed(final String role, final String org) {
        return allowedRole.equals(role) && allowedOrg.equals(org);
    }

    public String getAllowedRole() {
        return allowedRole;
    }

    public String getAllowedOrg() {
        return allowedOrg;
    }
}
