#!/bin/bash

# JWT ECDSA Key Generation Script
# This script generates ECDSA private and public keys for JWT signing
# and converts them to Base64 format for use in application.yaml

echo "JWT ECDSA Key Generation Script"
echo "=================================="

# Create keys directory if it doesn't exist
mkdir -p keys

# Generate ECDSA private key using prime256v1 curve (P-256)
echo "Generating ECDSA private key..."
openssl ecparam -genkey -name prime256v1 -noout -out keys/private-key.pem

if [ $? -eq 0 ]; then
    echo "Private key generated successfully: keys/private-key.pem"
else
    echo "Failed to generate private key"
    exit 1
fi

echo ""

# Generate corresponding public key
echo "Generating ECDSA public key..."
openssl ec -in keys/private-key.pem -pubout -out keys/public-key.pem

if [ $? -eq 0 ]; then
    echo "Public key generated successfully: keys/public-key.pem"
else
    echo "Failed to generate public key"
    exit 1
fi

echo ""

# Convert private key to PKCS8 format (required for Java)
echo "Converting private key to PKCS8 format..."
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keys/private-key.pem -out keys/private-key-pkcs8.pem

if [ $? -eq 0 ]; then
    echo "Private key converted to PKCS8 format: keys/private-key-pkcs8.pem"
else
    echo "Failed to convert private key to PKCS8 format"
    exit 1
fi

echo ""

# Display the keys for application.yaml
echo ""
echo "Private and Public ECDSA Keys:"
echo "=============================="
echo ""

cat keys/private-key-pkcs8.pem

echo ""

cat keys/public-key.pem