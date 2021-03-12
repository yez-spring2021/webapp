#!/bin/bash
sudo systemctl stop tomcat.service

sudo chown tomcat:tomcat /opt/tomcat/latest/webapps/*.war
## cleanup log files
#sudo rm -rf /opt/tomcat/logs/catalina*
#sudo rm -rf /opt/tomcat/logs/*.log
#sudo rm -rf /opt/tomcat/logs/*.txt
sudo systemctl start tomcat.service