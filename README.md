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

Iteration 2 Notes:
* For this iteration we attempted to finalize our front-end and link it to our back-end database, however we were not able to accomplish that this iteration. We have been able to get a dumby front-end up which displays the type of data we want and also have implmemented the political bias API into our server-side in order to process incoming user articles, but we were not able to full connect our database to the front end HTML code in order to display that data. Also missing from the app is the ability to automatically gather and parse through user history in order to populate the database in a non-manual fashion. Next steps for us over the next few days we will catch up with the linkage of the client and server sides of our app so that database population and additional features are the only things left for us to do over the next few iterations.

Work Done:

iter1:
Kevin/Simon/Connor/Joao 
 * Object Classes, Dao Interface and Sql2o Classes
 * Main.java and Server.java
 * initial extension implementation
 * DBPersistanceTest.java and APITests.java
 
 iter2:
 Joao
 * Political API integration into server-side of application and basic
 * Updated Article, Statistics and User classes to cater to API functionality
 * Added helper methods to server to help with political API integration
 Montese
 * HTML and CSS for client-side of application with stand-in data
 
