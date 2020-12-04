package model;

import org.sql2o.Sql2o;
import persistence.Sql2oArticleDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a User.
 */
public class User {
    private int userID;
    private String userName;
    private String userPassword;
    private int userStatsID;
    private List<UserReadings> userHistory;

    /**
     * The constructor for User.
     * @param userName the user's userName
     * @param password the user's password
     */
    public User(String userName, String password) {
        this.userPassword = password;
        this.userName = userName;
        this.userHistory = new ArrayList<>();
    }

    /**
     * Checks if User is equal to another User.
     * @param o the other User
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User u1 = (User) o;
        return userID == u1.userID;
    }

    /**
     * Presents User in String form.
     * @return the String form of User
     */
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", password='" + userPassword + '\'' +
                '}';
    }

    /**
     * Hashes the User.
     * @return the hashcode of the hashed User
     */
    @Override
    public int hashCode() {
        return Objects.hash(userID, userName, userPassword);
    }

    /**
     * Gets the encrypted password.
     * @deprecated
     * @return the encrypted password
     */
    public String getEncryption() {
        return this.userPassword;
    }

    /**
     * Sets id of User.
     * @param id the value to set id
     */
    public void setUserID(int id) {
        this.userID = id;
        //this.userStats = new Statistics(this.userID);
    }

    /**
     * Sets userPassword of User.
     * @param oldPassword the User's old password
     * @param newPassword the new password to set
     */
    public void setUserPassword(String oldPassword, String newPassword) {
        if (oldPassword.equals(this.userPassword)) {
            this.userPassword = newPassword;
        }
    }

    /**
     * Gets userID of User.
     * @return the userID
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Gets userName of User.
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Gets userPassword of User.
     * @return the userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Gets userHistory of User.
     * @return the userHistory
     */
    public List<UserReadings> getUserHistory() {
        return this.userHistory;
    }

    /**
     * Sets usernName of User.
     * @param userName the value to set userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
