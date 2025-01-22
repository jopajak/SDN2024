package pl.edu.agh.kt;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.packet.ARP;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.packet.TCP;
import net.floodlightcontroller.packet.UDP;

public class PacketExtractor {
    private static final Logger logger = LoggerFactory.getLogger(PacketExtractor.class);
    private FloodlightContext cntx;
    protected IFloodlightProviderService floodlightProvider;
    private Ethernet eth;
    private IPv4 ipv4;
    private ARP arp;
    private TCP tcp;
    private UDP udp;
    private OFMessage msg;

    private static final int[] VOIP_PORTS = {16384, 16385, 16386}; // Interesujące porty VoIP

    public PacketExtractor(FloodlightContext cntx, OFMessage msg) {
        this.cntx = cntx;
        this.msg = msg;
//        logger.info("PacketExtractor: Constructor method called");
    }

    public PacketExtractor() {
//        logger.info("PacketExtractor: Constructor method called");
    }

    public boolean packetExtract(FloodlightContext cntx) {
        this.cntx = cntx;
        return extractEth();
    }

    public boolean extractEth() {
        eth = IFloodlightProviderService.bcStore.get(cntx,
                IFloodlightProviderService.CONTEXT_PI_PAYLOAD);


        if (eth.getEtherType() == EthType.ARP) {
            arp = (ARP) eth.getPayload();
        }

        if (eth.getEtherType() == EthType.IPv4) {
            ipv4 = (IPv4) eth.getPayload();
            return extractIp();
        }

        return false;
    }

    public boolean extractIp() {
     
        if (IpProtocol.TCP == ipv4.getProtocol()) {
            tcp = (TCP) ipv4.getPayload();
            if (tcp.getSourcePort().getPort() == 500 || tcp.getDestinationPort().getPort() == 500) {
                return true; 
            }
        }

        if (IpProtocol.UDP == ipv4.getProtocol()) {
            udp = (UDP) ipv4.getPayload();
            return checkIfVoIPPorts(); // Sprawdzenie, czy ruch jest VoIP
        }

        return false;
    }

    public boolean checkIfVoIPPorts() {
        
        int destPort = udp.getDestinationPort().getPort();
        for (int voipPort : VOIP_PORTS) {
            if (destPort == voipPort) {
                logger.info("Detected VoIP traffic on port {}", destPort);
                return true;
            }
        }

        logger.info("UDP traffic not on VoIP ports (ports: 16384, 16385, 16386)");
        logger.info("PacketExtractor: UDP ruch nierozpoznany jako VoIP. Destination port: {}", udp.getDestinationPort().getPort());
        return false;
    }

    public void extractArp() {
        logger.info("ARP extractor");
    }
}
