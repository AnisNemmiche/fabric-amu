#!/usr/bin/env bash
#
# SPDX-License-Identifier: Apache-2.0




# default to using Manufacturer
ORG=${1:-Manufacturer}

# Exit on first error, print all commands.
set -e
set -o pipefail

# Where am I?
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

ORDERER_CA=${DIR}/test-network/organizations/ordererOrganizations/amu.com/tlsca/tlsca.amu.com-cert.pem
PEER0_MANUFACTURER_CA=${DIR}/test-network/organizations/peerOrganizations/manufacturer.amu.com/tlsca/tlsca.manufacturer.amu.com-cert.pem
PEER0_SUBCONTRACTOR_CA=${DIR}/test-network/organizations/peerOrganizations/subcontractor.amu.com/tlsca/tlsca.subcontractor.amu.com-cert.pem
# PEER0_ORG3_CA=${DIR}/test-network/organizations/peerOrganizations/org3.amu.com/tlsca/tlsca.org3.amu.com-cert.pem


if [[ ${ORG,,} == "manufacturer" || ${ORG,,} == "digibank" ]]; then

   CORE_PEER_LOCALMSPID=ManufacturerMSP
   CORE_PEER_MSPCONFIGPATH=${DIR}/test-network/organizations/peerOrganizations/manufacturer.amu.com/users/Admin@manufacturer.amu.com/msp
   CORE_PEER_ADDRESS=localhost:7051
   CORE_PEER_TLS_ROOTCERT_FILE=${DIR}/test-network/organizations/peerOrganizations/manufacturer.amu.com/tlsca/tlsca.manufacturer.amu.com-cert.pem

elif [[ ${ORG,,} == "subcontractor" || ${ORG,,} == "magnetocorp" ]]; then

   CORE_PEER_LOCALMSPID=SubcontractorMSP
   CORE_PEER_MSPCONFIGPATH=${DIR}/test-network/organizations/peerOrganizations/subcontractor.amu.com/users/Admin@subcontractor.amu.com/msp
   CORE_PEER_ADDRESS=localhost:9051
   CORE_PEER_TLS_ROOTCERT_FILE=${DIR}/test-network/organizations/peerOrganizations/subcontractor.amu.com/tlsca/tlsca.subcontractor.amu.com-cert.pem

else
   echo "Unknown \"$ORG\", please choose Manufacturer/Digibank or Subcontractor/Magnetocorp"
   echo "For example to get the environment variables to set upa Subcontractor shell environment run:  ./setOrgEnv.sh Subcontractor"
   echo
   echo "This can be automated to set them as well with:"
   echo
   echo 'export $(./setOrgEnv.sh Subcontractor | xargs)'
   exit 1
fi

# output the variables that need to be set
echo "CORE_PEER_TLS_ENABLED=true"
echo "ORDERER_CA=${ORDERER_CA}"
echo "PEER0_MANUFACTURER_CA=${PEER0_MANUFACTURER_CA}"
echo "PEER0_SUBCONTRACTOR_CA=${PEER0_SUBCONTRACTOR_CA}"
echo "PEER0_ORG3_CA=${PEER0_ORG3_CA}"

echo "CORE_PEER_MSPCONFIGPATH=${CORE_PEER_MSPCONFIGPATH}"
echo "CORE_PEER_ADDRESS=${CORE_PEER_ADDRESS}"
echo "CORE_PEER_TLS_ROOTCERT_FILE=${CORE_PEER_TLS_ROOTCERT_FILE}"

echo "CORE_PEER_LOCALMSPID=${CORE_PEER_LOCALMSPID}"
