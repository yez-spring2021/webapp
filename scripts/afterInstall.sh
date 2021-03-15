#!/bin/bash
sudo systemctl stop tomcat.service

sudo chown tomcat:tomcat /opt/tomcat/latest/webapps/*.war
sudo systemctl start tomcat.service