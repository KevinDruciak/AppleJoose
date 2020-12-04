package model;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the reading of an Article by a User.
 */
public class UserReadings {
    int userID;
    int articleID;
    long dateRead;
    int readingID;

    /**
     * Constructor for UserReadings.
     * @param articleid the articleID for UserReadings
     * @param userid the userID for UserReadings
     * @param dateRead the dateRead for UserReadings
     */
    public UserReadings (int articleid, int userid, long dateRead) {
        this.userID = userid;
        this.articleID = articleid;
        this.dateRead = dateRead;
    }

    /**
     * Checks if UserReadings is equal to another UserReadings.
     * @param o the other UserReadings
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserReadings readings = (UserReadings) o;
        return userID == readings.userID && articleID == readings.articleID
                && dateRead == readings.dateRead;
    }

    /**
     * Presents UserReadings in String form.
     * @return the String form of UserReadings
     */
    @Override
    public String toString() {
        return "UserReadings{" +
                "userid='" + userID +
                ", articleid='" + articleID +
                ", dateRead='" + dateRead +
                '}';
    }

    /**
     * Hashes the UserReadings.
     * @return the hashcode of the hashed UserReadings
     */
    @Override
    public int hashCode() {
        return Objects.hash(userID, articleID);
    }

    /**
     * Gets articleID of UserReadings
     * @return the articleID
     */
    public int getArticleID() {
        return this.articleID;
    }

    /**
     * Gets userId of UserReadings
     * @return the userID
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Gets dateRead of UserReadings
     * @return the dateRead
     */
    public long getDateRead() {
        return dateRead;
    }

    /**
     * Gets readingID of UserReadings
     * @return the readingID
     */
    public int getReadingID() {
        return readingID;
    }

    /**
     * Sets readingID of UserReadings
     * @param readingID the value to set readingID
     */
    public void setReadingID(int readingID) {
        this.readingID = readingID;
    }

    /**
     * Sets dateRead of UserReadings
     * @param dateRead the value to set dateRead
     */
    public void setDateRead(long dateRead) {
        this.dateRead = dateRead;
    }

    /**
     * Sets articleid of UserReadings
     * @param articleid the value to set articleid
     */
    public void setArticleID(int articleid) {
        this.articleID = articleid;
    }

    /**
     * Sets userid of UserReadings
     * @param userid thye value to set userid
     */
    public void setUserID(int userid) {
        this.userID = userid;
    }
}
