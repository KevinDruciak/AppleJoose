package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

/**
 * Represents a statistics container.
 */
public class Statistics {
    private int id;
    private int biasRating;
    private String biasName;
    private String favNewsSource;
    private String favTopic;
    private String execSummary;
    private int userID;

    /**
     * The constructor for Statistics.
     * @param biasRating the biasRating statistic
     * @param biasName the name associated with the biasRating
     * @param favNewsSource the most visited news source
     * @param favTopic the most visited topic
     * @param execSummary summary of an Article
     * @param userID the id of the user associated to this Statistics
     */
    public Statistics(int biasRating, String biasName, String favNewsSource, String favTopic, String execSummary, int userID) {
        this.biasRating = biasRating;
        this.biasName = biasName;
        this.favNewsSource = favNewsSource;
        this.favTopic = favTopic;
        this.execSummary = execSummary;
        this.userID = userID;
//        this.biasRating = 0;
//        this.biasName = this.createBiasName(this.biasRating);
//        this.favNewsSource = "";
//        this.favTopic = "";
//        this.execSummary = this.createExecSummary();
    }

    /**
     * Checks if Statistics is equal to another Statistics.
     * @param o the other Statistics
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistics stats = (Statistics) o;
        return userID == stats.userID;
    }

    /**
     * Presents statistics in String form.
     * @return the String form of Statistics
     */
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

    /**
     * Hashes the Statistics.
     * @return the hashcode of the hashed Statistics
     */
    @Override
    public int hashCode() { return Objects.hash(userID); }

    /**
     * Sets id of Statistics.
     * @param id the value to set id
     */
    public void setID(int id) { this.id = id; }

    /**
     * Gets id of Statistics.
     * @return the id
     */
    public int getID() { return this.id; }

    /**
     * Sets biasRating of Statistics.
     * @param biasRating the value to set biasRating
     */
    public void setBiasRating(int biasRating) { this.biasRating = biasRating; }

    /**
     * Sets favNews of Statistics.
     * @param favNews the value to set favNews
     */
    public void setFavNewsSource(String favNews) { this.favNewsSource = favNews; }

    /**
     * Sets favTopic of Statistics.
     * @param favTopic the value to set favTopic
     */
    public void setFavTopic(String favTopic) { this.favTopic = favTopic; }

    /**
     * Sets biasName of Statistics.
     * @param biasName the value to set biasName
     */
    public void setBiasName(String biasName) { this.biasName = biasName; }

    /**
     * Sets execSummary of Statistics.
     * @param execSummary the value to set execSummary
     */
    public void setExecSummary(String execSummary) { this.execSummary = execSummary; }

    /**
     * Gets biasRating of Statistics.
     * @return the biasRating
     */
    public int getBiasRating() { return this.biasRating; }

    /**
     * Gets biasName of Statistics.
     * @return the biasName
     */
    public String getBiasName() { return this.biasName; }

    /**
     * Gets favNewsSource of Statistics.
     * @return the favNewsSource
     */
    public String getFavNewsSource() { return this.favNewsSource; }

    /**
     * Gets favTopic of Statistics.
     * @return the favTopic
     */
    public String getFavTopic() { return this.favTopic; }

    /**
     * Gets execSummary of Statistics.
     * @return the execSummary
     */
    public String getExecSummary() { return this.execSummary; }

    /**
     * Gets userID of Statistics.
     * @return the userID
     */
    public int getUserID() { return this.userID; }

    /**
     * Associates a name to the integer valued biasRating.
     * @param bias the biasRating
     * @return the name of the biasRating
     */
    public String createBiasName(int bias) {
        String name;
        bias = bias / 8;
        switch (bias) {
            case -5:
            case -4:
                name = "Extreme Liberal Bias";
                break;
            case -3:
                name = "Strong Liberal Bias";
                break;
            case -2:
                name = "Moderate Liberal Bias";
                break;
            case -1:
                name = "Minimal Liberal Bias";
                break;
            case 0:
                name = "Minimal Bias";
                break;
            case 1:
                name = "Minimal Conservative Bias";
                break;
            case 2:
                name = "Moderate Conservative Bias";
                break;
            case 3:
                name = "Strong Conservative Bias";
                break;
            case 4:
            case 5:
                name = "Extreme Conservative Bias";
                break;
            default:
                name = "Bias Could not be determined";
        }
        return name;
    }

    /**
     * Creates a summary of these Statistics.
     * @return return the summary
     */
    public String createExecSummary() {
        String text;

        text = "You have " + biasName + ". Your favorite news source is " +
                favNewsSource + ". Your favorite topic to read about is " + favTopic + ".";

        return text;
    }

    /*
    public void updateBiasRating(List<UserReadings> userHistory) {
        double biasTotal = 0;
        double numArticles = 0;

        for (Article a : userHistory) {
            biasTotal += a.getBiasRating();
            numArticles++;
        }

        this.biasRating = (int) Math.round(biasTotal / numArticles);
    }

    public void updateBiasName() {
        this.biasName = createBiasName(this.biasRating);
    }

    public void updateFavNewsSource(List<UserReadings> userHistory) {
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

    public void updateFavTopic(List<UserReadings> userHistory) {
        Map<String, Integer> map = new HashMap<>();

        for (UserReadings r : userHistory) {
            Integer i = map.get(r.getArticle());
            map.put(r.getArticle(), i == null ? 1 : i + 1);
        }

        Map.Entry<String, Integer> max = null;

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue()) {
                max = e;
            }
        }

        this.favNewsSource = max.getKey();
    }

    public void updateRecentArticles(List<UserReadings> userHistory) {
        List<Article> recArticles = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            recArticles.add(userHistory.get(userHistory.size()

     */
}
