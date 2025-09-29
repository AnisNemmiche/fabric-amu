package org.hyperledger.fabric.samples.assettransfer;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ChaincodeException;
import java.nio.charset.StandardCharsets;

@Contract(name = "AccessControlContract")
public final class AccessControlContract implements ContractInterface {
    private final PolicyContract policy = new PolicyContract();
    private final AccessTokenContract token = new AccessTokenContract();

    @Transaction()
    public String requestAccess(final Context ctx, final String contractNumber) {
        var stub = ctx.getStub();
        byte[] vpBytes = stub.getTransient().get("vp");
        if (vpBytes == null) {
            throw new ChaincodeException("missing_vp", "The transient map must contain the 'vp'");
        }
        String vpJson = new String(vpBytes, StandardCharsets.UTF_8);
        String role   = extract(vpJson, "role");
        if (role == null) {
            return "{decision: deny, reason: role_missing}";
        }
        boolean allowed = new PolicyContract().isAllowed(role, contractNumber);
        String txId = stub.getTxId();
        long now = ctx.getStub().getTxTimestamp().getEpochSecond();
        String logJson = String.format(
            "{\"tx\":\"%s\",\"ts\":%d,\"role\":\"%s\",\"contractNumber\":\"%s\",\"permit\":%s}",
            txId, now, role, contractNumber, allowed);
        stub.setEvent("AccessDecision", logJson.getBytes());
        String key = stub.createCompositeKey("ACCESS_LOG", txId).toString();
        stub.putState(key, logJson.getBytes());
        if (!allowed) {
            return "{decision: deny}";
        }
        String tokenId = token.IssueToken(ctx, contractNumber, role);
        return String.format("{decision: permit, token: \"%s\"}", tokenId);
    }

    private static String extract(final String json, final String field) {
        final java.util.regex.Matcher m = java.util.regex.Pattern.compile("\"" + field + "\"\\s*:\\s*\"([^\"]+)\"").matcher(json);
        return m.find() ? m.group(1) : null;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String ListAccessLogs(final Context ctx) {
      JsonArray out = new JsonArray();
      try (QueryResultsIterator<KeyValue> it =
             ctx.getStub().getStateByPartialCompositeKey("ACCESS_LOG", new String[]{})) {
        for (KeyValue kv : it) {
          String v = new String(kv.getValue(), StandardCharsets.UTF_8);
          out.add(JsonParser.parseString(v));
        }
      } catch (Exception e) {
        throw new ChaincodeException("iterator_close_failed", e);
      }
      return out.toString();
    }
}
