package model;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

public class UserReadings {
    int userID;
    int articleID;
    long dateRead;
    int readingID;

    public UserReadings (int articleid, int userid, long dateRead) {
        this.userID = userid;
        this.articleID = articleid;
        this.dateRead = dateRead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserReadings readings = (UserReadings) o;
        return userID == readings.userID && articleID == readings.articleID
                && dateRead == readings.dateRead;
    }

    @Override
    public String toString() {
        return "UserReadings{" +
                "userid='" + userID +
                ", articleid='" + articleID +
                ", dateRead='" + dateRead +
                '}';
    }

    @Override
    public int hashCode() { return Objects.hash(userID, articleID); }

    public int getArticleID() { return this.articleID; }
    public int getUserID() { return this.userID; }
    public long getDateRead() { return dateRead; }
    public int getReadingID() { return readingID; }

    public void setReadingID(int readingID) { this.readingID = readingID; }
    public void setDateRead(long dateRead) { this.dateRead = dateRead; }
    public void setArticleID(int articleid) { this.articleID = articleid; }
    public void setUserID(int userid) { this.userID = userid; }
}
