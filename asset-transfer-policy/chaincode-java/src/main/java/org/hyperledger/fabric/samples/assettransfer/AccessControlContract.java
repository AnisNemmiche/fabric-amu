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
    private final PolicyDecisionContract decision = new PolicyDecisionContract(policy);
    private final AccessTokenContract token = new AccessTokenContract();

    @Transaction()
    public String requestAccess(final Context ctx, final String role, final String org) {
      boolean allowed = decision.checkAccess(role, org);

      var stub = ctx.getStub();
      long now = ctx.getStub().getTxTimestamp().getEpochSecond();
      String txId = stub.getTxId();
      String logJson = String.format(
          "{\"tx\":\"%s\",\"ts\":%d,\"role\":\"%s\",\"org\":\"%s\",\"permit\":%s}",
          txId, now, role, org, allowed);

      stub.setEvent("AccessDecision", logJson.getBytes());

      String key = stub.createCompositeKey("ACCESS_LOG", txId).toString();
      stub.putState(key, logJson.getBytes());

      return allowed ? "Access Granted" : "Access Denied";
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
