#!/bin/bash
sudo systemctl stop tomcat

sudo chown tomcat:tomcat /opt/tomcat/latest/webapps/*.war

# cleanup log files
sudo rm -rf /opt/tomcat/logs/catalina*
sudo rm -rf /opt/tomcat/logs/*.log
sudo rm -rf /opt/tomcat/logs/*.txt