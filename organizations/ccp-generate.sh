#!/usr/bin/env bash

function one_line_pem {
    echo "`awk 'NF {sub(/\\n/, ""); printf "%s\\\\\\\n",$0;}' $1`"
}

function json_ccp {
    local ORG=$1
    local ORGMSP=$2
    local P0PORT=$3
    local CAPORT=$4
    local PP=$(one_line_pem $5)
    local CP=$(one_line_pem $6)
    sed -e "s/\${ORG}/$ORG/g" \
        -e "s/\${ORGMSP}/$ORGMSP/g" \
        -e "s/\${P0PORT}/$P0PORT/g" \
        -e "s/\${CAPORT}/$CAPORT/g" \
        -e "s#\${PEERPEM}#$PP#g" \
        -e "s#\${CAPEM}#$CP#g" \
        organizations/ccp-template.json
}

function yaml_ccp {
    local ORG=$1
    local ORGMSP=$2
    local P0PORT=$3
    local CAPORT=$4
    local PP=$(one_line_pem $5)
    local CP=$(one_line_pem $6)
    sed -e "s/\${ORG}/$ORG/g" \
        -e "s/\${ORGMSP}/$ORGMSP/g" \
        -e "s/\${P0PORT}/$P0PORT/g" \
        -e "s/\${CAPORT}/$CAPORT/g" \
        -e "s#\${PEERPEM}#$PP#g" \
        -e "s#\${CAPEM}#$CP#g" \
        organizations/ccp-template.yaml | sed -e $'s/\\\\n/\\\n          /g'
}

# Manufacturer
ORGMSP=Manufacturer
ORG=manufacturer
P0PORT=7051
CAPORT=7054
PEERPEM=organizations/peerOrganizations/manufacturer.amu.local/tlsca/tlsca.manufacturer.amu.local-cert.pem
CAPEM=organizations/peerOrganizations/manufacturer.amu.local/ca/ca.manufacturer.amu.local-cert.pem

echo "$(json_ccp $ORG $ORGMSP $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/manufacturer.amu.local/connection-manufacturer.json
echo "$(yaml_ccp $ORG $ORGMSP $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/manufacturer.amu.local/connection-manufacturer.yaml

# Subcontractor
ORGMSP=Subcontractor
ORG=subcontractor
P0PORT=9051
CAPORT=8054
PEERPEM=organizations/peerOrganizations/subcontractor.amu.local/tlsca/tlsca.subcontractor.amu.local-cert.pem
CAPEM=organizations/peerOrganizations/subcontractor.amu.local/ca/ca.subcontractor.amu.local-cert.pem

echo "$(json_ccp $ORG $ORGMSP $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/subcontractor.amu.local/connection-subcontractor.json
echo "$(yaml_ccp $ORG $ORGMSP $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/subcontractor.amu.local/connection-subcontractor.yaml
