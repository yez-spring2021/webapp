#!/bin/bash

# Stoping tomcat
sudo systemctl stop tomcat

#removing previous build ROOT folder
sudo rm -rf /opt/tomcat/latest/webapps/ROOT