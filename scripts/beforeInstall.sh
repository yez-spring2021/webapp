#!/bin/bash

# Stoping tomcat
sudo systemctl stop tomcat.service

#removing previous build ROOT folder
sudo rm -rf /opt/tomcat/webapps/webapp-*.war