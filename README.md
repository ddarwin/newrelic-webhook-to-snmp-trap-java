# newrelic-wh2trap-server
New Relic Alert to SNMP Trap Plugin

This extension provides a Webhook Alert notification endpoint that converts that Alert into an SNMP Trap V2 format. This is hosted on-premise to allow direct integration to an SNMP Manager sitting behind a firewall. 

## Download the plugin

[Download link for extension](https://github.com/ddarwin/newrelic-wh2trap-server.git)

## Install the prerequisites

This is a Maven package that can be downloaded and built using "mvn install". Or simply download the ZIP and use the executable JAR file and config/ directory. 
This extension requires a Java 1.8 JRE because it uses Lambda expressions. 

## Install the plugin

1. [Download the plugin](https://github.com/ddarwin/newrelic-wh2trap-server/archive/master.zip)
2. Unzip the plugin into a working directory. 
   * **NOTE:** The zip contains a folder called `newrelic-wh2trap-server-master`.
3. CD to the newrelic-wh2trap-server-master directory. 
4. To build the Plug-in run 'mvn install'. This will create an executable JAR file, newrelic-wh2trap-server-0.0.1-SNAPSHOT.jar, in the target/ subdirectory. 
5. Copy the JAR file and the config/ directory to the location where you want to run the extension. They JAR and the config/ directory should be at the same level.
   
## Edit the configuration and run the executable JAR. 

1. Edit the config/Application.json to configure the Agent to connect to an SNMP Manager.
  - Modify the 'listenerPort' setting to change the port the Webhook listens on. Default is 4567.
  - Modify the agent stanza to change the SNMP Trap host, port, and community_string for the SNMP Trap destination. 
  - The Agent name is arbitrary.
  - You can specify multiple Agents for multiple Trap receivers. 
2. Edit the log4j.properties file to change the logging level or destination. Used logging levels are INFO (default), DEBUG. 
3. The Agent is an executable JAR file. Run with the command: 'java -jar newrelic-wh2trap-server-0.0.1-SNAPSHOT.jar'. Be sure you are pointing to a Java 1.8 or higher JRE/JDK. 

  
