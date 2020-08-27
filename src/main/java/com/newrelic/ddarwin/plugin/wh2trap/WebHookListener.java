package com.newrelic.ddarwin.plugin.wh2trap;

import static spark.Spark.*;

import org.apache.log4j.Logger;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class WebHookListener {

	String name, host, community, alertMsg;
	Integer port, lPort;
	private JSONObject jsonObj;
	private final Logger logger = Logger.getLogger(WebHookListener.class);
	
	public WebHookListener(String name, String host, Integer port, Integer lPort, String community) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.host = host;
		this.port = port;
		this.lPort = lPort;
		this.community = community;
		
	}
	
		
		public void startWebHookListener () {
			
		logger.info("The WebHook Listener is starting with port "+lPort);
		port(lPort);

		get("/hello/:name", (request, response) -> {
			logger.debug("Processing simple Hello (GET) request");
			return "Hello " + request.params(":name");
		});	
		
		get("/testTrap", (request, response) -> {

			String trapString = "{\"owner\":\"Donald Darwin\","
				+ "\"severity\":\"INFO\","
				+ "\"current_state\":\"test\","
				+ "\"policy_name\":\"New Relic Alert - Test Policy\","
				+ "\"condition_id\":0,\"incident_url\":\"http://google.com\","
				+ "\"event_type\":\"NOTIFICATION\","
				+ "\"incident_id\":0,\"account_name\":\"NewRelic Travel\","
				+ "\"details\":\"New Relic Alert - Channel Test\","
				+ "\"condition_name\":\"New Relic Alert - Test Condition\","
				+ "\"timestamp\":1450559269832}";
			
			jsonObj = (JSONObject) new JSONParser().parse(trapString);
			
			Trap trap = new Trap(this.host, this.port, this.community, this.jsonObj);
			logger.debug("Trap send was "+trap.sendTrap());
		
			logger.debug("Processing simple Hello (GET) request");
			return ("Test Trap generated");
		});
		
		post("/webhook", (request, response) -> {
			logger.debug("Processing WebHook Request");
			alertMsg = request.body();
			logger.debug("Request body is "+alertMsg);
			
			jsonObj = (JSONObject) new JSONParser().parse(alertMsg);
			
			Trap trap = new Trap(this.host, this.port, this.community, this.jsonObj);
			logger.debug("Trap send was "+trap.sendTrap());
				
			return "Received an Alert Post";
		});			
	}
}
