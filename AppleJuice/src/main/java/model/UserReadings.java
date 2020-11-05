package model;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

public class UserReadings {
    int userID;
    int articleID;
    int dateRead;
    int readingID;

    public UserReadings (int articleid, int userid, int dateRead, int readingID) {
        this.userID = userid;
        this.articleID = articleid;
        this.dateRead = dateRead;
        this.readingID = readingID;
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

    public int getArticleid() { return this.articleID; }
    public int getUserReadingsid() { return this.userID; }
    public int getDateRead() { return dateRead; }
    public int getReadingID() { return readingID; }

    public void setReadingID(int readingID) { this.readingID = readingID; }
    public void setDateRead(int dateRead) { this.dateRead = dateRead; }
    public void setArticleid(int articleid) { this.articleID = articleid; }
    public void setUserReadingsid(int userid) { this.userID = userid; }
}
