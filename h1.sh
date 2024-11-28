#!/bin/bash

#PREREQUISITES:
#mininet, hping3, netcat, tcpdump
#sudo apt install hping3
#sudo apt install netcat
#sudo apt install tcpdump

# Set up parameters
TARGET_IP="10.0.0.2"
PORTS=(16384 16385 16386)   
TOS=(32 184)
PCAP_FILE="mininet_traffic.pcap"

# Function to generate traffic from h1 to h2
generate_hping3_traffic() {
    echo "Generating traffic from h1 to $TARGET_IP..."
    for port in "${PORTS[@]}"; do
        local tos=${TOS[$((RANDOM % ${#TOS[@]}))]}
        echo "Sending traffic to port $port with TOS=$tos..."
        hping3 --udp -p "$port" -i u200000 --data 160 --tos "$tos" -q "$TARGET_IP" &
        sleep 2
    done
}

# Start tcpdump to capture traffic
echo "Starting tcpdump on h2 to capture traffic in $PCAP_FILE..."
tcpdump -i h1-eth0 -w "$PCAP_FILE" &
TCPDUMP_PID=$!

# Start traffic
#generate_hping3_traffic

# Run traffic for a specified duration
DURATION=20
#echo "Running traffic for $DURATION seconds..."
#sleep "$DURATION"

while true; do
    generate_hping3_traffic
    sleep "$DURATION"
done

# Stop all background processes
echo "Stopping all background processes..."
kill $TCPDUMP_PID
kill $(jobs -p)

# Display capture file
#echo "Traffic captured in $PCAP_FILE."

