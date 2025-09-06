package org.hyperledger.fabric.samples.assettransfer;

public final class AccessTokenContract {

    public String getDecision(final boolean accessDecision) {
        return accessDecision ? "Access Granted" : "Access Denied";
    }
}
