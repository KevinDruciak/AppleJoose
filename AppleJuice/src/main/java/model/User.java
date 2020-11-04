package model;

import org.sql2o.Sql2o;
import persistence.Sql2oArticleDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private int userID;
    private String userName;
    private Statistics userStats;
    private List<Article> userHistory;

    private String encryptedPW;

    public User(String userName) {
        this.userName = userName;
        this.userHistory = new ArrayList<>();
    }

    public User(String userName, String encryptedPW) {
        this.userName = userName;
        this.encryptedPW = encryptedPW;
        this.userHistory = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User u1 = (User) o;
        return userID == u1.userID;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                "}";
    }

    @Override
    public int hashCode() { return Objects.hash(userID, userName); }

    public void setUserID(int id) {
        this.userID = id;
        //this.userStats = new Statistics(this.userID);
    }

    public int getUserID() { return this.userID; }

    public String getUserName() { return this.userName; }

    public Statistics getUserStats() { return userStats; }

    public List<Article> getUserHistory() { return this.userHistory; }

    public void setUserName(String userName) { this.userName = userName; }

    public void updateUserStats() {
        userStats.updateBiasRating(this.userHistory);
        userStats.updateBiasName();
        userStats.updateFavNewsSource(this.userHistory);
        userStats.updateFavTopic(this.userHistory);
        userStats.updateRecentArticles(this.userHistory);
        userStats.updateExecSummary();
    }

    public void updateUserHistory() {
        //TODO: Call extension API to scan user history, get new News articles,
        // create new Article objects, and add them to userHistory list
        /*
        For now user articles will simply be all articles in the data base
         */
    }

    public void clearUserStats() {
        this.userStats = null;
        //this.userStats = new Statistics(this.userID);
    }

    public void clearUserHistory() {
        this.userHistory = null;
        this.userHistory = new ArrayList<>();
    }

}
