# Software Requirements Specifications

## Problem Statement
> Write a few sentences that describes the problem you are trying to solve. In other words, justify why this software project is needed.

>> Everyday people read, watch and listen to the news in order to receive reliable, up-to-date, and meaningful information about the world. Whether it's local news, national events, or reports from across the world, the news can unconsciously shape our very perception of the world. Especially in today's politically divisive climate, it is more important than ever for people to start understanding the relationship between their world views and where they get their news from. With biases changing from source to source, political spin taking hold of every major network, and ulterior motives driving heavily opinionated articles, it can be very hard to distinguish what is biased and what isn't. Therefore, people need a convenient way of tracking their news habits. People need to know where the majority of their news is coming from, how biased each article they read is, how long they spend reading these articles, the average bias of all the news they consume, and more. Our browser extension would log all the news articles a user reads, as well as metrics about said article (time spent, word count, political bias, etc), into a database. Our website would then process this data, and generate a comprehensive report on the user's entire news consumption. This would go a long way in incentivizing users to be more considerate about what news they are consuming, allowing users to identify their own biases, and giving insight into the relationship between a user's news consumption and their world views.


## Potential Clients
> Who are affected by this problem (and would benefit from the proposed solution)? I.e. the potential users of the software you are going to build.

>> Anyone who reads, watches or listens to the news are potential clients for this browser extension and could use it without hassle. The benefit they would receive would be getting concise feedback about their news habits, informing them about the biases they are being exposed to, and understanding the patterns in their news consumption.


## Proposed Solution
> Write a few sentences that describes how a software solution will solve the problem described above.

>> The solution would be a browser extension which tracks the readers webpage and article history. The extension would then store all article viewing history into a database, along with metrics about the user's interaction with said article (time spent reading, word count, political leaning, etc.). Then, users will be able to visit a webpage which would display an analysis of the user's news consumption habits. We would display the average political leaning of their articles, and whether they veer left, right, or center. Furthermore, we would identify their most frequented news sources (Fox, CNN, etc.). With their entire article history being tracked, users can get a comprehensive analysis of what exactly they are consuming, allowing them to gain a much better perspective on the implicit biases they may have and how their media consumption has affected them.

## Functional Requirements
> List the (functional) requirements that software needs to have in order to solve the problem stated above. It is useful to write the requirements in form of User Stories and group them into those that are essential (must have), and those which are non-essential (but nice to have).

### Must have
>> 1. As a news reader, I want to knowabout the political bias of each article I've read recently so that I can identify how skewed articles are on an individual basis.
>> 2. As a news reader, I want a report about the average bias of all articles I've read, so that I can get an overarching view on how my news sources may be influencing my own personal opinions on certain issues.
>> 3. As a news reader, I want to know which news sources I frequent most often, so that I can determine where the majority of my news is coming from.

### Nice to have
>> 1. As a news reader, I want some suggestions of news articles with different biases than my own so that I can get a better sense of all perspectives surrounding a given issue.
>> 2. As a news reader, I want a report about how I specifically interact with the news (spend short times on articles, only watch videos, only read about certain topics) so that I can use that information to improve my news reading habits.
>> 3. As a news reader, I want a yearly/long-term report about how I engage the news so that I can see how my habits change or remain the same over time.
>> 4. As a user, I would like to see how my reading habits stack up against those of my friends, so I can see the differences/similarities between our news habits and political leanings.
>> 5. As a news reader, I would like to know how long I'm spending on articles relative to their word count, so I can get an estimate how much of each article I am actually reading.
>> 6. As a news reader, I would like to know the complexity of the articles I'm reading, so I may know how strong my reading level is.
>> 7. As a news reader, I would like the articles I read to be fact-checked and to have a quick summary about the inaccuracies present in the article.
>> 8. As a user, I would like clear, interactive infographics on the various metrics that were collected, so that I can understand my own news reading tendencies at a glance.

## Software Architecture
> Will this be a Web/desktop/mobile (all, or some other kind of) application? Would it conform to the Client-Server software architecture?

>> This browser extension would follow a Client-Server software architecture. The client would access our website, and make an account. After making an account, they would download our browser extension, and log into their account there as well. Once completed, the extension would collect their article viewing history, as well as the various metrics we described above, and store this history into a relational database. In order to do this data collection, several exisitng chrome extension APIs would be used to create a comprehensive picture of the users histroy and interaction with their articles most prominently the chrome.history API. As for the political Bias analysis we will be using a political bias API developed by the team at theBipartisanPress.com, which we have recently been given access and permission to use. Their bias detection api return several useful metrics to us such as a -1 to 1 bias score, number of words in article, etc whcih we will utilize in our app. When the user clicks on our enxtension button they will access our website, which will access the stored data, and generate a visual report on their news consumption habits. This report would include recently visited news articles, the bias rating for each of those articles, which news sources you frequent most, your current bias rating based on your prolonged news history, and hopefully more. As far as server-side, we would like to keep it as lean as possible, only saving the minimal amount of data regarding user habits in our database tables. We would have a table where each row is an article, and each column is a metric about said article (political leaning, time spent, word count, etc.). We would have a table of users, which would store user information (username, password, etc.) as well as that user's corresponding article table. For each user, there will be a row in the statistics table, which holds the values of their overall news consumption habits (average political leaning of all viewed articles, average time spent on each article, average word count, and more).

## User Stories
> List major user stories here

>> A user has recently watched the Presidential debates and felt very passionately afterwards that his preferred candidate was a no-brainer choice for president. However, after meeting up with a friend they get into a heated argument as his friend was a a supporter of his opponent. Not understanding how his friend could possibly think that, our user opens our extension, only to realize that all the news he's been reading recently about the presidential campaign has been strongly favoring on side of the political spectrum. With this information, the user explored other news sources and came to realize that not only was his views being heavily skewed to one side by what he was reading but that there were some arguments to be made for the other candidate.
>>
>> A user has just come back from a fiery political debate during dinner with his family, him and his parents just couldn't see eye-to-eye in any of the political issues that were brought up. Confused by why this was he decided to show his parents a great browser extension which help demonstrate potential political biases in the news sources they were consuming online. After installing the extension, him and his parents sat down and read his and their reports side by side and saw that almost all of their new sources differed and were deeply situated on opposite sides of the political spectrum. Armed with this knowledge both of them vowed to read more moderate news sources and explore each others perspectives more intimately in order to gain some common ground and develop less divisive perspectives on political issues.

## Wireframes
>This should provide an overview of different views of the application's interface.

>![alt text](https://github.com/jhu-oose/2020-fall-group-apple-jOOSE/blob/master/docs/iteration1Wireframes.jpg)
