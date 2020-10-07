package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

public class Statistics {
    private int id;
    private int biasRating;
    private String biasName;
    private String favNewsSource;
    private String favTopic;
    private List<Article> recentArticles = new ArrayList<>();
    private String execSummary;
    private int userID;

    public Statistics(int biasRating, String biasName, String favNewsSource, String favTopic, String execSummary, int userID) {
        this.biasRating = biasRating;
        this.biasName = biasName;
        this.favNewsSource = favNewsSource;
        this.favTopic = favTopic;
        this.execSummary = execSummary;
        this.userID = userID;
        this.biasRating = 0;
        this.biasName = this.createBiasName(this.biasRating);
        this.favNewsSource = "";
        this.favTopic = "";
        this.execSummary = this.createExecSummary();
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
               // ", recentArticles=" + recentArticles.toString() + '\'' +
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

    //public List<Article> getRecentArticles() { return this.recentArticles; }

    public String getExecSummary() { return this.execSummary; }

    public int getUserID() { return this.userID; }

    public String createBiasName(int bias) {
        String name;
        bias = bias % 10;
        switch (bias) {
            case -10:
            case -9:
            case -8:
                name = "Extreme Liberal Bias";
                break;
            case -7:
            case -6:
            case -5:
                name = "Strong Liberal Bias";
                break;
            case -4:
            case -3:
                name = "Moderate Liberal Bias";
                break;
            case -2:
            case -1:
                name = "Minimal Liberal Bias";
                break;
            case 0:
                name = "Minimal Bias";
                break;
            case 1:
            case 2:
                name = "Minimal Conservative Bias";
                break;
            case 3:
            case 4:
                name = "Moderate Conservative Bias";
                break;
            case 5:
            case 6:
            case 7:
                name = "Strong Conservative Bias";
                break;
            case 8:
            case 9:
            case 10:
                name = "Extreme Conservative Bias";
                break;
            default:
                name = "Bias Could not be determined";
        }
        return name;
    }

    public String createExecSummary() {
        String text;

        text = "You are " + biasName + ". Your favorite news source is " +
        favNewsSource + ". Your favorite topic to read about is " + favTopic + ".";

        return text;
    }

    public void updateBiasRating(List<Article> userHistory) {
        int biasTotal = 0;

        for (Article a : userHistory) {
            biasTotal += a.getBiasRating();
        }

        this.biasRating = biasTotal;
    }

    public void updateBiasName() {
        this.biasName = createBiasName(this.biasRating);
    }

    public void updateFavNewsSource(List<Article> userHistory) {
        Map<String, Integer> map = new HashMap<>();

        for (Article a : userHistory) {
            Integer i = map.get(a.getNewsSource());
            map.put(a.getNewsSource(), i == null ? 1 : i + 1);
        }

        Map.Entry<String, Integer> max = null;

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue()) {
                max = e;
            }
        }

        this.favNewsSource = max.getKey();
    }

    public void updateFavTopic(List<Article> userHistory) {
        Map<String, Integer> map = new HashMap<>();

        for (Article a : userHistory) {
            Integer i = map.get(a.getTopic());
            map.put(a.getTopic(), i == null ? 1 : i + 1);
        }

        Map.Entry<String, Integer> max = null;

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue()) {
                max = e;
            }
        }

        this.favNewsSource = max.getKey();
    }

    public void updateRecentArticles(List<Article> userHistory) {
        List<Article> rArticles = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            rArticles.add(userHistory.get(userHistory.size() - (1 + i)));
        }

        this.recentArticles = rArticles;
    }

    public void updateExecSummary() {
        //TODO: create new ExecSummary and update objects execSummary
        this.createExecSummary();
    }
}
