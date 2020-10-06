package model;

import java.util.List;
import java.util.Objects;

public class User {
    private int id;
    private String userName;
    Statistics stats;
    List<Articles> history;

    public User(int id, String userName, Statistics stats, List<Articles> history) {
        this.id = id;
        this.userName = userName;
        this.stats = stats;
        this.history = history;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Statistics getStats() {
        return stats;
    }

    public void setStats(Statistics stats) {
        this.stats = stats;
    }

    public List<Articles> getHistory() {
        return history;
    }

    public void setHistory(List<Articles> history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", stats=" + stats +
                ", history=" + history +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(stats, user.stats) &&
                Objects.equals(history, user.history);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, stats, history);
    }
}
