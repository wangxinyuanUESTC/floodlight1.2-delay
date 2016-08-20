package net.floodlightcontroller.mactracker;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.restserver.IRestApiService;
import net.floodlightcontroller.threadpool.IThreadPoolService;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import org.sdnplatform.sync.thrift.SyncMessage;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.types.SwitchMessagePair;
/**
 * 自己按照floodlight官网上的步骤写的，主要功能是实现对特定消息的处理
 * @author wxy
 *
 */



public class MACTracker extends SyncMessage implements IOFMessageListener, IFloodlightModule {
	protected IFloodlightProviderService floodlightProvider;
    //需要一个存储器存储信息
	protected Set<Long> macAddresses;
	//用于把结果输出
	protected static Logger logger;
	protected static int strnumber=0;
	protected static int endnumber=0;
	protected IThreadPoolService threadPool = null;
	@Override
	public String getName() {
		// TODO Auto-generated method stub
//		return null;
		return MACTracker.class.getSimpleName();
	}

	
	
	public static void strTime(){
		logger.info("..strnumber:"+(strnumber++)+".......strtime:......"+System.currentTimeMillis());
	}
	public static  void endTime(){
		logger.info("..endnumber:"+(endnumber++)+".......endtime:......"+System.currentTimeMillis());
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
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		// TODO Auto-generated method stub
//		return null;
		Collection<Class<? extends IFloodlightService>> l =
				new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IOFSwitchService.class);
		l.add(IThreadPoolService.class);
		l.add(IRestApiService.class);
		return l;
	}

	@Override
	public void init(FloodlightModuleContext context) throws FloodlightModuleException {
		// TODO Auto-generated method stub
		//用于初始化REST API
				floodlightProvider=context.getServiceImpl(IFloodlightProviderService.class);
				threadPool =
		                context.getServiceImpl(IThreadPoolService.class);
				macAddresses=new ConcurrentSkipListSet<Long>();
				logger=LoggerFactory.getLogger(MACTracker.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		// TODO Auto-generated method stub
//		floodlightProvider.addOFMessageListener(OFType.ECHO_REQUEST, this);
//		floodlightProvider.addOFMessageListener(OFType.ECHO_REPLY, this);
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
//		floodlightProvider.addOFMessageListener(OFType.STATS_REQUEST, this);
//		floodlightProvider.addOFMessageListener(OFType.GET_CONFIG_REPLY, this);
//		floodlightProvider.addOFMessageListener(OFType.FEATURES_REPLY, this);
//		floodlightProvider.addOFMessageListener(OFType.HELLO, this);
//		floodlightProvider.addOFMessageListener(OFType.STATS_REPLY, this);
//		floodlightProvider.addOFMessageListener(OFType.FLOW_MOD, this);
//		floodlightProvider.addOFMessageListener(OFType.PACKET_OUT, this);
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch sw, OFMessage msg,
			FloodlightContext cntx) {
		// TODO Auto-generated method stub
//		return null;
		
		
		                    
		Ethernet eth =
                IFloodlightProviderService.bcStore.get(cntx,
                                            IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
 
        Long sourceMACHash = eth.getSourceMACAddress().getLong();
        if (!macAddresses.contains(sourceMACHash)) {
            macAddresses.add(sourceMACHash);
            logger.info("MAC 地址: {} 检测到的地址为: {}",
                    eth.getSourceMACAddress().toString(),
                    sw.getId().toString());
       
        
        }
        switch(msg.getType()) {
        case ECHO_REQUEST:
        	logger.info(".....................................ECHO_REQUEST");
            break;
        case ECHO_REPLY:
        	logger.info(".....................................ECHO_REPLY");
        	break;
        case PACKET_IN:
        	logger.info("...................................PACKET_IN");
        	break;
        case GET_CONFIG_REPLY:
        	logger.info("................................GET_CONFIG_REPLY");
            break;
        case STATS_REQUEST:
        	logger.info("................................STATS_REQUEST");
            break;
        case FEATURES_REPLY:
        	logger.info("................................FEATURES_REPLY");
            break;
        case HELLO:
        	logger.info("................................HELLO");
            break;
        case STATS_REPLY:
        	logger.info("................................STATS_REPLY");
            break;
        case FLOW_MOD:
        	logger.info("................................FLOW_MOD");
            break;
        case PACKET_OUT:
        	logger.info("................................PACKET_OUT");
            break;
            
        default:
            break;
    }
        return Command.CONTINUE;
	}

}
