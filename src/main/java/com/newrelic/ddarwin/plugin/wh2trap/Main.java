package com.newrelic.ddarwin.plugin.wh2trap;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.*;
import org.json.simple.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.log4j.*;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.UnknownHostException;

import com.typesafe.config.*;;

@SuppressWarnings("unused")
public class Main {
    public static void main(String[] args) throws ParseException {

    	String name = null, host = null, community = null;
    	Integer port = null;
    	Integer lPort = 4500;
    	
    	Logger logger = Logger.getLogger(Main.class);
		final String path = "config/log4j.properties";
		PropertyConfigurator.configure(path);
 
		Config conf = ConfigFactory.parseFile(new File("config/Application.json"));
		
		if (conf.hasPath("listenerPort")) {
			lPort = conf.getInt("listenerPort");
			logger.debug("Logger port being set to "+ lPort);
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
				
		    	WebHookListener wh = new WebHookListener(name, host, port, lPort, community);
		    	wh.startWebHookListener();
			}
		} else {
			logger.debug("There was no valid agent configuration");
		}

    }
}
