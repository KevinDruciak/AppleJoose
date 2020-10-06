package model;

import java.util.Objects;

public class Articles {
    String url;
    String title;
    int rating;
    double timeSpentOnArticle;
    int numWords;
    int timesVisited;
    String topic;
    private int articleID;

    public Articles(int articleID, String url, String title, int rating, double timeSpentOnArticle, int numWords, int timesVisited, String topic) {
        this.url = url;
        this.title = title;
        this.rating = rating;
        this.timeSpentOnArticle = timeSpentOnArticle;
        this.numWords = numWords;
        this.timesVisited = timesVisited;
        this.topic = topic;
    }

    public int getID() { return articleID; }

    public void setID(int articleID) { this.articleID = articleID; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getTimeSpentOnArticle() {
        return timeSpentOnArticle;
    }

    public void setTimeSpentOnArticle(double timeSpentOnArticle) {
        this.timeSpentOnArticle = timeSpentOnArticle;
    }

    public int getNumWords() {
        return numWords;
    }

    public void setNumWords(int numWords) {
        this.numWords = numWords;
    }

    public int getTimesVisited() {
        return timesVisited;
    }

    public void setTimesVisited(int timesVisited) {
        this.timesVisited = timesVisited;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Articles{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", rating=" + rating +
                ", timeSpentOnArticle=" + timeSpentOnArticle +
                ", numWords=" + numWords +
                ", timesVisited=" + timesVisited +
                ", topic='" + topic + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Articles articles = (Articles) o;
        return rating == articles.rating &&
                Double.compare(articles.timeSpentOnArticle, timeSpentOnArticle) == 0 &&
                numWords == articles.numWords &&
                timesVisited == articles.timesVisited &&
                Objects.equals(url, articles.url) &&
                Objects.equals(title, articles.title) &&
                Objects.equals(topic, articles.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title, rating, timeSpentOnArticle, numWords, timesVisited, topic);
    }
}
