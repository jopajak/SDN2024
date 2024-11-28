#!/bin/bash

#PREREQUISITES:
#mininet, hping3, netcat, tcpdump
#sudo apt install hping3
#sudo apt install netcat
#sudo apt install tcpdump

# Set up parameters
TARGET_IP="10.0.0.2"
PORTS=(16384 16385 16386)   
# PCAP_FILE="mininet_traffic.pcap"

# Function to start netcat listeners on h2
start_netcat_listeners() {
    echo "Starting netcat listeners on h2..."
    for port in "${PORTS[@]}"; do
        nc -u -l -p "$port" &
        echo "Netcat listening on port $port "
    done
}

# Start tcpdump to capture traffic
#echo "Starting tcpdump on h2 to capture traffic in $PCAP_FILE..."
#h1 tcpdump -i h1-eth0 -w "$PCAP_FILE" &
#TCPDUMP_PID=$!

# Start listening
#start_netcat_listeners

# Run traffic for a specified duration
DURATION=30
#echo "Listening traffic for $DURATION seconds..."
#sleep "$DURATION"

while true; do
    start_netcat_listeners
    sleep "$DURATION"
done

# Stop all background processes
echo "Stopping all background processes..."
kill $TCPDUMP_PID
kill $(jobs -p)

# Display capture file
#echo "Traffic captured in $PCAP_FILE."

