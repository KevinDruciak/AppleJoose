# Software Requirements Specificatins

## Problem Statement 
> Write a few sentences that describes the problem you are trying to solve. In other words, justify why this software project is needed.

>> Everyday people read, watch and listen to the news in order to recieve reliable, up-to-date, and meaningful information about the world. Whether it's local news, national events, or reports from across the world, the news can be not only very inundated but daunting. With biases changing from source to source, political spin taking hold of every major network, and alterior motives driving heavily opinionated articles, it can be very hard to distinguish what is biased and what isn't. These inherent biases can cause slight misunderstandings at its best to severe disinformation at its worst. A simple browser extension which allows users to receive feedback on the biases and tendencies of their news sources can go a long way in informing them how their news habits maybe be influencing their perspectives, and may incentive them to be more considerate about what news they are consuming.


## Potential Clients
> Who are affected by this problem (and would benefit from the proposed solution)? I.e. the potential users of the software you are going to build.

>> Anyone who reads, watches or listens to the news are potential clients for this browser extension and could use it without hassle or letting it get in your way. The benefit they would receive would be getting concise feedback about their news habits and informing them about the biases they are being exposed to.


## Proposed Solution
> Write a few sentences that describes how a software solution will solve the problem described above.

>> The solution would be a browser extension which tracks the readers webpage and article history and provides feedback on the biases of those news sources in a visual and concise manner.

## Functional Requirements
> List the (functional) requirements that software needs to have in order to solve the problem stated above. It is useful to write the requirements in form of User Stories and group them into those that are essential (must have), and those which are non-essential (but nice to have).

### Must have
>> 1. As a news reader, I want a short summary about my recent news history so that I can see any patterns about my news habits.
>> 2. As a news reader, I want a concise report about the biases of my news sources so that I can understand how my news sources may be influencing my own personal views on certain issues.
>> 3. As a new reader, I want to know how extreme my personal news habits have been so that I can explore different sources to get a more holistic perspective on any given issue.

### Nice to have
>> 1. As a news reader, I want some suggestions of news articles with alternate biases so that I can get a better sense of all perspectives surrounding a given issue.
>> 2. As a news reader, I want a report about how I specifically interact with the news (spend short times on articles, only watch videos, only read about certain topics) so that I can use that information to improve my news reading habits.
>> 3. As a new reader, I want a yearly/long-term report about how I engage the news so that I can see how my opinions change or remain the same over time.


## Software Architecture
> Will this be a Web/desktop/mobile (all, or some other kind of) application? Would it conform to the Client-Server software architecture? 

>> This browser extension would follow a Client-Server software architecture. On the client side, users would receive short reports about their current news bias based on a political bias API (which we have requested access to and are awating a response) when they click on the extension button or visit our webpage and log-in. This reports would include mostly recently visited news sources, the bias rating for each of those sources,and your current bias rating based on your prolonged news history. As far as server-side, we would like to keep it as lean as possible, only saving the minimal amount of data regarding user habits in our frequently used tables, while having deeper tables which collect more user history and information with the hope of using that to generate periodic, in-depth bias reports that our users can view. Frequently used tables would be user accounts, their current bias score, their most recent history and the bias ratings for those most recent articles.

## User Stories
> List major user stories here

>> A user has recently watched the Presidential debates and felt very passionately afterwards that his preferred candidate was a no-brainer choice for president. However, after meeting up with a friend they gt into a heated agruement as his friend was a a supporter of his opponent. Not understanding how his friend could possibly think that, our user opens our extension, only to realize that all the news he's been reading recently about the presidential campaign has been strongly favoring on side of the political spectrum. With this information, the user explored other news sources and came to realize that not only was his views being heavily skewed to one side by what he was reading but that there were some arguments to be made for the other candidate.
>> A user has just come back from a fiery political debate during dinner with his family, him and his parents just couldn't see eye-to-eye in any of the polticial issues that were brought up. Confused by why this was he decided to show his parents a great browser extension which help demonstrate ptential political biases in the news sources they were consuming online. After installing the extension, him and his parents sat down and read his and their reports side by side and saw that almost all of their new sources differed and were deeply situated on opposite sides of the political spectrum. Armed with this knowledge both of them vowed to read more moderate news sources and explore each others perspectives more intimately in order to gain some common ground and develop less divisive perspectives on political issues. 

## Wireframes
>This should provide an overview of different views of the application's interface.

>
