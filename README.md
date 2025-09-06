# ENV Variables

```bash
# Set global variables
export PATH=${PWD}/bin:${PWD}:$PATH
export FABRIC_CFG_PATH=${PWD}/config

# Manufacturer
export CORE_PEER_TLS_ENABLED=true
export CORE_PEER_LOCALMSPID=ManufacturerMSP
export CORE_PEER_TLS_ROOTCERT_FILE=${PWD}/organizations/peerOrganizations/manufacturer.amu.com/peers/peer0.manufacturer.amu.com/tls/ca.crt
export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/manufacturer.amu.com/users/Admin@manufacturer.amu.com/msp
export CORE_PEER_ADDRESS=localhost:7051

# Subcontractor
export CORE_PEER_LOCALMSPID=SubcontractorMSP
export CORE_PEER_TLS_ROOTCERT_FILE=${PWD}/organizations/peerOrganizations/subcontractor.amu.com/peers/peer0.subcontractor.amu.com/tls/ca.crt
export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/subcontractor.amu.com/users/Admin@subcontractor.amu.com/msp
export CORE_PEER_ADDRESS=localhost:9051

# Chaincode
export ORDERER_ADDR=localhost:7050
export ORDERER_CA=$PWD/organizations/ordererOrganizations/amu.com/orderers/orderer.amu.com/msp/tlscacerts/tlsca.amu.com-cert.pem
``` 

# Hyperledger Fabric

Remove any containers or artifacts from any previous runs

```bash
./network.sh down
```

Bring up the network

```bash
./network.sh up
```

Create a channel between Manufacturer and Subcontractor

```bash
./network.sh createChannel -c channel1
```

Bring up the network and create a channel in a single step

```bash
./network.sh up createChannel
```

Deploy chaincode basic

```bash
./network.sh deployCC -ccn basic -ccp asset-transfer-basic/chaincode-java -ccl java
```

# DID with VeramoCLi
!!! Necessite Node.js v20 !!!

 Installation
```bash
npm i -D @veramo/cli
npm i -D @veramo/did-resolver did-resolver key-did-resolver web-did-resolver
npx @veramo/cli config create-secret-key
```

On crée notre fichier de configuration `agent.yml` :

```bash
npx veramo config create
```

On genère nos did:key pour chaque organisation :

```bash
npx veramo did create
```


| Provider | Alias        | DID                                                      |
|----------|--------------|----------------------------------------------------------|
| did:key  | manufacturer | did:key:z6MkerahDNLuGq1gBhnygkcKnALKAoQqE4vTrtGPnjgbzeSb |
| did:key  | subcontractor| did:key:z6MkkX5CMMHZJGJqRCHrQ9Qfhn6TuahsFpYXzh3wHWA1ZiC1 |


On crée un VC pour le subcontractor (avec pour role = subcontractor) :

```bash
npx veramo credential create
```

```yaml
? Credential proofFormat jwt
? Issuer DID did:key:z6MkerahDNLuGq1gBhnygkcKnALKAoQqE4vTrtGPnjgbzeSb manufacturer
? Subject DID did:key:z6MkkX5CMMHZJGJqRCHrQ9Qfhn6TuahsFpYXzh3wHWA1ZiC1
? Credential Type VerifiableCredential,Profile
? Claim Type role
? Claim Value subcontractor
{
  credentialSubject: {
    role: 'subcontractor',
    id: 'did:key:z6MkkX5CMMHZJGJqRCHrQ9Qfhn6TuahsFpYXzh3wHWA1ZiC1'
  },
  issuer: { id: 'did:key:z6MkerahDNLuGq1gBhnygkcKnALKAoQqE4vTrtGPnjgbzeSb' },
  type: [ 'VerifiableCredential', 'Profile' ],
  '@context': [
    'https://www.w3.org/2018/credentials/v1',
    'https://veramo.io/contexts/profile/v1'
  ],
  issuanceDate: '2025-09-05T18:31:53.000Z',
  proof: {
    type: 'JwtProof2020',
    jwt: 'eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCJ9.eyJ2YyI6eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSIsImh0dHBzOi8vdmVyYW1vLmlvL2NvbnRleHRzL3Byb2ZpbGUvdjEiXSwidHlwZSI6WyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsIlByb2ZpbGUiXSwiY3JlZGVudGlhbFN1YmplY3QiOnsicm9sZSI6InN1YmNvbnRyYWN0b3IifX0sInN1YiI6ImRpZDprZXk6ejZNa2tYNUNNTUhaSkdKcVJDSHJROVFmaG42VHVhaHNGcFlYemgzd0hXQTFaaUMxIiwibmJmIjoxNzU3MDk3MTEzLCJpc3MiOiJkaWQ6a2V5Ono2TWtlcmFoRE5MdUdxMWdCaG55Z2tjS25BTEtBb1FxRTR2VHJ0R1BuamdiemVTYiJ9.2PrUvn7oTUh6P6epGKtNUCqOfSp0715FTnpIXT8L5f-5USZu3yI6nI9VduZiXz42ncEOdJLonJeAj0NgVE9_DQ'
  }
}
```

On crée notre VP (a partir du VC emit par le manufacturer) :

```bash
npx veramo presentation create
```

```yaml
? Holder DID did:key:z6MkkX5CMMHZJGJqRCHrQ9Qfhn6TuahsFpYXzh3wHWA1ZiC1 subcontractor
? Tag (threadId) sub1
? Number of Verifiers 1
? Presentation type VerifiablePresentation,Profile
? Select Verifier or enter manually did:key:z6MkerahDNLuGq1gBhnygkcKnALKAoQqE4vTrtGPnjgbzeSb
? Select credential {"role":"subcontractor","id":"did:key:z6MkkX5CMMHZJGJqRCHrQ9Qfhn6TuahsFpYXzh3wHWA1ZiC1"} | Issuer:
did:key:z6MkerahDNLuGq1gBhnygkcKnALKAoQqE4vTrtGPnjgbzeSb
? Add another credential? No
{
  tag: 'sub1',
  verifiableCredential: [
    {
      credentialSubject: {
        role: 'subcontractor',
        id: 'did:key:z6MkkX5CMMHZJGJqRCHrQ9Qfhn6TuahsFpYXzh3wHWA1ZiC1'
      },
      issuer: {
        id: 'did:key:z6MkerahDNLuGq1gBhnygkcKnALKAoQqE4vTrtGPnjgbzeSb'
      },
      type: [ 'VerifiableCredential', 'Profile' ],
      '@context': [
        'https://www.w3.org/2018/credentials/v1',
        'https://veramo.io/contexts/profile/v1'
      ],
      issuanceDate: '2025-09-05T18:31:53.000Z',
      proof: {
        type: 'JwtProof2020',
        jwt: 'eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCJ9.eyJ2YyI6eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSIsImh0dHBzOi8vdmVyYW1vLmlvL2NvbnRleHRzL3Byb2ZpbGUvdjEiXSwidHlwZSI6WyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsIlByb2ZpbGUiXSwiY3JlZGVudGlhbFN1YmplY3QiOnsicm9sZSI6InN1YmNvbnRyYWN0b3IifX0sInN1YiI6ImRpZDprZXk6ejZNa2tYNUNNTUhaSkdKcVJDSHJROVFmaG42VHVhaHNGcFlYemgzd0hXQTFaaUMxIiwibmJmIjoxNzU3MDk3MTEzLCJpc3MiOiJkaWQ6a2V5Ono2TWtlcmFoRE5MdUdxMWdCaG55Z2tjS25BTEtBb1FxRTR2VHJ0R1BuamdiemVTYiJ9.2PrUvn7oTUh6P6epGKtNUCqOfSp0715FTnpIXT8L5f-5USZu3yI6nI9VduZiXz42ncEOdJLonJeAj0NgVE9_DQ'
      }
    }
  ],
  holder: 'did:key:z6MkkX5CMMHZJGJqRCHrQ9Qfhn6TuahsFpYXzh3wHWA1ZiC1',
  verifier: [ 'did:key:z6MkerahDNLuGq1gBhnygkcKnALKAoQqE4vTrtGPnjgbzeSb' ],
  type: [ 'VerifiablePresentation', 'Profile' ],
  '@context': [ 'https://www.w3.org/2018/credentials/v1' ],
  issuanceDate: '2025-09-05T18:39:27.000Z',
  proof: {
    type: 'JwtProof2020',
    jwt: 'eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCJ9.eyJ2cCI6eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSJdLCJ0eXBlIjpbIlZlcmlmaWFibGVQcmVzZW50YXRpb24iLCJQcm9maWxlIl0sInZlcmlmaWFibGVDcmVkZW50aWFsIjpbImV5SmhiR2NpT2lKRlpFUlRRU0lzSW5SNWNDSTZJa3BYVkNKOS5leUoyWXlJNmV5SkFZMjl1ZEdWNGRDSTZXeUpvZEhSd2N6b3ZMM2QzZHk1M015NXZjbWN2TWpBeE9DOWpjbVZrWlc1MGFXRnNjeTkyTVNJc0ltaDBkSEJ6T2k4dmRtVnlZVzF2TG1sdkwyTnZiblJsZUhSekwzQnliMlpwYkdVdmRqRWlYU3dpZEhsd1pTSTZXeUpXWlhKcFptbGhZbXhsUTNKbFpHVnVkR2xoYkNJc0lsQnliMlpwYkdVaVhTd2lZM0psWkdWdWRHbGhiRk4xWW1wbFkzUWlPbnNpY205c1pTSTZJbk4xWW1OdmJuUnlZV04wYjNJaWZYMHNJbk4xWWlJNkltUnBaRHByWlhrNmVqWk5hMnRZTlVOTlRVaGFTa2RLY1ZKRFNISlJPVkZtYUc0MlZIVmhhSE5HY0ZsWWVtZ3pkMGhYUVRGYWFVTXhJaXdpYm1KbUlqb3hOelUzTURrM01URXpMQ0pwYzNNaU9pSmthV1E2YTJWNU9ubzJUV3RsY21Gb1JFNU1kVWR4TVdkQ2FHNTVaMnRqUzI1QlRFdEJiMUZ4UlRSMlZISjBSMUJ1YW1kaWVtVlRZaUo5LjJQclV2bjdvVFVoNlA2ZXBHS3ROVUNxT2ZTcDA3MTVGVG5wSVhUOEw1Zi01VVNadTN5STZuSTlWZHVaaVh6NDJuY0VPZEpMb25KZUFqME5nVkU5X0RRIl19LCJ0YWciOiJzdWIxIiwibmJmIjoxNzU3MDk3NTY3LCJpc3MiOiJkaWQ6a2V5Ono2TWtrWDVDTU1IWkpHSnFSQ0hyUTlRZmhuNlR1YWhzRnBZWHpoM3dIV0ExWmlDMSIsImF1ZCI6WyJkaWQ6a2V5Ono2TWtlcmFoRE5MdUdxMWdCaG55Z2tjS25BTEtBb1FxRTR2VHJ0R1BuamdiemVTYiJdfQ.bvyYPBHs2sYzbHfe8fwKz0W5d2_U3cndB0Xv2QaJd1ep5v5m2Z8hQS45LR0VzWBlOM6SxyBTMffZIEZFBwZRDw'
  }
}
```

Serveur Veramo :

```bash
npx veramo server --port 3332 --config ./agent.yml
```

# Chaincode

Set chaincode variables
```bash
export CC=AccessControlContract
export CHANNEL=mychannel

export P1=localhost:7051
export P1_TLS=$PWD/organizations/peerOrganizations/manufacturer.amu.com/peers/peer0.manufacturer.amu.com/tls/ca.crt
export P2=localhost:9051
export P2_TLS=$PWD/organizations/peerOrganizations/subcontractor.amu.com/peers/peer0.subcontractor.amu.com/tls/ca.crt
```

Deploy chaincode AccessControlContract

```bash
./network.sh deployCC -ccn $CC -ccp asset-transfer-policy/chaincode-java -ccl java
```

Get chaincode metadata
```bash
peer chaincode query -C $CHANNEL -n $CC -c '{"Args":["org.hyperledger.fabric:GetMetadata"]}'
```

Init ledger
```bash
peer chaincode invoke \
  -o "$ORDERER_ADDR" --ordererTLSHostnameOverride orderer.amu.com \
  --tls --cafile "$ORDERER_CA" \
  --peerAddresses "$P1" --tlsRootCertFiles "$P1_TLS" \
  --peerAddresses "$P2" --tlsRootCertFiles "$P2_TLS" \
  -C "$CHANNEL" -n "$CC" --waitForEvent \
  -c '{"Args":["AccessControlContract:requestAccess","subcontractor","subcontractor.amu.com"]}'
```

Lecture des logs d'accès
```bash
peer chaincode query -C "$CHANNEL" -n "$CC" \
  -c '{"Args":["AccessControlContract:ListAccessLogs"]}'
```

Test deny access
```bash
peer chaincode invoke -o "$ORDERER_ADDR" --ordererTLSHostnameOverride orderer.amu.com \
 --tls --cafile "$ORDERER_CA" \
 --peerAddresses "$P1" --tlsRootCertFiles "$P1_TLS" \
 --peerAddresses "$P2" --tlsRootCertFiles "$P2_TLS" \
 -C "$CHANNEL" -n "$CC" --waitForEvent \
 -c '{"Args":["AccessControlContract:requestAccess","subcontractor","notamu.com"]}'
 ```

Lecture des logs d'accès
```bash
peer chaincode query -C "$CHANNEL" -n "$CC" -c '{"Args":["AccessControlContract:ListAccessLogs"]}' | jq
```

Test de Request access avec une lecture seul donc non enregistré
```bash
peer chaincode query -C $CHANNEL -n $CC -c '{
  "Args":["AccessControlContract:requestAccess","subcontractor","subcontractor.amu.com"]
}'
```