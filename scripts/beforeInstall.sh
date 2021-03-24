#!/bin/bash

# Stopping tomcat
sudo systemctl stop tomcat

#removing previous build ROOT folder
sudo rm -rf /opt/tomcat/latest/webapps/ROOT
sudo rm /opt/cloudwatch-config.json
ls /opt