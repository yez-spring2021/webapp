# webapp
##Student Info
```
Name: Zhenyu Ye
Course: CSYE6225, Spring 2020
Email: ye.z@northeastern.edu
```
## Technology Stack
```
    Java 11
    SpringBoot & Hibernate ORM Framework
    Maven.
    MySQL 8.0
```
## Prerequisite
### Installation
java version 1.8 or later

MySQL 8.0 or later

Maven Dependency 3.6.3
### Environment Variable
Set up environment variables on the machine (Ubuntu version):

Temporary setup (**REQUIRED**):
```shell
export JAVA_HOME=/path/to/your/java/bin
export MYSQL_USERNAME={your mysql username}
export MYSQL_PASSWORD={your mysql password}
```
To set it permanently for all future bash sessions add such line to your `.bashrc` or `.zshrc` file in your `$HOME` directory.

To set it permanently, and system wide (all users, all processes) add set variable in `/etc/environment`:

```
sudo vim /etc/environment
```
This file only accepts variable assignments like:

```VARNAME="my value"```

**Do not use the export keyword here.**

*You need to logout from current user and login again so environment variables changes take place.*
#### Additional:
If you are using Intellij to run the project, you can set the environment variables: 

`Edit Configurations`-> `Configuration` tab -> Add the environment variables above in `Environment variables`

## Run Instruction
### Option 1: Run with IDE
1. Open the project with IDE. Intellij IDEA is recommended.
2. Run the program in IDE.

### Option 2: run with maven script
1. Go to the project directory
2. Open Terminal on current directory
3. Run `./mvnw spring-boot:run`

### Option 3: run with maven
1. Go to the project directory
2. Open Terminal on current directory
3. run `mvn clean install`
4. find jar file in targer folder. It would be `webapp-0.0.1-SNAPSHOT.jar`
5. run `java -jar <jar-file-name>`

## Hit API with Postman
The webapp would use port `8080`.

Hit the following endponts:
* /v1/user
* /v1/user/self
* /v1/user/self



