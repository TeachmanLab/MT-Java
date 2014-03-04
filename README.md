
A Server for hosting PIPlayer Trials.  Provides:
* User Authentication
* Participant Assignments
* Data Collection
* Data Reporting

Built in Java on the Spring framework.

Getting Started
===============
This project includes the Project Implicit Player as a Git SubModule.  so you will need to initialize that submodule with the following
commands:
$ git submodule init
$ git submodule update

Now you need to compile and build the project and start it up
$ mvn package 
$ java -jar target/pi-server-0.1.0.jar

You can now visit the website at : http://localhost:8080/index.html
