package model;

import org.sql2o.Sql2o;
import persistence.Sql2oArticleDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private int userID;
    private String userName;
    private String userPassword;
    private int userStatsID;
    private List<UserReadings> userHistory;

    public User(String userName, String password) {
        this.userPassword = password;
        this.userName = userName;
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
                ", password='" + userPassword + '\'' +
                '}';
    }

    public String getEncryption() {
        return this.userPassword;
    }

    @Override
    public int hashCode() { return Objects.hash(userID, userName, userPassword); }

    public void setUserID(int id) {
        this.userID = id;
        //this.userStats = new Statistics(this.userID);
    }

    public void setUserPassword(String oldPassword, String newPassword) {
        if (oldPassword == this.userPassword) {
            this.userPassword = newPassword;
        }
    }

    public int getUserID() { return this.userID; }

    public String getUserName() { return this.userName; }

    public String getUserPassword() { return userPassword; }

    public List<UserReadings> getUserHistory() { return this.userHistory; }

    public void setUserName(String userName) { this.userName = userName; }

}
