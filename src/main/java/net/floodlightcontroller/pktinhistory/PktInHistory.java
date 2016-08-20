package net.floodlightcontroller.pktinhistory;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.types.SwitchMessagePair;
import net.floodlightcontroller.restserver.IRestApiService;
public class PktInHistory implements IFloodlightModule,IPktinHistoryService, IOFMessageListener {
    protected IFloodlightProviderService floodlightProvider;
    protected ConcurrentCircularBuffer<SwitchMessagePair> buffer;
    protected IRestApiService restApi;
    protected static Logger logger;
    @Override
    public String getName() {
        return PktInHistory.class.getSimpleName();
    }
    @Override
    public boolean isCallbackOrderingPrereq(OFType type, String name) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean isCallbackOrderingPostreq(OFType type, String name) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        switch(msg.getType()) {
        case STATS_REQUEST:
        	buffer.add(new SwitchMessagePair(sw, msg));
            break;
            case PACKET_IN:
                buffer.add(new SwitchMessagePair(sw, msg));
                break;
            case ECHO_REQUEST:
            	buffer.add(new SwitchMessagePair(sw, msg));
            	logger.info("ECHO_REQUEST");
                break;
            case ECHO_REPLY:
            	buffer.add(new SwitchMessagePair(sw, msg));
            	logger.info("ECHO_REPLY");
            	break;
            default:
                break;
        }
        return Command.CONTINUE;	
    }
    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleServices() {
        // TODO Auto-generated method stub
//        return null;
    	 Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
         l.add(IPktinHistoryService.class);
         return l;
    }
    @Override
    public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
        // TODO Auto-generated method stub
//        return null;
    	 Map<Class<? extends IFloodlightService>, IFloodlightService> m = new HashMap<Class<? extends IFloodlightService>, IFloodlightService>();
         m.put(IPktinHistoryService.class, this);
         return m;
    }
    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
        Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
        l.add(IFloodlightProviderService.class);
        l.add(IRestApiService.class);
        return l;
    }
    @Override
    public void init(FloodlightModuleContext context) throws FloodlightModuleException {
        floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
        buffer = new ConcurrentCircularBuffer<SwitchMessagePair>(SwitchMessagePair.class, 100);
        restApi = context.getServiceImpl(IRestApiService.class);
        logger=LoggerFactory.getLogger(PktInHistory.class);
    }
    @Override
    public void startUp(FloodlightModuleContext context) {
		floodlightProvider.addOFMessageListener(OFType.ECHO_REQUEST, this);
		floodlightProvider.addOFMessageListener(OFType.ECHO_REPLY, this);
        floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
    	floodlightProvider.addOFMessageListener(OFType.STATS_REQUEST, this);
        restApi.addRestletRoutable(new PktInHistoryWebRoutable());
    }
    
    public ConcurrentCircularBuffer<SwitchMessagePair> getBuffer() {
        return buffer;
    }
	
}
