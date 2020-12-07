# 2020-fall-group-apple-jOOSE
Group Repo for team Apple jOOSE

To run our project:
REMOTELY
1. Acess this URL: apple-joose.herokuapp.com
2. If URL doesn't work, in Server.java set the static boolean LOCAL variable to false. Then, run Gradle build tasks clean, jar, deploy Heroku. In other words, this is how you manually deploy our application, however, it should be running to begin with.
3. If you'd like the Chrome Extension work with the remotely deployed application, you must change the URL we are sending post requests to, from "http://localhost:7000/chromeaddarticle" to "http://apple-joose.herokuapp.com/chromeaddarticle"
4. Furthermore, you must change the username parameter on line 7 to the username for your account.

LOCALLY
1. In Server.java set the static boolean LOCAL variable to true. Then run Server.java.
2. If you would like to use the Chrome Extension, simply go to background.js and enter the username for your account on line 7 in place of Connor. We will change the extension so it automatically grabs your username in the future. For now, you must enter it manually.
3. Go to chrome://extensions/ and enable developer settings for extensions.
4. Click 'Load Unpacked' button on the chrome://extensions/ page and then select the AppleJuice/src/main
folder as the extension source. (Cloning the repo or downloadign a copy is necessary for this to work)
5. Now the extension should show up on the chrome://extensions/ page as a viable option, with a button to enable it for use on your chrome browser, click it.
6. Whenever you visit a website, the extension logs the URL to a webpage.
7. You should now be able to access the website through localhost:7000.

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

Iteration 4 Notes:
* For this iteration, we managed to deploy our application to Heroku. As a result, our website is now remotely hosted.
* Additioanlly, we spent a lot of work merging our login feature into our product. We have a signup get and post endpoint to create a user account. We ensure that the username is unique and that the password matches the confirm password box. On success, it will add the username to the users table, as well as create a corresponding userStatistics object for the new user. In addition, the password gets stored in the database as an encrypted piece of text, increasing our application's security.
* Furthermore, we began implementing additional API endpoints to accomodate the Chrome Extension. Specifically, we needed a way for an add article post call to pass in the username of the person posting, that way their article can be linked back to the user that read it.
* Additionally, we ditched SQLLite and moved to PostgreSQL. This transition took some time as we had to reconfigure all database transactions to work with PostgreSQL.
* We also changed the view models. For the login page and sign-in page, we made the style of the user interface more consistent with our home page design. As a result, our view models are now more refined and polished.
* Notes for next iteration: we still are ironing out our underlying database operations. For some reason, we are still getting errors with our endpoints. This is due to constantly changing environments and class architectures. Every time we update our classes, it breaks our endpoints. Furthermore, when we moved to Heroku it broke our endpoints. Thus, much of our work has been devoted purely to getting our application into a stable viewable state. Once we have reached a stable version of our application, we can truly focus on implementing actual features and ways for the user to explore their own data. Hopefully, we can make this big push on our final iteration.

Iteration 5 Notes:
* For this iteration, we added the ability to view how the bias rating of all users on the site has changed over time. We plan on implementing a seecond line on this graph to display how your individual rating has changed over time as well. We also added a bar graph to track the most frequent topics for articles read by all users, as well as most frequent news sources for all users. We also plan on adding the individual user's topics and news sources to the bar graph, so that they can compare their reading habits to the rest of the users on the site.
* The Chrome Extension now fully writes all websites you visit to the database as articles. However, we are still working on getting it to retrieve the user's username automatically. As of now, it simply writes whatever website you visit to the article database under a specific user. This will be addressed prior to our final presentation.
* We had to create a few more VMs in order to display the aforementioned graphs. Additionally, we created additional endpoints to our API to load these pages with the statistics.
* We also managed to get our locally hosted applicatin to run, as per Debanik's request. So, hopefully grading will be easier. Please look at the top of the README for instructions on how to run our application locally.
* We also added cosmetic changes to the manual add articles page, as well as improving the design of our login page, 5 most recent articles, and more.

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

iter4:
Connor
* Updated API endpoints so Chrome Extension can make addarticle calls.
* Began integrating the ability to login through the Chrome Extension.

Montese
* Updated view model HTML.

Kevin/Simon
* Integrated log-in feature

Joao:
* Got Heroku hosting to work with our application.

iter5:
Connor
* Implemented Chrome Extension to add any website you visit to the articles database.
* Helped with CSS formatting issues.
* Fixed Server.java API endpoint for adding Chrome Article.

Simon
* Added statistics page which shows average bias rating over time for all users on the site.
* Helped with CSS formatting issues.
* Fixed various user/password issues, which broke the displaying of statistics and whatnot.
* Modified application so that it can run locally.

Kevin
* Added statistics to display the favorite news sources and topics of all users on the site.

Montese
* Additions and modifications to UI
* Made formatting for article cards more aesthetically pleasing.

Joao
* In process of fixing a bug in which the same article can not be added for different users.
