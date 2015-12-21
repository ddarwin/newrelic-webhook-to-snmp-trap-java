package com.newrelic.ddarwin.plugin.wh2trap;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.json.*;
import org.json.simple.*;
import org.json.simple.parser.*;

import org.snmp4j.*;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;

@SuppressWarnings("unused")
public class Trap {
	
String host, community_id;
Integer port = null;
JSONObject json;
UdpAddress ipAddress;

private final Logger logger = Logger.getLogger(Trap.class.getClass());

	public <JSONObj> Trap(String host, Integer port, String community, JSONObject json) throws UnknownHostException {
		// TODO Auto-generated constructor stub

		logger.debug("Created trap with debug ");	

		this.host = host;
		this.port = port;
		this.community_id = community;
		this.json = json;
		this.ipAddress = new UdpAddress(InetAddress.getByName(host),port);
	}
	
	public String sendTrap () {
	        
	    @SuppressWarnings("rawtypes")
		TransportMapping transport = null;

		logger.debug("The JSON object is "+json.toJSONString());

	      try
          {
			   //Create Transport Mapping
			  logger.debug("Owner is "+json.get("owner"));
			  logger.debug("Event Type is "+json.get("event_type"));
			  logger.debug("Current state is "+json.get("current_state"));
			  logger.debug("The Alert details are: "+json.get("detail"));
			  logger.debug("The host contains "+ipAddress.toString());
			
			  //Create Target 
			  logger.debug("Sending trap to " + ipAddress);
			  CommunityTarget comtarget = new CommunityTarget();
			  comtarget.setCommunity(new OctetString(community_id));
			  comtarget.setVersion(SnmpConstants.version2c);
			  comtarget.setAddress(ipAddress);
			  comtarget.setRetries(4);
			  comtarget.setTimeout(10000);
			  logger.debug("The Community String is "+comtarget.toString());

			
				try {
					transport = new DefaultUdpTransportMapping();
					transport.listen();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			  //Create PDU for V2 Trap
			  PDU pdu = new PDU();
			  pdu.add(new VariableBinding(SnmpConstants.snmpTrapEnterprise, new OID("1.3.6.1.4.1.1139")));
			  pdu.add(new VariableBinding(SnmpConstants.snmpTrapCommunity, new OctetString(community_id)));
			  pdu.add(new VariableBinding(SnmpConstants.sysName, new OctetString("New_Relic")));
			  pdu.add(new VariableBinding(SnmpConstants.sysName, new OctetString((String) json.get("event_type"))));
			  pdu.add(new VariableBinding(SnmpConstants.sysName, new OctetString((String) json.get("policy_name"))));
			  pdu.add(new VariableBinding(SnmpConstants.sysName, new OctetString((String) json.get("detail"))));
			  pdu.add(new VariableBinding(SnmpConstants.sysName, new OctetString((String) json.get("condition_name"))));
			  pdu.setType(PDU.TRAP);	
			
			  //Send the PDU
			  logger.debug("PDU contains "+pdu);
			  Snmp snmp = new Snmp(transport);
			  snmp.send(pdu, comtarget);
			  snmp.close();
			  logger.debug("Trap sent!!!");
			 }
			      catch (Exception e)
			 {
			   System.err.println("Error sending Trap to " + ipAddress + ":" + port);
			   System.err.println("Exception Message = " + e.getMessage());
 }
	    
		return "successful";
	}

}
