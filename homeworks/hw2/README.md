This is the folder for hw2 OOSE team apple-jOOSE

For this assignment the work done was primarily in modifying the AuthorDao, Sql2oAuthorDao as well as creating BookDao and Sql2oBookDao. Within each 4 classes where implemented (apart from listAll and add in Sql2oAuthorDao which were already implemented) which consisted of add, listAll, delete and update methods, all of which had some specific behaviors added to them. 

Some assumptions that where made and implemmented were first that the DB tables would be created in such a that if the Author were deleted all his books in the data base would also be deleted, meaning ON DELETE CASCADE and ON UPDATE CASCADE had to be used when creating the DB. Also, another assumption we made is that if the author was not already in the DB the book could not be added to it. Finally, when updating book, we assumed that isbn's don't change however if a new edition of a book comes out it would have a totally new isbn, hence our DB would treat it as an entirely new books and does not account for this scenario.

