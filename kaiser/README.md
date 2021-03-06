
# Setup for Kaiser Development
This documentation assumes you have followed the directions in the Main readme file. 

## Database setup
You will need to create new databases for running and testing Kiaser.  From a Mysql
prompt or console with full administrative access, run the following commands.  This assumes
the user "pi_user" already exists.  If you want to connect with different credentials, then
please modify these as appropriate:

```mysql
CREATE DATABASE kaiser CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE kaiser_test CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON kaiser.* TO 'pi_user'@'%' IDENTIFIED BY 'pi_password' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON kaiser_test.* TO 'pi_user'@'%' IDENTIFIED BY 'pi_password' WITH GRANT OPTION;
```

## Configuration
There is an example properties file located in 
MindTrails/kaiser/src/main/resources/application.properties.example

Copy this file and create an application.properties file right next to it.  
To keep us from ever committing application.properties files these files are ignored by
default.


## Adding a run Configuration for Kaiser
I am hopeful that Intellij will detect that you have a new Spring configuration when you load up this project for the first time.  
If not, first try right clicking on the Application file within the 
Kaiser subdirectory and clicking "Run", this should create a new
temporary run configuration for Kaiser that you can then rename to
"Kaiser" and save for repeated user.


## Running Kaiser 
Using the run configuration described above, you will want to start up
the server, and then go to:
http://localhost:8080/account/create

Kaiser does not have an eligibility questionnaire, so this is now
disabled and you can go directly to the create an account page.

Once you create an account, connect directly to the database and make that
account an administrator (set admin=true)
```mysql
update participant set admin=true where id=1;
```

## Testing
All the tests in Kaiser pass, They are copied over from R01 and might make 
verifying that your new condition assignments are working as expected, but
feel free to delete anything that doesn't need to be in there.


## Todo
Please see Anna's marvelous spreadsheet for items that need to be addressed.


