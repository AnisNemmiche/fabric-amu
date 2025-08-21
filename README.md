# Remove any containers or artifacts from any previous runs

```bash
./network.sh down
```

# Bring up the network

```bash
./network.sh up
```

# Create a channel between Manufacturer and Subcontractor

```bash
./network.sh createChannel -c channel1
```

# Bring up the network and create a channel in a single step

```bash
./network.sh up createChannel -c channel1
```

