package com.newrelic.ddarwin.plugin.wh2trap;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.*;
import org.json.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.log4j.*;
import java.io.IOException;

import com.typesafe.config.*;;

@SuppressWarnings("unused")
public class Main {
    public static void main(String[] args) throws ParseException {

    	String name = null, host = null, community = null;
    	Integer port = null;
    	
    	Logger logger = Logger.getLogger(Main.class.getClass());
//        static final Logger logger = Logger.getLogger(HelloWorld.class);
        final String path = "config/log4j.properties";
 
    	Config conf = ConfigFactory.parseFile(new File("config/Application.json"));
		Boolean isDebug = false;
		
	    PropertyConfigurator.configure(path);
		
		if (conf.hasPath("debug")) {
			logger.setLevel(Level.DEBUG);
			isDebug = true;
		} else {
			isDebug = false;
		}
		
		if  (conf.hasPath("agents")) {

			for (Config c : conf.getConfigList("agents")) {
					logger.debug("Host is "+ c.getString("hostname"));
					logger.debug("Name is "+ c.getString("name"));
					logger.debug("Port is "+c.getInt("port"));
					logger.debug("Community String is "+c.getString("community_string"));
				
			host = c.getString("hostname");
			name = c.getString("name");
			port = c.getInt("port");
			community = c.getString("community_string");
			
	    	WebHookListener wh = new WebHookListener(isDebug, name, host, port, community);
	    	wh.startWebHookListener();
			}
		} else {
			logger.debug("There was no valid agent configuration");
		}

		if  (conf.hasPath("testTrap")) {
			String trapString = "{\"owner\":\"Donald Darwin\",\"severity\":\"INFO\",\"current_state\":\"test\",\"policy_name\":\"New Relic Alert - Test Policy\",\"condition_id\":0,\"event_type\":\"NOTIFICATION\",\"incident_id\":0,\"account_name\":\"NewRelic Travel\",\"detail\":\"New Relic Alert - Channel Test\",\"condition_name\":\"New Relic Alert - Test Condition\",\"timestamp\":1450559269832}";
			JSONObject jsonObj = null;
			try {
					jsonObj = (JSONObject) new JSONParser().parse(trapString);
				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Trap trap = new Trap(isDebug, host, port, community, jsonObj);
				trap.sendTrap();
		}
    }
}
