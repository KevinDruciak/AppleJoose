package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Statistics {
    private int id;
    private int biasRating;
    private String biasName;
    private String favNewsSource;
    private String favTopic;
    private List<Article> recentArticles = new ArrayList<>();
    private String execSummary;
    private final int userID;

    public Statistics(int userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistics stats = (Statistics) o;
        return userID == stats.userID;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "biasRating='" + biasRating +
                ", biasName='" + biasName + '\'' +
                ", favNewsSource='" + favNewsSource + '\'' +
                ", favTopic=" + favTopic + '\'' +
                ", recentArticles=" + recentArticles.toString() + '\'' +
                ", execSummary=" + execSummary + '\'' +
                '}';
    }

    @Override
    public int hashCode() { return Objects.hash(userID); }

    public void setID(int id) { this.id = id; }

    public int getBiasRating() { return this.biasRating; }

    public String getBiasName() { return this.biasName; }

    public String getFavNewsSource() { return this.favNewsSource; }

    public String getFavTopic() { return this.favTopic; }

    public List<Article> getRecentArticles() { return this.recentArticles; }

    public String getExecSummary() { return this.execSummary; }

    public int getUserID() { return this.userID; }

    public String createBiasName(int bias) {
        String name;
        name = "";
        return name;
    }

    public String createExecSummary() {
        String text;

        text = "You are " + biasName + ". Your favorite news source is " +
        favNewsSource + ". Your favorite topic to read about is " + favTopic + ".";

        return text;
    }

    public void updateBiasRating() {
        //TODO: scan through userHistory, recalculated biasRating,
        // and update biasRating
    }

    public void updateBiasName() {
        //TODO: check biasRating, and update biasName
    }

    public void updateFavNewsSource() {
        //TODO: scan through userHistory, check for news source with most
        // instances, and update favNewsSource
    }

    public void updateFavTopic() {
        //TODO: scan through userHistory, check for topic with most
        // instances, and update favTopic
    }

    public void updateRecentArticles() {
        //TODO: scan through userHistory, check for 5 most recent articles,
        // and update recentArticles
    }

    public void updateExecSummary() {
        //TODO: create new ExecSummary and update objects execSummary
    }
}
