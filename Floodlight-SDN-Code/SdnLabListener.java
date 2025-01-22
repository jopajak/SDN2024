package pl.edu.agh.kt;

import java.util.Collection;
import java.util.Map;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFMeterFlags;
import org.projectfloodlight.openflow.protocol.OFMeterMod;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.types.OFPort;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;

import net.floodlightcontroller.core.IFloodlightProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import java.util.ArrayList;

public class SdnLabListener implements IFloodlightModule, IOFMessageListener {

    protected IFloodlightProviderService floodlightProvider;
    protected static Logger logger;

    @Override
    public String getName() {
        return SdnLabListener.class.getSimpleName();
    }

    @Override
    public boolean isCallbackOrderingPrereq(OFType type, String name) {
        return false;
    }

    @Override
    public boolean isCallbackOrderingPostreq(OFType type, String name) {
        return false;
    }

    @Override
    public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
    	logger.info("************* NEW PACKET IN *************");
        OFPacketIn pin = (OFPacketIn) msg;
        OFPort outPort = OFPort.of(0);
        PacketExtractor extractor = new PacketExtractor();

        // Initialize statistics collector
        StatisticsCollector statsCollector = StatisticsCollector.getInstance(sw);
        
        /* 
         Sciezki dla poszczegolnych switchy
         
         s1 -> s4 -> s3 Priorytetowa
         s1 -> s2 -> s3 Zwykla
                
          
          */
        
             
//        //Switch 1 (wejsciowy przy h1)
        if(sw.getId().getLong() == 1){
        	if (pin.getInPort() == OFPort.of(1)) { // Host h1 -> Switch s1
              double loadOnPort2 = statsCollector.getCurrentLoad(); // Obciążenie portu 2
              double threshold = 0.7;// Próg przepustowości
              
              if (loadOnPort2 > threshold) {
                  if (extractor.packetExtract(cntx)) {
                      outPort = OFPort.of(3); // Priorytetowy ruch na port 3 (góra do s4)
                  } else {
                      outPort = OFPort.of(2); // Ruch standardowy na port 2 (dół do s2)
                  }
              } else {
                  outPort = OFPort.of(2); // Brak przeciążenia, port 2
              }
          } else if (pin.getInPort() == OFPort.of(2)) {
              outPort = OFPort.of(1); // Ruch powrotny: s2 -> s1 -> h1
          } else if (pin.getInPort() == OFPort.of(3)) {
              outPort = OFPort.of(1); // Ruch powrotny: s4 -> s1 -> h1
          }
        }
        
        //Switch 2 (srodkowy)
        if(sw.getId().getLong() == 2){
        	if (pin.getInPort() == OFPort.of(1)) {
    			outPort = OFPort.of(2);
    		} else
    			outPort = OFPort.of(1); //ruch powrotny tylko gora
        }
        
        
        //Switch 4 (srodkowy)
        if(sw.getId().getLong() == 4){
        	if (pin.getInPort() == OFPort.of(1)) {
    			outPort = OFPort.of(2);
    		} else
    			outPort = OFPort.of(1); //ruch powrotny tylko gora
        }
        
        double loadOnPort1 = statsCollector.getCurrentLoad();
        
        //Switch 3
         if(sw.getId().getLong() == 3){
        	if (pin.getInPort() == OFPort.of(2)) { // s3 <- h2
              
              double threshold = 0.7;

              if (loadOnPort1 > threshold) {
                  if (extractor.packetExtract(cntx)) {
                      outPort = OFPort.of(3); // Priorytetowy ruch przez s4
                  } else {
                      outPort = OFPort.of(1); // Ruch standardowy przez s2
                  }
              } else {
                  outPort = OFPort.of(1); // Domyślnie do s2
              }
          } else if (pin.getInPort() == OFPort.of(3)) {
              outPort = OFPort.of(2); // s4 -> s3 -> h2
          } else if (pin.getInPort() == OFPort.of(1)) {
              outPort = OFPort.of(2); // s2 -> s3 -> h2
          }}
        
         // Informacje odnosnie przesylania
        Flows.forwardFirstPacket(sw, pin, outPort);
        
        // Zapisz przepływ w zależności od typu ruchu
        if (extractor.packetExtract(cntx)) {
            Flows.enqueue(sw, pin, cntx, outPort, 1); // Priorytetowa kolejka
            logger.info("Traffic prioritized for VoIP on switch {}", sw.getId());
            logger.info("Current load: {}", loadOnPort1);
            logger.info("Switch {}",sw.getId());
            logger.info("inputPort {} -> outputPort {}",pin.getInPort(),outPort);
        } else {
            Flows.enqueue(sw, pin, cntx, outPort, 0); // Kolejka standardowa

        }
        
        return Command.STOP;
    }
 


    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleServices() {
        return null;
    }

    @Override
    public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
        return null;
    }

    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
        Collection<Class<? extends IFloodlightService>> dependencies = new ArrayList<>();
        dependencies.add(IFloodlightProviderService.class);
        return dependencies;
    }

    @Override
    public void init(FloodlightModuleContext context) throws FloodlightModuleException {
        floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
        logger = LoggerFactory.getLogger(SdnLabListener.class);
    }

    @Override
    public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
        floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
        logger.info("SdnLabListener module started");
    }
}
