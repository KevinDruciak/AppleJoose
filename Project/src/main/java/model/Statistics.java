package model;

import java.util.List;
import java.util.Objects;

public class Statistics {
    int rating;
    String favNewsSource;
    String favTopic;
    List<Articles> recentArticles;
    String execSummary;
    int ID;

    public Statistics(int rating, String favNewsSource, String favTopic, List<Articles> recentArticles, String execSummary) {
        this.rating = rating;
        this.favNewsSource = favNewsSource;
        this.favTopic = favTopic;
        this.recentArticles = recentArticles;
        this.execSummary = execSummary;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFavNewsSource() {
        return favNewsSource;
    }

    public void setFavNewsSource(String favNewsSource) {
        this.favNewsSource = favNewsSource;
    }

    public String getFavTopic() {
        return favTopic;
    }

    public void setFavTopic(String favTopic) {
        this.favTopic = favTopic;
    }

    public List<Articles> getRecentArticles() {
        return recentArticles;
    }

    public void setRecentArticles(List<Articles> recentArticles) {
        this.recentArticles = recentArticles;
    }

    public String getExecSummary() {
        return execSummary;
    }

    public void setExecSummary(String execSummary) {
        this.execSummary = execSummary;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "rating=" + rating +
                ", favNewsSource='" + favNewsSource + '\'' +
                ", favTopic='" + favTopic + '\'' +
                ", recentArticles=" + recentArticles +
                ", execSummary='" + execSummary + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistics that = (Statistics) o;
        return rating == that.rating &&
                Objects.equals(favNewsSource, that.favNewsSource) &&
                Objects.equals(favTopic, that.favTopic) &&
                Objects.equals(recentArticles, that.recentArticles) &&
                Objects.equals(execSummary, that.execSummary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, favNewsSource, favTopic, recentArticles, execSummary);
    }
}
