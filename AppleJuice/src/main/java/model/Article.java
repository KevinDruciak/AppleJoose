package model;


import java.util.Objects;

public class Article {
    private int articleID;
    private final String url;
    private final String title;
    private final String newsSource;
    private final int biasRating;
    private final String topic;
    private final int numWords;

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
                "articleID= " + articleID +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", newSource='" + newsSource + '\'' +
                ", biasRating='" + biasRating +
                ", topic=" + topic + '\'' +
                ", numWords='" + numWords +
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

    public int getNumWords() { return this.numWords; }

}
