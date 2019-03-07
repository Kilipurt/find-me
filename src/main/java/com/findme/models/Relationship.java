package com.findme.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RELATIONSHIP")
public class Relationship implements Serializable {
    private String status;
    private User userFrom;
    private User userTo;

    @Id
    @OneToOne()
    @JoinColumn(name = "USER_FROM")
    public User getUserFrom() {
        return userFrom;
    }

    @Id
    @OneToOne()
    @JoinColumn(name = "USER_TO")
    public User getUserTo() {
        return userTo;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Relationship that = (Relationship) o;

        if (status != null ? !status.equals(that.status) : that.status != null)
            return false;
        if (userFrom != null ? !userFrom.equals(that.userFrom) : that.userFrom != null)
            return false;
        return userTo != null ? userTo.equals(that.userTo) : that.userTo == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (userFrom != null ? userFrom.hashCode() : 0);
        result = 31 * result + (userTo != null ? userTo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "status='" + status + '\'' +
                ", userFrom=" + userFrom +
                ", userTo=" + userTo +
                '}';
    }
}
