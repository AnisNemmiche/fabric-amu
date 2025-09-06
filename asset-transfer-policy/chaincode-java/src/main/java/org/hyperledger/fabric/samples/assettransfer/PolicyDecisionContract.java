package org.hyperledger.fabric.samples.assettransfer;

public final class PolicyDecisionContract {
    private final PolicyContract policy;

    public PolicyDecisionContract(final PolicyContract policy) {
        this.policy = policy;
    }

    public boolean checkAccess(final String role, final String org) {
        return policy.isAllowed(role, org);
    }
}
