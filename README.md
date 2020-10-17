# 2020-fall-group-apple-jOOSE
Group Repo for team Apple jOOSE

To run our project: 
1. Go to chrome://extensions/ and enable developer settings for extensions.
2. Once step 1 is complete, click 'Load Unpacked' button on the chrome://extensions/ page and then select the AppleJuice/src/main
folder as the extension source. (Cloning the repo or downloadign a copy is necessary for this to work)
3. Now the extension should show up on the chrome://extensions/ page as a viable option, with a button to enable it for use on your chrome browser, click it.
4. Finally, for the server side of the project, simply run the main method of the Main.java file under AppleJuice/src/main/java folder. Running this file should
create the database and establish the server connection to localhost:7000.
5. The app is now up and running and as features expand, this set up should work without having to change anything apart from running Main.java to establish the conenction with the server side and visiting chrome://extensions/ to make sure the extension has been updated to the most recent version. (Updating the extension simply consists of clicking the refresh button which will show up in the bottom of the extensions card)

Iteration 1 Notes:
* We definitely did not achieve all of our functional requirements as we are still lacking some
key API implementation and client side functionality. However, most of the server/database side of 
the project is complete and should be already have most of the functions we will need for the rest of 
this project already implemented.

Work Done:

Kevin/Simon/Connor/Joao 
 * Object Classes, Dao Interface and Sql2o Classes
 * Main.java and Server.java
 * initial extension implementation
 * DBPersistanceTest.java and APITests.java
 
