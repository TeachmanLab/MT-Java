
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
* Node (Server side Javascript - for building the PIPlayer, see http://howtonode.org)
* npm (Javascript Package Manager)
* mysql

Database Setup
---------------
Install MySQL, and execute the following commands to establish
a user account.  You can use a different password if you change
the datasource.password setting in src/main/resources/application.properties

> CREATE database pi;
> CREATE USER 'pi_user'@'localhost' IDENTIFIED BY 'pi_password';
> GRANT ALL PRIVILEGES ON pi.* TO 'pi_user'@'localhost' IDENTIFIED BY 'pi_password' WITH GRANT OPTION;

Running
--------
You can start up the webserver in development mode (meaning hot swappable / auto reloading) with:
$ ./gradlew clean bootrun
(on windows this is ./gradlew.bat clean bootrun)

You can now visit the website at : http://localhost:9000

Deploying
--------
You can generate a WAR file suitable for deployment in a web server with 
$ ./gradlew war

Testing
--------
$ ./gradlew test

Test results can be found in  ...PIServer/build/reports/tests/index.html

Security Overview
==================

Our Security Model is build on the popular Spring Security Framework.  Specifically version
3.2.3   We currently use a form based authentication (a web login form) that provides the following
basic projections and features:

* Every URL in the site requires authentication.
* CSRF attach prevention (http://en.wikipedia.org/wiki/Cross-site_request_forgery)
* Session Fixation Protection (http://en.wikipedia.org/wiki/Session_fixation)
* Security header integration
    * HTTP Strict Transport Security for secure requests
    * X-Content-Type-Options integration
    * Cache Control (can be overridden later by your application to allow caching of your static resources)
    * X-XSS-Protection integration
    * X-Frame-Options integration to help prevent Clickjacking

In production, we will secure the site using SSL encryption; all page requests occur over an HTTPS connection.



