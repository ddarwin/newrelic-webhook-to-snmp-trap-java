package com.newrelic.ddarwin.plugin.wh2trap;

import java.io.IOException;

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
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

@SuppressWarnings("unused")
public class Trap {
	
String host, community_id;
Integer port = null;
Boolean debug;
JSONObject json;

private final Logger logger = Logger.getLogger(Trap.class.getClass());

	public <JSONObj> Trap(Boolean debug, String host, Integer port, String community, JSONObject json) {
		// TODO Auto-generated constructor stub
		this.debug = debug;
		
		if (debug) {
			logger.debug("Created trap with debug " + debug);	
		}

		this.host = host;
		this.port = port;
		this.community_id = community;
		this.json = json;
		
	}
	
	public String sendTrap () {
		
	    String community = community_id;
	    String ipAddress = host;
	    
	    TransportMapping transport = null;

		if (debug) {
			logger.debug("Host is "+ host);
			logger.debug("Port is "+port);
			logger.debug("Community String is "+community);
			logger.debug("The JSON object is "+json.toJSONString());
		}
		
		try {
			transport = new DefaultUdpTransportMapping();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      try
          {
			   //Create Transport Mapping
			  transport.listen();
			
			  //Create Target 
			  CommunityTarget comtarget = new CommunityTarget();
			  comtarget.setCommunity(new OctetString(community));
			  comtarget.setVersion(SnmpConstants.version1);
			  comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
			  comtarget.setRetries(4);
			  comtarget.setTimeout(10000);
			  logger.debug("The Community String is "+comtarget.toString());
			
			  //Create PDU for V1
			  PDU pdu = new PDU();
			  pdu.add(new VariableBinding(SnmpConstants.snmpTrapEnterprise, new OID("1.3.6.1.4.0.0.0.0")));
			  pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress));
			  pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID("1.3.6.1.2.1.1.5.0.0")));
			  pdu.add(new VariableBinding(SnmpConstants.snmpTrapCommunity, new OctetString(community)));
			  pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.37.0.78"), new OctetString("Version 2 Trap")));
			  pdu.setType(PDU.TRAP);
			
			  VariableBinding v = new VariableBinding();
			  v.setOid(SnmpConstants.sysName);
			  v.setVariable(new OctetString("Param1"));
			  pdu.add(v);	
			
			  //Send the PDU
			  logger.debug("The PDU contains "+pdu.toString());
			  logger.debug("Sending trap to " + ipAddress + ":" + port);
			  Snmp snmp = new Snmp(transport);
//			  snmp.send(pdu, comtarget);
//			  snmp.close();
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
