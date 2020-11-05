package model;


import java.util.Objects;

public class Article {
    private int articleID;
    private final String url;
    private final String title;
    private final String newsSource;
    private final int biasRating;
    private final String topic;
    private  double timeOnArticle;
    private final int numWords;
    private  int timesVisited;

    public Article(String url, String title, String newsSource,
                   int biasRating, String topic, double timeOnArticle,
                   int numWords, int timesVisited) {

        this.url = url;
        this.title = title;
        this.newsSource = newsSource;
        this.biasRating = biasRating;
        this.topic = topic;
        this.timeOnArticle = 0;
        this.numWords = numWords;
        this.timesVisited = 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return url == article.url;
    }

    @Override
    public String toString() {
        return "Article{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", biasRating='" + biasRating +
                ", topic=" + topic + '\'' +
                ", timeOnArticle='" + timeOnArticle +
                ", numWords='" + numWords +
                ", timesVisited='" + timesVisited +
                '}';
    }

    @Override
    public int hashCode() { return Objects.hash(url, title, numWords); }

    public void setID(int id) { this.articleID = id; }

    public int getArticleID() { return this.articleID; }

    public String getUrl() { return this.url; }

    public String getTitle() { return this.title; }

    public String getNewsSource() { return this.newsSource; }

    public int getBiasRating() { return this.biasRating; }

    public String getTopic() { return this.topic; }

    public double getTimeOnArticle() { return this.timeOnArticle; }

    public int getNumWords() { return this.numWords; }

    public int getTimesVisited() { return this.timesVisited; }

    public void updateTimeOnArticle() {
        //TODO: get time on a webpage from extension API, and update objects value
    }

    public void updateTimesVisited() {
        //TODO: get numbers of url visits in user history from extension API,
        // and update objects value
    }
}
