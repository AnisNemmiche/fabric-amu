package main

import (
	"fmt"
	"rest-api-go/web"
)

func main() {
	//Initialize setup for Manufacturer
	cryptoPath := "../../test-network/organizations/peerOrganizations/manufacturer.amu.com"
	orgConfig := web.OrgSetup{
		OrgName:      "Manufacturer",
		MSPID:        "ManufacturerMSP",
		CertPath:     cryptoPath + "/users/User1@manufacturer.amu.com/msp/signcerts/cert.pem",
		KeyPath:      cryptoPath + "/users/User1@manufacturer.amu.com/msp/keystore/",
		TLSCertPath:  cryptoPath + "/peers/peer0.manufacturer.amu.com/tls/ca.crt",
		PeerEndpoint: "dns:///localhost:7051",
		GatewayPeer:  "peer0.manufacturer.amu.com",
	}

	orgSetup, err := web.Initialize(orgConfig)
	if err != nil {
		fmt.Println("Error initializing setup for Manufacturer: ", err)
	}
	web.Serve(web.OrgSetup(*orgSetup))
}
