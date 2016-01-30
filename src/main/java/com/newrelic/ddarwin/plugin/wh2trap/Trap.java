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

String kSnmpTrapOid = "1.3.6.1.4.1.16";

private final Logger logger = Logger.getLogger(Trap.class);

	public <JSONObj> Trap(String host, Integer port, String community, JSONObject json) throws UnknownHostException {
		// TODO Auto-generated constructor stub

		logger.debug("Created trap.");	

		this.host = host;
		this.port = port;
		this.community_id = community;
		this.json = json;
		this.ipAddress = new UdpAddress(InetAddress.getByName(host),port);
	}
	
	public String sendTrap () {


		logger.debug("The JSON object is "+json.toJSONString());

	      try
          {
			   //Create Transport Mapping
		        
		  	  TransportMapping transport = null;
			
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
			  pdu.add(new VariableBinding(SnmpConstants.snmpTrapEnterprise, new OID(kSnmpTrapOid)));
			  pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(kSnmpTrapOid+".60")));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".1"), new OctetString("Agent: "+"New_Relic")));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".2"), new OctetString("Owner: "+(String) json.get("owner"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".3"), new OctetString("Condition Name: "+(String) json.get("condition_name"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".4"), new OctetString("current_state: "+(String) json.get("current_state"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".5"), new OctetString("details: "+(String) json.get("details"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".6"), new OctetString("severity: "+(String) json.get("severity"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".7"), new OctetString("incident_api_url: "+(String) json.get("incident_api_url"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".8"), new OctetString("incident_url: "+(String) json.get("incident_url"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".9"), new OctetString("policy_url: "+(String) json.get("policy_url"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".10"), new OctetString("runbook_url: "+(String) json.get("runbook_url"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".11"), new OctetString("policy_name: "+(String) json.get("policy_name"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".12"), new OctetString("condition_id: "+(String) json.get("condition_id").toString())));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".13"), new OctetString("condition_name: "+(String) json.get("condition_name"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".14"), new OctetString("event_type: "+(String) json.get("event_type"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".15"), new OctetString("incident_id: "+(String) json.get("incident_id").toString())));			  
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".16"), new OctetString("incident_url: "+(String) json.get("incident_url"))));
			  pdu.add(new VariableBinding(new OID(kSnmpTrapOid+".17"), new OctetString("timestamp: "+(String) json.get("timestamp").toString())));

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
			   System.err.println("Error sending Trap to " + ipAddress + ":");
			   System.err.println("Exception Message = " + e.getMessage());
 }
	    
		return "successful";
	}

}
