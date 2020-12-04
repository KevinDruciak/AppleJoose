package model;


import java.util.Objects;

/**
 * Represents an article.
 */
public class Article {
    private int articleID;
    private final String url;
    private final String title;
    private final String newsSource;
    private final int biasRating;
    private final String topic;
    private final int numWords;

    /**
     * Constructor for Article.
     * @param url the article's URL
     * @param title the article's title
     * @param newsSource the news source of the article
     * @param biasRating the bias rating of the article
     * @param topic the topic of the article
     * @param numWords the number of words in the article
     */
    public Article(String url, String title, String newsSource,
                   int biasRating, String topic,
                   int numWords) {

        this.url = url;
        this.title = title;
        this.newsSource = newsSource;
        this.biasRating = biasRating;
        this.topic = topic;
        this.numWords = numWords;
    }

    /**
     * Determines if Article is equal to another.
     * @param o the other Article
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return url.equals(article.url);
    }

    /**
     * Presents Article in String form.
     * @return the String representation
     */
    @Override
    public String toString() {
        return "Article{" +
                "articleID= " + articleID +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", newSource='" + newsSource + '\'' +
                ", biasRating='" + biasRating +
                ", topic=" + topic + '\'' +
                ", numWords='" + numWords +
                '}';
    }

    /**
     * Hashes the Article.
     * @return the hashcode of the hashed Article
     */
    @Override
    public int hashCode() { return Objects.hash(url, title, numWords); }

    /**
     * Sets articleID of the Article.
     * @param id the value to set the articleID as
     */
    public void setID(int id) { this.articleID = id; }

    /**
     * Gets articleID of the Article.
     * @return the articleID
     */
    public int getArticleID() { return this.articleID; }

    /**
     * Gets url of the Article.
     * @return the url
     */
    public String getUrl() { return this.url; }

    /**
     * Gets title of the Article.
     * @return the title
     */
    public String getTitle() { return this.title; }

    /**
     * Gets newsSource of the Article.
     * @return the newsSource
     */
    public String getNewsSource() { return this.newsSource; }

    /**
     * Gets biasRating of the Article.
     * @return the biasRating
     */
    public int getBiasRating() { return this.biasRating; }

    /**
     * Gets topic of the Article.
     * @return the topic
     */
    public String getTopic() { return this.topic; }

    /**
     * Gets numWords of the Article.
     * @return the number of words in the Article
     */
    public int getNumWords() { return this.numWords; }
}
