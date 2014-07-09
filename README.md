
About 
=========

A Server for hosting PIPlayer Trials.  Please see (https://github.com/ProjectImplicit/PIPlayer)
This server provides:

* User Authentication
* Participant Assignments
* Data Collection
* Data Reporting
* Session management

It is built in Java using the Spring Framework.

Getting Started
===============

Requirements
---------------
You must have the following applications installed in order to build and run the server.
* Java 7 JDK - (http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
* Node (Server side Javascript - for building the PIPlayer, see http://howtonode.org)
* Bower (Javascript package management tool - just run "npm install bower -global")
* Mysql (Relational Database - http://dev.mysql.com/doc/refman/5.1/en/installing.html)

Database Setup
---------------
Install MySQL, and execute the following commands to establish
a user account.  You can use a different password if you change
the datasource.password setting in src/main/resources/application.properties

> CREATE database pi;
> CREATE USER 'pi_user'@'localhost' IDENTIFIED BY 'pi_password';
> GRANT ALL PRIVILEGES ON pi.* TO 'pi_user'@'localhost' IDENTIFIED BY 'pi_password' WITH GRANT OPTION;

Installing Javascript Dependencies
-------------------
Javascript dependencies, including the PIPlayer are installed using Bower, just run `bower install`


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



Adding a Questionnaire
======================

To Create a new Questionnaire you will to do 4 things:

1. Create an HTML web form to ask your questions.
2. Create a Java Model that represents the data from the form. (I promise this is simple)
3. A repository for storing your form data (Extremely simple)
4. Add details to the Questionnaire controller to allow you to correctly handle the form. 

The questionnaire must have a unique name from all other questionnaires.  It should not contain
spaces, or special characters, thought in a pinch it could use an underscore "_".  A good convention
is camelCase, where you upper case individual terms in your unique name, such as "UniqueName"

You will see references to "[UNIQUE_NAME]" in the steps below.  Please replace this with the name
of the form you are creating.  You may also see [UNIQUE_NAME_uc], at which point you should upper case the first letter.  For example IF [UNIQUE_NAME]  is "demographic" then [UNIQUE_NAME_uc] would be "Demographic"

Step 1:
-------
Create the html form.  New forms should be placed in 

/src/main/resources/templates/questions/[UNIQUE_NAME].html

It's a good idea to start with an existing form you like, and modify it.  However, there is nothing to prevent you from creating the page you want from scratch.  "Credibility" offers a good example of a simple one page form.  "Demographics" shows a multi-page form.  "DASS21" is a multi-page form with validation. 

Be sure to give the HTML <Form> tag a unique action.  
This will be used over again to wire your new questionnaire into the system, so make it unique and descriptive.  Making this the same as the file name of the form you are creating is recommended.
```
<form id="wizard" th:action="@{/questions/[UNIQUE_NAME]}" method="POST">
```
From here, you just create your HTML form elements.  Give thoughtful names to these elements, you will be using them again in the next step.

You can see your form as you develop it.  Just execute:
```prompt
gradlew bootrun
```
and visit http:\\localhost:9000/questions/[UNIQUE_NAME]

Any changes you make will be automatically visible by refreshing the page.  You don't need to stop and start the server to see your changes.

Step 2:
---------

Create a Java class for containing your form.  This should be located at:

/src/main/java/edu/virginia/psyc/pi/persistence/Questionnaire/[UNIQUE_NAME_uc].java

This file defines how your data will be stored in the database.  While this looks an awful lot like programming, it is a very boilerplate format, that can be quickly implemented over and over again.

It should look roughly like this:

```java
package edu.virginia.psyc.pi.persistence.Questionnaire;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 */
 @Entity
 @Table(name="Demographic")
 public class Demographic implements QuestionnaireData {
	
   @Id
   @GeneratedValue
   private int id;
				  
   @ManyToOne
   private ParticipantDAO participantDAO;
   private Date date;
							  
   // 1. Define your form fields here using appropriate types.
   //    These should match exactly the "name" attribute on the
   //    the form elements created in Step 1.
   // -----------------------------------------------------
   private String gender;
   private Date   birthdate;
   private String race;

   ...
   
   // 2. Create getters and setters for each field.
   // ------------------------------------------
   public void getGender() { return Gender; }
   public void setGender(String gender) { this.gender = gender; }

```

Step 3:
---------
Define a Java Repository - this file will be located here:
```
/src/main/java/edu/virginia/psyc/pi/persistence/Questionnaire/[UNIQUE_NAME_uc]Repository.java
```
The Repository is VERY simple, and consists completely of only the content shown below.  It exists to give us a critical hook into the database if we need to access this data later in a unique way.

```java
package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * User: dan
 * Date: 3/19/14
 * Time: 4:42 PM
 */
 public interface [UNIQUE_NAME_uc]Repository extends JpaRepository<[UNIQUE_NAME_uc], Long> {
     List<[UNIQUE_NAME_uc]> findByParticipantDAO(ParticipantDAO p);				 
 }
				 
```

Step 4:
---------
Wire up the Controller

There is a Questionnaire controller located at:
```
/src/main/java/edu/virginia/psyc/pi/mvc/QuestionController.java
```
You aren't creating a new file this time, just adding a new method to an existing file.

You need to define an additional method on this controller, that will take the data from the form
covert it to our model in step 2, then use the repository in step 3 to store it in the Database. While this sounds complicated, the code is shown in full below.  Simply replace the [UPPER_CASE] terms with the correct values, and you have completed adding the form:

```java
    @RequestMapping(value="[UNIQUE_NAME]", method = RequestMethod.POST)
	String handleCredibility(@ModelAttribute("[UNIQUE_NAME_lc]") [UNIQUE_NAME_uc] [UNIQUE_NAME],
		                                                    BindingResult result) {
        prepareQuestionnaireData([UNIQUE_NAME]);
		[UNIQUE_NAME_lc]Repository.save([UNIQUE_NAME]);
	    return "/home";
	}
```																 

That is it.  When participants fill out your new form, it will be stored in a new table named [UNIQUE_NAME] in the database.  From here we can create various reports to present this data which will be covered shortly.

