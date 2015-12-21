package com.newrelic.ddarwin.plugin.wh2trap;

import static spark.Spark.*;

import org.apache.log4j.Logger;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;;

public class WebHookListener {

	String name, host, community;
	Integer port;
	Boolean debug;
	private JSONObject jsonObj;
	private final Logger logger = Logger.getLogger(WebHookListener.class.getClass());
	
	public WebHookListener(Boolean debug, String name, String host, Integer port, String community) {
		// TODO Auto-generated constructor stub
		this.debug = debug;
		this.name = name;
		this.host = host;
		this.port = port;
		this.community = community;
		
	}
	
		
		public void startWebHookListener () {
			
		if (debug) {
			logger.debug("The host value in WH is "+host);
		}

		get("/hello/:name", (request, response) -> {
			if (debug) {
				logger.debug("The payload contains "+request.body().toString());
			}
				return "Hello " + request.params(":name");
		});
		
		get("/", (request, response) -> {
			if (debug) {
				logger.debug("Processing an alert GET");
				logger.debug("Payload length "+request.contentLength());
				logger.debug("Request content type is "+request.contentType());
				logger.debug("Request query parms are "+request.queryParams("myParm"));
				String parm = request.params("myParm");
				logger.debug("The payload contains "+parm);
				Trap trap = new Trap(this.debug, this.host, this.port, this.community, this.jsonObj);
				logger.debug("Trap send result = "+trap.sendTrap());
				
			}
			return "Received an Alert Get";
		});		
		
		post("/webhook", (request, response) -> {
			if (debug) {
				logger.debug("Processing an alert POST");
				logger.debug("Request body is "+request.body());
				
				jsonObj = (JSONObject) new JSONParser().parse(request.body());
				
				Trap trap = new Trap(this.debug, this.host, this.port, this.community, this.jsonObj);
				logger.debug("Trap send was "+trap.sendTrap());
				
			}
			return "Received an Alert Post";
		});			
	}
}
