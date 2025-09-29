package org.hyperledger.fabric.samples.assettransfer;

import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;

@Contract(name = "AccessTokenContract")
public final class PolicyContract implements ContractInterface {
    private final String allowedRole = "subcontractor";
    private final String allowedContractNumber = "SUB-2025";

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean isAllowed(final String role, final String contractNumber) {
        return allowedRole.equals(role) && allowedContractNumber.equals(contractNumber);
    }
}
