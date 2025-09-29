package org.hyperledger.fabric.samples.assettransfer;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Contract(name = "AccessTokenContract")
public final class AccessTokenContract implements ContractInterface {

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String IssueToken(final Context ctx, final String contractNumber, final String role) {
        var stub = ctx.getStub();
        long ts = stub.getTxTimestamp().getEpochSecond();
        String material = ts + "_" + contractNumber + "_" + role + "_" + stub.getTxId();
        String tokenId = sha256Hex(material);
        String event = "{" + "tokenId:" + tokenId + "," + "ts:" + ts + "," + "contract:" + contractNumber + "," + "role:" + role + "}";
        stub.setEvent("TokenIssued", event.getBytes(StandardCharsets.UTF_8));
        return tokenId;
    }

    private static String sha256Hex(final String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
