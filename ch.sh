#!/bin/bash

# ./network.sh down

# ./network.sh up createChannel

# Delete container
docker ps -a --filter "name=^dev-peer0\." -q | xargs -r docker rm -f

# Deploy chaincode
./network.sh deployCC -ccn $CC -ccp asset-transfer-policy/chaincode-java -ccl java

# Exec chaincode
peer chaincode invoke   -o "$ORDERER_ADDR" --ordererTLSHostnameOverride orderer.amu.com   --tls --cafile "$ORDERER_CA"   --peerAddresses "$P1" --tlsRootCertFiles "$P1_TLS"   --peerAddresses "$P2" --tlsRootCertFiles "$P2_TLS"   -C "$CHANNEL" -n "$CC" --waitForEvent   -c '{"Args":["AccessControlContract:requestAccess","SUB-2025"]}' --transient "{\"vp\":\"$(base64 -w0 vp.json)\"}"