# newrelic-wh2trap-server
New Relic Alert to SNMP Trap Plugin

This extension provides a Webhook Alert notification endpoint that converts that Alert into an SNMP Trap V2 format. This is hosted on-premise to allow direct integration to an SNMP Manager sitting behind a firewall. 

## Download the plugin

[Download link for extension](https://github.com/ddarwin/newrelic-wh2trap-server.git)

## Install the prerequisites

This is a Maven package that can be downloaded and built using "mvn install". 
This extension requires a Java 1.8 JRE because it uses Lambda expressions. 

## Install the plugin

1. You can download the NRwh2Trap.jar file from the repository.
   
## Edit the configuration and run the executable JAR. 

1. Edit the config/Application.json to configure the Agent to connect to an SNMP Manager.
  - Modify the 'listenerPort' setting to change the port the Webhook listens on. Default is 4567.
  - Modify the agent stanza to change the SNMP Trap host, port, and community_string for the SNMP Trap destination. 
  - The Agent name is arbitrary.
  - You can specify multiple Agents for multiple Trap receivers. 
2. Edit the log4j.properties file to change the logging level or destination. Used logging levels are INFO (default), DEBUG. 
3. The Agent is an executable JAR file. Run with the command: 'java -jar NRwh2Trap.jar'. Be sure you are pointing to a Java 1.8 or higher JRE/JDK. 

## Test access to the WebHook Listener from the Web

The New Relic WebHook listener must be accessible from the Internet to receive Alerts from New Relic. You can test access using a browser:

- HelloWorld test: from a browser enter the URL, http://\<webhook-listener-host\>:\<webhook-listener-port\>/hello/world
      This should return the message "Hello World"

- Test Trap: from a browser enter the URL, http://\<webhook-listener-host\>:\<webhook-listener-port\>/testTrap
      This should return the message "Test Trap Generated"
   
If these tests fail then either the webhook listener process is not reachable or the NRwh2Trap process is not running, or both. To resolve: 
   - Ensure the process is started by running the process, java -jar NRwh2Trap.jar.
   - Make sure your WebHook listener host:port are public and can be reached from the New Relic Cloud Service. 
   
## Create a New Relic notification channel 

Create a [New Relic WebHook Alert Notification Channel](https://docs.newrelic.com/docs/alerts-applied-intelligence/new-relic-alerts/alert-notifications/notification-channels-control-where-send-alerts) to receive Alert notifications to the NRwh2Trap service. The "base Url" value for the WebHook channel should be of the form,

> http://\<webhook-listener-host\>:\<webhook-listener-port\>/webhook.

Below is a screenshot of an example configuration: 

![New Relic WebHook Notification Channel definition](assets/images/webhook_notification_channel.jpg)







  
