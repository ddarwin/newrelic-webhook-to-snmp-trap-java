package com.newrelic.ddarwin.plugin.wh2trap;

import static spark.Spark.*;

import org.apache.log4j.Logger;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;;

public class WebHookListener {

	String name, host, community;
	Integer port, lPort;
	private JSONObject jsonObj;
	private final Logger logger = Logger.getLogger(WebHookListener.class.getClass());
	
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

		get("/hello/:name", (request, response) -> {
			logger.debug("Processing simple Hello (GET) request");
			return "Hello " + request.params(":name");
		});		
		
		post("/webhook", (request, response) -> {
			logger.debug("Processing WebHook Request");
			logger.debug("Request body is "+request.body());
			
			jsonObj = (JSONObject) new JSONParser().parse(request.body());
			
			Trap trap = new Trap(this.host, this.port, this.community, this.jsonObj);
			logger.debug("Trap send was "+trap.sendTrap());
				
			return "Received an Alert Post";
		});			
	}
}
