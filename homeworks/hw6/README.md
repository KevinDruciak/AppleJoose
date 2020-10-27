# MyBooksApp

HW6 submission

In this homework the MyBooksApp was adapted to be hosted out of a heroku server as opposed to a simple localhost as well as made so that auto-deployment and updating of the app with Github action was completed with each push to the master branch.

Some specific changes to the code were also needed in order to adapt the code from SQLite to PostgreSQL which is what works with non-local databases.

A few challenges that we had were with certain application errors when running the heroku app, however those were mostly related to how we were setting up the database and were eventually resolved.