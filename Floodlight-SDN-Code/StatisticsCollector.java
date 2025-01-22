package pl.edu.agh.kt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.projectfloodlight.openflow.protocol.OFPortStatsEntry;
import org.projectfloodlight.openflow.protocol.OFPortStatsReply;
import org.projectfloodlight.openflow.protocol.OFStatsReply;
import org.projectfloodlight.openflow.protocol.OFStatsRequest;
import org.projectfloodlight.openflow.types.OFPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ListenableFuture;

import net.floodlightcontroller.core.IOFSwitch;

public class StatisticsCollector {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsCollector.class);

    private double currentLoad = 0;
    private IOFSwitch sw;
    public final double capacity = 12500000; //w bajtach Bps -> odpowiednik 100Mbps
    /*   Skad tyle?
     *   chcemy 100Mbps = 100 000 000 bps
     *   
     *   100 000 000/8 -> Bajty 
     * 
     * */
    public static final int PORT_STATISTICS_POLLING_INTERVAL = 3000; // in ms
    private static StatisticsCollector singleton;

    private StatisticsCollector(IOFSwitch sw) {
        this.sw = sw;
        new Timer().scheduleAtFixedRate(new PortStatisticsPoller(), 0, PORT_STATISTICS_POLLING_INTERVAL);
    }

    public static StatisticsCollector getInstance(IOFSwitch sw) {
        logger.debug("getInstance() begin");
        synchronized (StatisticsCollector.class) {
            if (singleton == null) {
                logger.debug("Creating StatisticsCollector singleton");
                singleton = new StatisticsCollector(sw);
            }
        }
        logger.debug("getInstance() end");
        return singleton;
    }

    private long speed(long packet) {
        return (packet * 1000) / PORT_STATISTICS_POLLING_INTERVAL; // bytes/s
    }

    public double getCurrentLoad() {
        return currentLoad; 
    }
    

    private class PortStatisticsPoller extends TimerTask {
    	private long packets = 0;
    	
        @Override
        public void run() {
            logger.debug("Polling port statistics...");
            synchronized (StatisticsCollector.this) {
                if (sw == null) {
                    logger.error("No switch connected.");
                    return;
                }

                try {
                    OFStatsRequest<?> req = sw.getOFFactory().buildPortStatsRequest()
                            .setPortNo(OFPort.ANY)
                            .build();
                    ListenableFuture<?> future = sw.writeStatsRequest(req);
                    List<OFStatsReply> values = (List<OFStatsReply>) future.get(
                            PORT_STATISTICS_POLLING_INTERVAL * 1000 / 2, TimeUnit.MILLISECONDS);

                    OFPortStatsReply psr = (OFPortStatsReply) values.get(0);
                   
                    for (OFPortStatsEntry pse : psr.getEntries()) {
                        
                        System.out.println("Switch id: " + sw.getId().getLong());
                        
                        if(sw.getId().getLong() == 1){
                        	if(pse.getPortNo().getPortNumber() == 2){
                        		long actual_bytes = pse.getTxBytes().getValue() - packets;
                        		currentLoad = speed(actual_bytes)/capacity;
                        		System.out.println("Current speed is: " + speed(actual_bytes));
                        		System.out.println("Current load is: " + currentLoad);
                        		
                        		packets = pse.getTxBytes().getValue();
                        	}
                        }
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    logger.error("Error during statistics polling", ex);
                }
            }
        }
    }
}
