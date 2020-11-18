# 2020-fall-group-apple-jOOSE
Group Repo for team Apple jOOSE

To run our project: 
1. Go to chrome://extensions/ and enable developer settings for extensions.
2. Once step 1 is complete, click 'Load Unpacked' button on the chrome://extensions/ page and then select the AppleJuice/src/main
folder as the extension source. (Cloning the repo or downloadign a copy is necessary for this to work)
3. Now the extension should show up on the chrome://extensions/ page as a viable option, with a button to enable it for use on your chrome browser, click it.
4. Whenever you visit a website, the extension logs the URL to a webpage. Click on the extension icon in the top right hand corner to access this webpage.
5. Finally, for the server side of the project, simply run the main method of the Main.java file under AppleJuice/src/main/java folder. Running this file should
create the database and establish the server connection to localhost:7000.
6. The app is now up and running and as features expand, this set up should work without having to change anything apart from running Main.java to establish the conenction with the server side and visiting chrome://extensions/ to make sure the extension has been updated to the most recent version. (Updating the extension simply consists of clicking the refresh button which will show up in the bottom of the extensions card)

Iteration 1 Notes:
* We definitely did not achieve all of our functional requirements as we are still lacking some
key API implementation and client side functionality. However, most of the server/database side of 
the project is complete and should be already have most of the functions we will need for the rest of 
this project already implemented.

Iteration 2 Notes:
* For this iteration we attempted to finalize our front-end and link it to our back-end database, however we were not able to accomplish that this iteration. We have been able to get a dumby front-end up which displays the type of data we want and also have implmemented the political bias API into our server-side in order to process incoming user articles, but we were not able to full connect our database to the front end HTML code in order to display that data. Also missing from the app is the ability to automatically gather and parse through user history in order to populate the database in a non-manual fashion. Next steps for us over the next few days we will catch up with the linkage of the client and server sides of our app so that database population and additional features are the only things left for us to do over the next few iterations.

Iteration 2 UPDATE:
* Front-end now properly is capable of displaying a user's data. Because the statistics class is dependent on the API calls (which have yet to be implemented in future 
iterations), a test Statistics object was inserted using the constructor: Statistics(-3, "Moderate Liberal Bias", "New York Times", "Economy", "You have Moderate Liberal Bias. Your favorite news source is New York Times. Your favorite topic to read about is Economy", id). In reality, these fields would be 0/empty for a new user's statistics object 
(note that this is reflected in the actual constructor, which is commented out). This was done primarily to show an example of how it will appear when future implementations
are completed, and to show that it is properly functioning.
* To check this part, run the Main.java file and open http://localhost:7000/. Then sign in using any username (placeholder password for now) and the page with the user's stats
should show up.

Iteration 3 Notes:
* For this iteration we worked on improving the architecture of our Databases and classes. We implemented the UserReadings object in order to connect each Article to a specific user. In this way, we don't store the same article over and over if different users read it. Rather, we store one instance of the article, and store UserReading objects in a Database everytime someone decides to read that article. If an article is being read for the first time, we add an article to the Articles database as well as a UserReading to the UserReadings database.
* This architectural redesign allows us to implement many useful methods. Namely, we now have convenient methods for returning a list of the last n articles a user has read.
* However, there are still some issues with our redesigned architecture. Certain post and get calls to our API no longer work, due to changes in the parameters required to build certain objects. This will be addressed in our upcoming iteration.
* Due to changes in the architecture for our Databases and classes, some of the features we implemented for this iteration are not entirely functional. For example, our Google Chrome extension now interfaces with our Database and stores each URL you visit. However, due to the change in the way Articles/UserReadings are stored, the extension is not compatible with our current design. This is a small fix that will be completed in Iteration 4.
* Furthermore, we implemented a functional login page. A user can create an account with a username and password. This password is encrypted and stored in our database. When the user logs in, we check that the passwords match before allowing them to enter. However, due to a highly rare bug where insertions were always set to null, we didn't manage to merge this change onto our main branch.
* Our front-end now draws data from the database, rather than being manually populated.
* We implemented an add article button for manually adding in articles. Once an article gets added in (either manually or by /addArticle endpoint), the endpoint automatically extracts the article title and text, and calculates the bias score for that article. We are working on further improvements to increase the likelihood it selects the correct title for the article.

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
 
 Simon
 * Added basic user insertion to call user class methods (and later assign different users their own stats classes)
 * Added methods in Sql2oStatisticsDao to retrieve a user's relevant statistics
 * Changed HTML text files to proper VM files that can pull from user/stats tables; debugged and tested
 
 Connor
 * Working on adding functionality to chrome extension
 * Chrome Extension now logs all URLS a user visits
 * All visited URLs are displayed on a webpage, which can be accessed by clicking the extension icon in the top right hand corner.
 
iter3:

Montese
* Created add articles page, allow us to see the project in action in realtime
* Parsing article title, bias ranking and source now show up on the front page
* UI is way more fleshed out for the home page and closer to the final look of the project

Connor
* Made it so Chrome extension writes all visited URLs to article data base.
* Fixed APITests to reflect changes in article class and spl2oDao.
* Implemented sitesVisited update for articles.

Kevin/Simon
* Worked on secure login using BCrypt with Simon (discovered bug that makes this impossible??)
  * Unable to insert encrypted String into Users table (always NULL).
  * Does not work using hardcoded Strings either...
* Signup post/get to create account initially
* Login functionality (still in progress, will complete by iteration 4)

Joao
* Continued implementing DAO functions and building out database in order to support connection between front-end and back-end
