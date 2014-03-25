
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
* Gradle
* Node (Server side Javascript - for building the PIPlayer, see http://howtonode.org)
* npm (Javascript Package Manager)

Running
--------
You can start up the webserver in development mode (meaning hot swappable / auto reloading) with:
$ gradle clean bootrun

You can now visit the website at : http://localhost:9000

Deploying
--------
You can generate a WAR file suitable for deployment in a web server with 
$ gradle war

Testing
--------
$ gradle test

Test results can be found in  ...PIServer/build/reports/tests/index.html



