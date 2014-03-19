
A Server for hosting PIPlayer Trials.  Provides:
* User Authentication
* Participant Assignments
* Data Collection
* Data Reporting

Built in Java on the Spring framework.

Getting Started
===============
This project depends on the Project Implicit Player.  You will need to initialize that code base inside this one.  Check the code out into PIPlayer directory as so:
$ git clone git@github.com:ProjectImplicit/PIPlayer.git

Requirements
---------------
You must have the following applications installed in order to build and run the server.
* Java 7
* Maven 3 (Java Package Manager / Build Tool)
* Node (Server side Javascript - for building the PIPlayer, see http://howtonode.org)
* npm (Javascript Package Manager)


Now you need to compile and build the project and start it up
$ mvn package 
$ java -jar target/pi-server-0.1.0.jar

You can now visit the website at : http://localhost:8080/index.html

